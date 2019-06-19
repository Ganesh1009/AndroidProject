package de.innocow.innocow_v001.pojo.barn_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CowPositionInformation {
    @SerializedName("value")
    @Expose
    private List<RelativeCowPosition> cowPositionList;

    public List<RelativeCowPosition> getCowPositionList() {
        return cowPositionList;
    }

    public void setCowPositionList(List<RelativeCowPosition> cowPositionList) {
        this.cowPositionList = cowPositionList;
    }

    @Override
    public String toString() {
        return "CowPositionInformation{" +
                "cowPositionList=" + cowPositionList +
                '}';
    }
}
