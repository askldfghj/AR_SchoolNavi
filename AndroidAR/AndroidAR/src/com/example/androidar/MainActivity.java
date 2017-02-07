package com.example.androidar;

import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener {

	private SensorManager sm;
	private Sensor RotationVector;

	private LocationManager lm;
	private int numSatellite;

	private float[] orientationVals = new float[3];
	private float[] inRotationMatrix = new float[9];
	private float[] outRotationMatrix = new float[9];
	private mMatrix m1 = new mMatrix();

	private mMatrix finalR;
	private mMatrix tempR;

	boolean previewing = false;

	Location endLocation;
	Location curLocation;

	Datas datas;

	ArrayList<Building> buildingList = new ArrayList<Building>();

	public static double[] latArr = { 35.076306 };
	public static double[] lonArr = { 129.090032 };

	Preview mPreview;
	DrawMaker drawMaker;

	Maker maker;
	private Display display;

	long prev_time, next_time;

	double rate_factor;

	double tempD;
	String cut;

	LayoutInflater controlInflater = null;
	Button searchBtn;
	SeekBar bar;
	TextView tvDis;

	int seekValue;
	int alpha;

	Bundle extraBundle;
	Bundle pushBundle;

	private final static int SEARCH_ACTIVITY = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		mPreview = new Preview(this);
		drawMaker = new DrawMaker(this);

		datas = new Datas();
		LoadData();

		for (int i = datas.crosscount; i < datas.info_struct.size(); i++) {
			Building tempBuilding = new Building();

			tempBuilding = (Building) datas.info_struct.get(i);

			buildingList.add(tempBuilding);
		}

		buildingList.get(0).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_1);
		buildingList.get(1).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_2);
		buildingList.get(2).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_3);
		buildingList.get(3).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_4);
		buildingList.get(4).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_5);
		buildingList.get(5).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_6);
		buildingList.get(6).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_7);
		buildingList.get(7).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_8);
		buildingList.get(8).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_9);
		buildingList.get(9).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_10);
		buildingList.get(10).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_11);
		buildingList.get(11).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_12);
		buildingList.get(12).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_13);
		buildingList.get(13).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_14);
		buildingList.get(14).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_15);
		buildingList.get(15).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_16);
		buildingList.get(16).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_17);
		buildingList.get(17).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_18);
		buildingList.get(18).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_19);
		buildingList.get(19).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_20);
		buildingList.get(20).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_21);
		buildingList.get(21).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_22);
		buildingList.get(22).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_23);
		buildingList.get(23).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_24);
		buildingList.get(24).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_25);
		buildingList.get(25).bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_26);

		Toast.makeText(this, Double.toString(datas.info_struct.get(datas.crosscount).location.getLatitude()),
				Toast.LENGTH_SHORT).show();

		sm = (SensorManager) getSystemService(SENSOR_SERVICE);
		RotationVector = sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		display = MainActivity.this.getWindowManager().getDefaultDisplay();

		finalR = new mMatrix();
		tempR = new mMatrix();

		maker = new Maker();

		maker.setWidth(display.getWidth());
		maker.setHeight(display.getHeight());

		endLocation = new Location("gps");

		endLocation.setLatitude(35.075150);
		endLocation.setLongitude(129.086573);
		endLocation.setAltitude(0);

		curLocation = new Location("gps");

		curLocation.setLatitude(35.074756);
		curLocation.setLongitude(129.086817);
		curLocation.setAltitude(0);

		setContentView(mPreview);
		addContentView(drawMaker, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

		/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		controlInflater = LayoutInflater.from(getBaseContext());
		View viewControl = controlInflater.inflate(R.layout.widget, null);
		LayoutParams layoutParamsControl = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		this.addContentView(viewControl, layoutParamsControl);

		searchBtn = (Button) findViewById(R.id.searchBtn);
		bar = (SeekBar) findViewById(R.id.seekBar);
		tvDis = (TextView) findViewById(R.id.tvDis);
		tvDis.setTextColor(Color.RED);
		tvDis.setTextSize(30);

		bar.setOnSeekBarChangeListener(seekBarListener);
		bar.incrementProgressBy(1);

		searchBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent();

				ArrayList<Building> build = new ArrayList<Building>();

				for (int i = datas.crosscount; i < datas.info_struct.size(); i++) {
					Building b = new Building();
					b = (Building) datas.info_struct.get(i);
					build.add(b);
				}

				intent.putExtra("build", build);

				intent.setClassName("com.example.androidar", "com.example.androidar.SearchActivity");
				startActivityForResult(intent, SEARCH_ACTIVITY);
			}
		});
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);

		if (requestCode == SEARCH_ACTIVITY) {
		}

		if (resultCode == RESULT_OK) {
			extraBundle = intent.getExtras();
			String str = extraBundle.getString("key1");
			Toast.makeText(this, str, 0).show();
		}
	}

	public void LoadData() {
		XmlPullParser infolist;
		int index = 0;
		int input_ok = 0;
		try {
			// crossdata.xml �� ������ XmlPullParser �� �ִ´�.
			infolist = getResources().getXml(R.xml.crossdata);
			Cross_Info CInfo = new Cross_Info();
			// �Ľ��� �� END_DOCUMENT(���� �±�)�� ���ö� ���� �ݺ��Ѵ�.
			while (infolist.getEventType() != XmlPullParser.END_DOCUMENT) {
				// �±��� ù��° �Ӽ��� ��,
				if (infolist.getEventType() == XmlPullParser.START_TAG) {
					// �̸��� "lati" �϶�, ù��° �Ӽ����� ArrayList�� ����
					if (infolist.getName().equals("lati")) {
						CInfo.set_latitude(Double.parseDouble(infolist.getAttributeValue(0)));
					} else if (infolist.getName().equals("long")) {
						CInfo.set_longitude(Double.parseDouble(infolist.getAttributeValue(0)));
					} else if (infolist.getName().equals("id")) {
						CInfo.set_id(infolist.getAttributeValue(0));
						input_ok = 1;
					}
					if (input_ok == 1) {
						CInfo.set_index(index);
						CInfo.setLocation();
						datas.info_struct.add(CInfo);
						CInfo = new Cross_Info();
						input_ok = 0;
						index++;
						datas.crosscount++;
					}
				}
				// ���� �±׷� �̵�
				infolist.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// buildata.xml �� ������ XmlPullParser �� �ִ´�.
			infolist = getResources().getXml(R.xml.buildata);
			Building BInfo = new Building();
			// �Ľ��� �� END_DOCUMENT(���� �±�)�� ���ö� ���� �ݺ��Ѵ�.
			while (infolist.getEventType() != XmlPullParser.END_DOCUMENT) {
				// �±��� ù��° �Ӽ��� ��,
				if (infolist.getEventType() == XmlPullParser.START_TAG) {
					// �̸��� "lati" �϶�, ù��° �Ӽ����� ArrayList�� ����
					if (infolist.getName().equals("lati")) {
						BInfo.set_latitude(Double.parseDouble(infolist.getAttributeValue(0)));
					} else if (infolist.getName().equals("long")) {
						BInfo.set_longitude(Double.parseDouble(infolist.getAttributeValue(0)));
					} else if (infolist.getName().equals("id")) {
						BInfo.set_id(infolist.getAttributeValue(0));
					} else if (infolist.getName().equals("dep")) {
						BInfo.set_depart(infolist.getAttributeValue(0));
					} else if (infolist.getName().equals("name")) {
						BInfo.set_name(infolist.getAttributeValue(0));
						input_ok = 1;
					}
					if (input_ok == 1) {
						BInfo.set_index(index);
						BInfo.setLocation();
						datas.info_struct.add(BInfo);
						BInfo = new Building();
						input_ok = 0;
						index++;
					}
				}
				// ���� �±׷� �̵�
				infolist.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// graph.xml �� ������ XmlPullParser �� �ִ´�.
			infolist = getResources().getXml(R.xml.graph);
			Neighbors NInfo = new Neighbors();
			// �Ľ��� �� END_DOCUMENT(���� �±�)�� ���ö� ���� �ݺ��Ѵ�.
			while (infolist.getEventType() != XmlPullParser.END_DOCUMENT) {
				// �±��� ù��° �Ӽ��� ��,
				if (infolist.getEventType() == XmlPullParser.START_TAG) {
					// �̸��� "graph" �϶�, ù��° �Ӽ����� ArrayList�� ����
					if (infolist.getName().equals("A")) {
						NInfo.a = infolist.getAttributeValue(0);
					} else if (infolist.getName().equals("B")) {
						NInfo.b = infolist.getAttributeValue(0);
						input_ok = 1;
					}
					if (input_ok == 1) {
						datas.Neighbors_struct.add(NInfo);
						NInfo = new Neighbors();
						input_ok = 0;
					}
				}
				// ���� �±׷� �̵�
				infolist.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected void onResume() {
		super.onResume();

		rate_factor = (double) 0.035;

		prev_time = System.currentTimeMillis();

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3 * 1000, 0, gpsListener);
		// lm.addNmeaListener(gpsStatusNmeaListener);

		sm.registerListener(this, RotationVector, 50 * 1000);

	}

	protected void onPause() {
		super.onPause();

		lm.removeUpdates(gpsListener);
		// lm.removeNmeaListener(gpsStatusNmeaListener);

		sm.unregisterListener(this);
	}

	private SeekBar.OnSeekBarChangeListener seekBarListener = new SeekBar.OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			tvDis.setText("OpacityRate : " + Integer.toString(progress));
			seekValue = progress * 8;
		}
	};

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
			SensorManager.getRotationMatrixFromVector(inRotationMatrix, event.values);

			SensorManager.remapCoordinateSystem(inRotationMatrix, SensorManager.AXIS_Z, SensorManager.AXIS_MINUS_X,
					outRotationMatrix);

			tempR.set(outRotationMatrix[0], outRotationMatrix[1], outRotationMatrix[2], outRotationMatrix[3],
					outRotationMatrix[4], outRotationMatrix[5], outRotationMatrix[6], outRotationMatrix[7],
					outRotationMatrix[8]);

			finalR.toIdentity();
			finalR.prod(tempR);
			finalR.invert();

			maker.setRotationMatrix(finalR);

			for (int i = 0; i < buildingList.size(); i++) {
				float[] temp = new float[1];

				Location.distanceBetween(curLocation.getLatitude(), curLocation.getLongitude(),
						buildingList.get(i).location.getLatitude(), buildingList.get(i).location.getLongitude(), temp);

				Building building = buildingList.get(i);

				building.curDistance = temp[0];

				alpha = (int) (255f
						- (((((801f - (float) seekValue) * 255f) / 801f) * (building.curDistance / 801f)) * 2f));

				building.alpha = alpha;

				if (building.curDistance >= 150) {
					building.alpha = 0;
				}

				buildingList.set(i, building);
			}

			for (int i = 0; i < buildingList.size(); i++) {

				maker.setStrLoc(curLocation);
				maker.setEndLoc(buildingList.get(i).location);

				maker.calcMaker();

				//////////////////////////////////////////////////
				drawMaker.setWidth(maker.getWidth());
				drawMaker.setHeight(maker.getHeight());
				// endLocation은 다음 노드
				drawMaker.setDegree(maker.calcBearing(curLocation, endLocation));
				/////////////////////////////////////////////////

				drawMaker.buildingDrawList = buildingList;

				drawMaker.setMakerX(i, maker.makerX);
				drawMaker.setMakerY(i, maker.makerY);
			}

			drawMaker.postInvalidate();
			;

			next_time = System.currentTimeMillis();

			if (next_time - prev_time > 2000) {
				prev_time = System.currentTimeMillis();

			}

		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	private GpsStatus.NmeaListener gpsStatusNmeaListener = new GpsStatus.NmeaListener() {
		public void onNmeaReceived(long timestamp, String nmea) {
			// $GPGGA,054208.000,3725.973,N,12709.561,E,1,05(위성의
			// 수),1.90,141.50,M,160.50,M,,*58

			try {

				String str_temp[] = nmea.split(",");
				if (str_temp[0].equals("$GPGGA")) { // NMEA 0번 데이터가 "$GPGGA" 이면
													// gps 이다.

					numSatellite = Integer.parseInt(str_temp[7]);

					// double temp;
					//
					// temp = (Double.parseDouble(str_temp[2])/100)
					// * ((double) numSatellite * rate_factor)
					// + curLocation.getLatitude()
					// * ((double) 1 - (double) numSatellite * rate_factor);
					//
					// curLocation.setLatitude(temp);
					//
					// temp = (Double.parseDouble(str_temp[4])/100)
					// * ((double) numSatellite * rate_factor)
					// + curLocation.getLongitude()
					// * ((double) 1 - (double) numSatellite * rate_factor);
					//
					// curLocation.setLongitude(temp);
				}
			}

			catch (Exception e) {
			}
		}
	};

	LocationListener gpsListener = new LocationListener() {

		public void onStatusChanged(String provider, int status, Bundle extras) {

		}

		public void onProviderEnabled(String provider) {
			Toast.makeText(getApplicationContext(), "gpsEnabled", 0).show();
		}

		public void onProviderDisabled(String provider) {
			// Toast.makeText(getApplicationContext(), "gpsDisabled ver2.0",
			// 0).show();
		}

		public void onLocationChanged(Location location) {

			tempD = location.getLatitude();
			cut = String.format("%.7f", tempD);

			location.setLatitude(Double.parseDouble(cut));

			tempD = location.getLongitude();
			cut = String.format("%.7f", tempD);

			location.setLongitude(Double.parseDouble(cut));

			Toast.makeText(getApplicationContext(),
					"gpsUpdate " + location.getLatitude() + ", " + location.getLongitude(), 0).show();

			curLocation = location;
		}
	};

}

class DrawMaker extends View {

	public static final int numBuilding = 26;

	public ArrayList<Integer> makerX;
	public ArrayList<Integer> makerY;

	public ArrayList<Building> buildingDrawList;

	int width;
	int height;

	float degree;

	Bitmap arrow;

	public DrawMaker(Context context) {
		super(context);

		makerX = new ArrayList<Integer>();
		makerY = new ArrayList<Integer>();
		buildingDrawList = new ArrayList<Building>();

		for (int i = 0; i < numBuilding; i++) {
			makerX.add(100);
			makerY.add(100);

			Building building = new Building();

			buildingDrawList.add(building);
		}

		width = 2560;
		height = 1440;

		arrow = BitmapFactory.decodeResource(getResources(), R.drawable.arrow);
	}

	public void setBuildingDrawList(ArrayList<Building> buildingList) {
		this.buildingDrawList = buildingList;
	}

	public void setMakerX(int index, int makerX) {
		this.makerX.set(index, makerX);
	}

	public void setMakerY(int index, int makerY) {
		this.makerY.set(index, makerY);
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setDegree(float degree) {
		this.degree = degree;
	}

	@Override
	protected void onDraw(Canvas canvas) {

		Paint paint = new Paint();

		try {

			for (int i = 0; i < numBuilding; i++) {

				paint.setAlpha(buildingDrawList.get(i).alpha);
				canvas.drawBitmap(buildingDrawList.get(i).bitmap, makerX.get(i), makerY.get(i), paint);
			}
		}

		catch (Exception e) {
		}

		// 화살표 그리는 것
		Matrix matrix = new Matrix();
		matrix.postRotate(degree);

		paint.setAlpha(255);
		canvas.drawBitmap(rotateImage(arrow, degree), width - arrow.getWidth() * 2, height - arrow.getHeight() * 2,
				paint);

		super.onDraw(canvas);
	}

	// 이미지 Rotation
	private Bitmap rotateImage(Bitmap src, float degree) {

		int srcWidth = src.getWidth();
		int srcHeight = src.getHeight();

		Matrix matrix = new Matrix();

		matrix.postRotate(degree);

		return Bitmap.createBitmap(src, 0, 0, srcWidth, srcHeight, matrix, true);

	}
}

class Preview extends SurfaceView implements SurfaceHolder.Callback {
	SurfaceHolder mHolder;
	Camera mCamera;
	boolean previewing = false;

	Preview(Context context) {
		super(context);

		mHolder = getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void surfaceCreated(SurfaceHolder holder) {

		mCamera = Camera.open();
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException exception) {
			mCamera.release();
			mCamera = null;
			// TODO: add more exception handling logic here
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
		mCamera = null;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {

		if (previewing) {
			mCamera.stopPreview();
			previewing = false;
		}

		if (mCamera != null) {
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();
				previewing = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}