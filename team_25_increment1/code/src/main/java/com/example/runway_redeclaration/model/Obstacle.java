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
   * Constructor to create a new runway with the following parameters:
   *
   * @param name                  The name of the object
   * @param height                The height of the object
   * @param nFromCentre           The north/south distance from the centreline (negative number for
   *                              south)
   * @param eFromCentre           The east/west distance from the centreline (negative number for
   *                              west)
   * @param distanceFromThreshold The distance from the threshold of the runway
   */
  public Obstacle(String name, int height, int nFromCentre, int eFromCentre,
      int distanceFromThreshold) {
    this.name = name;
    this.height = height;
    this.nFromCentre = nFromCentre;
    this.eFromCentre = eFromCentre;
    this.distanceFromThreshold = distanceFromThreshold;
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
}

///**
// * The obstacle class
// */
//public class Obstacle extends Parent {
//
//  private StringProperty name;
//  private double height;
//  private IntegerProperty nFromCentre;
//  private IntegerProperty eFromCentre;
//  private int distanceFromThreshold;
//  private double positionX;
//  private double positionY;
//  private double width;
//
//  //JavaFX components
//  private TextField nameComponent;
//  private TextField heightComponent;
//  private TextField nFromCentreComponent;
//  private TextField eFromCentreComponent;
//  private TextField distanceFromThresholdComponent;
//  private Button submitButton;
//
//
//  /**
//   * Constructor to create a new runway with the following parameters:
//   *
//   * @param name                  The name of the object
//   * @param height                The height of the object
//   * @param nFromCentre           The north/south distance from the ccentreline (negative number for
//   *                              south)
//   * @param eFromCentre           The east/west distance from the ccentreline (negative number for
//   *                              west)
//   * @param distanceFromThreshold The distance from the threshold of the runway
//   */
//  public Obstacle(String name, int height, int nFromCentre, int eFromCentre,
//      int distanceFromThreshold) {
//    this.name = new SimpleStringProperty(name);
//    this.height =  height;
//    this.nFromCentre = new SimpleIntegerProperty(nFromCentre);
//    this.eFromCentre = new SimpleIntegerProperty(eFromCentre);
//    this.distanceFromThreshold =  distanceFromThreshold;
//
//    //JavaFX components
//    nameComponent = new TextField();
//    heightComponent = new TextField();
//    nFromCentreComponent = new TextField();
//    eFromCentreComponent = new TextField();
//    distanceFromThresholdComponent = new TextField();
//    submitButton = new Button("Submit");
//  }
//  /**
//   * Create a GridPane containing the JavaFX components for inputting obstacle information.
//   */
//  public GridPane addObstaclePane() {
//    GridPane addObstaclePane = new GridPane();
//    //Labels and text fields for each obstacle parameter
//    Label nameLabel = new Label("Name: ");
//    addObstaclePane.add(nameLabel, 0 ,0);
//    addObstaclePane.add(nameComponent, 1 ,0);
//
//    Label heightLabel = new Label("Height: ");
//    addObstaclePane.add(heightLabel, 0 ,1);
//    addObstaclePane.add(heightComponent, 1 ,1);
//
//    Label nFromCentreLabel = new Label("North/South Distance from Centerline: ");
//    addObstaclePane.add(nFromCentreLabel, 0 ,2);
//    addObstaclePane.add(nFromCentreComponent, 1 ,2);
//
//    Label eFromCentreLabel = new Label("East/West Distance from Centerline: ");
//    addObstaclePane.add(eFromCentreLabel, 0 ,3);
//    addObstaclePane.add(eFromCentreComponent, 1 ,3);
//
//    Label distanceFromThresholdLabel = new Label("Distance from Threshold: ");
//    addObstaclePane.add(distanceFromThresholdLabel, 0 ,4);
//    addObstaclePane.add(distanceFromThresholdComponent, 1 ,4);
//
//    return addObstaclePane;
//  }
//   /**
//   * set name
//    * @param name
//   */
//  public void setName(String name){
//    this.name.set(name);
//  }
//  /**
//   * set height
//   * @param height
//   */
//  public void setHeight(int height){
//    this.height = height;
//  }
//  /**
//   * set width
//   */
//  public void setWidth(int width){
//    this.width = width;
//  }
//
//  /**
//   * sets the position X
//   * @param positionX
//   */
//  public void setPositionX(double positionX) {
//    this.positionX = positionX;
//  }
//
//  /**
//   * sets the position Y
//   * @param positionY
//   */
//  public void setPositionY(double positionY) {
//    this.positionY = positionY;
//  }
//  /**
//   * set North/South from Centerline
//   * @param nFromCentre
//   */
//  public void setnFromCentre(int nFromCentre){
//    this.nFromCentre.set(nFromCentre);
//  }
//  /**
//   * set East/West from Centerline
//   */
//  public void setFromCentre(int eFromCentre){
//    this.eFromCentre.set(eFromCentre);
//  }
//  /**
//   * set distance from Threshold
//   */
//  public void setDistanceFromThreshold(int distanceFromThreshold){
//    this.distanceFromThreshold = distanceFromThreshold;
//  }
//  /**
//   * return name
//   */
//  public StringProperty getName() {
//    return name;
//  }
//
//  /**
//   * return height
//   */
//  public double getHeight() {
//    return height;
//  }
//
//  /**
//   *
//   * @return width
//   */
//  public double getWidth(){
//    return width;
//  }
//  /**
//   *
//   * @return position X
//   */
//  public double getPositionX(){
//    return positionX;
//  }
//  /**
//   *
//   * @return position Y
//   */
//  public double getPositionY(){
//    return positionY;
//  }
//  /**
//   * return north/south distance from centreline
//   */
//  public IntegerProperty getNFromCentre() {
//    return nFromCentre;
//  }
//
//  /**
//   * return east/west distance from centreline
//   */
//  public IntegerProperty getEFromCentre() {
//    return eFromCentre;
//  }
//
//  /**
//   * return distance from Threshold
//   */
//  public int getDistanceFromThreshold() {
//    return distanceFromThreshold;
//  }
//
//  /**
//   * return name Text field
//   */
//  public TextField getNameComponent(){
//    return nameComponent;
//  }
//
//  /**
//   * return height Text field
//   */
//  public TextField getHeightComponent(){
//    return heightComponent;
//  }
//
//  /**
//   * return North/South distance from centreline Text field
//   */
//  public TextField getnFromCentreComponent(){
//    return nFromCentreComponent;
//  }
//  /**
//   * return East/West distance from centreline Text field
//   */
//  public TextField geteFromCentreComponent(){
//    return eFromCentreComponent;
//  }
//  /**
//   * return distance from Threshold Text field
//   */
//  public TextField getDistanceFromThresholdComponent(){
//    return distanceFromThresholdComponent;
//  }
//  public Button getSubmitButton(){
//    return submitButton;
//  }
//
//  @Override
//  public Node getStyleableNode() {
//    return super.getStyleableNode();
//  }
//}
