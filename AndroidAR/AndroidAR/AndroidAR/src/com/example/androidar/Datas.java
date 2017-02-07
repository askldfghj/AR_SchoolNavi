package com.example.androidar;

import java.io.Serializable;
import java.util.ArrayList;

public class Datas implements Serializable{
	public ArrayList<Info_Struct> info_struct = new ArrayList<Info_Struct>();
	public ArrayList<Neighbors> Neighbors_struct = new ArrayList<Neighbors>();
	public int crosscount = 0;
}
