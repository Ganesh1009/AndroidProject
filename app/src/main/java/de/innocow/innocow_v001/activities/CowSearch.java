package de.innocow.innocow_v001.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.pojo.cowsearch.CowSearchResponse;
import de.innocow.innocow_v001.pojo.cowsearch.CowValues;
import de.innocow.innocow_v001.pojo.cowsearch.UnEquipUsedTagResponse;
import de.innocow.innocow_v001.pojo.cowsearch.UnEquippedTagResponse;
import de.innocow.innocow_v001.utilities.retrofit.RestClient;
import de.innocow.innocow_v001.utilities.retrofit.RetrofitBuilder;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;
import de.innocow.innocow_v001.utilities.views.recycler_views.RecyclerViewAdapter;
import de.innocow.innocow_v001.utilities.views.recycler_views.RecyclerViewCowPosNav;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CowSearch extends AppCompatActivity implements OnDisplayAvailableTagListener
        , OnTagUnequipClickListener, OnDisplayCowDetailsListener {

    private SharedPreferences preferences;
    private String token;
    private long farmId;
    private RecyclerView recyclerView;
    private String searchString;
    private SearchView searchKeyView;
    private ImageButton barcodeScanButton;
    private LinearLayout logoutButton;
    private Timer logoutTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cow_search);
        preferences = getApplicationContext()
                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);
        this.token = preferences.getString(StringConstants.TOKEN, null);
        this.farmId = preferences.getLong(StringConstants.FARM_ID, 0);
        initializeView();


        barcodeScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkSelfPermission(Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CowSearch.this,
                            new String[]{Manifest.permission.CAMERA}, 1);
                } else {
                    startActivityForResult(new Intent(CowSearch.this, BarcodeScanner.class), 1);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogoutTask();
            }
        });

    }

    private void searchCow(String searchPhrase) {

        if (StringUtils.isNotBlank(searchPhrase)) {
            searchKeyView.setQuery(searchPhrase, true);
        }
        searchKeyView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String searchKey) throws NullPointerException {
                        searchKeyView.clearFocus();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(final String newText) {
                        searchString = newText;
                        RestClient restClient = RetrofitBuilder.getRestClient();
                        Call<CowSearchResponse> call = restClient.cowSearch(token, newText, farmId);

                        call.enqueue(new Callback<CowSearchResponse>() {

                            @Override
                            public void onResponse(@NonNull Call<CowSearchResponse> call,
                                                   @NonNull Response<CowSearchResponse> response) {
                                if (response.body() != null && response.body()
                                        .getCowValueList() != null) {

                                    List<CowValues> cowValueResponseList = response.body()
                                            .getCowValueList();
                                    Log.d("Cow Search response", cowValueResponseList.toString());
                                    recyclerView.setAdapter(new RecyclerViewAdapter
                                            (CowSearch.this, cowValueResponseList, searchString));
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<CowSearchResponse> call,
                                                  @NonNull Throwable t) {
                                Log.e("Request failure", Arrays.toString(t.getStackTrace()));
                            }
                        });
                        return false;
                    }
                });
    }

    private void initializeView() {
        this.recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(new RecyclerViewAdapter(this,null, null));
        this.logoutButton = findViewById(R.id.layout_logout);
        this.recyclerView.setLayoutManager(mLayoutManager);
        this.searchKeyView = findViewById(R.id.cow_search_box);
        this.barcodeScanButton = findViewById(R.id.scan_barcode);
        ImageButton dashboardButton = findViewById(R.id.dashboardButton);
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CowSearch.this, Dashboard.class));
            }
        });
        barcodeShowCase(barcodeScanButton);
    }

    @Override
    protected void onResume() {
        super.onResume();
        preferences = getApplicationContext()
                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);
        this.token = preferences.getString(StringConstants.TOKEN, null);
        this.farmId = preferences.getLong(StringConstants.FARM_ID, 0);
        Intent intent = getIntent();
        searchString = intent.getStringExtra(StringConstants.SEARCH_PHRASE);
        searchCow(searchString);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && data != null) {
            searchString = data.getStringExtra("searchPhrase");
            searchCow(searchString);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("Request code", String.valueOf(requestCode));
        if (requestCode == 1 && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
            Log.d("Grant", String.valueOf(grantResults[0]));
            startActivityForResult(new Intent(CowSearch.this, BarcodeScanner.class), 1);
        }
    }

    @Override
    public void onDisplayAvailableTagClick(final CowValues cowValue) {
        final RestClient restClient = RetrofitBuilder.getRestClient();

        Call<UnEquippedTagResponse> unEquipTagResponseCall = restClient
                .getListOfUnequippedTags(token, farmId);

        unEquipTagResponseCall.enqueue(new Callback<UnEquippedTagResponse>() {
            @Override
            public void onResponse(@NonNull Call<UnEquippedTagResponse> call,
                                   @NonNull Response<UnEquippedTagResponse> response) {

                if (response.body() != null) {
                    Intent intent = new Intent(CowSearch.this
                            , TagWaehlen.class);
                    intent.putExtra(StringConstants.COW_NAME, cowValue.getName());
                    intent.putExtra(StringConstants.COW_ID, cowValue.getCowId());
                    intent.putExtra(StringConstants.UNEQUIPPED_TAG_LIST
                            , (Serializable) response.body()
                                    .getUnEquippedTagList());
                    intent.putExtra(StringConstants.TOKEN, token);
                    intent.putExtra("shedNumber", String.valueOf(cowValue.getShedNumber()));
                    Log.d("stall_num_cowsearch", String.valueOf(cowValue.getShedNumber()));

                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UnEquippedTagResponse> call,
                                  @NonNull Throwable t) {

            }
        });

    }

    @Override
    public void onTagUnequipClick(final CowValues cowValue) {
        RestClient client = RetrofitBuilder.getRestClient();
        Call<UnEquipUsedTagResponse> unEquipUsedTagResponseCall = client
                .unassignTagFromCow(token, cowValue.getTagId(), cowValue.getFarmId());

        unEquipUsedTagResponseCall.enqueue(new Callback<UnEquipUsedTagResponse>() {
            @Override
            public void onResponse(@NonNull Call<UnEquipUsedTagResponse> call
                    , @NonNull Response<UnEquipUsedTagResponse> response) {

                if (Objects.nonNull(response) && response.body() != null) {

                    int responseCode = response.code();
                    switch (responseCode) {
                        case 200:
                            Log.d("Request successful", response.body().toString());
                            Toast.makeText(getApplicationContext()
                                    , response.body().getValue()
                                    , Toast.LENGTH_LONG).show();

                            showSuccessAlertDialog(cowValue.getTagId());

                            RestClient restClient = RetrofitBuilder.getRestClient();
                            final SearchView searchKeyView = findViewById(R.id.cow_search_box);
                            String searchKey = String.valueOf(searchKeyView.getQuery());
                            Call<CowSearchResponse> cowSearch = restClient.cowSearch(token, searchKey, farmId);
                            cowSearch.enqueue(new Callback<CowSearchResponse>() {
                                @Override
                                public void onResponse(@NonNull Call<CowSearchResponse> call,
                                                       @NonNull Response<CowSearchResponse> response) {
                                    if (response.body() != null && response.body()
                                            .getCowValueList() != null) {
                                        List<CowValues> cowValueResponseList = response.body()
                                                .getCowValueList();
                                        recyclerView.setAdapter(new RecyclerViewAdapter
                                                (CowSearch.this, cowValueResponseList, searchString));
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<CowSearchResponse> call,
                                                      @NonNull Throwable t) {

                                }
                            });
                            break;
                        case 500:
                        case 503:
                            Toast.makeText(getApplicationContext()
                                    , response.body().getValue()
                                    , Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(getApplicationContext()
                                    , "Tag unequip operation failed"
                                    , Toast.LENGTH_LONG).show();
                            //Log.d("op failed {} {}" + responseCode, response.toString());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UnEquipUsedTagResponse> call, @NonNull Throwable t) {
                Log.d("Unequip Request failed", t.getMessage());
            }
        });
    }

    private void showSuccessAlertDialog(String tagId) {
        Dialog dialog = new Dialog(CowSearch.this);
        dialog.setContentView(R.layout.success_dialog);
//            ImageView checkSign = (ImageView) dialog.findViewById(R.id.check_sign);
        TextView alertMessage = dialog.findViewById(R.id.alertMessage);
        alertMessage.setText(String.format("Sensor %s wurde erfolgreich\n von dieser Kuh entfernt.", tagId));
        dialog.show();
    }

    @Override
    public void onDisplayCowDetailsClickListener(CowValues cowValues) {

        Intent intent = new Intent(CowSearch.this, CowDetails.class);
        intent.putExtra(StringConstants.COW_VALUE, cowValues);
        startActivity(intent);

    }

    private void barcodeShowCase(ImageButton barcodeScanButton) {

        BubbleShowCaseBuilder bubbleShowCaseBuilder = new BubbleShowCaseBuilder(this);
        bubbleShowCaseBuilder
                .title("Barcode")
                .description("Click to use Barcode scanner")
                .backgroundColorResourceId(R.color.colorPrimary)
                .targetView(barcodeScanButton)
                .showOnce("BUBBLE_SHOW_CASE_SEARCH_ID")
                .show();
    }

    private void performLogoutTask(){

        preferences = getApplicationContext()
                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.remove(StringConstants.TOKEN);
        preferenceEditor.remove(StringConstants.FARM_ID);
        preferenceEditor.remove(StringConstants.TOKEN_EXP_TIME);
        preferenceEditor.apply();
        startActivity(new Intent(CowSearch.this, Login.class));

    }
}
