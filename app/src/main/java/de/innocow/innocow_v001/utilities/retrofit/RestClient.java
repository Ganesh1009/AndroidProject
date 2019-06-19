package de.innocow.innocow_v001.utilities.retrofit;

import java.util.List;
import java.util.Map;

import de.innocow.innocow_v001.pojo.barn_details.BarnDimensions;
import de.innocow.innocow_v001.pojo.barn_details.SVGBarnPlan;
import de.innocow.innocow_v001.pojo.barn_details.BarnShapeDetails;
import de.innocow.innocow_v001.pojo.barn_details.BarnStatusDetails;
import de.innocow.innocow_v001.pojo.barn_details.CowPositionInformation;
import de.innocow.innocow_v001.pojo.cowdetails.Bookmarks;
import de.innocow.innocow_v001.pojo.cowdetails.CowActivity;
import de.innocow.innocow_v001.pojo.cowdetails.DOB;
import de.innocow.innocow_v001.pojo.cowdetails.HeatCycle;
import de.innocow.innocow_v001.pojo.cowsearch.CowBarnResponse;
import de.innocow.innocow_v001.pojo.cowsearch.CowSearchResponse;
import de.innocow.innocow_v001.pojo.cowsearch.CowTagResponse;
import de.innocow.innocow_v001.pojo.cowsearch.UnEquipUsedTagResponse;
import de.innocow.innocow_v001.pojo.cowsearch.UnEquippedTagResponse;
import de.innocow.innocow_v001.pojo.login.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestClient {
    @POST("v2/users/login")
    Call<LoginResponse>userLogin(@Header("content-type") String header, @Body Map credentials);

    @GET("v2/_search")
    Call <CowSearchResponse> cowSearch(@Header("Authorization") String authHeader,
                                           @Query(value = "searchKey") String searchKey,
                                            @Query(value = "farmID") Long farmID
    );

    @GET("v2/users/getBarns")
    Call<CowBarnResponse> getBarnsList(@Header("Authorization") String authHeader);

    @GET("v2/tags/searchUnequipTagsfromBarn/{farmId}/T")
    Call<UnEquippedTagResponse> getListOfUnequippedTags(@Header("Authorization") String authHeader,
                                                        @Path(value = "farmId") Long farmId);

    @GET("v2/tags/removeTagFromCow/{tagID}/{farmId}")
    Call<UnEquipUsedTagResponse> unassignTagFromCow(@Header("Authorization") String authHeader,
                                                    @Path(value = "tagID") String tagID,
                                                    @Path(value = "farmId") Long barnId);

    @GET("v2/tags/addTagToCow/{deviceName}/{farmId}/{cowID}")
    Call<CowTagResponse> assignTagToCow(@Header("Authorization") String token,
                                        @Path("deviceName") String deviceName,
                                        @Path("farmId") Long farmId,
                                        @Path("cowID") String cowID);

    @GET("v3/positions/{barnId}")
    Call<CowPositionInformation> getCowPositionInBarn(@Header("Authorization") String authHeader,
                                                      @Path(value = "barnId") int barnId,
                                                      @Query("fields") String status);

    @POST("v2/tags/getBarnStatus")
    Call<BarnStatusDetails> getBarnStatus(@Header("Authorization")String token,
                                          @Body List<Integer> barnId);

    @POST("v2/barnPlan/getShape")
    Call<BarnShapeDetails> getBarnCoordinates(@Header("Authorization")String token,
                                                   @Body List<Integer> barnId);


    @GET("v2/HBase/getPlan/{barnId}")
    Call<SVGBarnPlan> getBarnPlan(@Header("Authorization") String authHeader,
                                  @Path(value = "barnId") int barnId);

    @GET("v2/barnPlan/getBarnPlanData/{barnId}")
    Call<BarnDimensions> getDimensions(@Header("Authorization") String authHeader,
                                       @Path(value = "barnId") int barnId);

    @GET("v2/hourLabelData/getCowActivityFor24Hours/{cowId}")
    Call<CowActivity> getCowActivity(@Header("Authorization") String authHeader,
                                     @Path(value = "cowId") String cowId);

    @GET("v2/heatStatus/getHeatCycle/{cowId}")
    Call<HeatCycle> getHeatCycle(@Header("Authorization") String authHeader,
                                 @Path(value = "cowId") String cowId);
    @GET("v2/userCow/getAll")
    Call<Bookmarks> getBookmarks(@Header("Authorization") String authHeader);

    @GET("v2/masterDataCattle/findCowInfo/{cowId}")
    Call<DOB> getBirthdate(@Header("Authorization") String authHeader,
                           @Path(value = "cowId") String cowId);


}
