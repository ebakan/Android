package com.ebakan.sensorrific;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class SensorrificRenderer implements GLSurfaceView.Renderer {
	private FloatBuffer vertexBuffer, mab, mdb, msb, lPos, lab, ldb, lsb;
	private ShortBuffer headBuffer, baseBuffer, underBuffer, shaftBuffer;
	private float headLength, headGirth, shaftLength, shaftGirth;
	private int num = 128;
	private double rot;
	private Vector3 rotaxis;
	public SensorrificRenderer(float headLength, float headGirth, float shaftLength, float shaftGirth) {
		this.headLength = headLength;
		this.headGirth = headGirth;
		this.shaftLength = shaftLength;
		this.shaftGirth = shaftGirth;
		rot = 0.0;
		rotaxis = new Vector3(0.0, 0.0, 1.0);
	}
	public void setRot(Vector3 rotaxis, double rot) {
		this.rotaxis = rotaxis;
		this.rot = rot;
	}
	private float t = 0;
	@Override
	public void onDrawFrame(GL10 gl) {
		float red = (float) (Math.sin(t+0.0)*.5+.5);
		float blue = (float) (Math.sin(t+2.0/3*Math.PI)*.5+.5);
		float green = (float) (Math.sin(t+4.0/3*Math.PI)*.5+.5);
		gl.glLoadIdentity();
		gl.glTranslatef(0, 0, -10);
		//gl.glRotatef(t*20, 0.f, 1.f, 0.f);
		gl.glRotatef((float) rot, (float) rotaxis.x, (float) rotaxis.y, (float) rotaxis.z);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		// Counter-clockwise winding.
		gl.glFrontFace(GL10.GL_CCW);
		// Enable face culling.
		gl.glEnable(GL10.GL_CULL_FACE);
		// What faces to remove with the face culling.
		gl.glCullFace(GL10.GL_BACK);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColor4f(green, red, blue, 1.0f);
		gl.glDrawElements(GL10.GL_TRIANGLE_FAN, num+2, GL10.GL_UNSIGNED_SHORT, headBuffer);
		gl.glColor4f(blue, green, red, 1.0f);
		gl.glFrontFace(GL10.GL_CW);
		gl.glDrawElements(GL10.GL_TRIANGLE_FAN, num+2, GL10.GL_UNSIGNED_SHORT, baseBuffer);
		gl.glColor4f(red, blue, green, 1.0f);
		gl.glFrontFace(GL10.GL_CCW);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, (num+1)*2, GL10.GL_UNSIGNED_SHORT, underBuffer);
		gl.glColor4f(blue, red, green, 1.0f);
		gl.glDrawElements(GL10.GL_TRIANGLE_STRIP, (num+1)*2, GL10.GL_UNSIGNED_SHORT, shaftBuffer);
		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		// Disable face culling.
		gl.glDisable(GL10.GL_CULL_FACE);
		gl.glClearColor(red, green, blue, 1.0f);
		t+=.05;
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int w, int h) {
		// Sets the current view port to the new size.
		gl.glViewport(0, 0, w, h);
		// Select the projection matrix
		gl.glMatrixMode(GL10.GL_PROJECTION);
		// Reset the projection matrix
		gl.glLoadIdentity();
		// Calculate the aspect ratio of the window
		GLU.gluPerspective(gl, 45.0f, (float) w / (float) h, 0.1f, 100.0f);
		// Select the modelview matrix
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		// Reset the modelview matrix
		gl.glLoadIdentity();
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		// Set the background color to black ( rgba ).
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
		// Enable Smooth Shading, default not really needed.
		gl.glShadeModel(GL10.GL_SMOOTH);
		// Depth buffer setup.
		gl.glClearDepthf(1.0f);
		// Enables depth testing.
		gl.glEnable(GL10.GL_DEPTH_TEST);
		// The type of depth testing to do.
		gl.glDepthFunc(GL10.GL_LEQUAL);
		// Really nice perspective calculations.
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		mab = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mab.put(new float[]{1.0f, 0.0f, 0.0f, 1.0f});
		mab.position(0);
		mdb = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		mdb.put(new float[]{0.0f, 0.0f, 1.0f, 1.0f});
		mdb.position(0);
		msb = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		msb.put(new float[]{0.0f, 1.0f, 0.0f, 1.0f});
		msb.position(0);

		lPos = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		lPos.put(new float[]{0.0f, 0.0f, -8.0f, 1.0f});
		lPos.position(0);
		lab = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();	
		lab.put(new float[]{0.5f, 0.5f, 0.5f, 1.0f});
		lab.position(0);
		ldb = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		ldb.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
		ldb.position(0);
		lsb = ByteBuffer.allocateDirect(4 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		lsb.put(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
		lsb.position(0);
		
		gl.glEnable(GL10.GL_LIGHTING);
		gl.glEnable(GL10.GL_COLOR_MATERIAL);
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		
		gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, mab);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, mdb);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, msb);
        gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, 128.0f);

        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lPos);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lab);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, ldb);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, lsb);
        gl.glEnable(GL10.GL_LIGHT0);
		gl.glDisable(GL10.GL_TEXTURE);
        
		// Create arrow
		float[] vertices = new float[(num*3+2)*3];
		short[] headIndices = new short[num+2];
		short[] baseIndices = new short[num+2];
		short[] underIndices = new short[(num+1)*2];
		short[] shaftIndices = new short[(num+1)*2];
		// Bottom
		vertices[0] = 0;
		vertices[1] = 0;
		vertices[2] = 0;
		// Top
		vertices[3] = 0;
		vertices[4] = 0;
		vertices[5] = headLength + shaftLength;
		headIndices[0] = 1;
		baseIndices[0] = 0;
		headIndices[num+1] = (short) (2);
		baseIndices[num+1] = (short) (2+num*2);
		underIndices[2*num+0] = (short) (2);
		underIndices[2*num+1] = (short) (2+num);
		shaftIndices[2*num+0] = (short) (2+num);
		shaftIndices[2*num+1] = (short) (2+num*2);
		double trigFac = 2*Math.PI/num;
		double headRadius = headGirth / 2;
		double shaftRadius = shaftGirth / 2;
		for(int i=0;i<num;i++) {
			// Vertices
			double trigInc = trigFac*i;
			// Head
			vertices[6+i*3+0] = (float) (headRadius * Math.cos(trigInc));
			vertices[6+i*3+1] = (float) (headRadius * Math.sin(trigInc));
			vertices[6+i*3+2] = shaftLength;
			// Shaft Top
			vertices[6+num*3+i*3+0] = (float) (shaftRadius * Math.cos(trigInc));
			vertices[6+num*3+i*3+1] = (float) (shaftRadius * Math.sin(trigInc));
			vertices[6+num*3+i*3+2] = shaftLength;
			// Shaft Bottom
			vertices[6+num*6+i*3+0] = (float) (shaftRadius * Math.cos(trigInc));
			vertices[6+num*6+i*3+1] = (float) (shaftRadius * Math.sin(trigInc));
			vertices[6+num*6+i*3+2] = 0.f;
			
			// Indices
			headIndices[i+1] = (short) (2+i);
			baseIndices[i+1] = (short) (2+num*2+i);
			underIndices[2*i+0] = (short) (2+i);
			underIndices[2*i+1] = (short) (2+num+i);
			shaftIndices[2*i+0] = (short) (2+num+i);
			shaftIndices[2*i+1] = (short) (2+num*2+i);
		}
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		ByteBuffer hbb = ByteBuffer.allocateDirect(headIndices.length * 2);
		hbb.order(ByteOrder.nativeOrder());
		headBuffer = hbb.asShortBuffer();
		headBuffer.put(headIndices);
		headBuffer.position(0);
		ByteBuffer bbb = ByteBuffer.allocateDirect(baseIndices.length * 2);
		bbb.order(ByteOrder.nativeOrder());
		baseBuffer = bbb.asShortBuffer();
		baseBuffer.put(baseIndices);
		baseBuffer.position(0);
		ByteBuffer ubb = ByteBuffer.allocateDirect(underIndices.length * 2);
		ubb.order(ByteOrder.nativeOrder());
		underBuffer = ubb.asShortBuffer();
		underBuffer.put(underIndices);
		underBuffer.position(0);
		ByteBuffer sbb = ByteBuffer.allocateDirect(shaftIndices.length * 2);
		sbb.order(ByteOrder.nativeOrder());
		shaftBuffer = sbb.asShortBuffer();
		shaftBuffer.put(shaftIndices);
		shaftBuffer.position(0);
	}
}
