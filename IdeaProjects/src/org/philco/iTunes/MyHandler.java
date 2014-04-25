package org.philco.iTunes;

import org.philco.iTunes.elements.Element;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Stack;

public class MyHandler extends DefaultHandler {
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
	
	public Element getRootElement() {
		return rootElement;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		HashMap<String,String>attrs = new HashMap<String,String>();
//		if ( ++numElements > MAXELEMENTS)
//			System.exit(1);
		
		if (attributes.getLength() > 0) {
//			String tag = "<" + qName;
			for (int i = 0; i < attributes.getLength(); i++) {
				attrs.put(attributes.getLocalName(i), attributes.getValue(i));
//				tag += " " + attributes.getLocalName(i) + "="
//						+ attributes.getValue(i);
			}
//			tag += ">";
		}

		if (qName.equalsIgnoreCase("dict")) {
			tagDict = true;
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
		if (ParseXMLFileWithSAX.isParsing()) {
			parentElement = currentElement;
			currentElement = new Element(qName);
			if (attributes.getLength() > 0) {
				currentElement.setAttributes(attrs);
			}

			elementStack.push(currentElement);
		}
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {
		
		String chars = new String(ch, start, length);
		if (! chars.trim().isEmpty()) {
			if (ParseXMLFileWithSAX.isParsing())
				currentElement.setValue(chars.trim());
		}
		
		if ( parentElement != null )
			if (ParseXMLFileWithSAX.isParsing())
				parentElement.addChild(currentElement);

		if (tagDict) {
			tagDict = false;
		}

		if (tagKey) {
			tagKey = false;
		}

		// Doesn't handle Michael &#38; Evo
		if (tagString) {
			tagString = false;
		}

		if (tagInteger) {
			tagInteger = false;
		}

        if (tagTrue) {
            tagTrue = false;
        }

        if (tagFalse) {
            tagFalse = false;
        }

        if (tagDate) {
			tagDate = false;
		}

		if (tagPlist) {
			tagPlist = false;
			if (ParseXMLFileWithSAX.isParsing())
				rootElement = currentElement;
		}
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		System.out.println(currentElement);
		if (ParseXMLFileWithSAX.isParsing())
			parentElement = elementStack.pop();
	}
}