
package de.innocow.innocow_v001.pojo.cowsearch;

import androidx.annotation.NonNull;

import com.google.android.gms.vision.label.internal.client.INativeImageLabeler;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CowValues implements Serializable {

    @SerializedName("@timestamp")
    @Expose
    private String timestamp;
    @SerializedName("heat_status")
    @Expose
    private String heatStatus;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("@version")
    @Expose
    private String version;
    @SerializedName("tag_id")
    @Expose
    private String tagId;
    @SerializedName("cow_id")
    @Expose
    private String cowId;
    @SerializedName("barn_id")
    @Expose
    private Integer barnId;
    @SerializedName("farm_id")
    @Expose
    private Long farmId;

    @SerializedName("number_of_shed")
    @Expose
    private Integer shedNumber;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getShedNumber() {
        return shedNumber;
    }

    public void setShedNumber(Integer shedNumber) {
        this.shedNumber = shedNumber;
    }

    public String getHeatStatus() {
        return heatStatus;
    }

    public void setHeatStatus(String heatStatus) {
        this.heatStatus = heatStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
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

    public Integer getBarnId() {
        return barnId;
    }

    public void setBarnId(Integer barnId) {
        this.barnId = barnId;
    }

    public Long getFarmId() {
        return farmId;
    }

    public void setFarmId(Long farmId) {
        this.farmId = farmId;
    }

    @Override
    public String toString() {
        return "CowValues{" +
                "timestamp='" + timestamp + '\'' +
                ", shedNumber='" + shedNumber + '\'' +
                ", heatStatus='" + heatStatus + '\'' +
                ", name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", tagId='" + tagId + '\'' +
                ", cowId='" + cowId + '\'' +
                ", barnId=" + barnId +
                ", farmId=" + farmId +
                '}';
    }
}
