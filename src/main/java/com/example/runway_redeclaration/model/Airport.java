package com.example.runway_redeclaration.model;

/**
 * The airport class contains the values of safety variables like the resa, stripend etc. These
 * variables are used by the runway class in calculations
 */
public class Airport {

    /**
     * An area between the end of the runway and the end of the runway strip.
     * <p>
     * Default value is 60.
     */
    protected int stripEnd;

    /**
     * A safety area behind an aircraft to prevent the engine blast from damaging obstacles behind
     * it.
     * <p>
     * Default value is 300.
     */
    protected int blastProtection;

    /**
     * Runway End Safety Area
     * <p>
     * An area symmetrical about the extended runway centreline and adjacent to the end of the strip
     * to reduce the risk of damage to an aircraft undershooting/overrunning a runway.
     * <p>
     * Default value is 240.
     */
    protected int resa;

    public Airport(int stripEnd, int blastProtection, int resa) {
        this.stripEnd = stripEnd;
        this.blastProtection = blastProtection;
        this.resa = resa;
    }

    /**
     * Update details of the airport
     *
     * @param stripEnd        area at the end of the runway
     * @param blastProtection safety area behind an aircraft
     * @param resa            safety area to reduce damage when over/under shooting a runway
     */
    public void updateAirport(int stripEnd, int blastProtection, int resa) {
        this.stripEnd = stripEnd;
        this.blastProtection = blastProtection;
        this.resa = resa;
    }
}
