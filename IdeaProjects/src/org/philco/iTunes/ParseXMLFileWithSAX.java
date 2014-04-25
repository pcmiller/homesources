package org.philco.iTunes;

import org.philco.iTunes.elements.Element;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class ParseXMLFileWithSAX {
	private static final String resourceFile="src/org/philco/iTunes/resources/iTunes Music Library.xml";
    private static final String res="C:\\Users\\phil\\Documents\\sts-workspace\\git\\IdeaProjects\\src\\org\\philco\\iTunes\\resources";
//	private static final String xmlFilePath = "C:\\Users\\phil\\Music\\iTunes\\iTunes Music Library.xml";

	private static boolean parsing = true;
	public static void startParsing() { parsing = true; }
	public static void stopParsing() { parsing = false; }
	public static void toggleParsing() { parsing = !parsing; }
	public static boolean isParsing() { return parsing; }
	
	private static void printElement(Element element) {
		System.out.println(element);
		for ( Element elem : element.getChildren() ) 
			printElement(elem);
	}
	
	public static void main(String argv[]) {

		try {
            System.out.println("Directory = "+System.getProperty("user.dir"));
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			MyHandler myHandler = new MyHandler();
			saxParser.parse(resourceFile, myHandler);
			Element rootElement = myHandler.getRootElement();
			printElement(rootElement);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
