package de.innocow.innocow_v001.utilities;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.pojo.cowdetails.CowActivity;
import de.innocow.innocow_v001.utilities.retrofit.RestClient;
import de.innocow.innocow_v001.utilities.retrofit.RetrofitBuilder;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CowActivitiesPager extends Fragment {

    private View view;

    private String token, id;

    private static final int[] chartColors = {Color.rgb(169, 169, 169),
            Color.rgb(64, 224, 208), Color.rgb(255, 165, 0)};
    private PieChart pieChart;

    private List<PieEntry> entries = new ArrayList<>();

    public CowActivitiesPager (String token, String id) {
        this.token = token;
        this.id = id;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.fragment_cow_activities_pager,
                container, false);

        RestClient restClient = RetrofitBuilder.getRestClient();

        Call<CowActivity> cowActivityCall = restClient.getCowActivity(token, id);

        cowActivityCall.enqueue(new Callback<CowActivity>() {
            @Override
            public void onResponse(@NonNull Call<CowActivity> call,
                                   @NonNull Response<CowActivity> response) {

                int responseCode = response.code();
                if (response.body() != null) {

                    Log.d("response:", response.body().toString());
                    switch (responseCode) {
                        case 200:

                            double laying = response.body().getDailyCowActivity().getLaying0() +
                                    response.body().getDailyCowActivity().getLaying1() +
                                    response.body().getDailyCowActivity().getLaying2();

                            double rumination = response.body().getDailyCowActivity().getRumination3() +
                                    response.body().getDailyCowActivity().getRumination4() +
                                    response.body().getDailyCowActivity().getRumination5();

                            double vitality = response.body().getDailyCowActivity().getVitality9() +
                                    response.body().getDailyCowActivity().getVitality10() +
                                    response.body().getDailyCowActivity().getVitality11() +
                                    response.body().getDailyCowActivity().getVitality12();


                            entries.add(new PieEntry((float) vitality, StringConstants.VITALITY));
                            entries.add(new PieEntry((float) rumination, StringConstants.RUMINATION));
                            entries.add(new PieEntry((float) laying, StringConstants.LAYING));

                            ArrayList<Integer> colors = new ArrayList<>();
                            for (int c : chartColors)
                                colors.add(c);


                            PieDataSet set = new PieDataSet(entries,null);
                            set.setColors(colors);
                            PieData data = new PieData(set);

                            pieChart = view.findViewById(R.id.linechartfrag);
                            pieChart.setData(data);
                            pieChart.setNoDataTextTypeface(Typeface.MONOSPACE);
                            pieChart.setEntryLabelTextSize(14);
                            pieChart.setCenterTextSize(40);
                            data.setValueTextSize(12f);
                            data.setValueTextColor(Color.WHITE);
                            pieChart.setUsePercentValues(true);
                            pieChart.setCenterTextRadiusPercent(14);
                            pieChart.getLegend().setEnabled(false);
                            pieChart.getDescription().setEnabled(false);
                            pieChart.animateY(1000);
                            pieChart.invalidate(); // refresh

                            break;

                        case 400:
                            Log.d("Response code of ", responseCode +
                                    "for request " + response.body().toString());
                            break;
                        default:
                            Log.d("Not ok response",
                                    response.body().toString());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CowActivity> call, @NonNull Throwable t) {
                Log.e("Error in cow detail req", Arrays.toString(t.getStackTrace()));
            }
        });

        return view;
    }
}
