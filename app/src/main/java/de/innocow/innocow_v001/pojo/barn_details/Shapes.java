package de.innocow.innocow_v001.pojo.barn_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Shapes {

    @SerializedName("barnID")
    @Expose
    private int barnId;

    @SerializedName("type")
    @Expose
    private String shape;

    @SerializedName("coordinates")
    @Expose
    private List<List<List<String>>> coordinates;

    public int getBarnId() {
        return barnId;
    }

    public void setBarnId(int barnId) {
        this.barnId = barnId;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public List<List<List<String>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<List<String>>> coordinates) {
        this.coordinates = coordinates;
    }

//    @Override
//    public String toString() {
//        return "Shapes{" +
//                "barnId='" + barnId + '\'' +
//                ", shape='" + shape + '\'' +
//                ", coordinates=" + coordinates +
//                '}';
//    }
}
