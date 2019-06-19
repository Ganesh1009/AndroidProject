package de.innocow.innocow_v001.utilities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.SingleValueDataSet;
import com.anychart.charts.CircularGauge;
import com.anychart.core.axes.Circular;
import com.anychart.core.gauge.pointers.Bar;
import com.anychart.enums.Anchor;
import com.anychart.enums.LabelsOverlapMode;
import com.anychart.graphics.vector.Fill;
import com.anychart.graphics.vector.SolidFill;
import com.anychart.graphics.vector.text.HAlign;
import com.anychart.graphics.vector.text.VAlign;

import java.util.ArrayList;
import java.util.List;

import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.pojo.cowdetails.HeatCycle;
import de.innocow.innocow_v001.utilities.retrofit.RestClient;
import de.innocow.innocow_v001.utilities.retrofit.RetrofitBuilder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CowDataPager extends Fragment {


    private String token, id;

    private AnyChartView anyChartView;
    private CircularGauge circularGauge;

    public CowDataPager(String token, String id) {

        this.token = token;
        this.id = id;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_cow_data_pager, container, false);

        generateCircularGaugeView();
        anyChartView = view.findViewById(R.id.cowDataGaugeView);
        circularGauge = AnyChart.circular();
        return view;
    }

    private void generateCircularGaugeView() {

        RestClient restClient = RetrofitBuilder.getRestClient();

        Call<HeatCycle> heatCycleCall = restClient.getHeatCycle(token, id);
        heatCycleCall.enqueue(new Callback<HeatCycle>() {
            @Override
            public void onResponse(@NonNull Call<HeatCycle> call, @NonNull Response<HeatCycle> response) {
                if (response.body() != null) {

                    int responseCode = response.code();
                    switch (responseCode) {

                        case 200:
                            Log.d("heat cycle", response.body().toString());
                            Integer milkAvg = response.body().getHeatCycleGraph().getMilkAvg();
                            Integer daysInCycle = response.body().getHeatCycleGraph().getDaysInCycle();
                            Integer hoursInHeat = response.body().getHeatCycleGraph().getHoursInHeat();

                            List<Object> dataToGaugeView = new ArrayList<>();
                            dataToGaugeView.add(milkAvg);
                            dataToGaugeView.add(daysInCycle);
                            dataToGaugeView.add(hoursInHeat);
                            dataToGaugeView.add(100);
                            circularGauge.data(new SingleValueDataSet(dataToGaugeView));
                            circularGauge.startAngle(0d);
                            circularGauge.sweepAngle(270d);
                            circularGauge.fill("#fff")
                                    .stroke(null)
                                    .padding(0d, 0d, 0d, 0d)
                                    .margin(100d, 100d, 100d, 100d);

                            Circular xAxis = circularGauge.axis(0)
                                    .radius(100d)
                                    .width(1d)
                                    .fill((Fill)null);
                            xAxis.scale()
                                    .minimum(0d)
                                    .maximum(100d);
                            xAxis.labels().enabled(false);
                            xAxis.ticks().enabled(false);

                            circularGauge.label(0d)
                                    .text("Amount of milk")
                                    .useHtml(true)
                                    .hAlign(HAlign.CENTER)
                                    .vAlign(VAlign.MIDDLE);
                            circularGauge.label(0d)
                                    .anchor(Anchor.RIGHT_CENTER)
                                    .padding(0d, 10d, 0d, 0d)
                                    .height(17d / 2d + "%")
                                    .offsetY(100d + "%")
                                    .offsetX(0d);

                            Bar bar0 = circularGauge.bar(0d);
                            bar0.dataIndex(0d);
                            bar0.radius(100d);
                            bar0.width(17d);
                            bar0.fill(new SolidFill("#64b5f6", 1d));
                            bar0.zIndex(5d);
                            Bar bar100 = circularGauge.bar(100d);
                            bar100.dataIndex(3d);
                            bar100.radius(100d);
                            bar100.width(17d);
                            bar100.fill(new SolidFill("#F5F4F4", 1d));
                            bar100.stroke("1 #e5e4e4");
                            bar100.zIndex(4d);

                            circularGauge.label(1d)
                                    .text("Days in cycle")
                                    .useHtml(true)
                                    .hAlign(HAlign.CENTER)
                                    .vAlign(VAlign.MIDDLE);
                            circularGauge.label(1d)
                                    .anchor(Anchor.RIGHT_CENTER)
                                    .padding(0d, 10d, 0d, 0d)
                                    .height(17d / 2d + "%")
                                    .offsetY(80d + "%")
                                    .offsetX(0d);
                            Bar bar1 = circularGauge.bar(1d);
                            bar1.dataIndex(1d);
                            bar1.radius(80d);
                            bar1.width(17d);
                            bar1.fill(new SolidFill("#1976d2", 1d));
                            bar1.zIndex(5d);
                            Bar bar101 = circularGauge.bar(101d);
                            bar101.dataIndex(3d);
                            bar101.radius(100d);
                            bar101.width(17d);
                            bar101.fill(new SolidFill("#F5F4F4", 1d));
                            bar101.stroke("1 #e5e4e4");
                            bar101.zIndex(4d);

                            circularGauge.label(2d)
                                    .text("Hours in heat")
                                    .useHtml(true)
                                    .hAlign(HAlign.CENTER)
                                    .vAlign(VAlign.MIDDLE);
                            circularGauge.label(2d)
                                .anchor(Anchor.RIGHT_CENTER)
                                .padding(0d, 10d, 0d, 0d)
                                .height(17d / 2d + "%")
                                .offsetY(60d + "%")
                                .offsetX(0d);
                            Bar bar2 = circularGauge.bar(2d);
                            bar2.dataIndex(2d);
                            bar2.radius(60d);
                            bar2.width(17d);
                            bar2.fill(new SolidFill("#ef6c00", 1d));
                            bar2.zIndex(5d);
                            Bar bar102 = circularGauge.bar(102d);
                            bar102.dataIndex(3d);
                            bar102.radius(100d);
                            bar102.width(17d);
                            bar102.fill(new SolidFill("#F5F4F4", 1d));
                            bar102.stroke("1 #e5e4e4");
                            bar102.zIndex(4d);
                            anyChartView.setChart(circularGauge);
                            break;
                        case 400:
                            Log.d("Response code of ", responseCode +
                                    "for request " + response.body().toString());
                            break;
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<HeatCycle> call, @NonNull Throwable t) {

            }
        });

    }

}
