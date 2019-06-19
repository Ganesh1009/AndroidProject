package de.innocow.innocow_v001.pojo.barn_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BarnStatusDetails {
    @SerializedName("value")
    @Expose
    private List<BarnStatus> barnStatusList;

    public List<BarnStatus> getBarnStatusList() {
        return barnStatusList;
    }

    public void setBarnStatusList(List<BarnStatus> barnStatusList) {
        this.barnStatusList = barnStatusList;
    }

    @Override
    public String toString() {
        return "BarnStatusDetails{" +
                "barnStatusList=" + barnStatusList +
                '}';
    }
}
