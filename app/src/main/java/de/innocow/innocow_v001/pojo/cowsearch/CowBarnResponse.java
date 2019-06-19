package de.innocow.innocow_v001.pojo.cowsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CowBarnResponse {

    @SerializedName("value")
    @Expose
    private List<Barns> cowBarn;

    public List<Barns> getCowBarn() {
        return cowBarn;
    }

    public void setCowBarn(List<Barns> cowBarn) {
        this.cowBarn = cowBarn;
    }

    @Override
    public String toString() {
        return "CowBarnResponse{" +
                "getBarnsList=" + cowBarn +
                '}';
    }
}
