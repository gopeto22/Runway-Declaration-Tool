package com.example.runway_redeclaration.model;

/**
 * The obstacle class
 */
public class Obstacle {

  /**
   * The name of the obstacle.
   */
  private String name;

  /**
   * The height of the obstacle.
   */
  private int height;

  /**
   * The width of the obstacle.
   */
  private int width;

  /**
   * The length of the obstacle.
   */
  private int length;

  /**
   * The north/south distance of the obstacle from the centreline
   */
  private int nFromCentre;

  /**
   * The east/west distance of the obstacle from the centreline
   */
  private int eFromCentre;

  /**
   * The distance from the threshold (physical start) of the runway
   */
  private int distanceFromThreshold;

  /**
   * Estimated clearance time of obstacle (string)
   */
  private int clearanceTime;

  /**
   * Constructor to create a new runway with the following parameters:
   *  @param name                  The name of the object
   * @param height                The height of the object
   * @param nFromCentre           The north/south distance from the centreline (negative number for
 *                              south)
   * @param eFromCentre           The east/west distance from the centreline (negative number for
*                              west)
   * @param distanceFromThreshold The distance from the threshold of the runway
   * @param clearanceTime
   */
  public Obstacle(String name, int height, int width, int length, int nFromCentre, int eFromCentre,
                  int distanceFromThreshold, int clearanceTime) {
    this.name = name;
    this.height = height;
    this.width = width;
    this.length = length;
    this.nFromCentre = nFromCentre;
    this.eFromCentre = eFromCentre;
    this.distanceFromThreshold = distanceFromThreshold;
    this.clearanceTime = clearanceTime;
  }

  /**
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * @return height
   */
  public int getHeight() {
    return height;
  }

  /**
   * @return width
   */
  public int getObsWidth() {
    return width;
  }

  /**
   * @return length
   */
  public int getObsLength() {
    return length;
  }

  /**
   * @return nFromCentre
   */
  public int getNFromCentre() {
    return nFromCentre;
  }

  /**
   * @return eFromCentre
   */
  public int getEFromCentre() {
    return eFromCentre;
  }

  /**
   * @return distanceFromThreshold
   */
  public int getDistanceFromThreshold() {
    return distanceFromThreshold;
  }

  /**
   * @return clearanceTime
   */
  public int getClearanceTime() {
    return clearanceTime;
  }
}

