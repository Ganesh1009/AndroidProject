package de.innocow.innocow_v001.pojo.barn_details;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class BarnStatus implements Serializable {
    @SerializedName("barnID")
    @Expose
    private Integer barnId;

    @SerializedName("none")
    @Expose
    private Integer healthyCowCount;

    @SerializedName("beforeHeat")
    @Expose
    private Integer beforeHeat;

    @SerializedName("cowsInHeat")
    @Expose
    private Integer cowsInHeat;

    @SerializedName("afterHeat")
    @Expose
    private Integer afterHeat;

    @SerializedName("sick")
    @Expose
    private Integer sick;

    @SerializedName("task")
    @Expose
    private Integer task;

//    @SerializedName("statusList")
//    @Expose
//    private List<StatusList> statusList;


    public Integer getBarnId() {
        return barnId;
    }

    public void setBarnId(Integer barnId) {
        this.barnId = barnId;
    }

    public Integer getHealthyCowCount() {
        return healthyCowCount;
    }

    public void setHealthyCowCount(Integer healthyCowCount) {
        this.healthyCowCount = healthyCowCount;
    }

    public Integer getBeforeHeat() {
        return beforeHeat;
    }

    public void setBeforeHeat(Integer beforeHeat) {
        this.beforeHeat = beforeHeat;
    }

    public Integer getCowsInHeat() {
        return cowsInHeat;
    }

    public void setCowsInHeat(Integer cowsInHeat) {
        this.cowsInHeat = cowsInHeat;
    }

    public Integer getAfterHeat() {
        return afterHeat;
    }

    public void setAfterHeat(Integer afterHeat) {
        this.afterHeat = afterHeat;
    }

    public Integer getSick() {
        return sick;
    }

    public void setSick(Integer sick) {
        this.sick = sick;
    }

    public Integer getTask() {
        return task;
    }

    public void setTask(Integer task) {
        this.task = task;
    }

    @Override
    public String toString() {
        return "BarnStatus{" +
                "barnId=" + barnId +
                ", healthyCowCount=" + healthyCowCount +
                ", beforeHeat=" + beforeHeat +
                ", cowsInHeat=" + cowsInHeat +
                ", afterHeat=" + afterHeat +
                ", sick=" + sick +
                ", task=" + task +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BarnStatus)) return false;
        BarnStatus that = (BarnStatus) o;
        return Objects.equals(getBarnId(), that.getBarnId()) &&
                Objects.equals(getHealthyCowCount(), that.getHealthyCowCount()) &&
                Objects.equals(getBeforeHeat(), that.getBeforeHeat()) &&
                Objects.equals(getCowsInHeat(), that.getCowsInHeat()) &&
                Objects.equals(getAfterHeat(), that.getAfterHeat()) &&
                Objects.equals(getSick(), that.getSick()) &&
                Objects.equals(getTask(), that.getTask());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBarnId(), getHealthyCowCount(), getBeforeHeat(), getCowsInHeat(), getAfterHeat(), getSick(), getTask());
    }
}
