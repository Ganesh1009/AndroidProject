/*package de.innocow.innocow_v001;

public class LoginValues {
    private String username;
    private String password;
    private String farmId;

    public String getFarmId() {
        return farmId;
    }

    public void setFarmId(String farmId) {
        this.farmId = farmId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
*/


package de.innocow.innocow_v001.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class LoginValues {

//    public String getStatusMessage() {return statusMessage; }

//    public void setStatusMessage(String statusMessage) {this.statusMessage = statusMessage; }

//    @SerializedName("statusMessage")
//    @Expose
//   private String statusMessage;

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("apiKey")
    @Expose
    private String apiKey;
    @SerializedName("farmID")
    @Expose
    private long farmID;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("expirationTime")
    @Expose
    private Long expirationTime;
//    @SerializedName("userPermission")
//    @Expose
//    private UserPermission userPermission;
//    @SerializedName("userRoles")
//    @Expose
//    private List<UserRole> userRoles = null;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public long getFarmID() {
        return farmID;
    }

    public void setFarmID(long farmID) {
        this.farmID = farmID;
    }

    public String getToken() {
        return token;
    }

    public void setToken() {
        this.token = token;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }



/*    public UserPermission getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(UserPermission userPermission) {
        this.userPermission = userPermission;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<UserRole> userRoles) {
        this.userRoles = userRoles;
    }
*/
}

