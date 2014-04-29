package org.philco.pullParser;

import javax.xml.stream.XMLStreamException;

/**
 * Original version by phil on 4/26/14.
 */
public class Tokenizer {
    public static void main(String[] args) throws XMLStreamException {
        String inputFile = "animals.xml";
        try {
            XmlPullParser pullParser = new XmlPullParser(inputFile);
            pullParser.readXml();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
