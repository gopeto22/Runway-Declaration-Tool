package com.example.runway_redeclaration;

/**
 * DON'T MOVE THIS CLASS IF YOU DO IT BREAKS FOR SOME REASON
 */
public class ObstacleHistoryTableObject {
    private String name;
    private String add;
    private String del;

    public ObstacleHistoryTableObject(String name, String add, String del){
        this.name = name;
        this.add = add;
        this.del = del;
    }

    /**
     * getters
     */

    public String getName() {
        return name;
    }

    public String getAdd() {
        return add;
    }

    public String getDel() {
        return del;
    }
}
