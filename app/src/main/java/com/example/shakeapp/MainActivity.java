package com.example.shakeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    // Objects
    private SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private TextView acceleration;

    // current acceleration value and gravity
    private float accelValue;
    // last acceleration value and gravity
    private float accelLast;
    // acceleration value differ from gravity
    private float shake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting access / reference to accelerometer
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // registering listener and getting reference to text view
        sensorManager.registerListener(this, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        acceleration = (TextView) findViewById(R.id.acceleration);

        accelValue = SensorManager.GRAVITY_EARTH;
        accelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;
    }

    // implemeted metod for sensor event
    @Override
    public void onSensorChanged(SensorEvent event) {

        // setting values to floats x, y and z
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // setting text (and values) on text view to show the movement of accelerometer
        acceleration.setText("\n X: " + x +
                            "\n Y: " + y +
                            "\n Z: " + z);

        // setting values to acceleration for "shaking part" of app
        accelLast = accelValue;
        accelValue = (float) Math.sqrt((double) (x*x + y*y + z*z));
        float delta = accelValue - accelLast;
        shake = shake * 0.9f + delta;

        // if shake's value is bigger than 15 -> toast - information shows on the screen
        if (shake > 15) {
            Toast toast = Toast.makeText(getApplicationContext(), "Don't shake me. I got enough!", Toast.LENGTH_LONG);
            toast.show();
        }

        // setting image view (image to rotate)
        ImageView imv = (ImageView) findViewById(R.id.imgRotate);

        // "if else" for rotating of an image (if position changes, image is rotating / or not)
        if (x > 9 || x < -9) {
            imv.setRotation(imv.getRotation() + 90);
        }
        else if (y < -9) {
            imv.setRotation(imv.getRotation() + 180);
        }
        else if (y > 9) {
            imv.setRotation(imv.getRotation());
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
