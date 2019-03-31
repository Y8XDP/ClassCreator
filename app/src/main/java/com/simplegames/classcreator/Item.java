package com.simplegames.classcreator;

public class Item {
    private String ID;
    private String ClassName;

    public Item(String ID, String className) {
        this.ID = ID;
        ClassName = className;
    }

    public String getID() {
        return ID;
    }

    public String getClassName() {
        return ClassName;
    }
}
