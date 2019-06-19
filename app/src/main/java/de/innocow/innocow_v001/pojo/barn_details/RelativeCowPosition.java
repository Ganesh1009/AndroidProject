package de.innocow.innocow_v001.pojo.barn_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class RelativeCowPosition implements Serializable {

    @SerializedName("cowID")
    @Expose
    private String cowId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("numberOfShed")
    @Expose
    private String shedNumber;

    @SerializedName("status")
    @Expose
    private String status;

    private float colorIndicator;

    @SerializedName("timeStamp")
    @Expose
    private String timeStamp;

    @SerializedName("x")
    @Expose
    private float xPosition;

    @SerializedName("y")
    @Expose
    private float yPosition;

    @SerializedName("tagID")
    @Expose
    private String tagId;

    public String getCowId() {
        return cowId;
    }

    public void setCowId(String cowId) {
        this.cowId = cowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShedNumber() {
        return shedNumber;
    }

    public void setShedNumber(String shedNumber) {
        this.shedNumber = shedNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getColorIndicator() {
        return colorIndicator;
    }

    public void setColorIndicator(float colorIndicator) {
        this.colorIndicator = colorIndicator;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public float getxPosition() {
        return xPosition;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RelativeCowPosition)) return false;
        RelativeCowPosition that = (RelativeCowPosition) o;
        return Float.compare(that.getColorIndicator(), getColorIndicator()) == 0 &&
                Float.compare(that.getxPosition(), getxPosition()) == 0 &&
                Float.compare(that.getyPosition(), getyPosition()) == 0 &&
                Objects.equals(getCowId(), that.getCowId()) &&
                Objects.equals(getName(), that.getName()) &&
                Objects.equals(getShedNumber(), that.getShedNumber()) &&
                Objects.equals(getStatus(), that.getStatus()) &&
                Objects.equals(getTimeStamp(), that.getTimeStamp()) &&
                Objects.equals(getTagId(), that.getTagId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCowId(), getName(), getShedNumber(), getStatus(), getColorIndicator(), getTimeStamp(), getxPosition(), getyPosition(), getTagId());
    }

    @Override
    public String toString() {
        return "RelativeCowPosition{" +
                "cowId='" + cowId + '\'' +
                ", name='" + name + '\'' +
                ", shedNumber='" + shedNumber + '\'' +
                ", status='" + status + '\'' +
                ", colorIndicator=" + colorIndicator +
                ", timeStamp='" + timeStamp + '\'' +
                ", xPosition=" + xPosition +
                ", yPosition=" + yPosition +
                ", tagId='" + tagId + '\'' +
                '}';
    }
}
