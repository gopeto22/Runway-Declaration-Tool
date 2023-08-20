package com.example.runway_redeclaration;

/**
 * DON'T MOVE THIS CLASS IF YOU DO IT BREAKS FOR SOME REASON
 */
public class ChangelogTableObject {
    private String title;
    private String type;
    private String desc;
    private String time;
    private String name;

    public ChangelogTableObject(String title, String type, String desc, String time, String name) {
        this.title = title;
        this.type = type;
        this.desc = desc;
        this.time = time;
        this.name = name;
    }

    /**
     * getters
     */

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public String getTime() {
        return time;
    }

    public String getName() {
        return name;
    }
}
