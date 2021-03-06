package compiler.back.codeGen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import compiler.back.regAloc.VirtualRegister;
import compiler.back.regAloc.VirtualRegisterFactory;
import compiler.ir.cfg.*;
import compiler.ir.instructions.*;

public class CodeGenerator {

//	private static final int MAX_REG = 27;
	private static final boolean DEBUG = false;
	private HashMap<Integer, String> DEBUGMESG;
	
	
	private static final int FrameP = 28;
	private static final int StackP = 29;
	private static final int GlobalV = 30;
	private static final int ReturnA = 31;

	// Mnemonic-to-Opcode mapping
	static final String mnemo[] = { "ADD", "SUB", "MUL", "DIV", "MOD", "CMP",
			"ERR", "ERR", "OR", "AND", "BIC", "XOR", "LSH", "ASH", "CHK",
			"ERR", "ADDI", "SUBI", "MULI", "DIVI", "MODI", "CMPI", "ERRI",
			"ERRI", "ORI", "ANDI", "BICI", "XORI", "LSHI", "ASHI", "CHKI",
			"ERR", "LDW", "LDX", "POP", "ERR", "STW", "STX", "PSH", "ERR",
			"BEQ", "BNE", "BLT", "BGE", "BLE", "BGT", "BSR", "ERR", "JSR",
			"RET", "RDI", "WRD", "WRH", "WRL", "ERR", "ERR", "ERR", "ERR",
			"ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR",
			"ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR",
			"ERR", "ERR", "ERR", "ERR" };
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

	private List<CFG> CFGs;

	private CFG mainCFG;
	private CFG currentCFG;

	private int pc;
	private ArrayList<Integer> nativeCode;
	private Stack<Integer> fixup;
	private Stack<Integer> functionFixup;
	private Stack<CFG> functionsToFixup;

	public CodeGenerator(List<CFG> CFGs) {
		this.CFGs = CFGs;
	}

	public void generateCode(String outFile) {
		setupProgram();
		setupGlobals();

		int start = pc;// Start after var setup

		if (CFGs.size() > 1) {
			UnCondBraFwd(0);
		}

		processFunc();

		if (CFGs.size() > 1) {
			Fixup(start);
		}

		processMain();

		fixupFunctions();

		writeBin(outFile);
	}

	private void processFunc() {
		for (CFG function : CFGs) {
			// Process main separately
			if (function.label.equals("main")) {
				continue;
			}
			currentCFG = function;
			function.setStartLine(pc);

			Iterator<BasicBlock> blocks = function.topDownIterator();
			while (blocks.hasNext()) {
				BasicBlock currentBlock = (BasicBlock) blocks.next();
				currentBlock.startLine = pc;// Easy loopbacks

				preBlockProcessing(currentBlock);

				// Process block's instructions
				Iterator<Instruction> instructions = currentBlock
						.getInstructionsIterator();
				while (instructions.hasNext()) {
					Instruction instruction = (Instruction) instructions.next();
					produceCode(instruction);
				}

				postBlockProcessing(currentBlock);
			}

		}
	}

	private void processMain() {
		currentCFG = mainCFG;
		Iterator<BasicBlock> blocks = mainCFG.topDownIterator();
		while (blocks.hasNext()) {
			BasicBlock currentBlock = (BasicBlock) blocks.next();
			currentBlock.startLine = pc;// Easy loopbacks

			preBlockProcessing(currentBlock);

			// Process block's instructions
			Iterator<Instruction> instructions = currentBlock
					.getInstructionsIterator();
			while (instructions.hasNext()) {
				Instruction instruction = (Instruction) instructions.next();
				produceCode(instruction);
			}

			postBlockProcessing(currentBlock);
		}

		PutF2(RET, 0, 0, 0);// END MAIN!
	}

	private void preBlockProcessing(BasicBlock currentBlock) {
		// IF fixup
		if (currentBlock.label.equals("else")
				&& !currentBlock.isInstructionsEmpty()) {
			int elseBranch = fixup.pop();
			UnCondBraFwd(0);
			Fixup(elseBranch);
		} else if (currentBlock.label.equals("else")
				&& currentBlock.isInstructionsEmpty()) {
			Fixup(fixup.pop());
		}

		if (currentBlock.label.equals("fi-join")) {
			BasicBlock elseCheck = null;
			for (BasicBlock block : currentBlock.pred) {
				if (block.label.equals("else")) {
					elseCheck = block;
					break;
				}
			}
			if (elseCheck != null && !elseCheck.isInstructionsEmpty()) {
				FixAll(elseCheck.startLine);
			}
		}
		
		// Functions intro
		if (currentBlock.label.equals("start")) {
			AddDebug((currentCFG.isFunc()?"FUNCTION: ":"PROCEDURE: ")+currentCFG.label);
			if (!currentCFG.label.equals("main")){
				// Store Return Address
				int paramNum = currentCFG.getParamNum();
				PutF1(STW, ReturnA, FrameP, (paramNum + 2) * 4);
			}
		}


	}

	private void produceCode(Instruction instruction) {
		AddDebug(instruction.toString());

		//if outputOp has spill setup temp
		if(instruction.hasInputSpill()){
			//load b or c if spilled
			List<VirtualRegister> vReg = instruction.getInputOperands();
			if (vReg.size() >= 1){
				if(vReg.get(0).rReg==null){
					loadSpill(10, vReg.get(0));//B=10
				}
			}
			if (vReg.size() >= 2){
				if(vReg.get(1).rReg==null){
					loadSpill(11, vReg.get(1));//C=11
				}
			}
		}
		
		if (instruction instanceof Immediate) {
			Immediate ins = (Immediate) instruction;
			PutF1(ADDI, useRegA(ins.outputOp) , 0, ins.value);

		} else if (instruction instanceof StoreValue) {
			StoreValue ins = (StoreValue) instruction;
			// Global
			store(ins);

		} else if (instruction instanceof LoadValue) {
			LoadValue ins = (LoadValue) instruction;
			load(ins);

		} else if (instruction instanceof ControlFlowInstr) {
            ControlFlowInstr ins = (ControlFlowInstr) instruction;
            CondNegBraFwd(ins);

        } else if (instruction instanceof Move) {
            Move ins = (Move) instruction;
        	List<VirtualRegister> operands = ins.getInputOperands();
            PutF1(ADDI, useRegA(Instruction.resolve(ins).outputOp), useRegB(operands.get(0)), 0);

        } else if (instruction instanceof Index) {
        	Index ins = (Index) instruction;
        	List<VirtualRegister> operands = ins.getInputOperands();
            PutF1(SUB, useRegA(Instruction.resolve(ins).outputOp), useRegB(operands.get(0)), useRegC(operands.get(1)));

        } else if (instruction instanceof Return) {
			Return ins = (Return) instruction;
			int paramNum = currentCFG.getParamNum();
			boolean isFunc = currentCFG.isFunc();

			if (isFunc) {
				PutF1(STW, useRegA(ins.inputOps.get(0)) , FrameP, 4);
			}

			// Load RA
			PutF1(LDW, ReturnA, FrameP, (paramNum + 2) * 4);
			// Go To RA
			PutF1(RET, 0, 0, ReturnA);

		} else if (instruction instanceof ArithmeticBinary) {
			ArithmeticBinary ins = (ArithmeticBinary) instruction;
			List<VirtualRegister> inputs = ins.getInputOperands();
			PutF2(opCode(ins), useRegA(ins.outputOp) ,
					useRegB(inputs.get(0)) ,
					useRegC(inputs.get(1)) );

		} else if (instruction instanceof Call) {
			Call ins = (Call) instruction;

			// Call other functions
			CFG callee = null;
			for (CFG func : CFGs) {
				if (func.label.equals(ins.function.ident)) {
					callee = func;
					break;
				}
			}
			if (callee != null) {
				execFunction(ins, callee);
			}

			// Built in functions can be overridden
			if (ins.function.ident.equalsIgnoreCase("outputnum")) {
			    List<VirtualRegister> inputs = ins.getInputOperands();
				PutF2(WRD, 0,useRegB(inputs.get(0)) , 0);
			} else if (ins.function.ident.equalsIgnoreCase("outputnewline")) {
				PutF2(WRL, 0, 0, 0);
			} else if (ins.function.ident.equalsIgnoreCase("inputnum")) {
				PutF2(RDI, useRegA(ins.outputOp) , 0, 0);
			}

		}
		

		//TODO if outputOp has spill store temp

		if(instruction.hasOutputSpill()){
			//if spilled, store A
			storeSpill(9, instruction.outputOp);//A=9
		}
	}

	private void postBlockProcessing(BasicBlock currentBlock) {

		
		
		// While loop fixup
		BasicBlock whileStart = null;
		for (BasicBlock block : currentBlock.succ) {
			if (currentBlock.depth > block.depth) {
				whileStart = block;
				break;
			}
		}
		if (whileStart != null) {
			PutF1(BEQ, 0, 0, whileStart.startLine - pc);
			Fixup(fixup.pop());
		}

		if (currentBlock.label.equals("exit") && !currentCFG.label.equals("main")) {
			if (!currentCFG.isFunc()) {
				int paramNum = currentCFG.getParamNum();
				// Load RA
				PutF1(LDW, ReturnA, FrameP, (paramNum + 2) * 4);
				// Pop vars
				PutF1(ADDI, StackP, FrameP, 0);
				PutF2(RET, 0, 0, ReturnA);// END Procedure!
			}
		}
	}

	private void execFunction(Call ins, CFG callee) {
		int paramNum = callee.getParamNum();
		int varNum = callee.getVarNum();
		int arraySize = callee.getArraysSize();

		boolean isFunc = callee.isFunc();
		
		// push all live registers...
		List<VirtualRegister> liveRegs = getLiveRegs(ins);
		for (VirtualRegister vReg:liveRegs){
			if (vReg.rReg!=null){
				AddDebug(callee.label+": Store Live Register "+vReg.rReg.regNumber);
				Push(vReg.rReg.regNumber);
			}
		}
		
		// Store old FP
		AddDebug(callee.label+": Save old FP");
		Push(FrameP);

		// Put RA space
		AddDebug(callee.label+": Space for Return Address");
		Push(0);

		// Put Param, reverse order
		if (paramNum > 0) {
			List<VirtualRegister> params = ins.getInputOperands();
			for( int i = params.size()-1; i>=0;i--){
				// load word to mem
				AddDebug(callee.label+": Param");
				// check each param for spill
				if(params.get(i).rReg!=null){
					Push(useRegB(params.get(i)));
				}else{
					loadSpill(10, params.get(i));//B=10
					Push(10);
				}
			}
		}

		// Put RetVal on stack
		AddDebug(callee.label+": Space for Return Value");
		Push(0);

		// Save FP
		AddDebug(callee.label+": Save FP");
		PutF1(ADDI, FrameP, StackP, 0);

		// Put vars
//		if (varNum > 0) {
//			for (int i = 0; i < varNum; i++) {
				AddDebug(callee.label+": Add Var");
//				Push(0);
//			}
//		}
//
//		if (arraySize > 0) {
//			for (int i = 0; i < arraySize; i++) {
				AddDebug(callee.label+": Add Array");
//				Push(0);
//			}
//		}

		PutF1(ADDI, StackP, StackP, -(varNum + arraySize)*4);

		AddDebug("Jump to "+callee.label);
		if (callee.getStartLine() < 0) {
			// jump to function
			PutF1(JSR, 0, 0, callee.getStartLine() * 4);
		} else {
			// delay pointers till function is processed
			functionFixup.push(pc);
			functionsToFixup.push(callee);
			PutF1(JSR, 0, 0, 0);
		}
		
		// Function Happens HERE

		// pop vars
		AddDebug(callee.label+": Remove Vars & Arrays");
		PutF1(ADDI, StackP, FrameP, 0);

		// Load prev FP
		AddDebug(callee.label+": Load Previous FP");
		PutF1(LDW, FrameP, FrameP, (paramNum + 3) * 4);

		// IF func get return val
		if (isFunc) {
			// put ret val in x
			AddDebug(callee.label+": Grab Return Value");
			Pop(useRegA(ins.outputOp));
		} else {
			// remove empty return val
			AddDebug(callee.label+": Pop Return Value Space");
			Pop();
		}

		// Pop Parms
//		if (paramNum > 0) {
//			for (int i = paramNum - 1; i >= 0; i--) {
				AddDebug(callee.label+": Remove Parameters");
//				Pop();
//			}
//		}
		PutF1(ADDI, StackP, StackP, paramNum*4);

		// POP RA
		AddDebug(callee.label+": Pop RA");
		Pop();

		// Restore oldFP
		AddDebug(callee.label+": Restore old FP");
		Pop(FrameP);

		//TODO pop all live registers...
		for (VirtualRegister vReg:liveRegs){
			if (vReg.rReg!=null){
				AddDebug(callee.label+": Restore Live Register "+vReg.rReg.regNumber);
				Pop(vReg.rReg.regNumber);
			}
		}
	}

	private List<VirtualRegister> getLiveRegs(Call ins) {
		List<VirtualRegister> liveRegs = new ArrayList<VirtualRegister>();
		int lineNum = ins.getInstrNumber();
		
		for(VirtualRegister vReg:VirtualRegisterFactory.virtualRegisters){
			if(vReg.range.conflictsWith(lineNum)){
				liveRegs.add(vReg);
			}
		}
		
		return liveRegs;
	}

	private void setupProgram() {
		nativeCode = new ArrayList<Integer>();
		fixup = new Stack<Integer>();
		functionFixup = new Stack<Integer>();
		functionsToFixup = new Stack<CFG>();
		DEBUGMESG = new HashMap<Integer, String>();
		
		pc = 0;
		// Setup Frame Pointer at global value pointer
		PutF1(ADDI, FrameP, GlobalV, 0);
		PutF1(ADDI, StackP, GlobalV, 0);
		for (CFG cfg : CFGs) {
			if (cfg.label.equals("main")) {
				mainCFG = cfg;
			}
		}
	}

	private void setupGlobals() {
		// Zero out memory, could be reduced
		// Setup Global Variables
		AddDebug("Initalizing Global Vars");
//		for (int i = 0; i < mainCFG.getVarNum(); i++) {
			// Allocate memory
//			PutF1(STW, 0, StackP, 0);
			PutF1(ADDI, StackP, StackP, -4);
//		}
			
		// Setup Global Arrays
		AddDebug("Initalizing Global Arrays");
		int arraySize = mainCFG.getArraysSize();
//		System.err.println("arraySize: "+ arraySize);
//		for (int i = 0; i < arraySize; i++) {
			// Allocate memory
//			PutF1(STW, 0, StackP, 0);
//			PutF1(ADDI, StackP, StackP, -4);
//		}

		AddDebug("Initalizing Spill Area");
		int numberOfSpills=0;
		
		for(VirtualRegister vReg:VirtualRegisterFactory.virtualRegisters){
			if (vReg.rReg==null){
				numberOfSpills++;
			}
		}
		
//		for (int i = 0; i < numberOfSpills; i++) {
//			// Allocate memory
//			PutF1(STW, 0, StackP, 0);
//			PutF1(ADDI, StackP, StackP, -4);
//		}
		

		PutF1(ADDI, StackP, StackP, -(mainCFG.getVarNum() + arraySize + numberOfSpills)*4);
	}

	private void fixupFunctions() {
		while (!functionsToFixup.empty()) {
			CFG fixee = functionsToFixup.pop();
			int loc = functionFixup.pop();
			nativeCode.set(loc,
					JSR << 26 | 0 << 21 | 0 << 16 | fixee.getStartLine() * 4
							& 0xffff);
		}
	}

	private void load(LoadValue ins) {
		
		if (ins.symbol == null && ins.address != null) {
			// Load by address
//			System.err.println("Check: " + ins);
			List<VirtualRegister> address = ins.getInputOperands();
			if (address == null || address.isEmpty()) {
				return;
			}
            PutF1(LDX, useRegA(ins.outputOp), useRegB(address.get(0)), 0 );
		} else if (currentCFG.containsVar(ins.symbol.ident) && !currentCFG.label.equals("main")) {
			// Var
			PutF1(LDW, useRegA(ins.outputOp), FrameP, -GetVarAddress(ins.symbol.ident));
		} else if (currentCFG.containsParam(ins.symbol.ident) && !currentCFG.label.equals("main")) {
			// Param
			PutF1(LDW, useRegA(ins.outputOp), FrameP, 8 + GetParamAddress(ins.symbol.ident));
		} else if (mainCFG.containsVar(ins.symbol.ident)) {
			// Global Var
			PutF1(LDW, useRegA(ins.outputOp), GlobalV,-GetVarAddress(ins.symbol.ident));
		} else if (currentCFG.containsArray(ins.symbol.ident) && !currentCFG.label.equals("main")) {
			// Array base address
	        PutF1(ADDI, useRegA(ins.outputOp), FrameP, -GetArrayAddress(ins.symbol.ident) );
		} else if (mainCFG.containsArray(ins.symbol.ident)) {
			// Global Array base address
          PutF1(ADDI, useRegA(ins.outputOp), GlobalV, -GetArrayAddress(ins.symbol.ident) );
		}
	}

	private int useRegA(VirtualRegister vReg) {
		// IF !rReg THEN return spill(rReg) ELSE return rReg.regNumber
		if(vReg==null){
			return 0;
		}
		if(vReg.rReg == null){
			return 9;
		}
		return vReg.rReg.regNumber;
	}
	private int useRegB(VirtualRegister vReg) {
		// IF !rReg THEN return spill(rReg) ELSE return rReg.regNumber
		//TODO bug?
		if(vReg==null){
			return 0;
		}
		if(vReg.rReg == null){
			return 10;
		}
		return vReg.rReg.regNumber;
	}
	private int useRegC(VirtualRegister vReg) {
		// IF !rReg THEN return spill(rReg) ELSE return rReg.regNumber
		if(vReg==null){
			return 0;
		}
		if(vReg.rReg == null){
			return 11;
		}
		return vReg.rReg.regNumber;
	}


	private void store(StoreValue ins) {
		if (ins.symbol == null) {
			// Store by address
			List<VirtualRegister> address = ins.getInputOperands();
			if (address == null || address.isEmpty()) {
				return;
			}
			
			//TODO fix bug?
//			System.err.println("store: " + ins);
			
			PutF1(STX, useRegA(address.get(0)), useRegB(address.get(1)), 0);
		} else if (currentCFG.containsVar(ins.symbol.ident)
				&& !currentCFG.label.equals("main")) {
			// Var
		    List<VirtualRegister> inputs = ins.getInputOperands();
			PutF1(STW, useRegA(inputs.get(0)), FrameP,
					-GetVarAddress(ins.symbol.ident));
		} else if (currentCFG.containsParam(ins.symbol.ident)
				&& !currentCFG.label.equals("main")) {
			// Param
			PutF1(STW, useRegA(ins.outputOp), FrameP,
					8 + GetParamAddress(ins.symbol.ident));
		} else if (mainCFG.containsVar(ins.symbol.ident)) {
			// Global Var
            List<VirtualRegister> inputs = ins.getInputOperands();
			PutF1(STW, useRegA(inputs.get(0)),
					GlobalV, -GetVarAddress(ins.symbol.ident));
		} else if (currentCFG.containsArray(ins.symbol.ident)
				&& !currentCFG.label.equals("main")) {
			// Array
			System.err.println("Array Store shouldn't happen: "+ins);
		} else if (mainCFG.containsArray(ins.symbol.ident)) {
			// Global Array
			System.err.println("Global Array Store shouldn't happen: "+ins);
		}
	}

	private int GetParamAddress(String ident) {
		int ret = 0;
		if (currentCFG.containsParam(ident)) {
			ret = (currentCFG.getParam(ident)) * 4;
		} else {
			ret = Integer.MAX_VALUE;
			Error("GetParamAddress: Var does not exist: " + ident);
		}

		return ret;
	}

	private int GetVarAddress(String ident) {
		int ret = 0;

		if (currentCFG != null && currentCFG.containsVar(ident)) {
			ret = (currentCFG.getVar(ident)) * 4;
		} else if (mainCFG.containsVar(ident)) {
			ret = (mainCFG.getVar(ident)) * 4;
		} else {
			ret = Integer.MAX_VALUE;
			Error("GetVarAddress: Var does not exist: " + ident);
		}

		return ret;
	}

	private int GetArrayAddress(String ident) {
		int ret = 0;

		if (currentCFG != null && currentCFG.containsArray(ident)) {
			ret = (currentCFG.getArrayOffset(ident));
		} else if (mainCFG.containsArray(ident)) {
			ret = (mainCFG.getArrayOffset(ident));
		} else {
			ret = Integer.MAX_VALUE;
			Error("GetVarAddress: Var does not exist: " + ident);
		}

		return ret;
	}

	private static int negatedBranchOp(ControlFlowInstr instruction) {
		if (instruction instanceof BranchEqual) {
			return BNE;
		} else if (instruction instanceof BranchGreater) {
			return BLE;
		} else if (instruction instanceof BranchGreaterEqual) {
			return BLT;
		} else if (instruction instanceof BranchLesser) {
			return BGE;
		} else if (instruction instanceof BranchLesserEqual) {
			return BGT;
		} else if (instruction instanceof BranchNotEqual) {
			return BEQ;
		}
		return ERR;
	}

	private static int opCode(Instruction instruction) {
		if (instruction instanceof Add) {
			return ADD;
		} else if (instruction instanceof Sub) {
			return SUB;
		} else if (instruction instanceof Mul) {
			return MUL;
		} else if (instruction instanceof Div) {
			return DIV;
		} else if (instruction instanceof Cmp) {
			return CMP;
		}
		return ERR;
	}

	private void Push(int regNo) {
		PutF1(STW, regNo, StackP, 0);
		PutF1(ADDI, StackP, StackP, -4);
	}

	private void Pop(int regNo) {
		PutF1(ADDI, StackP, StackP, 4);
		PutF1(LDW, regNo, StackP, 0);
	}

	private void storeSpill(int reg, VirtualRegister vReg) {
		PutF1(STW, reg, GlobalV, -(mainCFG.getVarNum() + mainCFG.getArraysSize()+ vReg.spillLocation)*4);
	}

	private void loadSpill(int reg, VirtualRegister vReg) {
		PutF1(LDW, reg, GlobalV, -(mainCFG.getVarNum() + mainCFG.getArraysSize()+ vReg.spillLocation)*4);
	}

	private void Pop() {
		PutF1(ADDI, StackP, StackP, 4);
	}

	private void CondNegBraFwd(ControlFlowInstr ins) {
		fixup.push(pc);
		PutF1(negatedBranchOp(ins),
				useRegA(ins.inputOps.get(0)), 0, 0);
	}

	private void UnCondBraFwd(int loc) {
		PutF1(BEQ, 0, 0, loc);// Build linked list by storing previous value
		// fixup.push(pc - 1);
	}

	private void Fixup(int loc) {
		int part = (0xffff0000 + (pc - loc));
		int fixed = (nativeCode.get(loc) | 0x0000ffff) & part;
		nativeCode.set(loc, fixed);
	}

	private void FixAll(int loc) {
		int next;
		while (loc != 0) {
			next = nativeCode.get(loc) & 0x0000ffff; // extract next element of
														// linked list
			Fixup(loc);
			loc = next;
		}
	}

	private void PutF1(int op, int a, int b, int c) {
		nativeCode.add(pc++, op << 26 | a << 21 | b << 16 | c & 0xffff);
	}

	private void PutF2(int op, int a, int b, int c) {
		nativeCode.add(pc++, op << 26 | a << 21 | b << 16 | c & 0x1f);
	}

	private void PutF3(int op, int c) {
		nativeCode.add(pc++, op << 26 | c & 0xffffff);
	}

	public int[] getProgram() {
		int[] ret = new int[nativeCode.size()];
		for (int i = 0; i < nativeCode.size(); i++) {
			ret[i] = nativeCode.get(i);
		}
		return ret;
	}

	private void writeBin(String outFile) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(outFile));
			for (int i = 0; i < nativeCode.size(); i++) {
				if (i > 0) {
					out.write("\n");
				}
				out.write(nativeCode.get(i).toString());
				
				if (DEBUG && DEBUGMESG.containsKey(i)){
					for (String line:DEBUGMESG.get(i).split(System.getProperty("line.separator")) ){
						out.write("\n# "+line + "");
					}
//					out.write("\n# "+i+": "+DLX.disassemble(nativeCode.get(i)) +"\n");
				}
			}
			out.close();
		} catch (IOException e) {
		    System.err.println(outFile);
			e.printStackTrace();
		}

	}


	private void AddDebug(String string) {
		if (!DEBUGMESG.containsKey(pc)){
			DEBUGMESG.put(pc, "");
		}else{
			DEBUGMESG.put(pc, DEBUGMESG.get(pc) + "\n");
		}
		DEBUGMESG.put(pc, DEBUGMESG.get(pc) + string);
	}
	
	public void Error(String errorMsg) {
		System.err.println("PC = " + pc + " Compiler error: " + errorMsg);
	}
}