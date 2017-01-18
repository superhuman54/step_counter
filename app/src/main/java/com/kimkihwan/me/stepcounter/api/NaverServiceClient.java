package com.kimkihwan.me.stepcounter.api;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kimkihwan.me.stepcounter.BuildConfig;
import com.kimkihwan.me.stepcounter.logger.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The singleton for the NAVER geocode API
 */

public class NaverServiceClient {

    private static final String KEY_CLIENT_ID = "com.naver.openapi.client.id";
    private static final String KEY_CLIENT_SECRET = "com.naver.openapi.client.secret";

    private static NaverServiceClient instance;

    private final GeocodeService service;

    public static NaverServiceClient getInstance(Context ctx) {
        if (instance == null) {
            instance = new NaverServiceClient(ctx);
        }
        return instance;
    }

    private NaverServiceClient(Context ctx) {
//        final Pair<String, String> auth = clientKeys(ctx);

        Gson gson = new GsonBuilder()
                .create();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json")
                                .addHeader("X-Naver-Client-Id", BuildConfig.NAVER_CLIENT_ID)
                                .addHeader("X-Naver-Client-Secret", BuildConfig.NAVER_CLIENT_SECRET)
                                .build();
                        return chain.proceed(request);
                    }
                }).build();

        Retrofit.Builder retrofit = new Retrofit.Builder()
                .baseUrl("https://openapi.naver.com")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson));

        service = retrofit.build().create(GeocodeService.class);
    }

    private Pair<String, String> clientKeys(Context ctx) {
        Pair<String, String> pair = null;
        try {
            ApplicationInfo info = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            String id = info.metaData.getString(KEY_CLIENT_ID);
            String secret = info.metaData.getString(KEY_CLIENT_SECRET);
            if (id != null && secret != null) {
                Log.d(this, "id: " + id + ", secret: " + secret);
                pair = Pair.create(id, secret);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(this, "No package name found", e);
        }
        return pair;
    }

    public GeocodeService getService() {
        return service;
    }
}
