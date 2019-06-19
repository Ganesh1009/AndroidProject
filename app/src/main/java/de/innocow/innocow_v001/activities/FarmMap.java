package de.innocow.innocow_v001.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.pojo.barn_details.BarnDimensions;
import de.innocow.innocow_v001.pojo.barn_details.BarnShapeDetails;
import de.innocow.innocow_v001.pojo.barn_details.BarnStatus;
import de.innocow.innocow_v001.pojo.barn_details.BarnStatusDetails;
import de.innocow.innocow_v001.pojo.barn_details.SVGBarnPlan;
import de.innocow.innocow_v001.pojo.barn_details.Shapes;
import de.innocow.innocow_v001.pojo.cowsearch.Barns;
import de.innocow.innocow_v001.pojo.cowsearch.CowBarnResponse;
import de.innocow.innocow_v001.utilities.retrofit.RestClient;
import de.innocow.innocow_v001.utilities.retrofit.RetrofitBuilder;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FarmMap extends AppCompatActivity implements GoogleMap.OnCameraMoveListener,
        GoogleMap.OnInfoWindowClickListener, GoogleMap.InfoWindowAdapter {

    private static final float DEFAULT_CAMERA_ZOOM_LEVEL = 17.6f;
    private static final float INFO_WINDOW_VISIBLE_ZOOM_LEVEL = 15.4f;
    private String token;
    private RestClient client;
    private List<Integer> barnIdList;
    private GoogleMap map;
    private SharedPreferences preferences;
    private double[] polygonCentreCoordinates;
    private boolean isMarkerDisplayed = false;
    private Map<Integer, Marker> barnIdMarkerMap;
    private List<BarnStatus> barnStatusList;
    private List<Marker> markerList;
    private Timer logoutTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getApplicationContext()
                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);
        this.token = preferences.getString(StringConstants.TOKEN, null);
        setContentView(R.layout.activity_farm_map);

        LinearLayout logoutButton = findViewById(R.id.layout_logout);
        //ImageView logout = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                performLogoutTask();

            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        initializeAndRenderContentOnMap();
        ImageButton dashboardButton = findViewById(R.id.dashboardButton);
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FarmMap.this, Dashboard.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        preferences = getApplicationContext()
                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);
        this.token = preferences.getString(StringConstants.TOKEN, null);

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
    public void onCameraMove() {

        CameraPosition cameraPosition = map.getCameraPosition();

        if (cameraPosition.zoom >= INFO_WINDOW_VISIBLE_ZOOM_LEVEL && !isMarkerDisplayed) {
            for (Marker marker : markerList) {
                marker.showInfoWindow();
            }
            isMarkerDisplayed = true;
        } else if (cameraPosition.zoom < INFO_WINDOW_VISIBLE_ZOOM_LEVEL) {
            for (int key : barnIdMarkerMap.keySet()) {
                Objects.requireNonNull(barnIdMarkerMap.get(key)).hideInfoWindow();
            }
            isMarkerDisplayed = false;
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getInfoContents(Marker marker) {
        View mapView = FarmMap.this.getLayoutInflater()
                .inflate(R.layout.info_window, null);

        if (Objects.nonNull(barnIdMarkerMap.keySet())) {
            for (int barnId : barnIdMarkerMap.keySet()) {
                if (compareMarkerPosition(Objects
                        .requireNonNull(barnIdMarkerMap.get(barnId)), marker)) {

                    if (barnIdList!= null && barnIdList.size() > 0) {

                        for (BarnStatus status : barnStatusList) {

                            if (status.getBarnId() == barnId) {

                                TextView preCowHeatText = mapView.findViewById(R.id.pre_heat_text);
                                TextView cowHeatText = mapView.findViewById(R.id.cow_heat_text);
                                TextView postCowHeatText = mapView.findViewById(R.id.post_heat_text);
                                TextView cowHealthyText = mapView.findViewById(R.id.cow_healthy_text);
                                TextView cowSickText = mapView.findViewById(R.id.cow_sick_text);

                                preCowHeatText.setText(status.getBeforeHeat().toString());
                                cowHeatText.setText(status.getCowsInHeat().toString());
                                postCowHeatText.setText(status.getAfterHeat().toString());
                                cowHealthyText.setText(status.getHealthyCowCount().toString());
                                cowSickText.setText(status.getSick().toString());
                            }
                        }
                    }
                }
            }
        }
        return mapView;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        for (final int key : barnIdMarkerMap.keySet()) {

            if (compareMarkerPosition(marker, Objects.requireNonNull(barnIdMarkerMap.get(key)))) {
                client = RetrofitBuilder.getRestClient();


                Call<SVGBarnPlan> getBarnPlan = client.getBarnPlan(token, key);
                getBarnPlan.enqueue(new Callback<SVGBarnPlan>() {
                    @Override
                    public void onResponse(@NonNull Call<SVGBarnPlan> call,
                                           @NonNull Response<SVGBarnPlan> response) {
                        int responseCode = response.code();

                        switch (responseCode) {
                            case 200:
                                if (response.body() != null) {

                                    String svgInfo = response.body().getSvgInformation();

                                    Intent intent = new Intent(FarmMap.this,
                                            CowPosNav.class);
                                    intent.putExtra(StringConstants.BARN_ID, key);
                                    intent.putExtra(StringConstants.SVG_INFO, svgInfo);
                                    for (BarnStatus status : barnStatusList)
                                        if (status.getBarnId() == key)
                                            intent.putExtra(StringConstants.BARN_STATUS, status);

                                    startActivity(intent);
                                }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<SVGBarnPlan> call,
                                          @NonNull Throwable t) {

                    }
                });
            }
        }
    }

    private void initializeAndRenderContentOnMap() {
        Log.d("Map Initialization", "successful");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                renderMapContents();
                map.setOnCameraMoveListener(FarmMap.this);
                map.setOnInfoWindowClickListener(FarmMap.this);
                map.setInfoWindowAdapter(FarmMap.this);

            }
        });
    }

    private void renderMapContents() {

        client = RetrofitBuilder.getRestClient();
        Call<CowBarnResponse> getBarns = client.getBarnsList(token);
        getBarns.enqueue(new Callback<CowBarnResponse>() {
            @Override
            public void onResponse(@NonNull Call<CowBarnResponse> call,
                                   @NonNull Response<CowBarnResponse> response) {

                int responseCode = response.code();
                switch (responseCode) {
                    case 200:
                        if (Objects.nonNull
                                (Objects.requireNonNull(response.body()).getCowBarn()) &&
                                response.body().getCowBarn().size() > 0) {

                            barnIdList = new ArrayList<>();
                            for (Barns barns : response.body().getCowBarn())
                                barnIdList.add(barns.getBarnId());

                            Call<BarnShapeDetails> barnShapeDetailsCall =
                                    client.getBarnCoordinates(token, barnIdList);
                            barnShapeDetailsCall.enqueue(new Callback<BarnShapeDetails>() {
                                @Override
                                public void onResponse(@NonNull Call<BarnShapeDetails> call,
                                                       @NonNull Response<BarnShapeDetails> response) {

                                    int responseCode = response.code();
                                    switch (responseCode) {
                                        case 200:
                                            Log.d("Shape of barn",
                                                    (response.body() != null) ?
                                                            response.body().toString() : null);
                                            List<LatLng> coordinateList;
                                            if (response.body() != null) {

                                                List<Shapes> geoLocs = response.body()
                                                        .getBarnShapeList();

                                                barnIdMarkerMap = new HashMap<>();
                                                for (Shapes shapes : geoLocs) {
                                                    coordinateList = computeCoordinates(shapes);
                                                    plotPolygonOnMap(coordinateList);
                                                    LatLng polygonCentre =
                                                            new LatLng(polygonCentreCoordinates[0],
                                                                    polygonCentreCoordinates[1]);
                                                    markerList = new ArrayList<>();
                                                    Marker computedMaker = map.
                                                            addMarker(new MarkerOptions()
                                                                    .position(polygonCentre));
                                                    computedMaker.setAlpha(0);
                                                    markerList.add(computedMaker);
                                                    barnIdMarkerMap.put(shapes.getBarnId(), computedMaker);
                                                }
                                            }
                                            break;
                                        default:
                                            Toast.makeText(getApplicationContext()
                                                    , "Failed to fetch barn coordinates"
                                                    , Toast.LENGTH_LONG).show();
                                            Log.d("Failed to fetch barn coordinates with response code" +
                                                    String.valueOf(responseCode), response.toString());
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<BarnShapeDetails> call,
                                                      @NonNull Throwable t) {
                                    Log.e("Error" + call.request().toString(), t.getMessage());
                                }
                            });

                            Call<BarnStatusDetails> barnStatusCall = client
                                    .getBarnStatus(token, barnIdList);

                            barnStatusCall.enqueue(new Callback<BarnStatusDetails>() {
                                @Override
                                public void onResponse(@NonNull Call<BarnStatusDetails> call,
                                                       @NonNull Response<BarnStatusDetails> response) {
                                    int responseCode = response.code();

                                    switch (responseCode) {
                                        case 200:
                                            if (response.body() != null) {
                                                Log.d("Cow status in barn", response
                                                        .body().toString());
                                                barnStatusList = new ArrayList<>();
                                                barnStatusList.addAll(response.body().getBarnStatusList());
                                            }
                                            break;
                                        default:
                                            Toast.makeText(getApplicationContext()
                                                    , "Failed to fetch barn status"
                                                    , Toast.LENGTH_LONG).show();
                                            Log.d("Failed to fetch barn status with response code " +
                                                    String.valueOf(responseCode), response.toString());
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<BarnStatusDetails> call,
                                                      @NonNull Throwable t) {
                                    Log.e("Error" + call.request().toString(), t.getMessage());
                                }
                            });
                        }
                        break;
                    default:
                        Log.d("Request to get the barns failed. Response code",
                                String.valueOf(responseCode));
                }

            }

            @Override
            public void onFailure(@NonNull Call<CowBarnResponse> call,
                                  @NonNull Throwable t) {

            }
        });

    }

    private List<LatLng> computeCoordinates(Shapes shapes) {

        List<LatLng> coordinateList = new ArrayList<>();
        double latSum = 0.0f;
        double lngSum = 0.0f;
        polygonCentreCoordinates = new double[2];
        for (List<String> latLng : shapes.getCoordinates()
                .get(0)) {
            double lat = Double.valueOf(latLng.get(1));
            double lng = Double.valueOf(latLng.get(0));
            latSum += lat;
            lngSum += lng;
            coordinateList.add(new LatLng(lat, lng));
        }
        polygonCentreCoordinates[0] = latSum / (double) coordinateList.size();
        polygonCentreCoordinates[1] = lngSum / (double) coordinateList.size();
        return coordinateList;
    }

    private void plotPolygonOnMap(List<LatLng> coordinateList) {
        Polygon polygon = map.addPolygon(new PolygonOptions()
                .addAll(coordinateList)
                .visible(true)
                .fillColor(Color.GREEN)
        );
        polygon.setClickable(Boolean.TRUE);
        map.animateCamera(CameraUpdateFactory
                .newLatLngZoom(coordinateList.get(0),
                        DEFAULT_CAMERA_ZOOM_LEVEL));
    }

    private boolean compareMarkerPosition(Marker m1, Marker m2) {

        return ((m1.getPosition().longitude == m2.getPosition().longitude) &&
                (m1.getPosition().latitude == m2.getPosition().latitude)) ? Boolean.TRUE : Boolean.FALSE;
    }

    private void performLogoutTask(){

        preferences = getApplicationContext()
                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor preferenceEditor = preferences.edit();
        preferenceEditor.remove(StringConstants.TOKEN);
        preferenceEditor.remove(StringConstants.FARM_ID);
        preferenceEditor.remove(StringConstants.TOKEN_EXP_TIME);
        preferenceEditor.apply();
        startActivity(new Intent(FarmMap.this, Login.class));

    }
}
