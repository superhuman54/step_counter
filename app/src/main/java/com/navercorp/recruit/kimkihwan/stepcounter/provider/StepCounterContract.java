package com.navercorp.recruit.kimkihwan.stepcounter.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import com.navercorp.recruit.kimkihwan.stepcounter.BuildConfig;

/**
 * Created by jamie on 10/1/16.
 */

public final class StepCounterContract {

    public static final String AUTHORITY =
            String.format("%s.provider", BuildConfig.APPLICATION_ID);

    public static final Uri AUTHORITY_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT)
            .authority(AUTHORITY)
            .build();

    public interface ID extends BaseColumns {
        public static final String STARTED_DATE = "started_date";
    }

    public interface StepCounter extends ID {
        public static final String PATH = "stepcounter";

        public static final String STEPS = "steps";
        public static final String DISTANCE = "distance";
        public static final String UPDATED_DATEIME = "updated_datetime";

        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, PATH);
    }

    public interface Footprint extends ID {
        public static final String PATH = "footprint";

        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String ADDRESS = "address";
        public static final String UPDATED_DATETIME = "updated_datetime";

        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(AUTHORITY_URI, PATH);
    }


}
