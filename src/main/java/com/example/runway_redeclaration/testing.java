package com.example.runway_redeclaration;

import com.example.runway_redeclaration.model.Airport;
import com.example.runway_redeclaration.model.Obstacle;
import com.example.runway_redeclaration.model.Runway;
import com.example.runway_redeclaration.model.parser.XMLParser;

public class testing {

  public static void main(String[] args) {
    // I changed the runway constructor to only take necessary values. Now TODA/ASDA are calculated
    // in the Runway class itself.

//    try {
//      XMLParser.convertXmlToVariables(XMLParser.importXmlFile("src/main/java/com/example/runway_redeclaration/airport.xml"));
//    } catch (Exception e) {
//      e.printStackTrace();
//    }

//    Runway run09L = new Runway("09L", 3902, 0, 0, 306);
//    Runway run27R = new Runway("27R",3884,78,0,0);
//    Runway run09R = new Runway("09R",3660,0,0,307);
//    Runway run27L = new Runway("27L", 3660,0,0,0);
//
//    // Added the example scenarios to test different cases. Feel free to comment out some cases.
//    System.out.println("Scenario 1a:");
//    Obstacle obs1a = new Obstacle("test1a", 12, 0, 0, -50);
//    run09L.removeObstacle();
//    run09L.addObstacle(obs1a);
//    run09L.recalculate();
//    System.out.println("distanceFromCentreline (0):");
//    System.out.println(run09L.calculateDistanceFromCentreline());
//
//    System.out.println("\nScenario 1b:");
//    Obstacle obs1b = new Obstacle("test1b",12,0,0,3646);
//    run27R.removeObstacle();
//    run27R.addObstacle(obs1b);
//    run27R.recalculate();
//    System.out.println("distanceFromCentreLine (0):");
//    System.out.println(run27R.calculateDistanceFromCentreline());
//
//    System.out.println("\nScenario 2a:");
//    Obstacle obs2a = new Obstacle("test2a",25,-20,0,2853);
//    run09R.removeObstacle();
//    run09R.addObstacle(obs2a);
//    run09R.recalculate();
//    System.out.println("distanceFromCentreLine (20):");
//    System.out.println(run09R.calculateDistanceFromCentreline());
//
//    System.out.println("\nScenario 2b:");
//    Obstacle obs2b = new Obstacle("test2b", 25, -20, 0, 500);
//    run27L.addObstacle(obs2b);
//    run27L.recalculate();
//    System.out.println("distanceFromCentreLine (20):");
//    System.out.println(run27L.calculateDistanceFromCentreline());
//
//    System.out.println("\nScenario 3a:");
//    Obstacle obs3a = new Obstacle("test3a",15,60,0,150);
//    run09R.removeObstacle();
//    run09R.addObstacle(obs3a);
//    run09R.recalculate();
//    System.out.println("distanceFromCentreLine (60):");
//    System.out.println(run09R.calculateDistanceFromCentreline());
//
//    System.out.println("\nScenario 3b:");
//    Obstacle obs3b = new Obstacle("test3b",15,60,0,3203);
//    run27L.removeObstacle();
//    run27L.addObstacle(obs3b);
//    run27L.recalculate();
//    System.out.println("distanceFromCentreLine (60):");
//    System.out.println(run27L.calculateDistanceFromCentreline());
//
//    System.out.println("\nScenario 4a:");
//    Obstacle obs4a = new Obstacle("test4a",20,20,0,3546);
//    run09L.removeObstacle();
//    run09L.addObstacle(obs4a);
//    run09L.recalculate();
//    System.out.println("distanceFromCentreLine (20):");
//    System.out.println(run09L.calculateDistanceFromCentreline());
//
//    System.out.println("\nScenario 4b:");
//    Obstacle obs4b = new Obstacle("test4b",20,20,0,50);
//    run27R.removeObstacle();
//    run27R.addObstacle(obs4b);
//    run27R.recalculate();
//    System.out.println("distanceFromCentreLine (20):");
//    System.out.println(run27R.calculateDistanceFromCentreline());
  }
}
