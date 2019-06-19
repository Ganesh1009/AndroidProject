package de.innocow.innocow_v001.pojo.cowdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import de.innocow.innocow_v001.pojo.cowsearch.HeatCycleGraph;

public class HeatCycle {
    @SerializedName("value")
    @Expose
    private HeatCycleGraph heatCycleGraph;

    public HeatCycleGraph getHeatCycleGraph() {
        return heatCycleGraph;
    }

    public void setHeatCycleGraph(HeatCycleGraph heatCycleGraph) {
        this.heatCycleGraph = heatCycleGraph;
    }

    @Override
    public String toString() {
        return "HeatCycle{" +
                "heatCycleGraph=" + heatCycleGraph +
                '}';
    }
}
