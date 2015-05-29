package org.philco.animals;

import org.philco.pullParser.XMLToken;
import org.philco.pullParser.XmlPullParser;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;

/**
 * Original version by phil on 4/26/14.
 */
public class AnimalParser {
    static XmlPullParser pullParser;

    public static void main(String[] args) throws XMLStreamException {
        String inputFile = "animals.xml";
        try {
            pullParser = new XmlPullParser( inputFile );
            readXml( new XmlPullParser( inputFile ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Actually performs the read operation on the XML file by repeatedly
     * calling hasNext() and next() until the document ends.
     *
     * @throws XMLStreamException if the parsing fails.
     */
    public static void readXml(XmlPullParser pullParser) throws XMLStreamException {
        // iterate by calling hasNext in a loop until there are no more
        // elements left to process.
        XMLToken nextToken = pullParser.readXmlElement();
        while ( nextToken != XmlPullParser.eofToken ) {
            if ( ! nextToken.isComplete() ) {
                ArrayList<XMLToken>children = new ArrayList<XMLToken>();
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
}
