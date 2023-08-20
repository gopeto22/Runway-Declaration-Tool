package com.example.runway_redeclaration.model.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import org.xml.sax.SAXException;

public class XMLParserTest {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        // Import the sample XML file
        Document document = XMLParser.importXmlFile("pom.xml");
        Element rootElement = document.getDocumentElement();

        // Get the child elements with tag name "child" and process each one
        NodeList childElements = XMLParser.getChildElementsByTagName(rootElement, "child");
        for (int i = 0; i < childElements.getLength(); i++) {
            Element childElement = (Element) childElements.item(i);
            // Process each child element here
            String name = childElement.getAttribute("name");
            System.out.println("Child element " + i + " has name: " + name);
        }

        // Export the modified XML document to a file
        XMLParser.exportXmlFile(document, "output.xml");
        System.out.println("XML file exported successfully.");
    }
}
