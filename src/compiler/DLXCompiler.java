package compiler;
import ir.cfg.*;
import ir.instructions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import front.Parser;
import front.Parser.ParserException;
import front.Scanner.ScannerException;


public class DLXCompiler {
    private static final int FP = 28;
    private static final int SP = 29;
    private static final int GV = 30;
    private static final int RA = 31;
    // Mnemonic-to-Opcode mapping
    static final String mnemo[] = {
        "ADD", "SUB", "MUL", "DIV", "MOD", "CMP", "ERR", "ERR", "OR", "AND", "BIC", "XOR", "LSH", "ASH", "CHK", "ERR",
        "ADDI", "SUBI", "MULI", "DIVI", "MODI", "CMPI", "ERRI", "ERRI", "ORI", "ANDI", "BICI", "XORI", "LSHI", "ASHI", "CHKI", "ERR",
        "LDW", "LDX", "POP", "ERR", "STW", "STX", "PSH", "ERR", "BEQ", "BNE", "BLT", "BGE", "BLE", "BGT", "BSR", "ERR",
        "JSR", "RET", "RDI", "WRD", "WRH", "WRL", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR",
        "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR"};
    static final int ADD = 0;
    static final int SUB = 1;
    static final int MUL = 2;
    static final int DIV = 3;
    static final int MOD = 4;
    static final int CMP = 5;
    static final int OR = 8;
    static final int AND = 9;
    static final int BIC = 10;
    static final int XOR = 11;
    static final int LSH = 12;
    static final int ASH = 13;
    static final int CHK = 14;
    static final int ADDI = 16;
    static final int SUBI = 17;
    static final int MULI = 18;
    static final int DIVI = 19;
    static final int MODI = 20;
    static final int CMPI = 21;
    static final int ORI = 24;
    static final int ANDI = 25;
    static final int BICI = 26;
    static final int XORI = 27;
    static final int LSHI = 28;
    static final int ASHI = 29;
    static final int CHKI = 30;
    static final int LDW = 32;
    static final int LDX = 33;
    static final int POP = 34;
    static final int STW = 36;
    static final int STX = 37;
    static final int PSH = 38;
    static final int BEQ = 40;
    static final int BNE = 41;
    static final int BLT = 42;
    static final int BGE = 43;
    static final int BLE = 44;
    static final int BGT = 45;
    static final int BSR = 46;
    static final int JSR = 48;
    static final int RET = 49;
    static final int RDI = 50;
    static final int WRD = 51;
    static final int WRH = 52;
    static final int WRL = 53;
    static final int ERR = 63;
    
    
	public Parser parser;
	public TileTree tileTree;

	public DLXCompiler(String srcFile) {
		this.parser = new Parser(srcFile);
	}

	public void terminate() {
		parser.terminate();
	}

	public void compile() throws ParserException, ScannerException {

//		try {
			parser.parse();

			for (CFG cfg : parser.CFGs) {

				// skip main since is does not have any local variables
				if(cfg.label.equals("main")) {
					continue;
				}

				// Iterate over BBs and initialize entry and exit states
				// TODO update the iteration from linear to reverse post order
				Iterator<BasicBlock> blockIterator = cfg.topDownIterator();
				while (blockIterator.hasNext()) {
					BasicBlock bb = blockIterator.next();
					// create empty entry state (instruction place holders)
					bb.entryState = createStateVector(cfg.frame);
					// copy the entry state slots inside exit state slots
					// this is required to maintain the link between entry and exit state
					// for the slots that are not mutated in current basic block
					bb.exitState.addAll(bb.entryState); 
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
						} else if (values.size() > 1) { // multiple predecessors
							Phi phi = new Phi(values);
							phi.setInstrNumber(parser.instructionCnt++);
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
							Phi phi = (Phi) inst;
							Instruction value = testPhi(phi);
							if (value != null) {
								// this PHI is redundant and 
								// value holds the instruction it should be forwarded to
								Instruction.forward(phi, value);
								removablePHIs.add(phi);
							}
							
						}
					}
					bb.removeInstructions(removablePHIs);					
				}

			}
			assignRegisters();
			
			generateCode();
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
			state.add(new Instruction(-1));
		}
		return state;
	}
	
	private void constructSSA(BasicBlock bb) {
		// TODO the abstract interpretation should be extended to all
		// the other IR operations that either read or write scalars
		
		// perform abstract interpretation
		if (!bb.isInstructionsEmpty()) {
			List<Instruction> moves = new ArrayList<Instruction>();
			for (Instruction instr : bb.getInstructions()) {
				if (instr instanceof Move) {
					moves.add(instr);
					// mutate state vector, replace dest with src in the state vector
					
					// src can be a scalar, belonging to the frame, or the result of another instruction
					// we use resolve() to get the right value for it
					Instruction src = resolve(((Move) instr).src, bb);
					
					// dest is always a scalar
					Scalar dest = (Scalar)((Move) instr).dest; // a frame slot
					bb.exitState.set(dest.symbol.slot, src);
				} else if (instr instanceof ArithmeticBinary) {
					// read from local variables
					((ArithmeticBinary) instr).right = resolve(((ArithmeticBinary) instr).right, bb);
					((ArithmeticBinary) instr).left = resolve(((ArithmeticBinary) instr).left, bb);
				} else if (instr instanceof Return) {
					((Return) instr).returnValue = resolve(((Return) instr).returnValue, bb);
				} else if (instr instanceof Call) {
					List<Instruction> ssaArgs = new ArrayList<Instruction>();
					for(Instruction arg : ((Call)instr).args) {
						ssaArgs.add(resolve(arg, bb));
					}
					((Call)instr).args = ssaArgs;
				}
				 
			}
			// eliminate move instructions
			bb.removeInstructions(moves);
		}
	}
	
	// resolve the value of a read
	private Instruction resolve(Instruction inst, BasicBlock bb) {
		if (inst instanceof Scalar) {
			// if the instruction is a scalar then this is a read and
			// we need to actually get it's value from the state vector
			return bb.exitState.get(((Scalar)inst).symbol.slot);
		}
		// a value generating instruction (e.g. IMM)
		return inst;
	}
	
	
	/**
	 * Determine if PHI instruction is redundant.
	 * Functions of the form v2 = PHI(v1, v1, ..., v1) 
	 * 		are forwarded to their common operand (v2 → v1). 
	 * Functions of the form v2 = PHI(v1, v2, ..., v2) 
	 * 		are forwarded to their incoming operand (v2 → v1). 
	 * 
	 * @param phi
	 * @return the instruction the PHI should be forwarded to if redundant, null otherwise
	 */
	private Instruction testPhi(Phi phi) {

		// PHI's have at least two operands
		assert phi.values.size() >= 2;
		
		Instruction firstVal = null;

		// pick first value not equal to the PHI instruction itself
		for(int i = 0; i < phi.values.size(); i++) {
			firstVal = Instruction.resolve(phi.values.get(i));
			if (!firstVal.equals(phi)) {
				break;
			}
		}
		
		// all values should never be equal to PHI
		if (firstVal.equals(phi)) {
			System.err.println("All PHI values equals to PHI itself!");
		}
		
		// test 
		for(int i = 0; i < phi.values.size(); i++) {
			Instruction value = Instruction.resolve(phi.values.get(i));
			if(!value.equals(firstVal) && !value.equals(phi)) {
				return null; // this PHI is not redundant
			}
		}
		return firstVal; // this PHI is redundant, return the instruction it should be forwarded to
	}

	void assignRegisters(){
	    
	    
	    for (CFG cfg : parser.CFGs) {
            cfg.buildTileTree();
            cfg.buildLiveRanges();
            
            
            
	    }
	}

    private void generateCode() {
        // TODO Auto-generated method stub
        
    }
	
}
