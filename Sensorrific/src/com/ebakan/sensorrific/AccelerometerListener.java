package com.ebakan.sensorrific;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerListener implements SensorEventListener{
	private final SensorManager sensorManager;
	private final Sensor accelerometer;
	private float[] vals;
	private boolean started;
	private Callback callback;
	public AccelerometerListener(Context context, Callback callback) {
		this.callback = callback;
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		vals = new float[3];
		started = false;
		start();
	}
	
	public float[] getVals() {
		return vals;
	}
	
	public void start() {
		if(!started) {
			sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
		}
		started = true;
	}
	
	public void stop() {
		if(started) {
			sensorManager.unregisterListener(this);
		}
		started = false;
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		for(int i=0;i<3;i++) {
			vals[i]=event.values[i];
		}
		callback.callback();
	}

}
