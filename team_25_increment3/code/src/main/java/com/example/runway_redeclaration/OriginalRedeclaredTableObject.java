package com.example.runway_redeclaration;

/**
 * Class to display data in a table on the simulation scene.
 */
public class OriginalRedeclaredTableObject {
    /**
     * The parameter e.g. TORA
     */
    private String parameter;

    /**
     * The original value of the given parameter.
     */
    private String originalValue;

    /**
     * The redeclared value of the given parameter.
     */
    private String redeclaredValue;

    /**
     * OriginalRedeclaredTable constructor.
     *
     * @param parameter the parameter being displayed
     * @param originalValue original value for that parameter
     * @param redeclaredValue redeclared value for that parameter
     */
    public OriginalRedeclaredTableObject(String parameter, String originalValue, String redeclaredValue) {
        this.parameter = parameter;
        this.originalValue = originalValue;
        this.redeclaredValue = redeclaredValue;
    }

    /**
     * @return the parameter
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * @return the original value for the parameter
     */
    public String getOriginalValue() {
        return originalValue;
    }

    /**
     * @return the redeclared value for the parameter
     */
    public String getRedeclaredValue() {
        return redeclaredValue;
    }
}
