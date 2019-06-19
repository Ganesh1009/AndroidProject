package de.innocow.innocow_v001.pojo.cowdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyCowActivity {

    @SerializedName("0")
    @Expose
    private double laying0;

    @SerializedName("1")
    @Expose
    private double laying1;

    @SerializedName("2")
    @Expose
    private double laying2;

    @SerializedName("3")
    @Expose
    private double rumination3;

    @SerializedName("4")
    @Expose
    private double rumination4;

    @SerializedName("5")
    @Expose
    private double rumination5;

    @SerializedName("9")
    @Expose
    private double vitality9;

    @SerializedName("10")
    @Expose
    private double vitality10;

    @SerializedName("11")
    @Expose
    private double vitality11;

    @SerializedName("12")
    @Expose
    private double vitality12;

    public double getLaying0() {
        return laying0;
    }

    public void setLaying0(double laying0) {
        this.laying0 = laying0;
    }

    public double getLaying1() {
        return laying1;
    }

    public void setLaying1(double laying1) {
        this.laying1 = laying1;
    }

    public double getLaying2() {
        return laying2;
    }

    public void setLaying2(double laying2) {
        this.laying2 = laying2;
    }

    public double getRumination3() {
        return rumination3;
    }

    public void setRumination3(double rumination3) {
        this.rumination3 = rumination3;
    }

    public double getRumination4() {
        return rumination4;
    }

    public void setRumination4(double rumination4) {
        this.rumination4 = rumination4;
    }

    public double getRumination5() {
        return rumination5;
    }

    public void setRumination5(double rumination5) {
        this.rumination5 = rumination5;
    }

    public double getVitality9() {
        return vitality9;
    }

    public void setVitality9(double vitality9) {
        this.vitality9 = vitality9;
    }

    public double getVitality10() {
        return vitality10;
    }

    public void setVitality10(double vitality10) {
        this.vitality10 = vitality10;
    }

    public double getVitality11() {
        return vitality11;
    }

    public void setVitality11(double vitality11) {
        this.vitality11 = vitality11;
    }

    public double getVitality12() {
        return vitality12;
    }

    public void setVitality12(double vitality12) {
        this.vitality12 = vitality12;
    }

    @Override
    public String toString() {
        return "DailyCowActivity{" +
                "laying0=" + laying0 +
                ", laying1=" + laying1 +
                ", laying2=" + laying2 +
                ", rumination3=" + rumination3 +
                ", rumination4=" + rumination4 +
                ", rumination5=" + rumination5 +
                ", vitality9=" + vitality9 +
                ", vitality10=" + vitality10 +
                ", vitality11=" + vitality11 +
                ", vitality12=" + vitality12 +
                '}';
    }
}
