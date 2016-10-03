package com.navercorp.recruit.kimkihwan.stepcounter.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.navercorp.recruit.kimkihwan.stepcounter.logger.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jamie on 10/1/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "stepcounter.db";

    private static final int SCHEME_VERSION = 1;

    private static DatabaseHelper instance;

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEME_VERSION);
        this.context = context;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int i = 1; i <= SCHEME_VERSION; i++) {
            applySql(db, i);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (int i = ++oldVersion; i <= newVersion; i++) {
            applySql(db, i);
        }
    }

    private void applySql(SQLiteDatabase db, int version) {
        BufferedReader reader = null;
        try {
            String filename = String.format("%s.%d.sql", DB_NAME, version);
            InputStream in = context.getAssets().open(filename);
            reader = new BufferedReader(new InputStreamReader(in));

            StringBuilder statement = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                Log.d(this, "Read line: " + line);

                if (!TextUtils.isEmpty(line) && !line.startsWith("--")) {
                    statement.append(line.trim());
                }

                if (line.endsWith(";")) {
                    Log.d(this, "Execute statement: " + statement);
                    db.execSQL(statement.toString());
                    statement.setLength(0);
                }
            }
        } catch (IOException e) {
            Log.e(this, "Can not apply sql file", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.wtf(this, "Can not close reader", e);
                }
            }
        }
    }

    public interface Tables {
        public static final String STEPCOUNTER = "stepcounter";
        public static final String FOOTPRINT = "footprint";
    }
}
