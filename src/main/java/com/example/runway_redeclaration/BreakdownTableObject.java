package com.example.runway_redeclaration;

/**
 * DON'T MOVE THIS CLASS IF YOU DO IT BREAKS FOR SOME REASON
 */
public class BreakdownTableObject {
    private String eq;
    private String calc;
    private String result;

    public BreakdownTableObject(String eq, String calc, String result){
        this.eq = eq;
        this.calc = calc;
        this.result = result;
    }

    /**
     * getters
     */

    public String getEq() {
        return eq;
    }

    public String getCalc() {
        return calc;
    }

    public String getResult() {
        return result;
    }
}
