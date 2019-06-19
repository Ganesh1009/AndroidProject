package de.innocow.innocow_v001.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.pojo.barn_details.BarnDimensions;
import de.innocow.innocow_v001.pojo.barn_details.CowPositionInformation;
import de.innocow.innocow_v001.pojo.barn_details.RelativeCowPosition;
import de.innocow.innocow_v001.pojo.cowdetails.Bookmarks;
import de.innocow.innocow_v001.pojo.cowsearch.BookmarksList;
import de.innocow.innocow_v001.utilities.retrofit.RestClient;
import de.innocow.innocow_v001.utilities.retrofit.RetrofitBuilder;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;
import de.innocow.innocow_v001.utilities.views.CowPositionCanvasView;
import de.innocow.innocow_v001.utilities.views.recycler_views.RecyclerViewCowPosNav;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CowPosNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BitmapLayoutProvider
        , RelativeCowPositionProvider, BarnPhysicalDimensionProvider, OnRecyclerViewCowFilter {

    private String svgInfo;
    private CowPositionInformation cowPositionInformation;
    private String token;
    private int barnId;
    private int userSelection;
    private SearchView searchView;
    private String searchText = "";
    private Timer timer;
    private List<String> selectedCowsList;
    private List<RelativeCowPosition> recyclerViewBasedCowPositionList;
    private List<BookmarksList> recyclerBookmarksList;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private CowPositionCanvasView canvasView;
    private RecyclerView cowPosRecyclerView;
    private boolean isBookmarkedListDisplayed = false;
    private boolean isRecyclerViewPopulated = false;
    private ProgressBar progressBar;
    private BarnDimensions barnDimensions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);

        token = preferences.getString(StringConstants.TOKEN, null);

        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            svgInfo = bundle.getString(StringConstants.SVG_INFO);
            barnId = bundle.getInt(StringConstants.BARN_ID);
        }

        setContentView(R.layout.activity_cow_pos_nav);

        initializeView();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CowPosNav.this, FarmMap.class));

            }
        });

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        showcaseViewAsList(navigationView);

        navigationView.setItemIconTintList(null);

        fetchBarnDimensions();

        generateBookmarkedCowList();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                progressBar.setVisibility(View.VISIBLE);
                searchText = newText;
                stopTimer();
                filterCowPositionBasedOnSearchText(searchText);
                cowPosRecyclerView.setAdapter(new RecyclerViewCowPosNav(CowPosNav.this,
                        cowPositionInformation.getCowPositionList()));
                startTimer();
                return true;
            }
        });
        startTimer();

    }

    @Override
    public SVG provideBarnLayoutSVG() throws SVGParseException {

        return SVG.getFromString(svgInfo);
    }

    @Override
    public CowPositionInformation provideCowPosition() {
        return cowPositionInformation;
    }

    @Override
    public BarnDimensions providePhysicalBarnDimensions() {
        return barnDimensions;
    }

    @Override
    public void filterOnCowSelectedFromRecyclerView(RelativeCowPosition relativeCowPosition) {

        stopTimer();
        recyclerViewBasedCowPositionList = new ArrayList<>();
        recyclerViewBasedCowPositionList.add(relativeCowPosition);
        cowPositionInformation.setCowPositionList(recyclerViewBasedCowPositionList);
        searchView.clearFocus();
        startTimer();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cow_pos_nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_reset:
                resetAllElements();
                break;
            case R.id.action_view_as_list:
                displayDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        userSelection = item.getItemId();
        isRecyclerViewPopulated = false;
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initializeView() {

        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        canvasView = findViewById(R.id.positionView);
        searchView = findViewById(R.id.searchview_cow_pos_nav);
        cowPosRecyclerView = findViewById(R.id.cowPosRecyclerView);
        cowPosRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL, false));

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        searchView.clearFocus();
        LinearLayout canvasLinearLayout = findViewById(R.id.canvasLinearLayout);
        canvasLinearLayout.requestFocus();
    }

    private void stopTimer() {
        timer.cancel();
    }

    private void startTimer() {

        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                updatedCowPositions();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        canvasView.requestLayout();
                        canvasView.invalidate();
                    }
                });
            }
        };
        timer.schedule(timerTask, 2000, 2000);
    }

    private void fetchBarnDimensions() {

        RestClient client = RetrofitBuilder.getRestClient();
        Call<BarnDimensions> getBarnPhysicalDimensions = client.getDimensions(token, barnId);

        progressBar.setVisibility(View.VISIBLE);

        getBarnPhysicalDimensions.enqueue(new Callback<BarnDimensions>() {
            @Override
            public void onResponse(@NonNull Call<BarnDimensions> call,
                                   @NonNull Response<BarnDimensions> response) {

                int responseCode = response.code();

                if (response.body() != null) {

                    switch (responseCode) {

                        case 200:
                            barnDimensions = response.body();
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
            public void onFailure(@NonNull Call<BarnDimensions> call,
                                  @NonNull Throwable t) {
                Log.d("Request failure", Arrays.toString(t.getStackTrace()));
            }
        });
    }

    private void updatedCowPositions() {

        RestClient client = RetrofitBuilder.getRestClient();

        final Call<CowPositionInformation> cowPositionInformationCall =
                client.getCowPositionInBarn(token, barnId, StringConstants.COWS_STATUS_QP);

        cowPositionInformationCall.enqueue(new Callback<CowPositionInformation>() {
            @Override
            public void onResponse(@NonNull Call<CowPositionInformation> call,
                                   @NonNull Response<CowPositionInformation> response) {
                int responseCode = response.code();

                if (response.body() != null) {

                    switch (responseCode) {

                        case 200:
                            progressBar.setVisibility(View.GONE);

                            cowPositionInformation = response.body();

                            if (recyclerViewBasedCowPositionList != null) {
                                for (RelativeCowPosition relativeCowPosition :
                                        cowPositionInformation.getCowPositionList()) {
                                    if (StringUtils.equalsIgnoreCase(
                                            recyclerViewBasedCowPositionList.get(0).getTagId(),
                                            relativeCowPosition.getTagId()))
                                        recyclerViewBasedCowPositionList.set(0, relativeCowPosition);
                                }
                                cowPositionInformation.
                                        setCowPositionList(recyclerViewBasedCowPositionList);
                            }

                            filterBaseOnNavigationSelection();

                            filterCowPositionBasedOnSearchText(searchText);

                            filterPositionBasedOnCheckBoxSelection(selectedCowsList);

                            assignColorIndicatorToCowPosition();

                            if (!isRecyclerViewPopulated &&
                                    cowPositionInformation.getCowPositionList().size() > 0) {

                                cowPosRecyclerView.setAdapter(
                                        new RecyclerViewCowPosNav(CowPosNav.this,
                                                cowPositionInformation.getCowPositionList()));
                                isRecyclerViewPopulated = true;
                            }

                            break;

                        case 400:
                            Log.d("Response code of ", responseCode +
                                    "for request " + response.body().toString());
                            break;

                        default:
                            Log.d("Request Failed", response.body().toString());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CowPositionInformation> call,
                                  @NonNull Throwable t) {
                Log.e("Request failed", t.getMessage());

            }
        });
    }

    private void assignColorIndicatorToCowPosition() {

        for (RelativeCowPosition relativeCowPosition :
                cowPositionInformation.getCowPositionList()) {

            String cowStatus = relativeCowPosition.getStatus();
            switch ((cowStatus == null) ? "" : cowStatus) {
                case "100":
                    relativeCowPosition.setColorIndicator(1f);
                    break;
                case "0":
                    relativeCowPosition.setColorIndicator(2f);
                    break;
                default:
                    relativeCowPosition.setColorIndicator(0);
            }
        }
    }

    private void generateBookmarkedCowList() {

        RestClient client = RetrofitBuilder.getRestClient();
        final Call<Bookmarks> getBookmarks = client.getBookmarks(token);
        getBookmarks.enqueue(new Callback<Bookmarks>() {
            @Override
            public void onResponse(@NonNull Call<Bookmarks> call,
                                   @NonNull Response<Bookmarks> response) {

                int responseCode = response.code();

                if (response.body() != null) {

                    switch (responseCode) {

                        case 200:
                            recyclerBookmarksList = response.body().getBookmarksList();
                            break;

                        case 400:
                            Log.d("Response code of ", responseCode +
                                    "for request " + response.body().toString());
                            break;
                        default:
                            Log.d("Request Failed", response.body().toString());
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<Bookmarks> call, @NonNull Throwable t) {
                Log.d("Request failure", Arrays.toString(t.getStackTrace()));

            }
        });
    }

    private void filterBaseOnNavigationSelection() {

        switch (userSelection) {

            case R.id.nav_brunst:
                if (cowPositionInformation != null)
                    generatePositionList(StringConstants.IN_HEAT);
                break;

            case R.id.nav_gesund:
                if (cowPositionInformation != null)
                    generatePositionList(StringConstants.NOT_IN_HEAT);
                break;

            case R.id.nav_bookmarks:
                generatePositionList(StringConstants.BOOKMARKED);
                break;

            case R.id.nav_reset:
                resetAllElements();
                break;
        }
    }

    private void filterCowPositionBasedOnSearchText(String searchSequence) {

        if (cowPositionInformation != null) {
            List<RelativeCowPosition> searchBasedCowPositionList = new ArrayList<>();
            for (RelativeCowPosition relativeCowPosition : cowPositionInformation.getCowPositionList()) {

                if (StringUtils.isNotBlank(searchSequence) &&
                        StringUtils.containsIgnoreCase(relativeCowPosition.toString(), searchSequence)) {
                    searchBasedCowPositionList.add(relativeCowPosition);
                } else if (StringUtils.isBlank(searchSequence) && userSelection != R.id.nav_bookmarks)
                    searchBasedCowPositionList.add(relativeCowPosition);
            }
            if (searchBasedCowPositionList.size() > 0)
                cowPositionInformation.setCowPositionList(searchBasedCowPositionList);

            if (searchBasedCowPositionList.size() == 1)
                cowPositionInformation.setCowPositionList(searchBasedCowPositionList);
        }
    }

    private void filterPositionBasedOnCheckBoxSelection(List<String> selectedCowsList) {

        if (selectedCowsList != null && selectedCowsList.size() > 0) {

            List<RelativeCowPosition> selectedCowInformation = new ArrayList<>();
            for (RelativeCowPosition relativeCowPosition :
                    cowPositionInformation.getCowPositionList()) {
                if (selectedCowsList.contains(relativeCowPosition.getCowId()))
                    selectedCowInformation.add(relativeCowPosition);
            }
            cowPositionInformation.setCowPositionList(selectedCowInformation);
        }
    }

    private void generatePositionList(String userChoice) {

        List<RelativeCowPosition> userChoiceBasedRelativePositionList;

        switch (userChoice) {

            case StringConstants.NOT_IN_HEAT:

                userChoiceBasedRelativePositionList = new ArrayList<>();
                for (RelativeCowPosition position : cowPositionInformation.getCowPositionList()) {

                    if (position.getStatus().equals(StringConstants.NOT_IN_HEAT))
                        userChoiceBasedRelativePositionList.add(position);
                }
                cowPositionInformation.setCowPositionList(userChoiceBasedRelativePositionList);

                TextView numGesund = findViewById(R.id.num_gesund);
                numGesund.setText(String.valueOf(userChoiceBasedRelativePositionList.size()));

                break;


            case StringConstants.IN_HEAT:

                userChoiceBasedRelativePositionList = new ArrayList<>();
                for (RelativeCowPosition position : cowPositionInformation.getCowPositionList()) {

                    if (position.getStatus().equals(StringConstants.IN_HEAT))
                        userChoiceBasedRelativePositionList.add(position);
                }
                cowPositionInformation.setCowPositionList(userChoiceBasedRelativePositionList);

                TextView numBrunst = findViewById(R.id.num_brunst);
                numBrunst.setText(String.valueOf(userChoiceBasedRelativePositionList.size()));
                break;

            case StringConstants.BOOKMARKED:

                userChoiceBasedRelativePositionList = new ArrayList<>();
                for (BookmarksList bookmarksList : recyclerBookmarksList) {

                    for (RelativeCowPosition position : cowPositionInformation.getCowPositionList()) {

                        if (position.getCowId().equals(bookmarksList.getCowId()))
                            userChoiceBasedRelativePositionList.add(position);
                    }
                }

                if (!isBookmarkedListDisplayed) {
                    cowPosRecyclerView.setAdapter(new RecyclerViewCowPosNav(CowPosNav.this,
                            userChoiceBasedRelativePositionList));
                    isBookmarkedListDisplayed = true;
                }
                cowPositionInformation.setCowPositionList(userChoiceBasedRelativePositionList);
                break;
        }
    }

    private void displayDialog() {

        List<String> cowIdList = null;

        stopTimer();
        if (cowPositionInformation != null) {
            cowIdList = new ArrayList<>();
            for (RelativeCowPosition relativeCowPosition : cowPositionInformation.getCowPositionList()) {
                cowIdList.add(relativeCowPosition.getCowId());
            }
        }

        if (cowIdList != null) {

            final String[] items = cowIdList.toArray(new String[0]);

            selectedCowsList = new ArrayList<>();

            MaterialAlertDialogBuilder dialogBuilder =
                    new MaterialAlertDialogBuilder(CowPosNav.this, R.style.CustomMaterialTheme);

            dialogBuilder.setMultiChoiceItems(items,
                    null, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            //TODO: Select multiple cows to be displayed on the SVG
                            selectedCowsList.add(items[which]);
                        }
                    }).setNegativeButton(StringConstants.CANCEL, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startTimer();
                }
            }).setPositiveButton(StringConstants.ACCEPT, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    filterPositionBasedOnCheckBoxSelection(selectedCowsList);
                    startTimer();
                    progressBar.setVisibility(View.VISIBLE);
                }

            }).create().show();
        }

    }

    private void showcaseViewAsList(View navigationView) {
        BubbleShowCaseBuilder bubbleShowCaseBuilder = new BubbleShowCaseBuilder(this);
        bubbleShowCaseBuilder.title("View Map")
                .targetView(navigationView)
                .backgroundColor(R.color.colorPrimary)
                .show();
    }

    private void resetAllElements() {

        userSelection = 0;
        isBookmarkedListDisplayed = false;
        searchText = "";
        cowPositionInformation.getCowPositionList().clear();
        recyclerViewBasedCowPositionList = null;
        if (selectedCowsList != null) {
            selectedCowsList.clear();
        }

    }
    
}
