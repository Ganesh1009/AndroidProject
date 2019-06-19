package de.innocow.innocow_v001.pojo.cowsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UnEquippedTagList implements Serializable {

    @SerializedName("deviceName")
    @Expose
    private String unEquippedTag;

    public String getUnEquippedTag() {
        return unEquippedTag;
    }

    public void setUnEquippedTag(String unEquippedTag) {
        this.unEquippedTag = unEquippedTag;
    }

    @Override
    public String toString() {
        return "UnEquippedTagList{" +
                "unEquippedTag='" + unEquippedTag + '\'' +
                '}';
    }
}
