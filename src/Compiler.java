
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Steven Neisius
 */
public class Compiler {

    private static Scanner scn;
    private static HashMap<Integer, Integer> vars;
//    private static Queue<Integer> inputs;

    private static int pc;
    private static int[] buf;
    private static boolean[] R = new boolean [32];

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
    
    private static int basereg = 0;

    Compiler(String filename) {
        if (filename != null && !filename.isEmpty()) {
            System.out.println("Usage: java Compiler <file>");
            return;
        }

        scn = new Scanner(filename);
        vars = new HashMap<Integer, Integer>();

        pc = 0;
        buf = new int[2500];//FIXME size of memory buffer

        CodeParser parser = new CodeParser();

        if (parser.parseFile(filename)) {
            computation();//compile program
        }
    }

    public static void Error(String errorMsg) {
        System.err.println("Interpreter error: " + errorMsg);
    }

    private static boolean computation() {
        //computation = “main” [ varDecl ] “{” statSequence “}” “.” .
        boolean rtn = true;

        CheckFor(Scanner.mainToken);// "main"

        scn.Next();

        if (scn.sym == Scanner.varToken) {// "var" [VarDecl]
            rtn = rtn & varDecl();
        }

        CheckFor(Scanner.openbracketToken);// "{"

        scn.Next();
        rtn = rtn & statSequence();

        CheckFor(Scanner.closebracketToken);// "}"

        scn.Next();
        CheckFor(Scanner.periodToken);// "."

        if (!rtn) {
            Error("computation");
        }
        return rtn;
    }

    private static boolean varDecl() {
        //varDecl = “var” ident { “,” ident } “;” .
        boolean rtn = true;

        CheckFor(Scanner.varToken); // var

        scn.Next();
        CheckFor(Scanner.identToken); // ident
        vars.put(scn.id, 0);//TODO save var to memory

        scn.Next();
        while (scn.sym == 31) {// ","
            scn.Next();
            CheckFor(Scanner.identToken); // ident
            vars.put(scn.id, 0);//TODO save var to memory

            scn.Next();
        }

        CheckFor(Scanner.semiToken);// ";"

        scn.Next();

        if (!rtn) {
            Error("varDecl");
        }
        return rtn;
    }

    private static boolean statSequence() {
        //statSequence = statement { “;” statement }.
        boolean rtn = true;

        rtn = rtn & statement();

        while (scn.sym == Scanner.semiToken) {// ";"
            scn.Next();
            rtn = rtn & statement();
        }

        if (!rtn) {
            Error("statSequence");
        }
        return rtn;
    }

    private static boolean statement() {
        //statement = assignment | funcCall | ifStatement | whileStatement .
        boolean rtn = true;

        Result x;

        if (scn.sym == Scanner.ifToken) {// if
            scn.Next();
            Result follow = new Result();
            follow.fixuplocation = 0;

            x = relation();
            CondNegBraFwd(x);
            CheckFor(Scanner.thenToken);
            statSequence();

            while (scn.sym == Scanner.elseToken) {//elsif??
                scn.Next();
                UnCondBraFwd(follow);
                Fixup(x.fixuplocation);
                x = relation();
                CondNegBraFwd(x);
                CheckFor(Scanner.thenToken);
                statSequence();
            }

            if (scn.sym == Scanner.elseToken) {
                UnCondBraFwd(follow);
                Fixup(x.fixuplocation);
                statSequence();
            } else {
                Fixup(x.fixuplocation);
            }


            CheckFor(Scanner.fiToken);
            FixAll(follow.fixuplocation);//FIXME Works?

        } else if (scn.sym == Scanner.whileToken) {
            scn.Next();
            int looplocation = pc;

            x = relation();
            CondNegBraFwd(x);
            CheckFor(Scanner.doToken);
            statSequence();
            PutF1(BEQ, 0, 0, looplocation - pc);
            Fixup(x.fixuplocation);
            CheckFor(Scanner.odToken);

        } else if (scn.sym == Scanner.callToken) { // call
            x = funcCall();
        } else if (scn.sym == Scanner.identToken) { // ident
            x = assignment();
        } else {// empty statement invalid
            rtn = false;
        }

        if (!rtn) {
            Error("statement");
        }
        return rtn;
    }

//    private static void ifStatement() {//UNUSED ?
//        //ifStatement = “if” relation “then” statSequence [ “else” statSequence ] “fi”.
//        CheckFor(Scanner.ifToken); // if
//
//        scn.Next();
//
//        Result cond = relation();
//
//        CheckFor(Scanner.thenToken); // then
//
//
//
//        CheckFor(Scanner.fiToken);// fi
//
//        scn.Next();
//
//    }

    private static Result funcCall() {
        //funcCall = “call” ident [ “(“ [expression { “,” expression } ] “)” ].
        boolean rtn = true;

        Result x;

        ArrayList<Result> funcArgs = new ArrayList<Result>();

        CheckFor(Scanner.callToken); // call

        scn.Next();
        CheckFor(Scanner.identToken); // ident
        int func = scn.id;

        scn.Next();
        if (scn.sym == Scanner.openparenToken) { // "("
            scn.Next();

            if (!(scn.sym == Scanner.closeparenToken)) { // ")"
                funcArgs.add(expression());
                while (scn.sym == Scanner.commaToken) {// ","
                    scn.Next();
                    funcArgs.add(expression());//TODO load variables to memory
                }
            }

            CheckFor(Scanner.closeparenToken); // ")"

            scn.Next();
        }

        //TODO Jump to subroutine
        //TODO return path

        if (!rtn) {
            Error("funcCall");
        }
        return execFunc(func, funcArgs);
    }

    private static Result assignment() {
        //assignment = ident “<-” expression.
        Result x = new Result();

        CheckFor(scn.identToken); // ident
        Integer current = scn.id;//TODO get var's mem address

        if (!vars.containsKey(current)) {
            Error("unknown identifier: " + scn.Id2String(current));
        }

        scn.Next();
        CheckFor(Scanner.becomesToken); // "<-"

        scn.Next();
        vars.put(current, expression().value); // expression
        //TODO load value to memory location

        return x;
    }

    private static Result expression() {
        //expression = term {(“+” | “-”) term}.
        Result x, y;
        int op;

        x = term();

        while (scn.sym == Scanner.plusToken || scn.sym == Scanner.minusToken) { // "+" or "-"
            op = scn.sym;
            scn.Next();
            y = term();
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
                y = expression();
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
            y = factor();
            Compute(op, x, y);
        }

        return x;
    }

    private static Result factor() {
        //factor = ident | number | “(“ expression “)” | funcCall .
        Result x = new Result();
        boolean rtn = true;

        if (scn.sym == Scanner.numberToken) { // number
            x.value = scn.val;
            x.setConst();
            scn.Next();
        } else if (scn.sym == Scanner.identToken) { // ident
            x.address = vars.get(scn.id);//FIXME get identifier address
            x.setVar();
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

    private static Result execFunc(Integer func, ArrayList<Result> funcArgs) {
        int rtn = 0;
        String funcName = scn.Id2String(func);

        /* RDD a R.a := read a decimal number from the input F2 50
         * WRD b write the contents of R.b to the output in decimal F2 51
         * WRL start a new line on the output F1 53
         */
        Result x = new Result();

        if (funcName.equals("outputnum")) {
            PutF2(WRD, 0, funcArgs.get(0).value, 0);
        } else if (funcName.equals("outputnewline")) {
            PutF1(WRL, 0, 0, 0);
        } else if (funcName.equals("inputnum")) {
            //TODO get mem location, read number to it as var
        }
        return x;
    }

    private static void Compute(int op, Result x, Result y) {
        if (x.isConst() && y.isConst()) {
            switch (op) {
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
            if (x.regno == 0) {
                x.regno = AllocateReg();
                PutF1(ADD, x.regno, 0, 0);
            }
            if (y.isConst()) {
                PutF1(opCodeImm(op), x.regno, x.regno, y.value);
            } else {
                load(y);
                PutF1(opCode(op), x.regno, x.regno, y.regno);
                Deallocate(y);
            }
        }
    }

    private static void PutF1(int op, int a, int b, int c) {
        buf[pc++] = op << 26 | a << 21 | b << 16 | c & 0xffff;
    }

    private static void PutF2(int op, int a, int b, int c) {
        buf[pc++] = op << 26 | a << 21 | b << 16 | c & 0x1f;
    }

    private static void PutF3(int op, int c) {
        buf[pc++] = op << 26 | c & 0xffffff;
    }
    
    private static void CheckFor(int token) {
        if (scn.sym != token) {
            Error("Syntax Error");
        }
    }

    private static void load(Result x) {
        if (x.isVar()) {
            x.regno = AllocateReg();
            PutF1(LDW, x.regno, basereg, x.address);
            x.setReg();
        } else if (x.isConst()) {
            if (x.address == 0) {
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
        PutF1(BEQ, 0, 0, x.fixuplocation);//Build loked list by storing previous value
        x.fixuplocation = pc - 1;
    }

    private static void Fixup(int loc) {
        buf[loc] = buf[loc] & 0xffff0000 + (pc - loc);
    }

    private static void FixAll(int loc) {
        int next;
        while (loc != 0) {
            next = buf[loc] & 0x0000ffff; //extract next element of linked list
            Fixup(loc);
            loc = next;
        }
    }

    private static void Deallocate(Result y) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static int AllocateReg() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private static int opCodeImm(int op) {
        int regCode = opCode(op);
        if (regCode != ERR){
            return regCode + 16;
        }
        return ERR;
    }

    private static int opCode(int op) {
        switch(op){
            case Scanner.plusToken:
                return ADD;
            case Scanner.minusToken:
                return SUB;
            case Scanner.timesToken:
                return MUL;
            case Scanner.divToken:
                return DIV;
        }
        return ERR;
    }

    private static int negatedBranchOp(int cond) {
        switch(cond){
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

    int[] getProgram() {
        return buf;
    }

}
