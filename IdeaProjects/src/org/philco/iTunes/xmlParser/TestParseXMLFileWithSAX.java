package org.philco.iTunes.xmlParser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class TestParseXMLFileWithSAX {
	private static final String resourceFile="src/org/philco/iTunes/resources/iTunes Music Library.xml";
//	private static boolean parsing = true;
//	public static void startParsing() { parsing = true; }
//	public static void stopParsing() { parsing = false; }
//	public static void toggleParsing() { parsing = !parsing; }
//	public static boolean isParsing() { return parsing; }
	
	private static void printElement(Element element) {
		System.out.println(element);
		for ( Element elem : element.getChildren() ) 
			printElement(elem);
	}
	
	public static void main(String argv[]) {
        String inputFile = resourceFile;
        if ( argv.length > 0 )
            inputFile = argv[0];

		try {
            System.out.println("Directory = "+System.getProperty("user.dir"));
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			SAXHandler saxHandler = new SAXHandler();
			saxParser.parse(inputFile, saxHandler);
			Element rootElement = saxHandler.getRootElement();
			printElement(rootElement);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
