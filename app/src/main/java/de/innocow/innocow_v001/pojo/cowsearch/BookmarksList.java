package de.innocow.innocow_v001.pojo.cowsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BookmarksList implements Serializable {

    @SerializedName("numberOfShed")
    @Expose
    private Integer shedNumber;

    @SerializedName("heatStatus")
    @Expose
    private Integer heatStatus;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("tagID")
    @Expose
    private String tagId;

    @SerializedName("cowID")
    @Expose
    private String cowId;

    @SerializedName("farmID")
    @Expose
    private Long farmId;

    public Integer getShedNumber() {
        return shedNumber;
    }

    public void setShedNumber(Integer shedNumber) {
        this.shedNumber = shedNumber;
    }

    public Integer getHeatStatus() {
        return heatStatus;
    }

    public void setHeatStatus(Integer heatStatus) {
        this.heatStatus = heatStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getCowId() {
        return cowId;
    }

    public void setCowId(String cowId) {
        this.cowId = cowId;
    }

    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

}
