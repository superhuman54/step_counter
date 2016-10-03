package com.navercorp.recruit.kimkihwan.stepcounter.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.navercorp.recruit.kimkihwan.stepcounter.database.DatabaseOperator;
import com.navercorp.recruit.kimkihwan.stepcounter.logger.Log;
import com.navercorp.recruit.kimkihwan.stepcounter.model.StepCounter;
import com.navercorp.recruit.kimkihwan.stepcounter.provider.StepCounterContract;

import java.util.Date;

/**
 * Created by jamie on 10/1/16.
 */

public class SensorService extends Service implements SensorEventListener {

    private SensorManager sensorManager;

    private Sensor accelerormeter;

    private float lastX, lastY, lastZ;
    private long lastTime;

    private static final double SPPED_THRESHOLD = 800d;
    private static final double STRIDE = 0.7d; // in meter

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(this, "onCreate");
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeter = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(this, "onStartCommand");
        if (accelerormeter != null) {
            sensorManager.registerListener(this, accelerormeter, SensorManager.SENSOR_DELAY_NORMAL);
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this)
        {
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    //
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    int step = (int) event.values[0];
                    Log.d(this, "step: " + step);
                    Date today = new Date();
                    StepCounter recorded = DatabaseOperator.loadStepCounter(
                            this,
                            Uri.withAppendedPath(StepCounterContract.StepCounter.CONTENT_URI, DatabaseOperator.STARTED_DATE_FORMAT.format(today))
                    );

                    if (recorded == null) {
                        StepCounter started = new StepCounter()
                                .setStartedDate(DatabaseOperator.STARTED_DATE_FORMAT.format(today))
                                .setSteps(step)
                                .setDistance(step * STRIDE)
                                .setUpdateDatetime(DatabaseOperator.UPDATED_DATETIME_FORMAT.format(today));
                        DatabaseOperator.save(this, started);
                    } else {
                        recorded.setSteps(recorded.getSteps() + step)
                                .setDistance(recorded.getDistance() + STRIDE)
                                .setUpdateDatetime(DatabaseOperator.UPDATED_DATETIME_FORMAT.format(today));
                        DatabaseOperator.update(this, recorded);
                    }
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(this, "onDestroy()");
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
