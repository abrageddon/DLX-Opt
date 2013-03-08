package back.regAloc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


import ir.cfg.BasicBlock;
import ir.cfg.CFG;
import ir.cfg.Interval;
import ir.instructions.Instruction;
import ir.instructions.Phi;

public class RegisterAllocator {

	public List<CFG> CFGs;

	public RegisterAllocator(List<CFG> CFGs) {
		this.CFGs = CFGs;
	}

	public void allocateRegisters(){

		for (CFG cfg : CFGs) {
			//            cfg.buildLiveRanges();
		}
	}

	public void buildLiveRanges() {

		for (CFG cfg : this.CFGs) {

			// BUILDINTERVALS
			// for each block b in reverse order do
			Iterator<BasicBlock> blockIterator = cfg.bottomUpIterator();
			HashSet<PseudoRegister> live;
			ArrayList<Interval> intervals;
			while (blockIterator.hasNext()) {
				BasicBlock b = blockIterator.next();
				// live = union of successor.liveIn for each successor of b
				live = new HashSet<PseudoRegister>();
				for (BasicBlock succ:b.succ){
					live.addAll(succ.liveIn);
				}

				// for each phi function phi of successors of b do
				for (BasicBlock succ:b.succ){
					for(Instruction i:succ.getInstructions()){
						if(Phi.class.isAssignableFrom(i.getClass())){
							// live.add(phi.inputOf(b))
							for (Instruction value: ((Phi)i).values ){
								//TODO is correct?
								live.add( new PseudoRegister( value.getInstrNumber() ) );
							}
						}
					}
				}

				// for each output operand opd in live do
				for (Instruction i: b.getInstructions()){
					if (false){
						// intervals[opd].addRange(b.from, b.to)
					}
				}

				// for each operation op of b in reverse order do
				// for each output operand opd of op do
				// intervals[opd].setFrom(op.id)
				// live.remove(opd)
				// for each input operand opd of op do
				// intervals[opd].addRange(b.from, op.id)
				// live.add(opd)

				// for each phi function phi of b do
				for(Instruction i:b.getInstructions()){
					if(Phi.class.isAssignableFrom(i.getClass())){
						// live.remove(phi.output)
						live.remove(new PseudoRegister( ((Phi)i).getInstrNumber() ));
					}
				}

				// if b is loop header then
				if(b.label.equals("while-cond")){
					// loopEnd = last block of the loop starting at b
					BasicBlock loopEnd = null;
					// for each opd in live do
					// intervals[opd].addRange(b.from, loopEnd.to)
				}

				// b.liveIn = live
				b.liveIn = live;
			}
		}

	}
}
