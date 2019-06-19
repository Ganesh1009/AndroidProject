package de.innocow.innocow_v001.pojo.barn_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SVGBarnPlan {

    @SerializedName("value")
    @Expose
    private String svgInformation;

    public String getSvgInformation() {
        return svgInformation;
    }

    public void setSvgInformation(String svgInformation) {
        this.svgInformation = svgInformation;
    }

    @Override
    public String toString() {
        return "SVGBarnPlan{" +
                "svgInformation='" + svgInformation + '\'' +
                '}';
    }
}
