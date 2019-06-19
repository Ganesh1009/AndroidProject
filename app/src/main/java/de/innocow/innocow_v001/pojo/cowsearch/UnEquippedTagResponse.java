package de.innocow.innocow_v001.pojo.cowsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UnEquippedTagResponse {

    @SerializedName("value")
    @Expose
    private List<UnEquippedTagList> unEquippedTagList;

    public List<UnEquippedTagList> getUnEquippedTagList() {
        return unEquippedTagList;
    }

    public void setUnEquippedTagList(List<UnEquippedTagList> unEquippedTagList) {
        this.unEquippedTagList = unEquippedTagList;
    }

    @Override
    public String toString() {
        return "UnEquippedTagList{" +
                "unEquippedTagList=" + unEquippedTagList +
                '}';
    }
}
