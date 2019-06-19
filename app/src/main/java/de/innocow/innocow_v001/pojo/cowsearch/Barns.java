package de.innocow.innocow_v001.pojo.cowsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Barns {
    @SerializedName("id")
    @Expose
    private Integer barnId;

    public Integer getBarnId() {
        return barnId;
    }

    public void setBarnId(Integer barnId) {
        this.barnId = barnId;
    }

    @Override
    public String toString() {
        return "Barns{" +
                "barnId=" + barnId +
                '}';
    }
}
