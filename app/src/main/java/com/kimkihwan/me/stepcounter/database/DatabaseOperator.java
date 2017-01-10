package com.kimkihwan.me.stepcounter.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.kimkihwan.me.stepcounter.model.Footprint;
import com.kimkihwan.me.stepcounter.model.Snapshot;
import com.kimkihwan.me.stepcounter.model.StepCounter;
import com.kimkihwan.me.stepcounter.provider.StepCounterContract;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jamie on 10/2/16.
 */

public class DatabaseOperator {

    public static final SimpleDateFormat STARTED_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat UPDATED_DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Nullable
    public static StepCounter loadStepCounter(Context ctx, Uri uri) {
        StepCounter sc = null;
        String[] projection = new String[]{
                StepCounterContract.StepCounter.STARTED_DATE,
                StepCounterContract.StepCounter.STEPS,
                StepCounterContract.StepCounter.DISTANCE,
                StepCounterContract.StepCounter.UPDATED_DATEIME
        };
        Cursor c = ctx.getContentResolver().query(uri, projection, null, null, null);
        while (c != null && c.moveToNext()) {
            sc = new StepCounter();
            sc.setStartedDate(c.getString(c.getColumnIndex(StepCounterContract.StepCounter.STARTED_DATE)));
            sc.setSteps(c.getInt(c.getColumnIndex(StepCounterContract.StepCounter.STEPS)));
            sc.setDistance(c.getDouble(c.getColumnIndex(StepCounterContract.StepCounter.DISTANCE)));
            sc.setUpdateDatetime(c.getString(c.getColumnIndex(StepCounterContract.StepCounter.UPDATED_DATEIME)));
        }
        return sc;
    }

    @Nullable
    public static Footprint loadFootprint(Context ctx, Uri uri) {
        Footprint f = null;
        String[] projection = new String[]{
                StepCounterContract.Footprint.STARTED_DATE,
                StepCounterContract.Footprint.LATITUDE,
                StepCounterContract.Footprint.LONGITUDE,
                StepCounterContract.Footprint.ADDRESS,
                StepCounterContract.Footprint.UPDATED_DATETIME
        };

        Cursor c = ctx.getContentResolver().query(uri, projection, null, null, null);
        while (c != null && c.moveToNext()) {
            f = new Footprint();
            f.setStartedDate(c.getString(c.getColumnIndex(StepCounterContract.Footprint.STARTED_DATE)));
            f.setLatitude(c.getDouble(c.getColumnIndex(StepCounterContract.Footprint.LATITUDE)));
            f.setLongitude(c.getDouble(c.getColumnIndex(StepCounterContract.Footprint.LONGITUDE)));
            f.setAddress(c.getString(c.getColumnIndex(StepCounterContract.Footprint.ADDRESS)));
            f.setUpdatedDatetime(c.getString(c.getColumnIndex(StepCounterContract.Footprint.UPDATED_DATETIME)));
        }
        return f;
    }

    public static Snapshot takeSnapshot(Context ctx, Date date) {
        Snapshot snapshot = new Snapshot();
        StepCounter stepCounter = loadStepCounter(
                ctx,
                Uri.withAppendedPath(StepCounterContract.StepCounter.CONTENT_URI, DatabaseOperator.STARTED_DATE_FORMAT.format(date))
        );
        Footprint footprint = loadFootprint(
                ctx,
                Uri.withAppendedPath(StepCounterContract.Footprint.CONTENT_URI, DatabaseOperator.STARTED_DATE_FORMAT.format(date))
        );
        snapshot.setStepCounter(stepCounter);
        snapshot.setFootprint(footprint);
        return snapshot;
    }

    public static Uri save(Context ctx, StepCounter stepCounter) {
        ContentValues cv = new ContentValues();
        cv.put(StepCounterContract.StepCounter.STARTED_DATE, stepCounter.getStartedDate());
        cv.put(StepCounterContract.StepCounter.STEPS, stepCounter.getSteps());
        cv.put(StepCounterContract.StepCounter.DISTANCE, stepCounter.getDistance());
        cv.put(StepCounterContract.StepCounter.UPDATED_DATEIME, stepCounter.getUpdateDatetime());

        Uri uri = ctx.getContentResolver().insert(
                StepCounterContract.StepCounter.CONTENT_URI,
                cv
        );
        return uri;
    }

    public static int update(Context ctx, StepCounter stepCounter) {
        ContentValues cv = new ContentValues();
        cv.put(StepCounterContract.StepCounter.STEPS, stepCounter.getSteps());
        cv.put(StepCounterContract.StepCounter.DISTANCE, stepCounter.getDistance());
        cv.put(StepCounterContract.StepCounter.UPDATED_DATEIME, stepCounter.getUpdateDatetime());

        int count = ctx.getContentResolver()
                .update(Uri.withAppendedPath(StepCounterContract.StepCounter.CONTENT_URI, stepCounter.getStartedDate()),
                        cv,
                        null,
                        null
                );
        return count;
    }

    public static Uri save(Context ctx, Footprint footprint) {
        ContentValues cv = new ContentValues();
        cv.put(StepCounterContract.Footprint.STARTED_DATE, footprint.getStartedDate());
        cv.put(StepCounterContract.Footprint.LATITUDE, footprint.getLatitude());
        cv.put(StepCounterContract.Footprint.LONGITUDE, footprint.getLongitude());
        cv.put(StepCounterContract.Footprint.ADDRESS, footprint.getAddress());
        cv.put(StepCounterContract.Footprint.UPDATED_DATETIME, footprint.getUpdatedDatetime());

        Uri u = ctx.getContentResolver().insert(
                StepCounterContract.Footprint.CONTENT_URI,
                cv
        );
        return u;
    }

    public static int update(Context ctx, Footprint footprint) {
        ContentValues cv = new ContentValues();
        cv.put(StepCounterContract.Footprint.LATITUDE, footprint.getLatitude());
        cv.put(StepCounterContract.Footprint.LONGITUDE, footprint.getLongitude());
        cv.put(StepCounterContract.Footprint.ADDRESS, footprint.getAddress());
        cv.put(StepCounterContract.Footprint.UPDATED_DATETIME, footprint.getUpdatedDatetime());

        int count = ctx.getContentResolver()
                .update(Uri.withAppendedPath(StepCounterContract.Footprint.CONTENT_URI, footprint.getStartedDate()),
                        cv,
                        null,
                        null);
        return count;

    }
}
