package de.innocow.innocow_v001.utilities.retrofit;

import java.util.Arrays;
import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitBuilder {
    private static RestClient retrofit;
//    private static final String BASE_URL = "http://dev-hrushi.dev.innocow.cloud:9000/API/v2/";
//    private static final String BASE_URL = "https://cowcloudapi.staging.innocow.de/API/v2/";
      private static final String BASE_URL = "https://cowcloudapi.staging.innocow.cloud/API/";
//    private static final String BASE_URL = "https://cowcloudapi-demo.staging.innocow.de/API/v2/";
    public synchronized static RestClient getRestClient()
    {
        if (retrofit == null)
        {
            OkHttpClient httpClient = new OkHttpClient
                    .Builder()
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(RestClient.class);
        }
        return retrofit;
    }

}
