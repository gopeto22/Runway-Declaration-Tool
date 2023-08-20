package com.example.runway_redeclaration.model.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Paths;

public class AirportXMLParser {

    public static void main(String[] args) {
        try {
            // Import an XML file and get the root element
            File xmlFile = new File(Paths.get("").toAbsolutePath()
                    + "/src/main/java/com/example/runway_redeclaration/airport.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            Element rootElement = doc.getDocumentElement();

            // Output the contents of the XML file
            System.out.println("Airport: " + rootElement.getNodeName());
            System.out.println(
                    "Strip End: " + rootElement.getElementsByTagName("stripEnd").item(0).getAttributes()
                            .getNamedItem("metres").getNodeValue());
            System.out.println(
                    "Blast Protection: " + rootElement.getElementsByTagName("blastProtection").item(0)
                            .getAttributes().getNamedItem("metres").getNodeValue());
            System.out.println("RESA: " + rootElement.getElementsByTagName("resa").item(0).getAttributes()
                    .getNamedItem("metres").getNodeValue());

            // Output the contents of the runway element
            Element runwayElement = (Element) rootElement.getElementsByTagName("runway").item(0);
            System.out.println(
                    "Runway Designator: " + runwayElement.getElementsByTagName("designator").item(0)
                            .getAttributes().getNamedItem("code").getNodeValue());
            System.out.println(
                    "TORA: " + runwayElement.getElementsByTagName("tora").item(0).getAttributes()
                            .getNamedItem("metres").getNodeValue());
            System.out.println(
                    "Clearway: " + runwayElement.getElementsByTagName("clearway").item(0).getAttributes()
                            .getNamedItem("metres").getNodeValue());
            System.out.println(
                    "Stopway: " + runwayElement.getElementsByTagName("stopway").item(0).getAttributes()
                            .getNamedItem("metres").getNodeValue());
            System.out.println(
                    "Displaced Threshold: " + runwayElement.getElementsByTagName("displacedThreshold").item(0)
                            .getAttributes().getNamedItem("metres").getNodeValue());
            System.out.println(
                    "Obstacle Name: " + runwayElement.getElementsByTagName("obstacleName").item(0)
                            .getAttributes().getNamedItem("name").getNodeValue());
            System.out.println("Obstacle Distance from Threshold: " + runwayElement.getElementsByTagName(
                    "distanceFromThreshold").item(0).getAttributes().getNamedItem("distance").getNodeValue());
            System.out.println(
                    "Obstacle nFromCentre: " + runwayElement.getElementsByTagName("nFromCentre").item(0)
                            .getAttributes().getNamedItem("metres").getNodeValue());
            System.out.println(
                    "Obstacle eFromCentre: " + runwayElement.getElementsByTagName("eFromCentre").item(0)
                            .getAttributes().getNamedItem("metres").getNodeValue());

            // Output the contents of the obstacle element
            Element obstacleElement = (Element) rootElement.getElementsByTagName("obstacle").item(0);
            System.out.println(
                    "Obstacle Name: " + obstacleElement.getElementsByTagName("name").item(0).getAttributes()
                            .getNamedItem("name").getNodeValue());
            System.out.println(
                    "Obstacle Height: " + obstacleElement.getElementsByTagName("height").item(0)
                            .getAttributes().getNamedItem("metres").getNodeValue());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
