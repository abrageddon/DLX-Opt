package compiler.back.codeGen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import compiler.ir.cfg.*;
import compiler.ir.instructions.*;

public class CodeGenerator {

    private static final int FrameP = 28;
    private static final int StackP = 29;
    private static final int GlobalV = 30;
    private static final int ReturnA = 31;
    
    // Mnemonic-to-Opcode mapping
    static final String mnemo[] = {
        "ADD", "SUB", "MUL", "DIV", "MOD", "CMP", "ERR", "ERR", "OR", "AND", "BIC", "XOR", "LSH", "ASH", "CHK", "ERR",
        "ADDI", "SUBI", "MULI", "DIVI", "MODI", "CMPI", "ERRI", "ERRI", "ORI", "ANDI", "BICI", "XORI", "LSHI", "ASHI", "CHKI", "ERR",
        "LDW", "LDX", "POP", "ERR", "STW", "STX", "PSH", "ERR", "BEQ", "BNE", "BLT", "BGE", "BLE", "BGT", "BSR", "ERR",
        "JSR", "RET", "RDD", "WRD", "WRH", "WRL", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR", "ERR",
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
    static final int RDD = 50;
    static final int WRD = 51;
    static final int WRH = 52;
    static final int WRL = 53;
    static final int ERR = 63;
    
	private List<CFG> CFGs;
	
	private CFG mainCFG;
	private CFG currentCFG;

    private int pc;
    private ArrayList<Integer> nativeCode;
    private String outFile;

    public CodeGenerator(List<CFG> CFGs) {
    	this.CFGs = CFGs;
	}
    
	public void generateCode(String outFile) {
		this.outFile = outFile;
		setupProgram();
		setupGlobals();
		//TODO write to output
		
		processMain();
		
		
		PutF2(RET, 0, 0, 0);//END PROGRAM!
		
		writeBin(outFile);
    }



	

	
	
	
	
	
	
	
	

	private void processMain() {
		Iterator<BasicBlock> blocks = mainCFG.topDownIterator();
		while (blocks.hasNext()) {
			BasicBlock currentBlock = (BasicBlock) blocks.next();
			Iterator<Instruction> instructions = currentBlock.getInstructionsIterator();
			while (instructions.hasNext()) {
				Instruction instruction = (Instruction) instructions.next();
				produceCode(instruction);
			}
		}
	}

	private void produceCode(Instruction instruction) {
		if (instruction instanceof Immediate){
			Immediate ins = (Immediate)instruction;
	        PutF1(ADDI, ins.outputOp.regNumber, 0, ins.value);
	        
		}else if (instruction instanceof StoreValue){
			StoreValue ins = (StoreValue)instruction;
//	        PutF1(STX, 0, Instruction.resolve(ins.value).outputOp.regNumber, Instruction.resolve(ins.address.offset).outputOp.regNumber);
			//Global
	        PutF1(STW, Instruction.resolve(ins.value).outputOp.regNumber, GlobalV, GetVarAddress(ins.symbol.ident));
	        //TODO arrays, locals
	        
		}else if (instruction instanceof LoadValue){
			LoadValue ins = (LoadValue)instruction;
			//Global
	        PutF1(LDX, ins.outputOp.regNumber, GlobalV, GetVarAddress(ins.symbol.ident));
	        
		}else if (instruction instanceof Call){
			Call ins = (Call)instruction;
			if (ins.function.ident.equalsIgnoreCase("outputnum")){
				PutF2(WRD, 0, ins.args.get(0).outputOp.regNumber, 0);
			}else if (ins.function.ident.equalsIgnoreCase("outputnewline")){
				PutF2(WRL, 0, 0, 0);
			}else if (ins.function.ident.equalsIgnoreCase("inputnum")){
				PutF2(RDD, ins.outputOp.regNumber, 0, 0);
			} 
		}

	}

	private void setupProgram() {
        nativeCode = new ArrayList<Integer>();        
    	pc = 0;
        //Setup Frame Pointer at global value pointer
        PutF1(ADDI, FrameP, GlobalV, 0);
        PutF1(ADDI, StackP, GlobalV, 0);
        for (CFG cfg:CFGs){
        	if (cfg.label.equals("main")){
        		mainCFG = cfg;
        	}
        }
	}
	


	private void setupGlobals() {
        //Setup Global Variables
        for (int i = 0; i < mainCFG.getVarNum(); i++) {
            //Allocate memory
            PutF1(STW, 0, StackP, 0);
            PutF1(ADDI, StackP, StackP, -4);
        }
        //Setup Global Arrays
        for (int i = 0; i < mainCFG.getArraysSize(); i++) {
            //Allocate memory
            PutF1(STW, 0, StackP, 0);
            PutF1(ADDI, StackP, StackP, -4);
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

    private Result GetArrayAddress(int id, Result[] coord) {
        int[] maxDim;
        CFG arrayCFG = currentCFG;
        if (!currentCFG.containsArray(id)) {
        	arrayCFG = mainCFG;
        }
        maxDim = arrayCFG.getArrayDims(id);
        Result offset = new Result();

        offset.setConst();
        offset.value = (arrayCFG.getArrayOffset(id) + arrayCFG.getVarNum()) * 4;

        Result address = addAddress(0, maxDim, coord);
        load(address);//add offset to this reg

        //Negate address
        Result neg = new Result();
        neg.setConst();
        neg.value = -4;

        //TODO output code
//        Compute(Scanner.timesToken, address, neg);
        //add offset to the calculated address
//        Compute(Scanner.minusToken, address, offset);



        return address;
    }
    
    private static Result addAddress(int dim, int[] maxDim, Result[] coord) {
        if (dim >= maxDim.length) {
            return new Result();
        }

        Result address = new Result();

        if (dim == maxDim.length - 1) {//last dim
            address = coord[dim];

            return address;//just use regular value
        }

        address = coord[dim];

        Result tailDataSize = new Result();
        tailDataSize.setConst();
        tailDataSize.value = 1;

        for (int i = dim + 1; i < maxDim.length; i++) {
            tailDataSize.value *= maxDim[i];
        }

        Result subAddress = addAddress(dim + 1, maxDim, coord);


        //TODO output code
//        Compute(Scanner.timesToken, address, tailDataSize);

//        Compute(Scanner.plusToken, address, subAddress);

        return address;
    }
    
    private void Compute(int scnOp, Result x, Result y) {
        if (x.isConst() && y.isConst()) {
            switch (scnOp) {
                case ADD:
                    x.value += y.value;
                    break;
                case SUB:
                    x.value -= y.value;
                    break;
                case MUL:
                    x.value *= y.value;
                    break;
                case DIV:
                    x.value /= y.value;
                    break;
            }
        } else {
            //TODO load array vals properly
            load(x);
            if (!x.isReg() && x.regno == 0) {
                PutF1(ADD, x.regno, 0, 0);
            }
            if (y.isConst()) {
                PutF1(opCodeImm(scnOp), x.regno, x.regno, y.value);
            } else {
                load(y);
                PutF1(scnOp, x.regno, x.regno, y.regno);
            }
        }
    }

    private int opCodeImm(int op) {
        if (op != ERR) {
            return op + 16;
        }
        return ERR;
    }
    
    private void load(Result x) {
        if (x.isVar()) {
            PutF1(LDW, x.regno, FrameP, -(x.address));
            x.setReg();
        } else if (x.isGlobalVar()) {
            PutF1(LDW, x.regno, GlobalV, -x.address);
            x.setReg();
        } else if (x.isArray()) {
            PutF1(LDX, x.regno, FrameP, x.regno);
            x.setReg();
        } else if (x.isGlobalArray()) {
            PutF1(LDX, x.regno, GlobalV, x.regno);
            x.setReg();
        } else if (x.isParam()) {
            PutF1(LDW, x.regno, FrameP, (8 + x.address));
            x.setReg();
        } else if (x.isConst()) {
            PutF1(ADDI, x.regno, 0, x.value);
            x.setReg();
        }
    }
    
    private static int negatedBranchOp(int cond) {
        switch (cond) {
            case BEQ:
                return BNE;
            case BNE:
                return BEQ;
            case BLT:
                return BGE;
            case BLE:
                return BGT;
            case BGE:
                return BLT;
            case BGT:
                return BLE;
        }
        return ERR;
    }
    
    private void CondNegBraFwd(Result x) {
        x.fixuplocation = pc;
        PutF1(negatedBranchOp(x.cond), x.regno, 0, 0);
    }

    private void UnCondBraFwd(Result x) {
        PutF1(BEQ, 0, 0, x.fixuplocation);//Build linked list by storing previous value
        x.fixuplocation = pc - 1;
    }

    private void Fixup(int loc) {
        int part = (0xffff0000 + (pc - loc));
        int fixed = (nativeCode.get(loc) | 0x0000ffff) & part;
        nativeCode.set(loc, fixed);
    }

    private void FixAll(int loc) {
        int next;
        while (loc != 0) {
            next = nativeCode.get(loc) & 0x0000ffff; //extract next element of linked list
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
			for (int i=0; i<nativeCode.size(); i++){
				if (i>0){
					out.write("\n");
				}
				out.write(nativeCode.get(i).toString());
			}
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    public void Error(String errorMsg) {
        System.err.println("PC = " + pc + " Compiler error: " + errorMsg);
    }
}
