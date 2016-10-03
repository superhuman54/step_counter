package com.navercorp.recruit.kimkihwan.stepcounter.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.navercorp.recruit.kimkihwan.stepcounter.database.DatabaseHelper;
import com.navercorp.recruit.kimkihwan.stepcounter.logger.Log;

import java.util.Arrays;

/**
 * Created by jamie on 10/1/16.
 */

public class StepCounterProvider extends ContentProvider {

    private static final int CODE_ALL_STEPCOUNTERS = 1000;
    private static final int CODE_STEPCOUNTER_ID = 1001;

    private static final int CODE_ALL_FOOTPRINTS = 2000;
    private static final int CODE_FOOTPRINT_ID = 2001;

    private static final SparseArray<String> URI_CODE_TABLE_MAP =
            new SparseArray<>();

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_CODE_TABLE_MAP.put(CODE_ALL_STEPCOUNTERS,
                DatabaseHelper.Tables.STEPCOUNTER);

        URI_CODE_TABLE_MAP.put(CODE_STEPCOUNTER_ID,
                DatabaseHelper.Tables.STEPCOUNTER);

        URI_CODE_TABLE_MAP.put(CODE_ALL_FOOTPRINTS,
                DatabaseHelper.Tables.FOOTPRINT);

        URI_CODE_TABLE_MAP.put(CODE_FOOTPRINT_ID,
                DatabaseHelper.Tables.FOOTPRINT);

        URI_MATCHER.addURI(StepCounterContract.AUTHORITY,
                StepCounterContract.StepCounter.PATH,
                CODE_ALL_STEPCOUNTERS);

        URI_MATCHER.addURI(StepCounterContract.AUTHORITY,
                StepCounterContract.StepCounter.PATH + "/*",
                CODE_STEPCOUNTER_ID);

        URI_MATCHER.addURI(StepCounterContract.AUTHORITY,
                StepCounterContract.Footprint.PATH,
                CODE_ALL_FOOTPRINTS);

        URI_MATCHER.addURI(StepCounterContract.AUTHORITY,
                StepCounterContract.Footprint.PATH + "/*",
                CODE_FOOTPRINT_ID);
    }

    private DatabaseHelper helper;

    @Override
    public boolean onCreate() {
        Log.d(this, "StepCounterProvider onCreate()");
        helper = DatabaseHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        if (projection == null)
            throw new IllegalArgumentException("Projection must not be null");

        sortOrder = sortOrder != null ? sortOrder : StepCounterContract.ID.STARTED_DATE;

        SQLiteDatabase db = helper.getReadableDatabase();

        int code = URI_MATCHER.match(uri);
        switch (code) {
            case CODE_ALL_STEPCOUNTERS:
            case CODE_ALL_FOOTPRINTS:
                cursor = db.query(URI_CODE_TABLE_MAP.get(code),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case CODE_STEPCOUNTER_ID:
            case CODE_FOOTPRINT_ID:
                if (selection == null && selectionArgs == null) {
                    selection = StepCounterContract.ID.STARTED_DATE + " = ?";
                    selectionArgs = new String[]{uri.getLastPathSegment()};
                } else {
                    throw new IllegalArgumentException("Selection must be null");
                }
                cursor = db.query(URI_CODE_TABLE_MAP.get(code),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id;
        Log.d(this, "Insert with uri: " + uri.toString());
        int code = URI_MATCHER.match(uri);

        switch (code) {
            case CODE_ALL_STEPCOUNTERS:
            case CODE_ALL_FOOTPRINTS:
                id = helper
                        .getWritableDatabase()
                        .insert(URI_CODE_TABLE_MAP.get(code), null, values);
                break;
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        Log.d(this, "Id: " + id);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        int code = URI_MATCHER.match(uri);
        Log.d(this, "Update with uri: " + uri.toString());
        switch (code) {
            case CODE_ALL_STEPCOUNTERS:
            case CODE_ALL_FOOTPRINTS:
                count = helper.getWritableDatabase()
                        .update(URI_CODE_TABLE_MAP.get(code),
                                values,
                                selection,
                                selectionArgs);
                break;
            case CODE_STEPCOUNTER_ID:
            case CODE_FOOTPRINT_ID:
                if (selection == null && selectionArgs == null) {
                    selection = StepCounterContract.ID.STARTED_DATE + " = ?";
                    selectionArgs = new String[]{uri.getLastPathSegment()};
                    Log.d(this, "Selection: " + selection + ", SelectionArgs: " + Arrays.toString(selectionArgs));
                } else {
                    throw new IllegalArgumentException("Selection must be null");
                }

                count = helper.getWritableDatabase()
                        .update(URI_CODE_TABLE_MAP.get(code),
                                values,
                                selection,
                                selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        Log.d(this, "Count: " + count);
        return count;
    }
}
