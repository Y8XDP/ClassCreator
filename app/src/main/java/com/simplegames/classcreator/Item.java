package com.simplegames.classcreator;

public class Item {
    private String ID;
    private String ClassName;
    private String ClassExtend;

    public Item(String ID, String className, String ex) {
        this.ID = ID;
        this.ClassExtend = ex;
        ClassName = className;
    }

    public String getClassExtend() {
        return ClassExtend;
    }

    public String getID() {
        return ID;
    }

    public String getClassName() {
        return ClassName;
    }
}
