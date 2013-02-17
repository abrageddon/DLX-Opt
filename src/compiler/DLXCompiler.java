package compiler;
import ir.cfg.BasicBlock;
import ir.cfg.CFG;
import ir.instructions.ArithmeticBinary;
import ir.instructions.Cmp;
import ir.instructions.Immediate;
import ir.instructions.Instruction;
import ir.instructions.Move;
import ir.instructions.Phi;
import ir.instructions.Scalar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import front.Parser;
import front.Parser.ParserException;
import front.Scanner.ScannerException;


public class DLXCompiler {

	public Parser parser;

	public DLXCompiler(String srcFile) {
		this.parser = new Parser(srcFile);
	}

	public void terminate() {
		parser.terminate();
	}

	public void compile() throws ParserException, ScannerException {

//		try {
			parser.parse();
			List<CFG> CFGs = parser.CFGs;

			for (CFG cfg : CFGs) {//

				// skip main since is does not have any local variables
				if(cfg.label.equals("main")) {
					continue;
				}

				// Iterate over BBs and initialize entry and exit states
				// TODO update the iteration from linear to reverse post order
				Iterator<BasicBlock> blockIterator = cfg.topDownIterator();
				while (blockIterator.hasNext()) {
					BasicBlock bb = blockIterator.next();
//					bb.entryState.addAll(cfg.frame); // wrong, should have empty slots
//					bb.exitState.addAll(cfg.frame); // wrong, should have empty slots
					bb.entryState = createStateVector(cfg.frame);
					bb.exitState = createStateVector(cfg.frame);
					constructSSA(bb);
				}

				// link entry and exit states
				blockIterator = cfg.topDownIterator();
				while (blockIterator.hasNext()) {
					BasicBlock bb = blockIterator.next();
					int slot = 0;
					for (Instruction val : bb.entryState) {
						List<Instruction> values = new ArrayList<Instruction>();
						for (BasicBlock pred : bb.pred) {
							values.add(pred.exitState.get(slot));
						}
						if (values.size() == 1) { // only one predecessor
							Instruction.forward(val, values.get(0));
						} else if (values.size() > 1) {
							Phi phi = new Phi(values);
							Instruction.forward(val, phi);
							bb.prependInstruction(phi);
						}
						slot++;
					}

				}
				// simplify PHIs
				blockIterator = cfg.topDownIterator();
				while (blockIterator.hasNext()) {
					BasicBlock bb = blockIterator.next();
					List<Instruction> removablePHIs = new ArrayList<Instruction>();
					for (Instruction inst : bb.getInstructions()) {
						if (inst instanceof Phi) {
							if (allEqual(((Phi) inst).values)) {
								removablePHIs.add(inst);
							}
						}
					}
					bb.removeInstructions(removablePHIs);					
				}

			}
//		} catch (ParserException
//				| ScannerException e) {
//			e.printStackTrace();
//		} finally {
//			// close output file and scanner
//			//        out.println("}");
//			//        out.close();
//			parser.terminate();
//		}

	}

	private List<Instruction> createStateVector(List<Instruction> frame) {
		List<Instruction> state = new ArrayList<Instruction>();
		for (int i = 0; i < frame.size(); i++) {
			state.add(new Instruction());
		}
		return state;
	}
	
	private void constructSSA(BasicBlock bb) {
		// TODO not sure what this abstract interpretation step should do,
		// since is not very clear in the paper:
		// "
		// 	Local variable assignments update the state vector, 
		// 	reads from local variables select the last written 
		// 	value for a given variable in the state vector.
		// "
		
		
		// perform abstract interpretation
		if (!bb.isInstructionsEmpty()) {
			List<Instruction> moves = new ArrayList<Instruction>();
			for (Instruction instr : bb.getInstructions()) {
				if (instr instanceof Move) {
					moves.add(instr);
					// mutate state vector, replace dest with src
					// eliminate move instruction
					Instruction src = ((Move) instr).src;
					Scalar frameDest = (Scalar)((Move) instr).dest; // a frame slot
					Instruction stateDest = bb.entryState.get(frameDest.symbol.slot);
//					assert frameDest == stateDest;
					Instruction.forward(stateDest, src);
					bb.exitState.set(frameDest.symbol.slot, src);
				} else if (instr instanceof ArithmeticBinary) {
					// read from local variables
					if (((ArithmeticBinary) instr).left instanceof Scalar) {
						int slot = ((Scalar)(((ArithmeticBinary) instr).left)).symbol.slot;
						((ArithmeticBinary) instr).left = bb.entryState.get(slot);
					}
					if (((ArithmeticBinary) instr).right instanceof Scalar) {
						int slot = ((Scalar)(((ArithmeticBinary) instr).right)).symbol.slot;
						((ArithmeticBinary) instr).right = bb.entryState.get(slot);
					}
				}
			}
			bb.removeInstructions(moves);
		}
	}	
	
	private boolean allEqual(List<Instruction> values) {
		Instruction firstVal = values.get(0);
		for(int i = 1; i < values.size(); i++) {
			if(!firstVal.equals(values.get(i))) {
				return false;
			}
		}
		return true;
	}

}
