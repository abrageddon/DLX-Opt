package oldDLX;


public class TestScanner {

    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("Usage: java TestScanner <file>");
            return;
        }

        java.util.Map< Integer, String > symMap =
            new java.util.HashMap< Integer, String >();
        symMap.put(  0, "error");
        symMap.put(  1, "times");
        symMap.put(  2, "div");
        symMap.put( 11, "plus");
        symMap.put( 12, "minus");
        symMap.put( 20, "eql");
        symMap.put( 21, "neq");
        symMap.put( 22, "lss");
        symMap.put( 23, "geq");
        symMap.put( 24, "leq");
        symMap.put( 25, "gtr");
        symMap.put( 30, "period");
        symMap.put( 31, "comma");
        symMap.put( 32, "openbracket");
        symMap.put( 34, "closebracket");
        symMap.put( 35, "closeparen");
        symMap.put( 40, "becomes");
        symMap.put( 41, "then");
        symMap.put( 42, "do");
        symMap.put( 50, "openparen");
        symMap.put( 60, "number");
        symMap.put( 61, "ident");
        symMap.put( 70, "semi");
        symMap.put( 80, "end");
        symMap.put( 81, "od");
        symMap.put( 82, "fi");
        symMap.put( 90, "else");
        symMap.put(100, "call");
        symMap.put(101, "if");
        symMap.put(102, "while");
        symMap.put(103, "return");
        symMap.put(110, "var");
        symMap.put(111, "arr");
        symMap.put(112, "function");
        symMap.put(113, "proc");
        symMap.put(150, "begin");
        symMap.put(200, "main");
        symMap.put(255, "eof");

        Scanner scn = new Scanner(args[0]);
        for (;;) {
            if (!symMap.containsKey(scn.sym)) {
                scn.Error("unknown symbol: " + Integer.toString(scn.sym));
            } else {
                System.out.print(symMap.get(scn.sym));
                if (scn.sym == 60) {
                    System.out.print("[" + Integer.toString(scn.val) + "]");
                }
                if (scn.sym == 61) {
                    System.out.print("[" + scn.Id2String(scn.id) + "]");
                }
                System.out.println();
            }

            if (scn.sym == 255)
                break;
            scn.Next();
        }
    }
}

