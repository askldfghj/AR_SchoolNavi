package com.example.androidar;

import android.location.Location;

public class Maker {

	public float DEFAULT_VIEW_ANGLE;

	public Vector normalVector = new Vector(0, 1, 0);
	public Vector camVector = new Vector();
	public Vector tempa = new Vector();
	public static int alpha, beta;

	public Location strLoc;
	public Location endLoc;

	public static mMatrix rotationM = new mMatrix();

	public Vector locationVector = new Vector(0, 0, 0);

	public int width;
	public int height;

	public int makerX;
	public int makerY;

	float distW, distH;
	float scale;

	public static int temp;

	public Maker() {
		DEFAULT_VIEW_ANGLE = (float) Math.toRadians(45);
		alpha = 0;
	}

	public void setRotationMatrix(mMatrix RM) {
		this.rotationM = RM;
	}

	public void setStrLoc(Location loc) {
		strLoc = loc;
	}

	public void setEndLoc(Location loc) {
		endLoc = loc;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void calcMaker() {

		conLocToVec(strLoc, endLoc, locationVector);

		locationVector.prod(rotationM);

		distW = (width / 2) / (float) Math.tan(DEFAULT_VIEW_ANGLE / 2);
		distH = (height / 2) / (float) Math.tan(DEFAULT_VIEW_ANGLE / 2);

		// tempa 양쪽에 출력되는 것을 막기위한 장치 이다.


		tempa.set(locationVector.x, locationVector.y + alpha, locationVector.z);

		if (locationVector.length() > tempa.length()) {
			makerX = (int) ((locationVector.x * distW) / locationVector.y);
			makerY = -(int) ((locationVector.z * distH) / locationVector.y);

			makerX = width/2 + makerX;
			makerY = height/2 + makerY;
		}

		else {
			makerX = 100000;
			makerY = 100000;
		}

	}

	public static void conLocToVec(Location strLoc, Location endLoc, Vector outVector) {

		float[] x = new float[1];

		float[] y = new float[1];

		Location.distanceBetween(strLoc.getLatitude(), strLoc.getLongitude(), endLoc.getLatitude(),
				strLoc.getLongitude(), x);

		Location.distanceBetween(strLoc.getLatitude(), strLoc.getLongitude(), strLoc.getLatitude(),
				endLoc.getLongitude(), y);

		float z;

		// strLocation이 endLocation 보다 클때 -값을 곱해준다.
		z = 0;

		if (strLoc.getLatitude() > endLoc.getLatitude()) {
			x[0] *= -1;
			alpha = -1;
		} else
			alpha = 1;

		if (strLoc.getLongitude() > endLoc.getLongitude()) {
			y[0] *= -1;
			beta = -1;
		} else
			beta = 1;

		if (alpha == 1 && beta == 1)
			alpha = -1;
		else if (alpha == -1 && beta == 1)
			alpha = 1;
		else if (alpha == -1 && beta == -1)
			alpha = -1;
		else if (alpha == 1 && beta == -1)
			alpha = 1;

		outVector.set(x[0], y[0], z);
	}

	public static float calcBearing(Location strLoc, Location endLoc) {

		float degree;

		float[] x = new float[1];

		float[] y = new float[1];

		Location.distanceBetween(strLoc.getLatitude(), strLoc.getLongitude(), endLoc.getLatitude(),
				strLoc.getLongitude(), x);

		Location.distanceBetween(strLoc.getLatitude(), strLoc.getLongitude(), strLoc.getLatitude(),
				endLoc.getLongitude(), y);

		if (strLoc.getLatitude() > endLoc.getLatitude()) {
			x[0] *= -1;
		}

		if (strLoc.getLongitude() > endLoc.getLongitude()) {
			y[0] *= -1;
		}

		float z = 0;

		Vector vector = new Vector();
		vector.set(x[0], y[0], z);
		vector.prod(rotationM);
		
		Vector tempv = new Vector();
		
		tempv.set(vector.x, vector.y + alpha, vector.z);
		
		degree = 0;
		
		if (vector.length() > tempv.length())
		{
			degree = (float)(Math.toDegrees(Math.atan(vector.x/vector.y)));
		}
		
		else if(vector.length()< tempv.length())
		{
			degree = (float)(Math.toDegrees(Math.atan(vector.x/vector.y))) + 180f;
		}
		
		
		return degree;

	}
}
