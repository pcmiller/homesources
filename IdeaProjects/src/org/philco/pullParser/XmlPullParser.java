package org.philco.pullParser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Stack;

/**
 * This is a simple XML stream reader example that is fairly much self
 * contained.
 */
public class XmlPullParser {

    XMLToken xmlToken;
    public static XMLToken endToken = new XMLToken ("</endToken>");
    public static XMLToken eofToken = new XMLToken("EOF");
    private StringBuilder currentText;
    private XMLStreamReader xmlReader;
    private Stack<Integer> savedEventStack;

    /**
     * Create the stream reader class and put in the mappings between
     * element names and the Element type (currentElement). This mapping
     * is used to work out how to handle each data type.
     */
    public XmlPullParser(String inputFile) throws Exception {
        savedEventStack = new Stack<Integer>();
        InputStream xmlStream = XmlPullParser.class.getResourceAsStream(inputFile);
        if ( xmlStream == null ) {
            System.err.println("Can't find xml resource for " + inputFile);
            System.exit(1);
        }

        XMLInputFactory factory = XMLInputFactory.newFactory();
        xmlReader = factory.createXMLStreamReader(xmlStream);
    }

    public XMLToken readXmlElement() throws XMLStreamException {
        XMLToken xmlTokenCopy;
        while (xmlReader.hasNext()) {
            // get the next event and process it.
            int eventType = ( ! savedEventStack.empty() )
              ? savedEventStack.pop()
              : xmlReader.next();

            switch (eventType) {
                case XMLEvent.CDATA:
                case XMLEvent.SPACE:
                case XMLEvent.CHARACTERS:
                    processText(xmlReader.getText());
                    break;
                case XMLEvent.END_ELEMENT:
                    ended(xmlReader.getLocalName());
                    xmlTokenCopy = xmlToken;
                    xmlToken = null;
                    if ( xmlTokenCopy == null )
                        return endToken;
                    else
                        return xmlTokenCopy;
                case XMLEvent.START_ELEMENT:
                    if ( xmlToken != null && ! xmlToken.isComplete() ) {
                        savedEventStack.push(eventType);
                        xmlTokenCopy = xmlToken;
                        xmlToken = null;
                        return xmlTokenCopy;
                    }
                    startElement(xmlReader);
                    break;
            }
        }
        return eofToken;
    }

    /**
     * Handles the start of a new XML element, so we can prepare for the new
     * element. In our case we clear down the text storage and set the element
     * type field.
     */
    private void startElement(XMLStreamReader xmlReader) {
        String localName = xmlReader.getLocalName();
        currentText = new StringBuilder(256);
        xmlToken = new XMLToken(localName);
        int attributes = xmlReader.getAttributeCount();
        for (int i = 0; i < attributes; ++i) {
            attribute(xmlReader.getAttributeLocalName(i),
                      xmlReader.getAttributeValue(i));
        }
    }

    /**
     * Called when text is found within the element, this may be whitespace,
     * text or CDATA.
     * @param text the text to be added to the current elements data.
     */
    private void processText(String text) {
        if (currentText != null) {
            currentText.append(text);
        }
    }

    /**
     * Called for each attribute in the start element call. With the name and
     * value.
     * @param localName the name of the attribute
     * @param value the value of the attribute
     */
    private void attribute(String localName, String value) {
        xmlToken.addAttribute(localName, value);
    }

    /**
     * Called each time an element ends, with the name of the element that
     * has just ended.
     * @param localName the element name that has ended
     */
    private void ended(String localName) {
        // find the element type, and see if we can process it.
        if ( xmlToken != null ) {
            xmlToken.setComplete ( true );
            xmlToken.addAttribute ( localName, currentText.toString () );
            currentText = null;
        }
    }
}
