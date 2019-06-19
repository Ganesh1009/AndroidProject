package de.innocow.innocow_v001.pojo.barn_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class StatusList {
    @SerializedName("tagID")
    @Expose
    private String tagId;

    @SerializedName("cowID")
    @Expose
    private String cowId;

    @SerializedName("status")
    @Expose
    private String status;

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getCowId() {
        return cowId;
    }

    public void setCowId(String cowId) {
        this.cowId = cowId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusList{" +
                "tagId='" + tagId + '\'' +
                ", cowId='" + cowId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
