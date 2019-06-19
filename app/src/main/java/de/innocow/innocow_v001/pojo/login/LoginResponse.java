
package de.innocow.innocow_v001.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

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
    private LoginValues value;

    public Integer getStatusCode() { return statusCode; }

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

    public LoginValues getValue() {
        return value;
    }

    public void setValue(LoginValues value) {this.value = value;}

}
