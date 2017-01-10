package com.kimkihwan.me.stepcounter.api;

import com.kimkihwan.me.stepcounter.service.GeocodeResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jamie on 10/3/16.
 */

public interface GeocodeService {

    @GET("/v1/map/reversegeocode")
    Call<GeocodeResponse> geocode(@Query("query") String latlng);
}
