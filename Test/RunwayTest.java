import com.example.runway_redeclaration.model.Obstacle;
import com.example.runway_redeclaration.model.Runway;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test the calculations performed by the runway class.
 */
public class RunwayTest {

    /**
     * A runway to be tested.
     */
    private Runway runway;

    /**
     * An obstacle for the runway.
     */
    private Obstacle obstacle;

    /**
     * Nothing to do for the set up.
     */
    @Before
    public void setUp() {
    }

    /**
     * Reset the runway and obstacle for use in the next test.
     */
    @After
    public void tearDown() {
        runway.removeObstacle();
        runway = null;
        obstacle = null;
    }

    @Test
    public void testHeathrowScenario1a() {
        runway = new Runway("09L", 3902, 0, 0, 306);
        obstacle = new Obstacle("test1a", 12, 0, 0, -50);

        runway.addObstacle(obstacle);
        runway.recalculate();

        assertEquals(0, runway.getDistanceFromCentreline());
        assertEquals(3902, runway.originalToraProperty().get());
        assertEquals(3902, runway.originalTodaProperty().get());
        assertEquals(3902, runway.originalAsdaProperty().get());
        assertEquals(3596, runway.originalLdaProperty().get()); // Changed this from 3595 in the spec to 3956, typo
        assertEquals(3346, runway.getRedeclaredToda());
        assertEquals(3346, runway.getRedeclaredToda());
        assertEquals(3346, runway.getRedeclaredAsda());
        assertEquals(2986, runway.getRedeclaredLda()); // Changed this from 2985 in spec to 2986, typo
    }

    @Test
    public void testHeathrowScenario1b() {
        runway = new Runway("27R", 3884, 78, 0, 0);
        obstacle = new Obstacle("test1b", 12, 0, 0, 3646);

        runway.addObstacle(obstacle);
        runway.recalculate();

        assertEquals(0, runway.getDistanceFromCentreline());
        assertEquals(3884, runway.originalToraProperty().get());
        assertEquals(3962, runway.originalTodaProperty().get());
        assertEquals(3884, runway.originalAsdaProperty().get());
        assertEquals(3884, runway.originalLdaProperty().get());
        assertEquals(2986, runway.getRedeclaredTora());
        assertEquals(2986, runway.getRedeclaredToda());
        assertEquals(2986, runway.getRedeclaredAsda());
        assertEquals(3346, runway.getRedeclaredLda());
    }

    @Test
    public void testHeathrowScenario2a() {
        runway = new Runway("09R", 3660, 0, 0, 307);
        obstacle = new Obstacle("test2a", 25, -20, 0, 2853);

        runway.addObstacle(obstacle);
        runway.recalculate();

        assertEquals(20, runway.getDistanceFromCentreline());
        assertEquals(3660, runway.originalToraProperty().get());
        assertEquals(3660, runway.originalTodaProperty().get());
        assertEquals(3660, runway.originalAsdaProperty().get());
        assertEquals(3353, runway.originalLdaProperty().get());
        assertEquals(1850, runway.getRedeclaredTora());
        assertEquals(1850, runway.getRedeclaredToda());
        assertEquals(1850, runway.getRedeclaredAsda());
        assertEquals(2553, runway.getRedeclaredLda());
    }

    @Test
    public void testHeathrowScenario2b() {
        runway = new Runway("27L", 3660, 0, 0, 0);
        obstacle = new Obstacle("test2b", 25, -20, 0, 500);

        runway.addObstacle(obstacle);
        runway.recalculate();

        assertEquals(20, runway.getDistanceFromCentreline(), 0);
        assertEquals(3660,runway.originalToraProperty().get());
        assertEquals(3660,runway.originalTodaProperty().get());
        assertEquals(3660,runway.originalAsdaProperty().get());
        assertEquals(3660,runway.originalLdaProperty().get());
        assertEquals(2860,runway.getRedeclaredTora());
        assertEquals(2860,runway.getRedeclaredToda());
        assertEquals(2860,runway.getRedeclaredAsda());
        assertEquals(1850,runway.getRedeclaredLda());
    }

    @Test
    public void testHeathrowScenario3a() {
        runway = new Runway("09R", 3660, 0, 0, 307);
        obstacle = new Obstacle("test3a", 15, 60, 0, 150);

        runway.addObstacle(obstacle);
        runway.recalculate();

        assertEquals(60, runway.getDistanceFromCentreline());
        assertEquals(3660, runway.originalToraProperty().get());
        assertEquals(3660, runway.originalTodaProperty().get());
        assertEquals(3660, runway.originalAsdaProperty().get());
        assertEquals(3353, runway.originalLdaProperty().get());
        assertEquals(2903, runway.getRedeclaredTora());
        assertEquals(2903, runway.getRedeclaredToda());
        assertEquals(2903, runway.getRedeclaredToda());
        assertEquals(2393, runway.getRedeclaredLda());
    }

    @Test
    public void testHeathrowScenario3b() {
        runway = new Runway("27L", 3660, 0, 0, 0);
        obstacle = new Obstacle("test3b", 15, 60, 0, 3203);

        runway.addObstacle(obstacle);
        runway.recalculate();

        assertEquals(60, runway.getDistanceFromCentreline(), 0);
        assertEquals(3660,runway.originalToraProperty().get());
        assertEquals(3660,runway.originalTodaProperty().get());
        assertEquals(3660,runway.originalAsdaProperty().get());
        assertEquals(3660,runway.originalLdaProperty().get());
        assertEquals(2393, runway.getRedeclaredTora());
        assertEquals(2393, runway.getRedeclaredToda());
        assertEquals(2393, runway.getRedeclaredAsda());
        assertEquals(2903, runway.getRedeclaredLda());
    }

    @Test
    public void testHeathrowScenario4a() {
        runway = new Runway("09L", 3902, 0, 0, 306);
        obstacle = new Obstacle("test4a", 20, 20, 0, 3546);

        runway.addObstacle(obstacle);
        runway.recalculate();

        assertEquals(20, runway.getDistanceFromCentreline(), 0);
        assertEquals(3902, runway.originalToraProperty().get());
        assertEquals(3902, runway.originalTodaProperty().get());
        assertEquals(3902, runway.originalAsdaProperty().get());
        assertEquals(3596, runway.originalLdaProperty().get()); // Changed this from 3595 in the spec to 3956, typo
        assertEquals(2792, runway.getRedeclaredTora());
        assertEquals(2792, runway.getRedeclaredToda());
        assertEquals(2792, runway.getRedeclaredAsda());
        assertEquals(3246, runway.getRedeclaredLda());
    }

    @Test
    public void testHeathrowScenario4b() {
        runway = new Runway("27R", 3884, 78, 0, 0);
        obstacle = new Obstacle("test4b", 20, 20, 0, 50);

        runway.addObstacle(obstacle);
        runway.recalculate();

        assertEquals(20, runway.getDistanceFromCentreline(), 0);
        assertEquals(3884, runway.originalToraProperty().get());
        assertEquals(3962, runway.originalTodaProperty().get());
        assertEquals(3884, runway.originalAsdaProperty().get());
        assertEquals(3884, runway.originalLdaProperty().get());
        assertEquals(3534, runway.getRedeclaredTora());
        assertEquals(3612, runway.getRedeclaredToda());
        assertEquals(3534, runway.getRedeclaredAsda());
        assertEquals(2774, runway.getRedeclaredLda());
    }

    @Test
    public void testObstacle50mBeforeStart() {
        runway = new Runway("09L", 3902, 0, 0, 306);
        obstacle = new Obstacle("test50mBeforeStart", 12, 0, 0, -50);
        runway.addObstacle(obstacle);
        runway.recalculate();


        assertEquals(0, runway.getDistanceFromCentreline());
        assertEquals(3902, runway.originalToraProperty().get());
        assertEquals(3902, runway.originalTodaProperty().get());
        assertEquals(3902, runway.originalAsdaProperty().get());
        assertEquals(3596, runway.originalLdaProperty().get());
        assertEquals(3346, runway.getRedeclaredTora());
        assertEquals(3346, runway.getRedeclaredToda());
        assertEquals(3346, runway.getRedeclaredAsda());
        assertEquals(2986, runway.getRedeclaredLda());
    }
    @Test
    public void testObstacle60mAfterEnd() {
        runway = new Runway("27R", 3884, 78, 0, 0);
        obstacle = new Obstacle("test60mAfterEnd", 12, 0, 0, 3646);
        runway.addObstacle(obstacle);
        runway.recalculate();



        assertEquals(0, runway.getDistanceFromCentreline());
        assertEquals(3884, runway.originalToraProperty().get());
        assertEquals(3962, runway.originalTodaProperty().get());
        assertEquals(3884, runway.originalAsdaProperty().get());
        assertEquals(3884, runway.originalLdaProperty().get());
        assertEquals(2986, runway.getRedeclaredTora());
        assertEquals(2986, runway.getRedeclaredToda());
        assertEquals(2986, runway.getRedeclaredAsda());
        assertEquals(3346, runway.getRedeclaredLda());
    }
    @Test
    public void testObstacleGreaterThan75m() {
        runway = new Runway("09R", 3660, 0, 0, 307);
        obstacle = new Obstacle("testGreaterThan75m", 25, -76, 0, 2853);
        runway.addObstacle(obstacle);
        runway.recalculate();


        assertEquals(76, runway.getDistanceFromCentreline());
        assertEquals(3660, runway.originalToraProperty().get());
        assertEquals(3660, runway.originalTodaProperty().get());
        assertEquals(3660, runway.originalAsdaProperty().get());
        assertEquals(3353, runway.originalLdaProperty().get());
        assertEquals(3660, runway.getRedeclaredTora());
        assertEquals(3660, runway.getRedeclaredToda());
        assertEquals(3660, runway.getRedeclaredAsda());
        assertEquals(3353, runway.getRedeclaredLda());
    }




}
