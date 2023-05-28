package com.example.diplom.model;

public class EstimateLine {

    private String subtaskName;
    private String subtaskTo;
    private String subtaskFrom;
    private String tabName;
    private String key;
    private String userId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSubtaskName() {
        return subtaskName;
    }

    public String getSubtaskTo() {
        return subtaskTo;
    }

    public String getSubtaskFrom() {
        return subtaskFrom;
    }

    public String getUserId() {
        return userId;
    }

    public EstimateLine(String subtaskName, String subtaskTo, String subtaskFrom, String userId, String tabName) {
        this.subtaskName = subtaskName;
        this.subtaskTo = subtaskTo;
        this.subtaskFrom = subtaskFrom;
        this.tabName = tabName;
        this.userId = userId;
    }

    public EstimateLine() {

    }

    public String getTabName() {
        return tabName;
    }
}

