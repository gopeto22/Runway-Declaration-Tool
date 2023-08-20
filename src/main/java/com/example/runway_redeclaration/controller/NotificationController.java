package com.example.runway_redeclaration.controller;

import com.example.runway_redeclaration.model.parser.XMLParser;
import com.example.runway_redeclaration.view.component.NotificationDisplay;
import com.example.runway_redeclaration.view.ui.RunwayWindow;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class NotificationController {

    /**
     * runway window
     */
    RunwayWindow runwayWindow;

    public NotificationController(RunwayWindow window) {
        runwayWindow = window;
    }

    /**
     * Update the XML file with a new notification
     */
    public void addNotif(String title, String type, String desc) {

        //get time
        String time = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss").format(new java.util.Date());

        try {
            Document document;
            document = XMLParser.importXmlFile(runwayWindow.getXmlFile());

            Node notifs = document.getElementsByTagName("notifications").item(0);

            // Create new notification element
            Element notif = document.createElement("notification");

            // Add the attributes to the notification
            Element titleElement = document.createElement("title");
            titleElement.setAttribute("title", title);
            notif.appendChild(titleElement);

            Element typeElement = document.createElement("type");
            typeElement.setAttribute("type", type);
            notif.appendChild(typeElement);

            Element descElement = document.createElement("description");
            descElement.setAttribute("description", desc);
            notif.appendChild(descElement);

            Element timeElement = document.createElement("time");
            timeElement.setAttribute("time", time);
            notif.appendChild(timeElement);

            Element nameElement = document.createElement("username");
            nameElement.setAttribute("username", runwayWindow.getUsername());
            notif.appendChild(nameElement);

            //add to XML file
            notifs.appendChild(notif);
            XMLParser.exportXmlFile(document, runwayWindow.getXmlFile());

            //NOTIFICATION POPUP
            NotificationDisplay notificationDisplay = new NotificationDisplay(title, type, desc, time, runwayWindow.getUsername());

            // Display a notification
            notificationDisplay.showNotification();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DOMException e) {
            e.printStackTrace();
        }
    }
}
