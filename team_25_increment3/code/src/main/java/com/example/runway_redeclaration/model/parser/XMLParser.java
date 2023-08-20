package com.example.runway_redeclaration.model.parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/*
 *Get document builder
 *Get Document
 * Normalise xml structure
 *Get all the elements by the tag name
 */

public class XMLParser {

    public static void main(String[] args) {

    }

    public XMLParser() throws ParserConfigurationException, IOException, SAXException {
    }

    /**
     * Get the value in a tag attribute pair in some element.
     *
     * @param element current xml element
     * @param tag to sort by
     * @param attribute to get value from
     * @return the value stored at the tag attribute pair of the given element
     */
    public static String getTagAttributeElement(Element element, String tag, String attribute) {
        return element.getElementsByTagName(tag).item(0).getAttributes().getNamedItem(attribute)
                .getNodeValue();
    }

    // Method to import an XML file and return a Document object

    /**
     * Import an XML file from a File object.
     *
     * @param file
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document importXmlFile (File file) throws
            ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        return document;
    }

    /**
     * Import an XML file from a fileName
     *
     * @param fileName
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static Document importXmlFile (String fileName) throws
            ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(fileName));
        document.getDocumentElement().normalize();
        return document;
    }

    // Method to export a Document object to an XML file
    public static void exportXmlFile (Document document, String fileName) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(fileName));
        transformer.transform(source, result);
    }

    public static void exportXmlFile (Document document, File file) {
        // Save the document to the XML file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        }
        DOMSource source = new DOMSource(document);
        StreamResult result1 = new StreamResult(file);
        try {
            transformer.transform(source, result1);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }


    // Method to get a NodeList of elements with a specified tag name from a parent element
    public static NodeList getChildElementsByTagName (Element parent, String tagName){
        return parent.getElementsByTagName(tagName);
    }

    //Method to process an XML file
    public static void processXmlFile () throws
            ParserConfigurationException, SAXException, IOException, TransformerException {
        // Import an XML file and get the root element
        Document document = XMLParser.importXmlFile("input.xml");
        Element rootElement = document.getDocumentElement();

        // Do some processing with the root element
        NodeList childElements = XMLParser.getChildElementsByTagName(rootElement, "child");
        for (int i = 0; i < childElements.getLength(); i++) {
            Element childElement = (Element) childElements.item(i);
            // Process each child element
        }

        // Export the modified XML document to a file
        XMLParser.exportXmlFile(document, "output.xml");
    }

    //method to convert contents of XML to variables?? (not too sure)
    public static HashMap<String, Object> convertXmlToVariables (Document document){
        Element rootElement = document.getDocumentElement();
        HashMap<String, Object> variables = new HashMap<>();
        NodeList childNodes = rootElement.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {
            if (childNodes.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                Element childElement = (Element) childNodes.item(i);
                String varName = childElement.getNodeName();
                String varValue = childElement.getTextContent();
                variables.put(varName, varValue);
                //System.out.println(childElement.getAttribute("name"));

                NodeList attributeDetails = childNodes.item(i).getChildNodes();
                for (int j = 0; j < attributeDetails.getLength(); j++) {
                    if (attributeDetails.item(j).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
                        Element attributeElement = (Element) attributeDetails;
                        System.out.println("  " + attributeElement.getTagName() + ":" +
                                attributeElement.getAttribute(""));
                        //System.out.println(attributeElement.getTagName() + ":" + attributeElement.getNodeValue());
                    }
                }
            }

        }
        return variables;
    }


    // Import an XML file
    Document document = XMLParser.importXmlFile("input.xml");
    HashMap<String, Object> variables = XMLParser.convertXmlToVariables(document);

    // Access the variables
    String firstName = (String) variables.get("firstName");
    int age = Integer.parseInt((String) variables.get("age"));

}
