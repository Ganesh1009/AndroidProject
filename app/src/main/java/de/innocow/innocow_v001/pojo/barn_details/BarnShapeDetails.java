package de.innocow.innocow_v001.pojo.barn_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BarnShapeDetails {

    @SerializedName("value")
    @Expose
    private List<Shapes> barnShapeList;

    public List<Shapes> getBarnShapeList() {
        return barnShapeList;
    }

    public void setBarnShapeList(List<Shapes> barnShapeList) {
        this.barnShapeList = barnShapeList;
    }

    @Override
    public String toString() {
        return "BarnShapeDetails{" +
                "barnShapeList=" + barnShapeList +
                '}';
    }
}
