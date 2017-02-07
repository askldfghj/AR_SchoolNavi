package com.example.androidar;

import java.io.Serializable;

import android.location.Location;

public class Info_Struct implements Serializable{
	protected double Latitude;
	protected double Longitude;
	public transient Location location;
	private int Index;
	private String ID;
	
	public Info_Struct()
	{
		location = new Location("gps");
	}
	
	public void set_latitude(double lat)
	{
		Latitude = lat;
	}
	public void set_longitude(double longi)
	{
		Longitude = longi;
	}
	public void set_id(String id)
	{
		ID = id;
	}
	public void set_index(int i)
	{
		Index = i;
	}
	public double get_latitude()
	{
		return Latitude;
	}
	public double get_longitude()
	{
		return Longitude;
	}
	
	public String get_id()
	{
		return ID;
	}
	public int get_index()
	{
		return Index;
	}
	public void setLocation()
	{
		
		location.setLatitude(Latitude);
		location.setLongitude(Longitude);
	}
}

class Cross_Info extends Info_Struct implements Serializable
{	
	
}

class Neighbors implements Serializable
{
	String a;
	String b;
}
