package de.innocow.innocow_v001.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.apache.commons.lang3.StringUtils;

import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.pojo.cowdetails.DOB;
import de.innocow.innocow_v001.pojo.cowsearch.CowValues;
import de.innocow.innocow_v001.utilities.CowActivitiesPager;
import de.innocow.innocow_v001.utilities.CowDataPager;
import de.innocow.innocow_v001.utilities.retrofit.RestClient;
import de.innocow.innocow_v001.utilities.retrofit.RetrofitBuilder;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CowDetails extends AppCompatActivity {

    private String token, id;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences preferences;
        preferences = getApplicationContext()
                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);
        token = preferences.getString(StringConstants.TOKEN, null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cow_details);

        //ViewPager
        ViewPager viewPager = findViewById(R.id.view_pager);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        Bundle bundle = getIntent().getExtras();

        CowValues cowValues = null;
        if (bundle != null)
            cowValues = (CowValues) bundle.getSerializable(StringConstants.COW_VALUE);


        ImageView statusImage = findViewById(R.id.imageView2);


        if (cowValues != null) {

            TextView cowId = findViewById(R.id.id_cow_details);
            id = cowValues.getCowId();
            cowId.setText(id);

            TextView cowName = findViewById(R.id.name_cow_details);
            cowName.setText(cowValues.getName());

            TextView stallNumber = findViewById(R.id.stall_cow_details);
            stallNumber.setText(cowValues.getShedNumber().toString());

            RestClient restClient = RetrofitBuilder.getRestClient();
            Call<DOB> dobCall = restClient.getBirthdate(token, id);
            dobCall.enqueue(new Callback<DOB>() {
                @Override
                public void onResponse(@NonNull Call<DOB> call, @NonNull Response<DOB> response) {

                    if (response.body() != null) {
                        TextView cowDate = findViewById(R.id.date_cow_details);
                        cowDate.setText(response.body().getCowBirthdate().getBirthdate());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DOB> call, @NonNull Throwable t) {
                    Log.d("dob", "Fail");

                }
            });


            ImageButton dashboardButton = findViewById(R.id.dashboardButton);
            dashboardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(CowDetails.this, Dashboard.class));
                }
            });

            if (StringUtils.isBlank(cowValues.getTagId()))
                statusImage.setImageResource(R.drawable.kopf_rund);
            else if (cowValues.getHeatStatus().equals(StringConstants.IN_HEAT))
                statusImage.setImageResource(R.drawable.ic_cow_heat);
            else statusImage.setImageResource(R.drawable.ic_healthy);


        }

    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final int NUM_ITEMS = 2;

        ScreenSlidePagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new CowActivitiesPager(token, id);
                case 1:
                    return new CowDataPager(token, id);
                default:
                    return new CowActivitiesPager(token, id);
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

}
