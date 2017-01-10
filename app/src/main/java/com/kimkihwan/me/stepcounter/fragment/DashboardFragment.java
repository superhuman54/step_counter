package com.kimkihwan.me.stepcounter.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.kimkihwan.me.stepcounter.R;
import com.kimkihwan.me.stepcounter.database.DatabaseOperator;
import com.kimkihwan.me.stepcounter.model.Footprint;
import com.kimkihwan.me.stepcounter.model.Snapshot;
import com.kimkihwan.me.stepcounter.model.StepCounter;
import com.kimkihwan.me.stepcounter.provider.StepCounterContract;
import com.kimkihwan.me.stepcounter.service.LocationService;
import com.kimkihwan.me.stepcounter.service.SensorService;
import com.kimkihwan.me.stepcounter.util.UnitUtils;

import java.util.Date;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by jamie on 10/2/16.
 */

public class DashboardFragment extends SectionFragment implements CompoundButton.OnCheckedChangeListener {

    private ToggleButton start;
    private TextView steps; // indicates steps taken so far
    private TextView distance; // indicates the distance tracked so far
    private TextView address; // for the spot you are now

    private static final String KEY_RUNNING = "running";
    private SharedPreferences preferences;

    private ContentObserver observer = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            update();
        }
    };

    public static DashboardFragment newInstance(int position, String title) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_POSITION, position);
        args.putString(ARG_SECTION_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        start = (ToggleButton) rootView.findViewById(R.id.start);
        start.setOnCheckedChangeListener(this);
        steps = (TextView) rootView.findViewById(R.id.steps);
        distance = (TextView) rootView.findViewById(R.id.distance);
        address = (TextView) rootView.findViewById(R.id.address);

        getContext().getContentResolver().registerContentObserver(StepCounterContract.StepCounter.CONTENT_URI, true, observer);
        getContext().getContentResolver().registerContentObserver(StepCounterContract.Footprint.CONTENT_URI, true, observer);

        if (!canCount()) {
            TextView warning = (TextView) rootView.findViewById(R.id.warning);
            warning.setVisibility(View.VISIBLE);
        }
        if (preferences.getBoolean(KEY_RUNNING, false)) {
            start.performClick();
        }
        return rootView;
    }

    private boolean canCount() {
        SensorManager manager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        Sensor stepcounter = manager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        return stepcounter != null;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    private void update() {
        final Date now = new Date();
        Observable
                .create(new Observable.OnSubscribe<Snapshot>() {
                    @Override
                    public void call(Subscriber<? super Snapshot> subscriber) {
                        Snapshot snapshot = DatabaseOperator.takeSnapshot(getContext(), now);
                        subscriber.onNext(snapshot);
                        subscriber.onCompleted();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Snapshot>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Snapshot snapshot) {
                        StepCounter sc = snapshot.getStepCounter();
                        Footprint f = snapshot.getFootprint();
                        if (sc != null) {
                            steps.setText(String.valueOf(sc.getSteps()));
                            distance.setText(UnitUtils.forDisplay(sc.getDistance()));
                        }
                        if (f != null) {
                            address.setText(f.getAddress());
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().getContentResolver().unregisterContentObserver(observer);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Intent sensorIntent = new Intent();
        sensorIntent.setClass(getContext(), SensorService.class);

        Intent locationIntent = new Intent();
        locationIntent.setClass(getContext(), LocationService.class);
        if (isChecked) {
            preferences.edit().putBoolean(KEY_RUNNING, true).apply();
            getContext().startService(sensorIntent);
            getContext().startService(locationIntent);
        } else {
            preferences.edit().putBoolean(KEY_RUNNING, false).apply();
            getContext().stopService(sensorIntent);
            getContext().stopService(locationIntent);
        }
    }
}
