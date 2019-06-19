//package de.innocow.innocow_v001.activities;
//
//import android.Manifest;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.ImageButton;
//import android.widget.SearchView;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import org.apache.commons.lang3.StringUtils;
//
//import java.io.Serializable;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AlertDialog;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import de.innocow.innocow_v001.R;
//import de.innocow.innocow_v001.pojo.cowsearch.Barns;
//import de.innocow.innocow_v001.pojo.cowsearch.CowBarnResponse;
//import de.innocow.innocow_v001.pojo.cowsearch.CowSearchResponse;
//import de.innocow.innocow_v001.pojo.cowsearch.CowValues;
//import de.innocow.innocow_v001.pojo.cowsearch.UnEquipUsedTagResponse;
//import de.innocow.innocow_v001.pojo.cowsearch.UnEquippedTagResponse;
//import de.innocow.innocow_v001.utilities.views.recycler_views.RecyclerViewAdapter;
//import de.innocow.innocow_v001.utilities.retrofit.RestClient;
//import de.innocow.innocow_v001.utilities.retrofit.RetrofitBuilder;
//import de.innocow.innocow_v001.utilities.string_constants.StringConstants;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class TagManagement extends AppCompatActivity
//        implements OnDisplayAvailableTagListener, OnTagUnequipClickListener {
//
//    private List<String> barnIdList;
//    private String token;
//    private Long farmId;
//    private RecyclerView recyclerView;
//    private String searchString;
//    private SearchView searchKeyView;
//    private ImageButton barcodeScanButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tag_management);
//        initializeView();
//        barcodeScanButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkSelfPermission(Manifest.permission.CAMERA) !=
//                        PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(TagManagement.this,
//                            new String[]{Manifest.permission.CAMERA}, 1);
//                } else {
//                    startActivityForResult(new Intent(TagManagement.this, BarcodeScanner.class), 1);
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences preferences = getApplicationContext()
//                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);
//        this.token = preferences.getString(StringConstants.TOKEN, null);
//        this.farmId = preferences.getLong(StringConstants.FARM_ID, 0);
//        Intent intent = getIntent();
//        searchString = intent.getStringExtra(StringConstants.SEARCH_PHRASE);
//        searchCow(searchString);
//    }
//
//    private void initializeView() {
//        this.recyclerView = findViewById(R.id.recyclerView);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
//        this.recyclerView.setLayoutManager(mLayoutManager);
//        this.searchKeyView = findViewById(R.id.cow_search_box_tagMgm1);
//        this.barcodeScanButton = findViewById(R.id.scan_barcode_tagMgm1);
//        ImageButton dashboardButton = findViewById(R.id.dashboardButton);
//        dashboardButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(TagManagement.this, Dashboard.class));
//            }
//        });
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 1 && data != null) {
//            searchString = data.getStringExtra("searchPhrase");
//            searchCow(searchString);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.d("Request code", String.valueOf(requestCode));
//        if (requestCode == 1 && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
//            Log.d("Grant", String.valueOf(grantResults[0]));
//            startActivityForResult(new Intent(TagManagement.this, BarcodeScanner.class), 1);
//        }
//    }
//
//    @Override
//    public void onDisplayAvailableTagClick(final CowValues cowValue) {
//        final RestClient restClient = RetrofitBuilder.getRestClient();
//        Call<CowBarnResponse> cowBarns = restClient.getBarnsList(token);
//        cowBarns.enqueue(new Callback<CowBarnResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<CowBarnResponse> call,
//                                   @NonNull Response<CowBarnResponse> response) {
//                if (response.body() != null && response.body().getCowBarn() != null) {
//                    barnIdList = new ArrayList<>();
//                    for (Barns barn : response.body().getCowBarn()) {
//                        barnIdList.add(String.valueOf(barn.getBarnId()));
//                    }
//                }
//
//                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(TagManagement.this);
//                View view = View.inflate(getApplicationContext(), R.layout.spinner_barn_id, null);
//                alertBuilder.setView(view);
//
//                final Spinner spinner = view.findViewById(R.id.spinner_barn_id_alert_dialog);
//                ArrayAdapter arrayAdapter = new ArrayAdapter<>(TagManagement.this
//                        , android.R.layout.simple_spinner_item
//                        , barnIdList);
//                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner.setAdapter(arrayAdapter);
//                alertBuilder
//                        .setTitle(StringConstants.MSG_CHOOSE_BARN)
//                        .setPositiveButton(StringConstants.ACCEPT, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Call<UnEquippedTagResponse> unEquipTagResponseCall = restClient
//                                        .getListOfUnequippedTags(token, Integer
//                                                .valueOf(spinner.getSelectedItem().toString()));
//
//                                unEquipTagResponseCall.enqueue(new Callback<UnEquippedTagResponse>() {
//                                    @Override
//                                    public void onResponse(@NonNull Call<UnEquippedTagResponse> call,
//                                                           @NonNull Response<UnEquippedTagResponse> response) {
//
//                                        if (response.body() != null) {
//                                            Intent intent = new Intent(TagManagement.this
//                                                    , TagWaehlen.class);
//                                            intent.putExtra(StringConstants.COW_NAME, cowValue.getName());
//                                            intent.putExtra(StringConstants.COW_ID, cowValue.getCowId());
//                                            intent.putExtra(StringConstants.BARN_ID, spinner.getSelectedItem()
//                                                    .toString());
//                                            intent.putExtra(StringConstants.UNEQUIPPED_TAG_LIST
//                                                    , (Serializable) response.body()
//                                                            .getUnEquippedTagList());
//                                            intent.putExtra(StringConstants.TOKEN, token);
//                                            intent.putExtra(StringConstants.FARM_ID, farmId);
//                                            startActivityForResult(intent,1);
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void onFailure(@NonNull Call<UnEquippedTagResponse> call,
//                                                          @NonNull Throwable t) {
//
//                                    }
//                                });
//                            }
//                        })
//                        .setNegativeButton(StringConstants.CANCEL, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        }).create().show();
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<CowBarnResponse> call, @NonNull Throwable t) {
//
//            }
//        });
//    }
//
//    @Override
//    public void onTagUnequipClick(CowValues cowValue) {
//        RestClient client = RetrofitBuilder.getRestClient();
//        Call<UnEquipUsedTagResponse> unEquipUsedTagResponseCall = client
//                .unassignTagFromCow(token, cowValue.getTagId(), cowValue.getFarmId());
//
//        unEquipUsedTagResponseCall.enqueue(new Callback<UnEquipUsedTagResponse>() {
//            @Override
//            public void onResponse(@NonNull Call<UnEquipUsedTagResponse> call
//                    , @NonNull Response<UnEquipUsedTagResponse> response) {
//
//                if (Objects.nonNull(response) && response.body() != null) {
//
//                    int responseCode = response.code();
//                    switch (responseCode) {
//                        case 200:
//                            Log.d("Request successful", response.body().toString());
//                            Toast.makeText(getApplicationContext()
//                                    , response.body().getValue()
//                                    , Toast.LENGTH_LONG).show();
//
//                            RestClient restClient = RetrofitBuilder.getRestClient();
//                            final SearchView searchKeyView = findViewById(R.id.cow_search_box_tagMgm1);
//                            String searchKey = String.valueOf(searchKeyView.getQuery());
//                            Call<CowSearchResponse> cowSearch = restClient.cowSearch(token, searchKey, farmId);
//                            cowSearch.enqueue(new Callback<CowSearchResponse>() {
//                                @Override
//                                public void onResponse(@NonNull Call<CowSearchResponse> call,
//                                                       @NonNull Response<CowSearchResponse> response) {
//                                    if (response.body() != null && response.body()
//                                            .getCowValueList() != null) {
//                                        List<CowValues> cowValueResponseList = response.body()
//                                                .getCowValueList();
//                                        recyclerView.setAdapter(new RecyclerViewAdapter
//                                                (TagManagement.this, cowValueResponseList, searchString));
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(@NonNull Call<CowSearchResponse> call,
//                                                      @NonNull Throwable t) {
//
//                                }
//                            });
//                            break;
//                        case 500:
//                        case 503:
//                            Toast.makeText(getApplicationContext()
//                                    , response.body().getValue()
//                                    , Toast.LENGTH_LONG).show();
//                            break;
//                        default:
//                            Toast.makeText(getApplicationContext()
//                                    , "Tag unequip operation failed"
//                                    , Toast.LENGTH_LONG).show();
//                            Log.d("Tag unequip operation failed with response code {} {}" +
//                                    String.valueOf(responseCode), response.toString());
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<UnEquipUsedTagResponse> call, @NonNull Throwable t) {
//                Log.d("Unequip Request failed", t.getMessage());
//            }
//        });
//    }
//
//    private void searchCow(String searchPhrase) {
//
//        if (StringUtils.isNotBlank(searchPhrase)) {
//            searchKeyView.setQuery(searchPhrase, true);
//        }
//        searchKeyView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String searchKey) throws NullPointerException {
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(final String newText) {
//                searchString = newText;
//                RestClient restClient = RetrofitBuilder.getRestClient();
//                Call<CowSearchResponse> call = restClient.cowSearch(token, newText, farmId);
//
//                call.enqueue(new Callback<CowSearchResponse>() {
//
//                    @Override
//                    public void onResponse(@NonNull Call<CowSearchResponse> call,
//                                           @NonNull Response<CowSearchResponse> response) {
//                        if (response.body() != null && response.body()
//                                .getCowValueList() != null) {
//                            List<CowValues> cowValueResponseList = response.body()
//                                    .getCowValueList();
//                            recyclerView.setAdapter(new RecyclerViewAdapter
//                                    (TagManagement.this, cowValueResponseList, searchString));
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(@NonNull Call<CowSearchResponse> call,
//                                          @NonNull Throwable t) {
//
//                    }
//                });
//                return false;
//            }
//        });
//    }
//}
