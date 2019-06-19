package de.innocow.innocow_v001.pojo.cowdetails;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CowActivity {

    @SerializedName("value")
    @Expose
    private DailyCowActivity dailyCowActivity;

    public DailyCowActivity getDailyCowActivity() {
        return dailyCowActivity;
    }

    public void setDailyCowActivity(DailyCowActivity dailyCowActivity) {
        this.dailyCowActivity = dailyCowActivity;
    }

    @Override
    public String toString() {
        return "CowActivity{" +
                "dailyCowActivity=" + dailyCowActivity +
                '}';
    }
}
