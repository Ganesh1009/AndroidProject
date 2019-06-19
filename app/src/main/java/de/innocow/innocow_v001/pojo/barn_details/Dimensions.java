package de.innocow.innocow_v001.pojo.barn_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dimensions {

    @SerializedName("id")
    @Expose
    private Integer barnId;

    @SerializedName("originX")
    @Expose
    private float originX;

    @SerializedName("originY")
    @Expose
    private float originY;

    @SerializedName("dimensionX")
    @Expose
    private float dimensionX;

    @SerializedName("dimensionY")
    @Expose
    private float dimensionY;


    public Integer getBarnId() {
        return barnId;
    }

    public void setBarnId(Integer barnId) {
        this.barnId = barnId;
    }

    public float getOriginX() {
        return originX;
    }

    public void setOriginX(float originX) {
        this.originX = originX;
    }

    public float getOriginY() {
        return originY;
    }

    public void setOriginY(float originY) {
        this.originY = originY;
    }

    public float getDimensionX() {
        return dimensionX;
    }

    public void setDimensionX(float dimensionX) {
        this.dimensionX = dimensionX;
    }

    public float getDimensionY() {
        return dimensionY;
    }

    public void setDimensionY(float dimensionY) {
        this.dimensionY = dimensionY;
    }

    @Override
    public String toString() {
        return "Dimensions{" +
                "barnId=" + barnId +
                ", originX=" + originX +
                ", originY=" + originY +
                ", dimensionX=" + dimensionX +
                ", dimensionY=" + dimensionY +
                '}';
    }
}
