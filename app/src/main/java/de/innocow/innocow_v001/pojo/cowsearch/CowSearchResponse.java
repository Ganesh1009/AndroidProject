
package de.innocow.innocow_v001.pojo.cowsearch;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CowSearchResponse {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("value")
    @Expose
    private List<CowValues> cowValuesListvalue = null;

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

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public List<CowValues> getCowValueList() {
        return cowValuesListvalue;
    }

    public void setCowValueList(List<CowValues> value) {
        this.cowValuesListvalue = value;
    }

}
