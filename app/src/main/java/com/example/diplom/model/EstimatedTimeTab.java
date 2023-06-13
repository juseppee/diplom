package com.example.diplom.model;

public class EstimatedTimeTab {

    private String tabName;
    private Integer tabTimeTo;
    private Integer tabTimeFrom;

    public EstimatedTimeTab(String tabName, Integer tabTimeFrom, Integer tabTimeTo) {
        this.tabName = tabName;
        this.tabTimeFrom = tabTimeFrom;
        this.tabTimeTo = tabTimeTo;
    }

    public String getTabName() {
        return tabName;
    }

    public Integer getTabTimeTo() {
        return tabTimeTo;
    }

    public Integer getTabTimeFrom() {
        return tabTimeFrom;
    }
}
