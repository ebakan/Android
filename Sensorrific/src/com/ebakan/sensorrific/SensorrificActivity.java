package com.ebakan.sensorrific;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SensorrificActivity extends Activity {
	//private TextView result;
	private GLSurfaceView view;
	private SensorrificRenderer renderer;
	private AccelerometerListener accelerometerListener;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        view = new GLSurfaceView(this);
        renderer = new SensorrificRenderer(1.0f, 1.5f, 1.0f, 1.0f);
        view.setRenderer(renderer);
        setContentView(view);
        accelerometerListener = new AccelerometerListener(this, new Callback() {public void callback() { update();}});
        //result = (TextView) findViewById(R.id.result);
        //result.setText("No result yet");
    }
    @Override
    protected void onResume() {
    	super.onResume();
    	accelerometerListener.start();
    }

    @Override
    protected void onPause() {
    	accelerometerListener.stop();
    	super.onPause();
    }
    
    public void update() {
    	float[] vals = accelerometerListener.getVals();
    	Vector3 direction = new Vector3((double) vals[0], (double) -vals[1], (double) vals[2]);
    	Vector3 unit = new Vector3(0.0, 0.0, 1.0);
    	Vector3 cross = direction.cross(unit);
    	double angle = Math.acos(direction.dot(unit)/(unit.magnitude()*direction.magnitude()))*180/Math.PI;
    	//System.out.printf("%f %f %f %f\n", cross.x, cross.y, cross.z, angle);
    	renderer.setRot(cross, angle);
    }
}