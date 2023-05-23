package com.example.diplom;

public class Estimate {

    private String estimateName;
    private String estimateDesc;
    private String estimateAddInfo;
    private String estimateImage;
    private String key;
    private String userId;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEstimateName() {
        return estimateName;
    }

    public String getEstimateDesc() {
        return estimateDesc;
    }

    public String getEstimateAddInfo() {
        return estimateAddInfo;
    }

    public String getEstimateImage() {
        return estimateImage;
    }

    public String getUserId() {
        return userId;
    }

    public Estimate(String estimateName, String estimateDesc, String estimateAddInfo, String estimateImage, String userId) {
        this.estimateName = estimateName;
        this.estimateDesc = estimateDesc;
        this.estimateAddInfo = estimateAddInfo;
        this.estimateImage = estimateImage;
        this.userId = userId;
    }

    public Estimate() {

    }
}
