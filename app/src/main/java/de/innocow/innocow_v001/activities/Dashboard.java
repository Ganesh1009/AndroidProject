package de.innocow.innocow_v001.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.text.Line;

import java.util.Timer;
import java.util.TimerTask;

import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;

public class Dashboard extends AppCompatActivity {

    private SharedPreferences preferences;
    Context context;
    private Timer logoutTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        preferences = getApplicationContext()
                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);

        setContentView(R.layout.activity_dashboard);

        ImageView cowSearchCardView = findViewById(R.id.imageView_cow_search);
        cowSearchCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCowSearch();
            }
        });

        ImageView farmMapCardView = findViewById(R.id.imageView_map);
        farmMapCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenFarmMap();
            }
        });

        TextView cowSearchTextView = findViewById(R.id.tv_kuh_suche);
        cowSearchTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doCowSearch();
            }
        });

        TextView farmMapTextVIew = findViewById(R.id.tv_farm_map);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            farmMapTextVIew.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
        }
        farmMapTextVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenFarmMap();
            }
        });


        LinearLayout logoutButton = findViewById(R.id.layout_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogoutTask();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (logoutTimer != null)
            logoutTimer.cancel();
    }

    @Override
    protected void onPause() {

        super.onPause();
        logoutTimer = new Timer();
        logoutTimer.schedule(new TimerTask() {
                                 @Override
                                 public void run() {
                                     performLogoutTask();
                                 }
                             }
                , preferences.getLong(StringConstants.TOKEN_EXP_TIME,
                        StringConstants.DEFAULT_LOGOUT_TIME)
        );
    }

    void doCowSearch() {
        startActivity(new Intent(Dashboard.this, CowSearch.class));
    }

    void doOpenFarmMap() {
        Intent intent = new Intent(Dashboard.this, FarmMap.class);
        startActivity(intent);
    }

    private void performLogoutTask(){

        preferences = getApplicationContext()
                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.remove(StringConstants.TOKEN);
        preferenceEditor.remove(StringConstants.FARM_ID);
        preferenceEditor.remove(StringConstants.TOKEN_EXP_TIME);
        preferenceEditor.apply();
        startActivity(new Intent(Dashboard.this, Login.class));

    }
}

