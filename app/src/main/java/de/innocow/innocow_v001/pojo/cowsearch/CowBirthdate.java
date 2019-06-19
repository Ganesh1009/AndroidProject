package de.innocow.innocow_v001.pojo.cowsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CowBirthdate implements Serializable {
    @SerializedName("birthday")
    @Expose
    private String birthdate;

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public String toString() {
        return "CowBirthdate{" +
                "birthdate='" + birthdate + '\'' +
                '}';
    }
}
