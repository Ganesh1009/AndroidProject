package de.innocow.innocow_v001.pojo.cowsearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HeatCycleGraph implements Serializable {
    @SerializedName("milkAvg")
    @Expose
    private int milkAvg;

    @SerializedName("hoursInHeat")
    @Expose
    private int hoursInHeat;

    @SerializedName("daysInCycle")
    @Expose
    private int daysInCycle;

    public int getMilkAvg() {
        return milkAvg;
    }

    public void setMilkAvg(int milkAvg) {
        this.milkAvg = milkAvg;
    }

    public int getHoursInHeat() {
        return hoursInHeat;
    }

    public void setHoursInHeat(int hoursInHeat) {
        this.hoursInHeat = hoursInHeat;
    }

    public int getDaysInCycle() {
        return daysInCycle;
    }

    public void setDaysInCycle(int daysInCycle) {
        this.daysInCycle = daysInCycle;
    }

    @Override
    public String toString() {
        return "HeatCycleGraph{" +
                "milkAvg=" + milkAvg +
                ", hoursInHeat=" + hoursInHeat +
                ", daysInCycle=" + daysInCycle +
                '}';
    }
}