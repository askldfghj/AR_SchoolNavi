package com.example.androidar;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.location.Location;

public class Building extends Info_Struct implements Serializable{
	
	//0~255
	public transient int alpha;
	
	public transient Bitmap bitmap;
	
	public transient float curDistance;
	private String Depart;
	private String Name;
	
	
	public Building()
	{
		alpha = 255;
		curDistance = 0f;
	}
	
	public void set_depart(String dep)
	{
		Depart = dep;
	}
	
	public String get_depart()
	{
		return Depart;
	}
	public void set_name(String na)
	{
		Name = na;
	}
	
	public String get_name()
	{
		return Name;
	}
}
