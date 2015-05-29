package org.philco.animals;

/* Written by phil on 5/6/14. */

import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.Symbol;
import java_cup.runtime.SymbolFactory;
import org.philco.pullParser.XMLToken;
import org.philco.pullParser.XmlPullParser;

import javax.xml.stream.XMLStreamException;
import java.io.*;
import java.util.ArrayList;

public class scanner {
//    static InputStream inputStream = System.in;
    static String inputFilename;
    static XmlPullParser pullParser;

    public static void setDebugScanner(boolean debugScanner) {
        scanner.debugScanner = debugScanner;
    }
    static boolean debugScanner = false;

    public static void main(String[] args) {

        if ( args.length < 1) {
            System.err.println("No input file specified.");
            System.exit(1);
        }

        System.out.println("Working directory is " + System.getProperty("user.dir"));

        for ( String arg : args ) {
            if ( "-x".equals(arg))
                setDebugScanner(true);
            else if (arg.startsWith("-")) {
                System.err.println("Unrecognized flag " + arg);
                System.exit(1);
            } else
                scanner.inputFilename = arg;
        }
        if ( ! new File(scanner.inputFilename).exists() ) {
            System.err.println("No such file: " + scanner.inputFilename);
            System.exit(1);
        } else {
            System.out.println("Found input file " + scanner.inputFilename);
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

//    public static void setInputStream(String inputFile) {
//        try {
//            inputStream = new FileInputStream(inputFile);
//        } catch (FileNotFoundException e) {
//            System.err.println("No such file: " + inputFile);
//            e.printStackTrace();
//        }
//    }

    /* single lookahead character */
//    protected static int next_char;

    // since cup v11 we use SymbolFactories rather than Symbols
    private static SymbolFactory sf = new ComplexSymbolFactory();

    /* advance input by one character */
    protected static void advance()
            throws IOException
    { /*next_char = inputStream.read();*/ }

    /* initialize the scanner */
    public static void init()
            throws IOException
    {
        try {
            pullParser = new XmlPullParser(inputFilename);
            readXml( pullParser );
        } catch ( Exception e ) {
            System.err.println("Scanner initialization failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        advance();
    }

    /**
     * Actually performs the read operation on the XML file by repeatedly
     * calling hasNext() and next() until the document ends.
     *
     * @throws javax.xml.stream.XMLStreamException if the parsing fails.
     */
    public static void readXml(XmlPullParser pullParser) throws XMLStreamException {
        // iterate by calling hasNext in a loop until there are no more
        // elements left to process.
        XMLToken nextToken = pullParser.readXmlElement();
        while ( nextToken != XmlPullParser.eofToken ) {
            if ( ! nextToken.isComplete() ) {
                ArrayList<XMLToken> children = new ArrayList<XMLToken>();
                nextToken = readXmlChild( pullParser, children );
                System.out.println( "token: " + nextToken.toString() );
            } else {
                System.out.println( "token: " + nextToken.toString() );
            }
            nextToken = pullParser.readXmlElement();
            System.out.println( "token: " + nextToken.toString() );
        }
    }

    private static XMLToken readXmlChild(XmlPullParser pullParser, ArrayList<XMLToken>children)  throws XMLStreamException {
        XMLToken nextToken = pullParser.readXmlElement();
        while ( ( nextToken != XmlPullParser.endToken ) &&
                ( nextToken != XmlPullParser.eofToken ) ) {
            children.add( nextToken );
            System.out.println( "child: " + nextToken.toString() );
            nextToken = pullParser.readXmlElement();
        }
        return nextToken;
    }

    static class XMLTokenToSymbol {
        String name;
        Integer id;

        XMLTokenToSymbol(String name, Integer id) {
            this.name = name;
            this.id = id;
        }
    }

    static XMLTokenToSymbol[] tokens = {
            new XMLTokenToSymbol("id", sym.ID),
            new XMLTokenToSymbol("animal", sym.ANIMAL),
            new XMLTokenToSymbol("name", sym.NAME),
            new XMLTokenToSymbol("location", sym.LOCATION),
            new XMLTokenToSymbol("type", sym.TYPE),
            new XMLTokenToSymbol("eof", sym.EOF),
    };
    static XMLTokenToSymbol xmlTokenToSymbolError =
            new XMLTokenToSymbol("error", sym.error);

    static XMLTokenToSymbol lookup(XMLToken xmlToken) {
        for (XMLTokenToSymbol xtts : tokens)
            if ( xtts.name.equals(xmlToken.getTagName()))
                return xtts;
        return xmlTokenToSymbolError;
    }

    /* recognize and return the next complete token */
    public static Symbol next_token()
            throws IOException
    {
        XMLToken xmlToken;
        XMLTokenToSymbol xmlTokenToSymbol;
        try {
            xmlToken = pullParser.readXmlElement();
            xmlTokenToSymbol = lookup(xmlToken);
        } catch (XMLStreamException xe) {
            throw new IOException("XMLStreamException: "+xe.getMessage());
        }
        switch (xmlTokenToSymbol.id) {
            case sym.ID:
            case sym.ANIMAL:
            case sym.NAME:
            case sym.LOCATION:
            case sym.TYPE:
            case sym.EOF:
                return sf.newSymbol(xmlTokenToSymbol.name,xmlTokenToSymbol.id);
            default:
                return sf.newSymbol(xmlTokenToSymbolError.name,
                                    xmlTokenToSymbolError.id);
        }
    }
}