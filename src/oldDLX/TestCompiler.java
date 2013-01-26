package oldDLX;


import java.io.*;
import java.util.*;

public class TestCompiler {
    public static void main(String args[]) {
        if (args.length < 2) {
            System.err.println("Usage: TestCompiler <code file> <data file>");
            return;
        }

        try {
            // Redirect System.in from DLX to data file
            InputStream origIn = System.in,
                        newIn = new BufferedInputStream(
                                new FileInputStream(args[1]));
            System.setIn(newIn);

            Compiler p = new Compiler(args[0]);
            int prog[] = p.getProgram();
            if (prog == null) {
                System.err.println("Error compiling program!");
                return;
            }

            DLX dlx = new DLX();
            dlx.load(prog);
            dlx.displayProgram();
            dlx.execute();

            System.setIn(origIn);
            newIn.close();
        } catch (IOException e) {
            System.err.println("Error reading input files!");
        }
    }

}

