package oldDLX;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Scanner {

    public int sym = -1;
    public int val;
    public int id;
    private PushbackReader in;
    private HashMap<Integer, String> identifiers;
    private Integer nextID;
    public static final int timesToken = 1;
    public static final int divToken = 2;
    public static final int plusToken = 11;
    public static final int minusToken = 12;
    public static final int eqlToken = 20;
    public static final int neqToken = 21;
    public static final int lssToken = 22;
    public static final int geqToken = 23;
    public static final int leqToken = 24;
    public static final int gtrToken = 25;
    public static final int periodToken = 30;
    public static final int commaToken = 31;
    public static final int openbracketToken = 32;
    public static final int closebracketToken = 34;
    public static final int closeparenToken = 35;
    public static final int becomesToken = 40;
    public static final int thenToken = 41;
    public static final int doToken = 42;
    public static final int openparenToken = 50;
    //Excluded due to special case
    public static final int numberToken = 60;
    public static final int identToken = 61;
    public static final int semiToken = 70;
    public static final int endToken = 80;
    public static final int odToken = 81;
    public static final int fiToken = 82;
    public static final int elseToken = 90;
    public static final int callToken = 100;
    public static final int ifToken = 101;
    public static final int whileToken = 102;
    public static final int returnToken = 103;
    public static final int varToken = 110;
    public static final int arrToken = 111;
    public static final int funcToken = 112;
    public static final int procToken = 113;
    public static final int beginToken = 150;
    public static final int mainToken = 200;
    public static final int eofToken = 255;

    public final void Next() {
        if (sym == 255) {
            Error("called Next() on EOF");
            return;
        }

        try {
            char currChar;

            currChar = (char) in.read();

            //Eat Whitespace
            while (Character.isWhitespace(currChar)) {
                currChar = (char) in.read();
            }

            //Identify Next Character
            if (currChar == (char) -1) {
                //End Of File
                sym = 255;
            } else if (Character.isDigit(currChar)) {
                //IS DIGIT
                sym = 60;
                String numberString = "";
                numberString += currChar;
                currChar = (char) in.read();
                while (Character.isDigit(currChar)) {
                    numberString += currChar;
                    currChar = (char) in.read();
                }

                in.unread(currChar);// replace next non digit

                val = Integer.valueOf(numberString);

            } else if (Character.isLetter(currChar)) {
                //IS LETTER
                String letterString = "";
                letterString += currChar;
                currChar = (char) in.read();
                while (Character.isLetterOrDigit(currChar)) {
                    letterString += currChar;
                    currChar = (char) in.read();
                }

                in.unread(currChar);//replace next non character

                id = String2Id(letterString);

                if (id > 255) {
                    sym = 61;
                } else {
                    sym = id;
                }

            } else {
                //IS NON-ALPHANUM
                String punctString = "";
                punctString += currChar;
                char nextChar = (char) in.read();
                if (((currChar == '<' || currChar == '>' || currChar == '=' || currChar == '!') && nextChar == '=')
                        || (currChar == '<' && nextChar == '-')) {
                    punctString += nextChar;
                } else {
                    in.unread(nextChar);//replace next non character
                }

                id = String2Id(punctString);

                if (id <= 255) {
                    sym = id;
                } else {
                    sym = 0;
                    Error("Unknown Punctuation " + punctString);
                }
            }
        } catch (IOException ex) {
            Error(ex.getMessage());
        }
    }

    public Scanner(String fileName) {
        try {
            //open file
            in = new PushbackReader(new FileReader(fileName));

            identifiers = new HashMap<Integer, String>();
            nextID = 256;//Variable ID's are symbols 256+

            // Preload Word Table
            identifiers.put(1, "*");//timesToekn
            identifiers.put(2, "/");//divToken

            identifiers.put(11, "+");//plusToken
            identifiers.put(12, "-");//minusToken

            identifiers.put(20, "==");//eqlToken
            identifiers.put(21, "!=");//neqToken
            identifiers.put(22, "<");//lssToken
            identifiers.put(23, ">=");//geqToken
            identifiers.put(24, "<=");//leqToken
            identifiers.put(25, ">");//gtrToken

            identifiers.put(30, ".");//periodToken
            identifiers.put(31, ",");//commaToken
            identifiers.put(32, "[");//openbracketToken
            identifiers.put(34, "]");//closebracketToken
            identifiers.put(35, ")");//closeparenToken

            identifiers.put(40, "<-");//becomesToken
            identifiers.put(41, "then");//thenToken
            identifiers.put(42, "do");//doToken

            identifiers.put(50, "(");//openparenToken

            //Excluded due to special case
//            identifiers.put(60, "number");//number
//            identifiers.put(61, "ident");//ident

            identifiers.put(70, ";");//semiToken

            identifiers.put(80, "}");//endToken
            identifiers.put(81, "od");//odToken
            identifiers.put(82, "fi");//fiToken

            identifiers.put(90, "else");//elseToken

            identifiers.put(100, "call");//callToken
            identifiers.put(101, "if");//ifToken
            identifiers.put(102, "while");//whileToken
            identifiers.put(103, "return");//returnToken

            identifiers.put(110, "var");//varToken
            identifiers.put(111, "array");//arrToken
            identifiers.put(112, "function");//funcToken
            identifiers.put(113, "procedure");//procToken

            identifiers.put(150, "{");//beginToken
            identifiers.put(200, "main");//mainToken

            //get first character
            Next();
        } catch (FileNotFoundException ex) {
            Error("File Not Found!");
        }
    }

    /**
     * Converts given id to name; returns null in case of error
     */
    public String Id2String(int id) {
        if (identifiers.containsKey(id)) {
            return identifiers.get(id);
        }

        // Error
        return null;
    }

    public final void Error(String errorMsg) {
        System.err.println("Scanner error: " + errorMsg);
        sym = 0;
    }

    /**
     * Converts given name to id; returns -1 in case of error
     */
    public int String2Id(String name) {
        int ret = -1;
        if (identifiers.containsValue(name)) {
            return getKeyByValue(identifiers, name);
        } else {
            ret = nextID;
            identifiers.put(nextID++, name);
        }

        // Error
        return ret;
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Entry<T, E> entry : map.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }
}
