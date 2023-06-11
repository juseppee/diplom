package com.example.diplom.model;

public class EstimateSubTask {

    private String subtaskName;
    private String subtaskTo;
    private String subtaskFrom;
    private String tabName;
    private String key;
    private String userId;


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


    public EstimateSubTask(String subtaskName, String subtaskTo, String subtaskFrom, String userId, String tabName) {
        this.subtaskName = subtaskName;
        this.subtaskTo = subtaskTo;
        this.subtaskFrom = subtaskFrom;
        this.tabName = tabName;
        this.userId = userId;
    }

    public EstimateSubTask() {

    }

    public String getTabName() {
        return tabName;
    }
}

