package oldDLX;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Steven Neisius
 */
public class Parser {

    private static Scanner scn;
    private static HashMap<Integer, Integer> vars;
    private static Queue<Integer> inputs;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Interpreter <file>");
            return;
        }

        scn = new Scanner(args[0]);
        vars = new HashMap<Integer, Integer>();
        inputs = new LinkedList<Integer>();
        try {
            BufferedReader readIn = new BufferedReader(new FileReader(args[1]));
                while (readIn.ready()) {
                    String line = readIn.readLine();
                    if (!line.isEmpty()){
                        inputs.add(Integer.valueOf(Integer.valueOf(line)));
                    }
                }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }catch (IOException ex) {
                Logger.getLogger(Parser.class.getName()).log(Level.SEVERE, null, ex);
        }

        CodeParser parser = new CodeParser();

        if (parser.parseFile(args[0])){
            computation();
        }
    }

    public static void Error(String errorMsg) {
        System.err.println("Interpreter error: " + errorMsg);
    }

    private static boolean computation() {
        //computation = “main” [ varDecl ] “{” statSequence “}” “.” .
        boolean rtn = true;

        rtn = rtn & (scn.sym == 200);// "main"

        scn.Next();

        if (scn.sym == 110) {// "var" [VarDecl]
            rtn = rtn & varDecl();
        }

        rtn = rtn & (scn.sym == 150);// "{"

        scn.Next();
        rtn = rtn & statSequence();

        rtn = rtn & (scn.sym == 80);// "}"

        scn.Next();
        rtn = rtn & (scn.sym == 30);// "."

        if (!rtn) {
            Error("computation");
        }
        return rtn;
    }

    private static boolean varDecl() {
        //varDecl = “var” ident { “,” ident } “;” .
        boolean rtn = true;

        rtn = rtn & (scn.sym == 110); // var

        scn.Next();
        rtn = rtn & (scn.sym == 61); // ident
        vars.put(scn.id, 0);

        scn.Next();
        while (scn.sym == 31) {// ","
            scn.Next();
            rtn = rtn & (scn.sym == 61); // ident
            vars.put(scn.id, 0);

            scn.Next();
        }

        rtn = rtn & (scn.sym == 70);// ";"

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

        while (scn.sym == 70) {// ";"
            scn.Next();
            rtn = rtn & statement();
        }

        if (!rtn) {
            Error("statSequence");
        }
        return rtn;
    }

    private static boolean statement() {
        //statement = assignment | funcCall | ifStatement .
        boolean rtn = true;

        if (scn.sym == 101) {// if
            rtn = rtn & ifStatement();
        } else if (scn.sym == 100) { // call
            funcCall();
        } else if (scn.sym == 61) { // ident
            rtn = rtn & assignment();
        } else {// empty statement invalid
            rtn = false;
        }

        if (!rtn) {
            Error("statement");
        }
        return rtn;
    }

    private static boolean ifStatement() {
        //ifStatement = “if” relation “then” statSequence [ “else” statSequence ] “fi”.
        boolean rtn = true;


        rtn = rtn & (scn.sym == 101); // if

        scn.Next();

        boolean cond = relation();


        rtn = rtn & (scn.sym == 41); // then

        if (cond) {
            scn.Next();
            rtn = rtn & statSequence();
        } else {
            while ((scn.sym != 90 && scn.sym != 82)) {
                scn.Next();
                if (scn.sym == 101) {
                    int ifs = 1;
                    while (ifs != 0) {
                        scn.Next();
                        if (scn.sym == 101) {
                            ifs++;
                        } else if (scn.sym == 82) {
                            ifs--;
                        }
                    }
                    scn.Next();
                }
            }
        }
        //else noExecStats
        //match ifs++


        if (scn.sym == 90 && !cond) {// else
            scn.Next();
            rtn = rtn & statSequence();
        }

        while (scn.sym != 82) {
            scn.Next();
            if (scn.sym == 101) {
                int ifs = 1;
                while (ifs != 0) {
                    scn.Next();
                    if (scn.sym == 101) {
                        ifs++;
                    } else if (scn.sym == 82) {
                        ifs--;
                    }
                }
                scn.Next();
            }
        }

        rtn = rtn & (scn.sym == 82);// fi

        scn.Next();

        if (!rtn) {
            Error("ifStatement");
        }
        return rtn;
    }

    private static int funcCall() {
        //funcCall = “call” ident [ “(“ [expression { “,” expression } ] “)” ].
        boolean rtn = true;
        int func = 0;
        ArrayList<Integer> funcArgs = new ArrayList<Integer>();

        rtn = rtn & (scn.sym == 100); // call

        scn.Next();
        rtn = rtn & (scn.sym == 61); // ident
        func = scn.id;

        scn.Next();
        if (scn.sym == 50) { // "("
            scn.Next();

            if (!(scn.sym == 35)) { // ")"
                funcArgs.add(expression());
                while (scn.sym == 31) {// ","
                    scn.Next();
                    funcArgs.add(expression());
                }
            }

            rtn = rtn & (scn.sym == 35); // ")"

            scn.Next();
        }

        if (!rtn) {
            Error("funcCall");
        }
        return execFunc(func, funcArgs);
    }

    private static boolean assignment() {
        //assignment = ident “<-” expression.
        boolean rtn = true;

        rtn = rtn & (scn.sym == 61); // ident
        Integer current = scn.id;

        if (!vars.containsKey(current)) {
            Error("unknown identifier: " + scn.Id2String(current));
        }

        scn.Next();
        rtn = rtn & (scn.sym == 40); // "<-"

        scn.Next();
        vars.put(current, expression()); // expression

        if (!rtn) {
            Error("assignment");
        }
        return rtn;
    }

    private static int expression() {
        //expression = term {(“+” | “-”) term}.
        int value = 0;

        value = term();

        while (scn.sym == 11 || scn.sym == 12) { // "+" or "-"
            if (scn.sym == 11) {
                scn.Next();
                value += term();
            } else if (scn.sym == 12) {
                scn.Next();
                value -= term();
            }
        }
        return value;
    }

    private static boolean relation() {
        //relation = expression relOp expression .
        int lhs = expression();

        int operation = relOp();

        int rhs = expression();

        boolean rtn = false;

        switch (operation) {
            case 20://==
                rtn = lhs == rhs;
                break;
            case 21://!=
                rtn = lhs != rhs;
                break;
            case 22://<
                rtn = lhs < rhs;
                break;
            case 23://>=
                rtn = lhs >= rhs;
                break;
            case 24://<=
                rtn = lhs <= rhs;
                break;
            case 25://>
                rtn = lhs > rhs;
                break;
        }


        return rtn;
    }

    private static int term() {
        //term = factor { (“*” | “/”) factor}.
        int value = 0;

        value = factor();

        while (scn.sym == 1 || scn.sym == 2) { // "*" or "/"
            if (scn.sym == 1) {
                scn.Next();
                value *= factor();
            } else if (scn.sym == 2) {
                scn.Next();
                value /= factor();
            }
        }

        return value;
    }

    private static int relOp() {
        //relOp = “==“ | “!=“ | “<“ | “<=“ | “>“ | “>=“.
        boolean rtn = true;
        int value = 0;
        if (scn.sym < 20 || scn.sym > 25) {
            rtn = false;
        } else {
            value = scn.sym;
        }
        scn.Next();

        if (!rtn) {
            Error("relOp");
        }
        return value;
    }

    private static int factor() {
        //factor = ident | number | “(“ expression “)” | funcCall .
        boolean rtn = true;
        int value = 0;

        if (scn.sym == 60) { // number
            value = scn.val;
            scn.Next();
        } else if (scn.sym == 61) { // ident
            value = vars.get(scn.id);
            scn.Next();
        } else if (scn.sym == 50) { // "("
            scn.Next();
            value = expression();
            rtn = rtn & (scn.sym == 35); // ")"
            scn.Next();
        } else if (scn.sym == 100) { // call
            value = funcCall();
        } else {
            rtn = false;
        }

        if (!rtn) {
            Error("factor");
        }
        return value;
    }

    private static int execFunc(Integer func, ArrayList<Integer> funcArgs) {
        int rtn = 0;
        String funcName = scn.Id2String(func);

        if (funcName.equals("outputnum")) {
            rtn = funcArgs.get(0);
            System.out.print(rtn);
        } else if (funcName.equals("outputnewline")) {
            System.out.println();
        } else if (funcName.equals("inputnum")) {
            if (!inputs.isEmpty()) {
                rtn = inputs.remove();
            }
        }
        return rtn;
    }
}
