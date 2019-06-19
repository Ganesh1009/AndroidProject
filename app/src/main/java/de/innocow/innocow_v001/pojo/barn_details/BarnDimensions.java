package de.innocow.innocow_v001.pojo.barn_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BarnDimensions {

    @SerializedName("value")
    @Expose
    Dimensions dimensions;

    public Dimensions getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public String toString() {
        return "BarnDimensions{" +
                "dimensions=" + dimensions +
                '}';
    }
}
