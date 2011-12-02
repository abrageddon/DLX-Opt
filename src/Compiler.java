
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Steven Neisius
 */
public class Compiler {

    private static Scanner scn;
//    private static ArrayList<Integer> vars;
    private static HashMap<String, Function> funcs;
    private static String scope;
//    private static Queue<Integer> inputs;
    private static int pc;
    private static ArrayList<Integer> buf;
    private static boolean[] R = new boolean[32];
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
    static final int ERR = 63; // error opcode which is insertered by loader


    public Compiler(String filename) {
        if (filename != null && filename.isEmpty()) {
            Error("Usage: java Compiler <file>");
            return;
        }

        scn = new Scanner(filename);
//        vars = new ArrayList<Integer>();

        funcs = new HashMap<String, Function>();
        funcs.put("main", new Function(0, false));//main doesnt return a value

        pc = 0;
        buf = new ArrayList<Integer>();// size of memory buffer

        computation();//compile program
    }

    public static void Error(String errorMsg) {
        System.err.println("PC = " + pc + " Compiler error: " + errorMsg);
    }

    private static boolean computation() {
        //computation = “main” [ varDecl ] { funcDecl } “{” statSequence “}” “.” .
        //TODO computation = “main” { varDecl } { funcDecl } “{” statSequence “}” “.” .
        boolean rtn = true;

        for (int i = 0; i < R.length; i++) {
            R[i] = true;
        }
        R[0] = false;//block special registers
        R[FP] = false;
        R[SP] = false;
        R[GV] = false;
        R[RA] = false;

        CheckFor(Scanner.mainToken);// "main"
        //Setup Frame Pointer
        PutF1(ADDI, FP, GV, 0);
        PutF1(ADDI, SP, GV, 0);
        scope = "main";
        scn.Next();

        while (scn.sym == Scanner.varToken || scn.sym == Scanner.arrToken) {// { varDecl }
            rtn = rtn & varDecl();
        }

        //TODO make one output
        //Setup Global Variables
        for (int i = 0; i < funcs.get(scope).getVarNum(); i++) {
            Push(new Result());
        }
        //Setup Arrays
        for (int i = 0; i < funcs.get(scope).getArraysSize(); i++) {
            Push(new Result());
        }

        if (scn.sym == Scanner.procToken || scn.sym == Scanner.funcToken) {// funcDecl

            Result start = new Result();
            start.fixuplocation = 0;
            //Skip functions when running
            UnCondBraFwd(start);

            while (scn.sym == Scanner.procToken || scn.sym == Scanner.funcToken) {
                rtn = rtn & funcDecl();
            }

            Fixup(start.fixuplocation);
            scope = "main";//return to global scope
        }


        CheckFor(Scanner.beginToken);// "{"
        scn.Next();

        rtn = rtn & statSequence();

        CheckFor(Scanner.endToken);// "}"

        scn.Next();
        CheckFor(Scanner.periodToken);// "."

        PutF2(RET, 0, 0, 0);//END PROGRAM!

        if (!rtn) {
            Error("computation");
        }
        return rtn;
    }

    private static boolean varDecl() {
        //varDecl = “var” ident { “,” ident } “;” .
        //TODO varDecl = typeDecl ident { “,” ident } “;” .

        boolean rtn = true;



        //TODO typeDecl = “var” | “array” “[“ number “]” { “[“ number “]” }.
        if (scn.sym == Scanner.varToken) { // var
            scn.Next();

            CheckFor(Scanner.identToken); // ident
            AddVar(scn.id);
            scn.Next();


            //TODO copy down
            while (scn.sym == Scanner.commaToken) {// ","
                scn.Next();

                CheckFor(Scanner.identToken); // ident
                AddVar(scn.id);
                scn.Next();
            }

        } else if (scn.sym == Scanner.arrToken) {// array
            scn.Next();

            ArrayList<Integer> arrayDim = new ArrayList<Integer>();
            scn.Next();

            do {
                CheckFor(Scanner.openbracketToken); // [
                scn.Next();

                CheckFor(Scanner.numberToken); // number
                arrayDim.add(scn.val);//Set dimention
                scn.Next();

                CheckFor(Scanner.closebracketToken); // ]
                scn.Next();
            } while (scn.sym == Scanner.openbracketToken);//while more dimentions


            while (scn.sym == Scanner.commaToken) {// ","
                scn.Next();

                //TODO while more idents
                CheckFor(Scanner.identToken); // ident
                AddArray(scn.id, arrayDim);
                scn.Next();
            }
        }

        CheckFor(Scanner.semiToken);// ";"
        scn.Next();

        if (!rtn) {
            Error("varDecl");
        }
        return rtn;
    }

    private static boolean funcDecl() {
        // funcDecl = (“function” | “procedure”) ident [formalParam] “;” funcBody “;” .
        boolean rtn = true;

        boolean isFunc = false;
        if (scn.sym == Scanner.funcToken) { // function
            isFunc = true;
        } else if (scn.sym == Scanner.procToken) {
            isFunc = false;
        } else {
            Error("funcDecl: not function or procedure");
        }
        scn.Next();

        CheckFor(Scanner.identToken); // ident
        scope = scn.Id2String(scn.id);
        if (funcs.containsKey(scope)) {
            Error("Overriding Function: " + scope);
        }
        funcs.put(scope, new Function(pc, isFunc));
        scn.Next();


        // formalParam = “(“ [ident { “,” ident }] “)”.
        if (scn.sym == Scanner.openparenToken) {
            scn.Next();
            if (scn.sym == Scanner.closeparenToken) {
            } else if (scn.sym == Scanner.identToken) {// ident
                AddParam(scn.id);
                scn.Next();

                while (scn.sym == Scanner.commaToken) {// ","
                    scn.Next();

                    CheckFor(Scanner.identToken); // ident
                    AddParam(scn.id);
                    scn.Next();
                }

                CheckFor(Scanner.closeparenToken);
            }
            scn.Next();
        }

        CheckFor(Scanner.semiToken);// ";"
        scn.Next();


        // funcBody = { varDecl } “{” [ statSequence ] “}”.


        while (scn.sym == Scanner.varToken || scn.sym == Scanner.arrToken) {// { varDecl }
            rtn = rtn & varDecl();
        }

        CheckFor(Scanner.beginToken);// "{"
        scn.Next();

        //Store Return Addrewss
        int paramNum = funcs.get(scope).getParamNum();
        PutF1(STW, RA, FP, (paramNum + 2) * 4);

        rtn = rtn & statSequence();

        CheckFor(Scanner.endToken);// "}"
        scn.Next();

        CheckFor(Scanner.semiToken);// ";"
        scn.Next();


        if (!isFunc) {
            //Load RA
            PutF1(LDW, RA, FP, (paramNum + 2) * 4);
            //Pop vars
            PutF1(ADDI, SP, FP, 0);
            //Load prev FP
//            PutF1(LDW, FP, FP, (paramNum + 3) * 4);
            //Go To RA
            PutF1(RET, 0, 0, RA);
        }


        if (!rtn) {
            Error("funcDecl");
        }
        return rtn;
    }

    private static boolean statSequence() {
        //statSequence = statement { “;” statement }.
        boolean rtn = true;

        Result x = statement();
        Deallocate(x);

        while (scn.sym == Scanner.semiToken) {// ";"
            scn.Next();
            x = statement();
            Deallocate(x);
        }

        if (!rtn) {
            Error("statSequence");
        }
        return rtn;
    }

    private static Result statement() {
        //statement = assignment | funcCall | ifStatement | whileStatement | returnStatement.

        Result x = new Result();

        if (scn.sym == Scanner.returnToken) {
            scn.Next();

            int paramNum = funcs.get(scope).getParamNum();
//            int varNum = funcs.get(scope).getVarNum();
            boolean isFunc = funcs.get(scope).isFunc();

            if (isFunc) {
                //Get return value
                x = expression();
                load(x);
                PutF1(STW, x.regno, FP, 4);
//                Deallocate(x);
            }

            //Func brach back

            //Load RA
            PutF1(LDW, RA, FP, (paramNum + 2) * 4);
            //Go To RA
            PutF1(RET, 0, 0, RA);


        } else if (scn.sym == Scanner.ifToken) {// if
            scn.Next();
            Result follow = new Result();
            follow.fixuplocation = 0;

            x = relation();
            CondNegBraFwd(x);
            Deallocate(x);
            CheckFor(Scanner.thenToken);
            scn.Next();

            statSequence();

//            while (scn.sym == Scanner.elseToken) {//elsif??
//                scn.Next();
//                UnCondBraFwd(follow);
//                Fixup(x.fixuplocation);
//                x = relation();
//                CondNegBraFwd(x);
//                CheckFor(Scanner.thenToken);
//                statSequence();
//            }

            if (scn.sym == Scanner.elseToken) {
                scn.Next();

                UnCondBraFwd(follow);
                Fixup(x.fixuplocation);
                statSequence();
            } else {
                Fixup(x.fixuplocation);
            }


            CheckFor(Scanner.fiToken);
            scn.Next();

            FixAll(follow.fixuplocation);

        } else if (scn.sym == Scanner.whileToken) {
            scn.Next();
            int looplocation = pc;

            x = relation();

            CondNegBraFwd(x);

            Deallocate(x);

            CheckFor(Scanner.doToken);
            scn.Next();

            statSequence();

            PutF1(BEQ, 0, 0, looplocation - pc);

            Fixup(x.fixuplocation);


            CheckFor(Scanner.odToken);
            scn.Next();

        } else if (scn.sym == Scanner.callToken) { // call
            x = funcCall();
        } else if (scn.sym == Scanner.identToken) { // ident
            x = assignment();
        }

        return x;
    }

    private static Result funcCall() {
        //funcCall = “call” ident [ “(“ [expression { “,” expression } ] “)” ].
        boolean rtn = true;

        Result x = new Result();

        ArrayList<Result> funcArgs = new ArrayList<Result>();

        CheckFor(Scanner.callToken); // call

        scn.Next();
        CheckFor(Scanner.identToken); // ident
        int func = scn.id;
        String funcName = scn.Id2String(func);
        int paramNum = 0;
        if (funcs.containsKey(funcName)) {
            paramNum = funcs.get(funcName).getParamNum();
        } else if (funcName.equals("outputnum")) {
            paramNum = 1;
        }

        scn.Next();
        if (scn.sym == Scanner.openparenToken) { // "("
            scn.Next();

            if (!(scn.sym == Scanner.closeparenToken)) { // ")"

                int i = 0;

                funcArgs.add(expression());

                //Store prev result
                Push(funcArgs.get(i));
                Deallocate(funcArgs.get(i));

                while (scn.sym == Scanner.commaToken) {// ","
                    scn.Next();

                    funcArgs.add(expression());
                    i++;

                    //Store prev result
                    Push(funcArgs.get(i));
                    Deallocate(funcArgs.get(i));

                }
            }

            CheckFor(Scanner.closeparenToken); // ")"
            scn.Next();
        }

        if (!rtn) {
            Error("funcCall");
        }

        for (int i = 0; i < paramNum; i++) //Get prev result
        {
            funcArgs.get(i).regno = AllocateReg();
            funcArgs.get(i).setReg();
            Pop(funcArgs.get(i));
        }

        x = execFunc(func, funcArgs);

        return x;
    }

    private static Result execFunc(Integer func, ArrayList<Result> funcArgs) {
        String funcName = scn.Id2String(func);

        /* RDI a R.a := read a decimal number from the input F2 50
         * WRD b write the contents of R.b to the output in decimal F2 51
         * WRL start a new line on the output F1 53
         */
        Result x = new Result();

        if (funcs.containsKey(funcName)) {
            // access functions

            int paramNum = funcs.get(funcName).getParamNum();
            int varNum = funcs.get(funcName).getVarNum();

            boolean isFunc = funcs.get(funcName).isFunc();


            //Store old FP
            Result oldFP = new Result();
            oldFP.setReg();
            oldFP.regno = FP;
            Push(oldFP);



            //Put RA
            Push(new Result());




            //Put Param, reverse order
            if (paramNum > 0) {
                for (int i = 0; i < paramNum; i++) {
                    //if const, then load to register
                    load(funcArgs.get(i));
                    //load word to mem
                    Push(funcArgs.get(i));

                    Deallocate(funcArgs.get(i));
                }
            }


            //Put RetVal on stack
//            if (isFunc) {
            Push(new Result());
//            }

            //Update FP
            PutF1(ADDI, FP, SP, 0);

            //Put vars
            if (varNum > 0) {
                for (int i = 0; i < varNum; i++) {
                    Push(new Result());
                }
            }

            //jump to function
            PutF1(JSR, 0, 0, funcs.get(funcName).getStartLine() * 4);

            //Function Happens

            //pop vars
            PutF1(ADDI, SP, FP, 0);

            //Load prev FP
            PutF1(LDW, FP, FP, (paramNum + 3) * 4);

            //IF func get return val
            if (isFunc) {
                //put ret val in x
                x.setReg();
                x.regno = AllocateReg();//just below funcFP
                Pop(x);
            } else {
                //remove empty return val
                Pop();
            }

            //Pop Parms

            if (paramNum > 0) {
                for (int i = paramNum - 1; i >= 0; i--) {
                    Pop();
                }
            }

            //POP RA
            Pop();

            //Restore oldFP
            oldFP.setReg();
            oldFP.regno = FP;//just below funcFP
            Pop(oldFP);




        } else if (funcName.equals("outputnum")) {
            x = funcArgs.get(0);
            if (!x.isReg()) {
                load(x);
            }
            PutF2(WRD, 0, funcArgs.get(0).regno, 0);
        } else if (funcName.equals("outputnewline")) {
            PutF1(WRL, 0, 0, 0);
        } else if (funcName.equals("inputnum")) {
            x.regno = AllocateReg();
            x.setReg();
            PutF2(RDI, x.regno, 0, 0);
        }
        return x;
    }

    private static Result assignment() {
        //assignment = ident “<-” expression.
        //TODO assignment = ident { “[“ expression “]” } “<-” expression.

        Result x = new Result();

        CheckFor(Scanner.identToken); // ident
        if (funcs.get(scope).containsParam(scn.id)) {
            x.setParam();
            x.address = GetParamAddress(scn.id);
        } else if (funcs.get(scope).containsVar(scn.id)) {
            x.setVar();
            x.address = GetVarAddress(scn.id);
        } else if (funcs.get("main").containsVar(scn.id)) {
            x.setGlobalVar();
            x.address = GetVarAddress(scn.id);
        } else if (funcs.get("main").containsArray(scn.id)) {
            x.setArray();
            //[x]...
            Result[] coord = new Result[funcs.get("main").getArrayDims(scn.id).length];
            for (int i=0;i<coord.length;i++){
                coord[i]=new Result();
                scn.Next();

                CheckFor(Scanner.openbracketToken); // [
                scn.Next();

                coord[i] = expression();//get dimetion
                scn.Next();

                CheckFor(Scanner.closebracketToken); // ]
                scn.Next();

            }

            x.address = GetArrayAddress(scn.id, coord);
        }

        if (x.address == Integer.MAX_VALUE) {
            Error("unknown identifier: " + scn.Id2String(scn.id));
        }

        scn.Next();
        CheckFor(Scanner.becomesToken); // "<-"

        scn.Next();
        Result y = expression();

        if (!y.isReg()) {
            load(y);
        }
        if (x.isVar()) {
            PutF1(STW, y.regno, FP, -x.address);
        } else if (x.isGlobalVar()) {
            PutF1(STW, y.regno, GV, -x.address);
        }
        Deallocate(y);

        return x;
    }

    private static int GetArrayAddress(int id, Result[] coord) {
        int[] maxDim = funcs.get(scope).getArrayDims(scn.id);
        int offset = funcs.get(scope).getArrayOffset(id);
        int address = 0 ;

        //TODO get address

        return offset + address;
    }

    private static Result expression() {
        //expression = term {(“+” | “-”) term}.
        Result x, y;
        int op;

        x = term();

        while (scn.sym == Scanner.plusToken || scn.sym == Scanner.minusToken) { // "+" or "-"
            op = scn.sym;
            scn.Next();

            //Store prev result
            Push(x);
            Deallocate(x);

            y = term();

            //Get prev result
            x.regno = AllocateReg();
            x.setReg();
            Pop(x);

            Compute(op, x, y);
        }
        return x;
    }

    private static Result relation() {
        //relation = expression relOp expression .
        Result x, y;
        int op;

        x = expression();

        switch (scn.sym) {
            case Scanner.eqlToken://==
            case Scanner.neqToken://!=
            case Scanner.lssToken://<
            case Scanner.geqToken://>=
            case Scanner.leqToken://<=
            case Scanner.gtrToken://>
                op = scn.sym;
                scn.Next();

                //Store prev result
                Push(x);
                Deallocate(x);

                y = expression();

                //Get prev result
                x.regno = AllocateReg();
                x.setReg();
                Pop(x);

                Compute(CMP, x, y);


                x.setCond();
                x.cond = op;
                x.fixuplocation = 0;
                break;
            default:
                Error("Relation: Syntax Error");
        }
        return x;
    }

    private static Result term() {
        //term = factor { (“*” | “/”) factor}.
        Result x, y;
        int op;

        x = factor();

        while (scn.sym == Scanner.timesToken || scn.sym == Scanner.divToken) { // "*" or "/"
            op = scn.sym;
            scn.Next();


            //Store prev result
            Push(x);
            Deallocate(x);

            y = factor();

            //Get prev result
            x.regno = AllocateReg();
            x.setReg();
            Pop(x);

            Compute(op, x, y);
        }

        return x;
    }

    private static Result factor() {
        //factor = ident | number | “(“ expression “)” | funcCall .
        //TODO factor = ident { “[“ expression “]” } | number | “(“ expression “)” | funcCall .

        Result x = new Result();
        boolean rtn = true;

        if (scn.sym == Scanner.numberToken) { // number
            x.value = scn.val;
            x.setConst();
            scn.Next();
        } else if (scn.sym == Scanner.identToken) { // ident
            if (funcs.get(scope).containsParam(scn.id)) {
                x.setParam();
                x.address = GetParamAddress(scn.id);
            } else if (funcs.get(scope).containsVar(scn.id)) {
                x.setVar();
                x.address = GetVarAddress(scn.id);
            } else if (funcs.get("main").containsVar(scn.id)) {
                x.setGlobalVar();
                x.address = GetVarAddress(scn.id);
            }
            scn.Next();
        } else if (scn.sym == Scanner.openparenToken) { // "("
            scn.Next();

            x = expression();

            CheckFor(Scanner.closeparenToken); // ")"
            scn.Next();

        } else if (scn.sym == Scanner.callToken) { // call
            x = funcCall();
        } else {
            rtn = false;
        }

        if (!rtn) {
            Error("factor");
        }
        return x;
    }

    private static void Compute(int scnOp, Result x, Result y) {
        if (x.isConst() && y.isConst()) {
            switch (scnOp) {
                case Scanner.plusToken:
                    x.value += y.value;
                    break;
                case Scanner.minusToken:
                    x.value -= y.value;
                    break;
                case Scanner.timesToken:
                    x.value *= y.value;
                    break;
                case Scanner.divToken:
                    x.value /= y.value;
                    break;
            }
        } else {
            load(x);
            if (!x.isReg() && x.regno == 0) {
                x.regno = AllocateReg();//TODO if regno zero?
                PutF1(ADD, x.regno, 0, 0);
            }
            if (y.isConst()) {
                PutF1(opCodeImm(scnOp), x.regno, x.regno, y.value);
            } else {
                load(y);
                PutF1(opCode(scnOp), x.regno, x.regno, y.regno);
                Deallocate(y);
            }
        }
    }

    private static void PutF1(int op, int a, int b, int c) {
        buf.add(pc++, op << 26 | a << 21 | b << 16 | c & 0xffff);
    }

    private static void PutF2(int op, int a, int b, int c) {
        buf.add(pc++, op << 26 | a << 21 | b << 16 | c & 0x1f);
    }

    private static void PutF3(int op, int c) {
        buf.add(pc++, op << 26 | c & 0xffffff);
    }

    private static boolean CheckFor(int token) {
        if (scn.sym != token) {
            Error("CheckFor: Syntax Error: " + scn.Id2String(token));
            return false;
        }
        return true;
    }

    private static void load(Result x) {
        if (x.isVar()) {
            x.regno = AllocateReg();
            PutF1(LDW, x.regno, FP, -(x.address));
            x.setReg();
        } else if (x.isGlobalVar()) {
            x.regno = AllocateReg();
            PutF1(LDW, x.regno, GV, -x.address);
            x.setReg();
        } else if (x.isParam()) {
            x.regno = AllocateReg();
            PutF1(LDW, x.regno, FP, (8 + x.address));
            x.setReg();
        } else if (x.isConst()) {
            if (x.value == 0) {
                x.regno = 0;
            } else {
                x.regno = AllocateReg();
                PutF1(ADDI, x.regno, 0, x.value);
            }
            x.setReg();
        }
    }

    private static void CondNegBraFwd(Result x) {
        x.fixuplocation = pc;
        PutF1(negatedBranchOp(x.cond), x.regno, 0, 0);
    }

    private static void UnCondBraFwd(Result x) {
        PutF1(BEQ, 0, 0, x.fixuplocation);//Build linked list by storing previous value
        x.fixuplocation = pc - 1;
    }

    private static void Fixup(int loc) {
        int part = (0xffff0000 + (pc - loc));
        int fixed = (buf.get(loc) | 0x0000ffff) & part;
        buf.set(loc, fixed);
    }

    private static void FixAll(int loc) {
        int next;
        while (loc != 0) {
            next = buf.get(loc) & 0x0000ffff; //extract next element of linked list
            Fixup(loc);
            loc = next;
        }
    }

    private static void Deallocate(Result y) {
        if (y.regno > 0 && y.regno < 28) {
            R[y.regno] = true;
        }
    }

    private static int AllocateReg() {
        for (int i = 1; i < R.length - 4; i++) {
            if (R[i] == true) {
                R[i] = false;
                return i;
            }
        }
        return -1;
    }

    private static int opCodeImm(int op) {
        int regCode = opCode(op);
        if (regCode != ERR) {
            return regCode + 16;
        }
        return ERR;
    }

    private static int opCode(int op) {
        switch (op) {
            case Scanner.plusToken:
                return ADD;
            case Scanner.minusToken:
                return SUB;
            case Scanner.timesToken:
                return MUL;
            case Scanner.divToken:
                return DIV;
            case CMP:
                return CMP;
        }
        return ERR;
    }

    private static int negatedBranchOp(int cond) {
        switch (cond) {
            case Scanner.eqlToken:
                return BNE;
            case Scanner.neqToken:
                return BEQ;
            case Scanner.lssToken:
                return BGE;
            case Scanner.leqToken:
                return BGT;
            case Scanner.geqToken:
                return BLT;
            case Scanner.gtrToken:
                return BLE;
        }
        return ERR;
    }

    private static int GetVarAddress(int id) {
        int ret = 0;

        if (funcs.get(scope).containsVar(id)) {
            ret = (funcs.get(scope).getVar(id)) * 4;
        } else if (!scope.equals("main") && funcs.get("main").containsVar(id)) {
            ret = (funcs.get("main").getVar(id)) * 4;
        } else {
            ret = Integer.MAX_VALUE;
            Error("GetVarAddress: Var does not exist: " + scn.Id2String(id));
        }

        return ret;
    }

    private static int GetParamAddress(int id) {
        int ret = 0;
        if (funcs.get(scope).containsParam(id)) {
            ret = (funcs.get(scope).getParam(id)) * 4;
        } else {
            ret = Integer.MAX_VALUE;
            Error("GetParamAddress: Var does not exist: " + scn.Id2String(id));
        }

        return ret;
    }

    private static void AddVar(int id) {
        funcs.get(scope).addVar(id);
    }

    private static void AddArray(int id, ArrayList<Integer> arrayDim) {
        funcs.get(scope).addArray(id, arrayDim);
    }

    private static void AddParam(int id) {
        funcs.get(scope).addParam(id);
    }

    public int[] getProgram() {
        int[] ret = new int[buf.size()];
        for (int i = 0; i < buf.size(); i++) {
            ret[i] = buf.get(i);
        }
        return ret;
    }

    private static void Push(Result x) {
        if (!x.isReg()) {
            load(x);
        }
        PutF1(STW, x.regno, SP, 0);
        PutF1(ADDI, SP, SP, -4);
    }

    private static void Pop(Result x) {
        PutF1(ADDI, SP, SP, 4);
        if (x.isReg()) {
            PutF1(LDW, x.regno, SP, 0);
        }
    }

    private static void Pop() {
        PutF1(ADDI, SP, SP, 4);
    }
}
