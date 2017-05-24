package com.example.myfirstapp.bean;

/**
 * Created by 若希 on 2017/5/17.
 */

public class CollectData {
    private String collectId;
    private String collectTitle;
    private String collectType;
    private String collectUrl;
    private String collectPhoto;
    private String collectTime;
    private String collectAuthor;


    public CollectData(String collectId, String collectTitle, String collectType, String collectUrl, String collectPhoto, String collectTime,String collectAuthor) {
        this.collectId = collectId;
        this.collectTitle = collectTitle;
        this.collectType = collectType;
        this.collectUrl = collectUrl;
        this.collectPhoto = collectPhoto;
        this.collectTime = collectTime;
        this.collectAuthor=collectAuthor;
    }

    public String getCollectAuthor() {
        return collectAuthor;
    }

    public void setCollectAuthor(String collectAuthor) {
        this.collectAuthor = collectAuthor;
    }

    public String getCollectId() {
        return collectId;
    }

    public void setCollectId(String collectId) {
        this.collectId = collectId;
    }

    public String getCollectTitle() {
        return collectTitle;
    }

    public void setCollectTitle(String collectTitle) {
        this.collectTitle = collectTitle;
    }

    public String getCollectType() {
        return collectType;
    }

    public void setCollectType(String collectType) {
        this.collectType = collectType;
    }

    public String getCollectUrl() {
        return collectUrl;
    }

    public void setCollectUrl(String collectUrl) {
        this.collectUrl = collectUrl;
    }

    public String getCollectPhoto() {
        return collectPhoto;
    }

    public void setCollectPhoto(String collectPhoto) {
        this.collectPhoto = collectPhoto;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }
}
