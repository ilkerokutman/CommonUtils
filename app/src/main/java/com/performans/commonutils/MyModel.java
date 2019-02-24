package com.performans.commonutils;

public class MyModel {

    String title, desc;
    boolean hasAction;

    public MyModel() {

    }

    public MyModel(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public MyModel(String title, String desc, boolean hasAction) {
        this.title = title;
        this.desc = desc;
        this.hasAction = hasAction;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public boolean hasAction() {
        return hasAction;
    }
}
