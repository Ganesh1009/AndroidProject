package de.innocow.innocow_v001.pojo.cowsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CowTagResponse {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("value")
    @Expose
    private String value;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "CowTagResponse{" +
                "statusCode=" + statusCode +
                ", success=" + success +
                ", value='" + value + '\'' +
                '}';
    }
}
