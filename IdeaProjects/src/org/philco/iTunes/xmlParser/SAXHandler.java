package org.philco.iTunes.xmlParser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class SAXHandler extends DefaultHandler {
 	private boolean tagDict = false;
	private boolean tagKey = false;
	private boolean tagString = false;
	private boolean tagInteger = false;
	private boolean tagDate = false;
	private boolean tagPlist = false;
	private boolean tagTrue = false;
    private boolean tagFalse = false;

//	int numElements = 0;
//	int MAXELEMENTS = 1000;
	
	Stack<Element> elementStack = new Stack<Element>();
	Element currentElement = null;
	Element parentElement = null;
	Element rootElement = null;

    ArrayList<track>tracks = new ArrayList<track>();
    track currentTrack;
    Element key, value;

	public Element getRootElement() {
		return rootElement;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		HashMap<String,String>attrs = new HashMap<String,String>();

		if (attributes.getLength() > 0) {
			for (int i = 0; i < attributes.getLength(); i++) {
				attrs.put(attributes.getLocalName(i), attributes.getValue(i));
			}
		}

		if (qName.equalsIgnoreCase("dict")) {
			tagDict = true;
            currentTrack = new track();
		}

		else if (qName.equalsIgnoreCase("key")) {
			tagKey = true;
		}

		else if (qName.equalsIgnoreCase("string")) {
			tagString = true;
		}

		else if (qName.equalsIgnoreCase("integer")) {
			tagInteger = true;
		}

		else if (qName.equalsIgnoreCase("true")) {
			tagTrue = true;
		}

        else if (qName.equalsIgnoreCase("false")) {
            tagFalse = true;
        }

        else if (qName.equalsIgnoreCase("date")) {
			tagDate = true;
		}

		else if (qName.equalsIgnoreCase("plist")) {
			tagPlist = true;
		}

		else {
			System.err.println("Error: unrecognized tag " + qName);
			System.exit(1);
		}
//		if (TestParseXMLFileWithSAX.isParsing()) {
			parentElement = currentElement;
			currentElement = new Element(qName);
			if (attributes.getLength() > 0) {
				currentElement.setAttributes(attrs);
			}

			elementStack.push(currentElement);
//		}
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {
		
		String chars = new String(ch, start, length);
		if (! chars.trim().isEmpty()) {
//			if (TestParseXMLFileWithSAX.isParsing())
				currentElement.setValue(chars.trim());
		}
		
		if ( parentElement != null )
//			if (TestParseXMLFileWithSAX.isParsing())
				parentElement.addChild(currentElement);

		if (tagDict) {
			tagDict = false;
            tracks.add(currentTrack);
            currentTrack = null;
		}

		if (tagKey) {
			tagKey = false;
            key = currentElement;
		}

		// Doesn't handle Michael &#38; Evo
		if (tagString) {
			tagString = false;
            value = currentElement;
		}

		if (tagInteger) {
			tagInteger = false;
            value = currentElement;
		}

        if (tagTrue) {
            tagTrue = false;
            value = currentElement;
        }

        if (tagFalse) {
            tagFalse = false;
            value = currentElement;
        }

        if (tagDate) {
			tagDate = false;
            value = currentElement;
		}

		if (tagPlist) {
			tagPlist = false;
//			if (TestParseXMLFileWithSAX.isParsing())
				rootElement = currentElement;
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		System.out.println(currentElement);
        if ( ( key != null ) && (value != null) ) {
            try {
                currentTrack.add(key.getValue(), value.getValue());
                System.out.println(currentTrack);
            } catch (UnrecognizedElementException e) {
                System.err.println(e.getMessage());
            }
            key = null;
            value = null;
        }
//		if (TestParseXMLFileWithSAX.isParsing())
			parentElement = elementStack.pop();
	}
}