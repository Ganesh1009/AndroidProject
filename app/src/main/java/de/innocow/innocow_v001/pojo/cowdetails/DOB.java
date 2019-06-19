package de.innocow.innocow_v001.pojo.cowdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.innocow.innocow_v001.pojo.cowsearch.CowBirthdate;

public class DOB {
    @SerializedName("value")
    @Expose
    private CowBirthdate cowBirthdate;

    public CowBirthdate getCowBirthdate() {
        return cowBirthdate;
    }

    public void setCowBirthdate(CowBirthdate cowBirthdate) {
        this.cowBirthdate = cowBirthdate;
    }

    @Override
    public String toString() {
        return "DOB{" +
                "cowBirthdate=" + cowBirthdate +
                '}';
    }


}
