package com.kimkihwan.me.stepcounter.service;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.kimkihwan.me.stepcounter.api.NaverServiceClient;
import com.kimkihwan.me.stepcounter.database.DatabaseOperator;
import com.kimkihwan.me.stepcounter.logger.Log;
import com.kimkihwan.me.stepcounter.model.Footprint;
import com.kimkihwan.me.stepcounter.provider.StepCounterContract;

import java.io.IOException;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by jamie on 10/2/16.
 */

public class LocationService extends Service
        implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    private GoogleApiClient client;
    private LocationRequest request;

    private HandlerThread locationHandlerThread = new LocationHandler();
    private Call<GeocodeResponse> call;

    private static final long POLLING_FREQ = 1000 * 60 * 5;
    private static final long FASTEST_UPDATE_FREQ = 1000 * 60 * 1;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(this, "onCreate()");
        createGoogleClient();
        locationHandlerThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(this, "onStartCommand()");
        if (!client.isConnected())
            client.connect();
        return START_NOT_STICKY;
    }

    private void createGoogleClient() {
        if (client == null) {
            client = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    private void createRequest() {
        request = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(POLLING_FREQ)
                .setFastestInterval(FASTEST_UPDATE_FREQ);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(this, "onDestroy()");
        stopLocationUpdates();
        if (client.isConnected()) {
            client.disconnect();
        }
        locationHandlerThread.quit();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(this, "Google client is connected");
        createRequest();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (client != null) {
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(client, request, this, locationHandlerThread.getLooper());
        }
    }

    protected void stopLocationUpdates() {
        if (client.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    client, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location l) {
        if (l != null) {
            Date located = new Date(l.getTime());
            call = NaverServiceClient
                    .getInstance(this)
                    .getService()
                    .geocode(latlng(l.getLatitude(), l.getLongitude()));
            try {

                // synchronous web call
                Response<GeocodeResponse> result = call.execute();

                if (!result.isSuccessful()) {
                    okhttp3.Response raw = result.raw();
                    Log.e(this, "error message: " + raw.message());
                }
                GeocodeResponse response = result.body();

                if (response != null) {
                    GeocodeResponse.Result.Address geocoded = null;

                    for (GeocodeResponse.Result.Address each : response.result.items) {
                        geocoded = each;
                        break;
                    }
                    Footprint recorded = DatabaseOperator.loadFootprint(
                            this,
                            Uri.withAppendedPath(StepCounterContract.Footprint.CONTENT_URI, DatabaseOperator.STARTED_DATE_FORMAT.format(located)));

                    if (geocoded != null) {
                        if (recorded == null) {
                            Footprint footprint = new Footprint()
                                    .setStartedDate(DatabaseOperator.STARTED_DATE_FORMAT.format(located))
                                    .setLatitude(geocoded.point.y)
                                    .setLongitude(geocoded.point.x)
                                    .setAddress(geocoded.address)
                                    .setUpdatedDatetime(DatabaseOperator.UPDATED_DATETIME_FORMAT.format(located));
                            DatabaseOperator.save(this, footprint);
                        } else {
                            recorded.setLatitude(geocoded.point.y)
                                    .setLongitude(geocoded.point.x)
                                    .setAddress(geocoded.address)
                                    .setUpdatedDatetime(DatabaseOperator.UPDATED_DATETIME_FORMAT.format(located));
                            DatabaseOperator.update(this, recorded);
                        }
                    }
                }
            } catch (IOException e) {
                Log.e(this, "Could not call", e);
            }
        } else {
            Log.wtf(this, "No location detected");
        }
    }

    private String latlng(double lat, double lng) {
        return String.valueOf(lng) + "," + String.valueOf(lat);
    }

    private class LocationHandler extends HandlerThread {

        public LocationHandler() {
            super("LocationHandler", Process.THREAD_PRIORITY_BACKGROUND);
        }
    }

}
