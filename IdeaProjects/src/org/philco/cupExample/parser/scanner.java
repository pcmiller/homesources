package org.philco.cupExample.parser;

/* Written by phil on 5/6/14. */

// Simple Example Scanner Class

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
import org.philco.animals.sym;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class scanner {
    static InputStream inputStream = System.in;

    public static void main(String[] args) {
        boolean do_debug_parse = false;

        if ( args.length < 1) {
            System.err.println("No input file specified.");
            System.exit(1);
        }

        for ( String arg : args ) {
            if ( "-d".equals(arg))
                do_debug_parse = true;
            else if (arg.startsWith("-")) {
                System.err.println("Unrecognized flag " + arg);
                System.exit(1);
            } else {
                scanner.setInputStream(arg);
            }
        }

        Symbol symbol;
        try {
            scanner.init();
            boolean EOF_not_found = true;
            while ( EOF_not_found ) {
                symbol = scanner.next_token();
                if ( symbol == null )
                    break;
                System.out.println("token: " + symbol.toString());
                if ( symbol.sym == sym.EOF )
                    EOF_not_found = false;
            }

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

    public static void setInputStream(String inputFile) {
        try {
            inputStream = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            System.err.println("No such file: " + inputFile);
            e.printStackTrace();
        }
    }

    /* single lookahead character */
    protected static int next_char;

    // since cup v11 we use SymbolFactories rather than Symbols
    private static SymbolFactory sf = new ComplexSymbolFactory();

    /* advance input by one character */
    protected static void advance()
            throws java.io.IOException
    { next_char = inputStream.read(); }

    /* initialize the scanner */
    public static void init()
            throws java.io.IOException
    { advance(); }

    /* recognize and return the next complete token */
    public static Symbol next_token()
            throws java.io.IOException
    {
        for (;;)
            switch (next_char)
            {
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
	      /* parse a decimal integer */
                int i_val = 0;
                do {
                    i_val = i_val * 10 + (next_char - '0');
                    advance();
                } while (next_char >= '0' && next_char <= '9');
                return sf.newSymbol("NUMBER",sym.NUMBER, i_val);

                case ';': advance(); return sf.newSymbol("SEMI", sym.SEMI);
                case '+': advance(); return sf.newSymbol("PLUS",sym.PLUS);
                case '-': advance(); return sf.newSymbol("MINUS",sym.MINUS);
                case '*': advance(); return sf.newSymbol("TIMES",sym.TIMES);
                case '/': advance(); return sf.newSymbol("DIVIDE",sym.DIVIDE);
                case '%': advance(); return sf.newSymbol("MOD",sym.MOD);
                case '(': advance(); return sf.newSymbol("LPAREN",sym.LPAREN);
                case ')': advance(); return sf.newSymbol("RPAREN",sym.RPAREN);

                case -1: return sf.newSymbol("EOF",sym.EOF);

                default:
	      /* in this simple scanner we just ignore everything else */
                    advance();
                    break;
            }
    }
}
