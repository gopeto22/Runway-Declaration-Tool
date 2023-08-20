package com.example.runway_redeclaration.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.HashMap;

/**
 * The runway class
 * <p>
 * (VERY IMPORTANT) make sure to display all values in metres.
 */
public class Runway extends Airport {
    /**
     * Name of the runway.
     * <p>
     * Each runway is named with a number from 01 to 36, based on the degree of the runway's heading
     * direction. Runway 09 points to the East (90 degrees clockwise from North); similarly, 27 points
     * to the West. Since a physical runway can be used in both directions they will be split up into
     * 2 distinct logical runways with 18 points between them. In the case of parallel runways
     * (runways with multiple lanes), a character is appended to the names of the runways to identify
     * the relative position. These characters will be either L (Left), C (Centre) or R (Right).
     */
    private String designator;

    /**
     * Take-Off Run Available in metres.
     * <p>
     * The length of the runway available for take-ff. Where an aircraft can start taking off to the
     * point where it must have taken off (where the runway can no longer support the weight of the
     * aircraft under normal conditions).
     */
    private final IntegerProperty originalTora = new SimpleIntegerProperty();

    /**
     * Take-Off Distance Available in metres.
     * <p>
     * The length of TORA plus any Clearway. The total distance for take-off and initial ascent.
     */
    private final IntegerProperty originalToda = new SimpleIntegerProperty();

    /**
     * Accelerate Stop Distance Available in metres.
     * <p>
     * The length of TORA plus any Stopway. The total distance available to the aircraft in case of an
     * aborted take-off.
     */
    private final IntegerProperty originalAsda = new SimpleIntegerProperty();

    /**
     * Landing Distance Available
     * <p>
     * The length of the runway available for landing. The start of the runway is called the
     * threshold. When an object is present on the runway the threshold (start of the runway) will
     * change, in this case the LDA and TORA will differ. If there is no object then the LDA and TORA
     * will be the same (possible edge cases).
     */
    private final IntegerProperty originalLda = new SimpleIntegerProperty();

    /**
     * A displaced runway threshold.
     * <p>
     * A runway threshold can be displaced by an object on the runway or initially for operational
     * reasons. When this happens the runway threshold will be located at a point other than the
     * physical beginning or end of a runway. This displaced portion can be used for take-off but not
     * for landing. When landing, an aircraft can use the displaced area on the opposite end of the
     * runway (??? I guess when applied to real life this means when the plane is slow it can avoid an
     * object?)
     */
    private int displacedThreshold;

    /**
     * An area beyond the end of the TORA that is considered free from obstructions, can be used
     * during take-off to climb to specified height.
     */
    private int clearway;

    /**
     * An area beyond the end of the TORA that can be used to safely stop an aircraft in an emergency
     * (abandoned take-off).
     */
    private int stopway;

    /**
     * Approach Landing Surface.
     * <p>
     * The surface formed between the runway and the top of the obstacle when taking into account the
     * airport's minimum angle of ascent (usually 1:50).
     */
    private int als;

    /**
     * Take-Off Climb Surface.
     * <p>
     * The surface formed between the top of the obstacle and the runway whn taking into account the
     * airport's minimum angle of ascent (usually 1:50).
     */
    private int tocs;

    /**
     * An obstacle on the runway.
     * <p>
     * There can only be either 0 or 1 obstacles on a runway at any given point, otherwise no
     * redeclaration can take place.
     */
    private Obstacle obstacle;

    /**
     * The distance of the obstacle from the centreline.
     */
    private int distanceFromCentreline;

    /**
<<<<<<< HEAD
     * 1 if landing towards, 0 if nocalc, -1 if landing over.
     */
    private int landingTowards;

    /**
     * True if obstacle is on or above the line, false otherwise.
     */
    private boolean obstacleAboveLine;

    /**
     * Variables for the breakdown table
     */
    private String eqType;
    private String toraEq;
    private String toraCalc;
    private String todaEq;
    private String todaCalc;
    private String asdaEq;
    private String asdaCalc;
    private String ldaEq;
    private String ldaCalc;

    /**
     * Variables for visualisation.
     */
    private HashMap<String, Integer> redeclaredToraMap = new HashMap<>();
    private HashMap<String, Integer> redeclaredTodaMap = new HashMap<>();
    private HashMap<String, Integer> redeclaredAsdaMap = new HashMap<>();
    private HashMap<String, Integer> redeclaredLdaMap = new HashMap<>();

    /**
     * Redeclared Take-Off Run Available in metres.
     */
    private final IntegerProperty redeclaredTora = new SimpleIntegerProperty();

    /**
     * Redeclared Take-Off Distance Available in metres.
     */
    private final IntegerProperty redeclaredToda = new SimpleIntegerProperty();

    /**
     * Redeclared Accelerate Stop Distance Available in metres.
     */
    private final IntegerProperty redeclaredAsda = new SimpleIntegerProperty();

    /**
     * Redeclared Landing Distance Available in metres.
     */
    private final IntegerProperty redeclaredLda = new SimpleIntegerProperty();

    /**
     * Bindable original TORA property.
     *
     * @return original TORA value
     */
    public IntegerProperty originalToraProperty() {
        return originalTora;
    }

    /**
     * Bindable original TODA property.
     *
     * @return original TODA value
     */
    public IntegerProperty originalTodaProperty() {
        return originalToda;
    }

    /**
     * Bindable original ASDA property.
     *
     * @return original ASDA value
     */
    public IntegerProperty originalAsdaProperty() {
        return originalAsda;
    }

    /**
     * Bindable original LDA property.
     *
     * @return original LDA value
     */
    public IntegerProperty originalLdaProperty() {
        return originalLda;
    }

    /**
     * Bindable redeclared TORA property.
     *
     * @return redeclared TORA value
     */
    public IntegerProperty redeclaredToraProperty() {
        return redeclaredTora;
    }

    /**
     * Bindable redeclared TODA property.
     *
     * @return redeclared TODA value
     */
    public IntegerProperty redeclaredTodaProperty() {
        return redeclaredToda;
    }

    /**
     * Bindable redeclared ASDA property.
     *
     * @return redeclared ASDA value
     */
    public IntegerProperty redeclaredAsdaProperty() {
        return redeclaredAsda;
    }

    /**
     * Bindable redeclared LDA property.
     *
     * @return redeclared LDA value
     */
    public IntegerProperty redeclaredLdaProperty() {
        return redeclaredLda;
    }

    /**
     * Runway constructor with predefined stripEnd, blastProtection, resa values.
     *
     * @param stripEnd           airport strip end
     * @param blastProtection    airport blast protection
     * @param resa               airport resa
     * @param designator         the name of the runway (based on the degree of the runway’s heading
     *                           direction and its position)
     * @param originalTora       Take-Off Run Available - the length of the runway available for
     *                           take-off.
     * @param clearway           an area beyond the end of the TORA for use during take-off.
     * @param stopway            an area beyond the end of the TORA for use during aborted take-off.
     * @param displacedThreshold the initial displaced threshold due to operational reasons.
     */
    public Runway(int stripEnd, int blastProtection, int resa, String designator, int originalTora, int clearway, int stopway,
                  int displacedThreshold) {
        super(stripEnd, blastProtection, resa);
        this.designator = designator;
        this.originalTora.set(originalTora);
        this.clearway = clearway;
        this.stopway = stopway;
        this.displacedThreshold = displacedThreshold;

        calculateOriginalValues();
    }

    /**
     * Runway constructor with default airport stripEnd, blastProtection and resa values.
     *
     * @param designator         the name of the runway (based on the degree of the runway’s heading
     *                           direction and its position)
     * @param originalTora       Take-Off Run Available - the length of the runway available for
     *                           take-off.
     * @param clearway           an area beyond the end of the TORA for use during take-off.
     * @param stopway            an area beyond the end of the TORA for use during aborted take-off.
     * @param displacedThreshold the initial displaced threshold due to operational reasons.
     */
    public Runway(String designator, int originalTora, int clearway, int stopway, int displacedThreshold) {
        super(60, 300, 240);
        this.designator = designator;
        this.originalTora.set(originalTora);
        this.clearway = clearway;
        this.stopway = stopway;
        this.displacedThreshold = displacedThreshold;

        calculateOriginalValues();
    }

    /**
     * Calculate the original runway values.
     */
    private void calculateOriginalValues() {
        obstacle = null;
        originalToda.set(originalTora.get() + clearway);
        originalAsda.set(originalTora.get() + stopway);
        originalLda.set(originalTora.get() - displacedThreshold);
    }

    /**
     * Calculate the distance of an object from the centreline given the cardinal coordinates.
     */
    private void calculateDistanceFromCentreline() {
        // Think of the cardinal coordinates as a point on a graph, with the y axis being the
        // north/south line and the x axis being the east/south line.

        // 1. Calculate the equation of the runway as a line on the graph.
        // 2. Calculate the equation of a perpendicular line to the runway going through the n/e point
        // of the obstacle.
        // 3. Find the intersection point.
        // 4. Find the distance between the intersection point and the n/e point of the obstacle.

        // Calculate the equation of the runway as a line on the graph.
        int angle = Integer.parseInt(designator.substring(0, 2)) * 10;
        Line2D runwayLine = new Line2D.Double();
        runwayLine.setLine(0, 0, originalTora.get() * Math.cos(Math.toRadians(angle)),
                originalTora.get() * Math.sin(Math.toRadians(angle)));

        // Calculate the equation of a perpendicular line to the runway.
        Point2D obstaclePoint = new Double();
        obstaclePoint.setLocation(obstacle.getNFromCentre(), obstacle.getEFromCentre());

        // Find the distance between the intersection point and the n/e point.
        distanceFromCentreline = (int) runwayLine.ptLineDist(obstaclePoint);
        obstacleAboveLine = runwayLine.relativeCCW(obstaclePoint) >= 0;
    }

    /**
     * Add an obstacle to the runway.
     *
     * @param obstacle on the runway
     */
    public void addObstacle(Obstacle obstacle) {
        this.obstacle = obstacle;
        calculateDistanceFromCentreline();
    }

    /**
     * Remove an obstacle from the runway.
     */
    public void removeObstacle() {
        this.obstacle = null;
    }

    /**
     * Return the obstacle on the runway
     */
    public Obstacle getObstacle() {
        return obstacle;
    }

    /**
     * When the obstacle is closer to the threshold, the recalculation calculates Taking Off Away
     * [from object] and Landing Over [object]
     */
    private void takeOffAwayLandingOver() {
        // Calculate ALS (Approach Landing Surface).
        // This is just the name for tempthreshold as defined in the spec, I changed it around a bit
        // and admit that it isn't as clean as before, but it's more readable now.
        als = Math.max(50 * obstacle.getHeight(), resa);

        // Calculate TORA, TODA and ASDA for taking off away.

        redeclaredTora.set(
                originalTora.get() - blastProtection - obstacle.getDistanceFromThreshold()
                        - displacedThreshold);
        System.out.println("TORA = " + redeclaredTora.get() + " metres.");

        redeclaredToda.set(
                originalToda.get() - blastProtection - obstacle.getDistanceFromThreshold()
                        - displacedThreshold);
        System.out.println("TODA = " + redeclaredToda.get() + " metres.");

        redeclaredAsda.set(
                originalAsda.get() - blastProtection - obstacle.getDistanceFromThreshold()
                        - displacedThreshold);
        System.out.println("ASDA = " + redeclaredAsda.get() + " metres.");

        // Calculate LDA for landing over.
        if (als + stripEnd > blastProtection) {
            redeclaredLda.set(originalLda.get() - obstacle.getDistanceFromThreshold() - als - stripEnd);
            System.out.println("LDA = " + redeclaredLda.get() + " metres.");
        } else {
            redeclaredLda.set(originalLda.get() - obstacle.getDistanceFromThreshold() - blastProtection);
            System.out.println("LDA = " + redeclaredLda.get() + "metres.");
        }

        //update breakdown
        eqType = "The obstacle is closer to the start of the runway - calculating the Take-Off Away [from object], Landing Over [object] values:";
        //the	blast	protection	allowance is	deducted	from	the	distance.
        toraEq = "TORA = Original TORA - Blast Protection - Distance from Threshold - Displaced Threshold";
        toraCalc = originalTora.get() + " - " + blastProtection + " - " + obstacle.getDistanceFromThreshold() + " - " + displacedThreshold;
        redeclaredToraMap.put("Blast Protection", blastProtection);
        redeclaredToraMap.put("Distance From Threshold", obstacle.getDistanceFromThreshold());
        redeclaredToraMap.put("Displaced Threshold", displacedThreshold);
        redeclaredToraMap.put("Redeclared TORA", redeclaredTora.get());
        //If	there	exist	any	Clearway	and/or	Stopway	then	those	values	should	be	added	to	the	reduced
        //TORA	for	the	TODA	and	ASDA	values.
        todaEq = "TODA = Recalculated TORA + Clearway";
        todaCalc = redeclaredTora.get() + " + " + clearway;
        redeclaredTodaMap.put("Blast Protection", blastProtection);
        redeclaredTodaMap.put("Distance From Threshold", obstacle.getDistanceFromThreshold());
        redeclaredTodaMap.put("Displaced Threshold", displacedThreshold);
        redeclaredTodaMap.put("Redeclared TORA", redeclaredTora.get());
        redeclaredTodaMap.put("Clearway", clearway);

        asdaEq = "ASDA = Recalculated TORA + Stopway";
        asdaCalc = redeclaredTora.get() + " + " + stopway;
        redeclaredAsdaMap.put("Blast Protection", blastProtection);
        redeclaredAsdaMap.put("Distance From Threshold", obstacle.getDistanceFromThreshold());
        redeclaredAsdaMap.put("Displaced Threshold", displacedThreshold);
        redeclaredAsdaMap.put("Redeclared TORA", redeclaredTora.get());
        redeclaredAsdaMap.put("Stopway", stopway);

        //calculated	by
        //establishing	a	temporary	threshold	with	a	1	in	50	slope	(ALS)	from	the	highest	point	of	the
        //obstacle	(or	by	establishing	a	new	RESA	if	this	is	larger)	plus	the	strip	end	value.	This	must	be
        //larger	than	the	blast	protection	value	of	the	aircraft,	otherwise the	blast	protection	value	must	be
        //taken	into	account	instead.
        if (als + stripEnd < blastProtection) {
            ldaEq = "LDA = TORA - Displaced Threshold - Distance From Threshold - Blast Protection";
            ldaCalc = originalTora.get() + " - " + displacedThreshold + " - " + obstacle.getDistanceFromThreshold() + " - " + blastProtection;
            redeclaredLdaMap.put("Displaced Threshold", displacedThreshold);
            redeclaredLdaMap.put("Distance From Threshold", obstacle.getDistanceFromThreshold());
            redeclaredLdaMap.put("Blast Protection", blastProtection);
            redeclaredLdaMap.put("Redeclared LDA", redeclaredLda.get());
        } else if (50 * obstacle.getHeight() > resa) {
            ldaEq = "LDA = TORA - Displaced Threshold - Distance From Threshold - Slope Calculation(ALS) - Strip End";
            ldaCalc = originalTora.get() + " - " + displacedThreshold + " - " + obstacle.getDistanceFromThreshold() + " - (50 * " + obstacle.getHeight() + ") - " + stripEnd;
            redeclaredLdaMap.put("Displaced Threshold", displacedThreshold);
            redeclaredLdaMap.put("Distance From Threshold", obstacle.getDistanceFromThreshold());
            redeclaredLdaMap.put("ALS", als);
            redeclaredLdaMap.put("Strip End", stripEnd);
            redeclaredLdaMap.put("Redeclared LDA", redeclaredLda.get());
        } else {
            ldaEq = "LDA = TORA - Displaced Threshold - Distance From Threshold - RESA - Strip End";
            ldaCalc = originalTora.get() + " - " + displacedThreshold + " - " + obstacle.getDistanceFromThreshold() + " - " + resa + " - " + stripEnd;
            redeclaredLdaMap.put("Displaced Threshold", displacedThreshold);
            redeclaredLdaMap.put("Distance From Threshold", obstacle.getDistanceFromThreshold());
            redeclaredLdaMap.put("RESA", resa);
            redeclaredLdaMap.put("Strip End", stripEnd);
            redeclaredLdaMap.put("Redeclared LDA", redeclaredLda.get());
        }

        landingTowards = -1;

        checkValidRedeclaredValues();
    }

    /**
     * Check that the values are not negative.
     */
    private void checkValidRedeclaredValues() {
        if (redeclaredTora.get() < 0) {
            redeclaredTora.set(0);
        }
        if (redeclaredToda.get() < 0) {
            redeclaredToda.set(0);
        }
        if (redeclaredAsda.get() < 0) {
            redeclaredAsda.set(0);
        }
        if (redeclaredLda.get() < 0) {
            redeclaredLda.set(0);
        }
    }

    /**
     * When the obstacle is closer to the opposite threshold, the recalculation calculates Taking Off
     * Towards [object] and Landing Towards [object].
     */
    private void takeOffTowardsLandingTowards() {
        // Again, same logic just with a different name.
        tocs = Math.max(50 * obstacle.getHeight(), resa);

        // Calculate TORA, TODA and ASDA for taking off towards.

        redeclaredTora.set(obstacle.getDistanceFromThreshold() + displacedThreshold - tocs - stripEnd);
        System.out.println("TORA = " + redeclaredTora.get() + " metres.");

        redeclaredToda.set(redeclaredTora.get());
        System.out.println("TODA = " + redeclaredToda.get() + " metres.");

        redeclaredAsda.set(redeclaredTora.get());
        System.out.println("ASDA = " + redeclaredAsda.get() + " metres.");

        // Calculate LDA for landing towards
        redeclaredLda.set(obstacle.getDistanceFromThreshold() - stripEnd - resa);
        System.out.println("LDA = " + redeclaredLda.get() + " metres.");

        //update the breakdown
        eqType = "The obstacle is closer to the end of the runway - calculating the Take-Off Towards [object], Landing Towards [object] values:";
        //temporary	end	of	TORA	to	be	established with the 1	in	50	slope	(TOCS)	from	the	highest	point	of
        //the	obstacle	and	this	value	must	be	larger	than	the	RESA	value,	otherwise the	RESA	value	is	taken
        //into	account	instead.	The	value	of	the	strip	end	must	also	be	deducted.
        if (50 * obstacle.getHeight() > resa) {
            toraEq = "TORA = Distance from Threshold + Displaced Threshold - Slope Calculation(TOCS) - Strip End";
            toraCalc = obstacle.getDistanceFromThreshold() + " + " + displacedThreshold + " - (50 * " + obstacle.getHeight() + ") - " + stripEnd;
            redeclaredToraMap.put("Distance From Threshold", obstacle.getDistanceFromThreshold());
            redeclaredToraMap.put("Displaced Threshold", displacedThreshold);
            redeclaredToraMap.put("Slope Calculation", 50 * obstacle.getHeight());
            redeclaredToraMap.put("Strip End", stripEnd);
            redeclaredToraMap.put("Redeclared TORA", redeclaredTora.get());

        } else {
            toraEq = "TORA = Distance from Threshold + Displaced Threshold - RESA - Strip End";
            toraCalc = obstacle.getDistanceFromThreshold() + " + " + displacedThreshold + " - " + resa + " - " + stripEnd;
            redeclaredToraMap.put("Distance From Threshold", obstacle.getDistanceFromThreshold());
            redeclaredToraMap.put("Displaced Threshold", displacedThreshold);
            redeclaredToraMap.put("RESA", resa);
            redeclaredToraMap.put("Strip End", stripEnd);
            redeclaredToraMap.put("Redeclared TORA", redeclaredTora.get());
        }
        //the	ASDA	and	TODA
        //values	are	equal	to	the	TORA	here
        todaEq = "TODA = Recalculated TORA";
        todaCalc = Integer.toString(redeclaredTora.get());
        redeclaredTodaMap = redeclaredToraMap;

        asdaEq = "ASDA = Recalculated TORA";
        asdaCalc = Integer.toString(redeclaredTora.get());
        redeclaredAsdaMap = redeclaredToraMap;

        //The	distance	is	from	the	threshold
        //to	the	obstacle,	with	the	requirement	of	the	RESA	plus	the	strip	end	value.
        ldaEq = "LDA = Distance from Threshold - RESA - Strip End";
        ldaCalc = obstacle.getDistanceFromThreshold() + " - " + resa + " - " + stripEnd;
        redeclaredLdaMap.put("Distance From Threshold", obstacle.getDistanceFromThreshold());
        redeclaredLdaMap.put("RESA", resa);
        redeclaredLdaMap.put("Strip End", stripEnd);
        redeclaredLdaMap.put("Redeclared LDA", redeclaredLda.get());

        System.out.println("Setting landingTowards to true");
        landingTowards = 1;

        checkValidRedeclaredValues();
    }

    /**
     * When no calculation needed (no obstacle/obstacle not blocking runway).
     */
    private void noCalc() {
        redeclaredTora.set(originalTora.get());
        System.out.println("TORA = " + redeclaredTora.get() + " metres.");

        redeclaredToda.set(originalToda.get());
        System.out.println("TODA = " + redeclaredToda.get() + " metres.");

        redeclaredAsda.set(originalAsda.get());
        System.out.println("ASDA = " + redeclaredAsda.get() + " metres.");

        redeclaredLda.set(originalLda.get());
        System.out.println("LDA = " + redeclaredLda.get() + " metres.");

        //update breakdown
        toraEq = "TORA = Original TORA";
        toraCalc = Integer.toString(originalTora.get());
        todaEq = "TODA = TORA + Clearway";
        todaCalc = originalTora.get() + " + " + clearway;
        asdaEq = "ASDA = TORA + Stopway";
        asdaCalc = originalTora.get() + " + " + stopway;
        ldaEq = "LDA = TORA - Displaced Threshold";
        ldaCalc = originalTora.get() + " - " + displacedThreshold;

        redeclaredLdaMap.put("Displaced Threshold", displacedThreshold);

        landingTowards = 0;
    }

    /**
     * distanceFromCentreline getter.
     *
     * @return the distance of the runways obstacle from the centreline
     */
    public int getDistanceFromCentreline() {
        return distanceFromCentreline;
    }

    /**
     * Recalculate runway parameters depending on where the obstacle is.
     */
    public void recalculate() {
        // If there is no obstacle then we don't need to redeclare the runway.
        if (obstacle == null) {
            System.out.println("No obstacle on runway - no recalculations needed");
            eqType = "No obstacle on runway - no recalculations needed:";
            noCalc();
        }
        // If the obstacle is not blocking the runway then we don't need to redeclare the runway.
        else if (distanceFromCentreline > 75 || (obstacle.getDistanceFromThreshold() < (-stripEnd)) ||
                (obstacle.getDistanceFromThreshold() > (originalTora.get() + stripEnd))) {
            System.out.println("Obstacle is not blocking the runway - no recalculations needed.");
            eqType = "Obstacle is not blocking the runway - no recalculations needed:";
            noCalc();
        }
        // If the obstacle is closer to the start of the runway then calculate the Take-Off Away,
        // Landing Over values. The object must also be
        else if (obstacle.getDistanceFromThreshold() < (originalTora.get() / 2)) {
            System.out.println(
                    "The obstacle is closer to the start of the runway - calculating the Take-Off Away, Landing Over value");
            takeOffAwayLandingOver();
        }
        // Otherwise, if the obstacle is closer to the end of the runway then calculate the Take-Off
        // Towards, Landing Towards values.
        else if (obstacle.getDistanceFromThreshold() >= (originalTora.get() / 2)) {
            System.out.println(
                    "The obstacle is closer to the end of the runway - calculating the Take-Off Towards, Landing Towards value");
            takeOffTowardsLandingTowards();
        } else {
            System.out.println("Case not detected. Aborting.");
        }
    }

    /**
     * Getters for breakdown (for SimulateScene)
     *
     * @return
     */

    public String getEqType() {
        return eqType;
    }

    public String getToraEq() {
        return toraEq;
    }

    public String getToraCalc() {
        return toraCalc;
    }

    public String getTodaEq() {
        return todaEq;
    }

    public String getTodaCalc() {
        return todaCalc;
    }

    public String getAsdaEq() {
        return asdaEq;
    }

    public String getAsdaCalc() {
        return asdaCalc;
    }

    public String getLdaEq() {
        return ldaEq;
    }

    public String getLdaCalc() {
        return ldaCalc;
    }

    public String getDesignator() {
        return designator;
    }

    public int getRedeclaredTora() {
        return redeclaredTora.get();
    }

    public int getRedeclaredToda() {
        return redeclaredToda.get();
    }

    public int getRedeclaredAsda() {
        return redeclaredAsda.get();
    }

    public int getRedeclaredLda() {
        return redeclaredLda.get();
    }

    public int getDisplacedThreshold() {
        return displacedThreshold;
    }

    public int getStopway() {
        return stopway;
    }

    public int getClearway() {
        return clearway;
    }

    public int isLandingTowards() {
        return landingTowards;
    }

    public HashMap<String, Integer> getRedeclaredToraMap() {
        return redeclaredToraMap;
    }

    public HashMap<String, Integer> getRedeclaredTodaMap() {
        return redeclaredTodaMap;
    }

    public HashMap<String, Integer> getRedeclaredAsdaMap() {
        return redeclaredAsdaMap;
    }

    public HashMap<String, Integer> getRedeclaredLdaMap() {
        return redeclaredLdaMap;
    }

    public boolean isObstacleAboveLine() {
        return obstacleAboveLine;
    }
}
