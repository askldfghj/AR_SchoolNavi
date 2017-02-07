package com.example.androidar;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends Activity {
	Bundle extraBundle;

	Intent intent;
	Button searchBtn;
	Spinner spinnerSearch;
	TextView tvInfo;
	TextView tvLoc;

	ArrayList<String> items = new ArrayList<String>();
	ArrayList<Building> build;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);

		Intent getintent = getIntent();
		build = (ArrayList<Building>) getintent.getSerializableExtra("build");

		searchBtn = (Button) findViewById(R.id.searchBtn);
		spinnerSearch = (Spinner) findViewById(R.id.spinnerSearch);
		tvInfo = (TextView) findViewById(R.id.tvInfo);

		tvLoc = (TextView) findViewById(R.id.tvLoc);

		items.add("목적지 건물을 선택하세요");
		for (int i = 0; i < build.size(); i++) {
			items.add(build.get(i).get_name());
		}

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
				items);
		// spinnerSearch.setAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_dropdown_item_1line, items));
		spinnerSearch.setPrompt("목적지 건물 선택");
		spinnerSearch.setAdapter(adapter);

		spinnerSearch.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
				// TODO Auto-generated method stub

				// Toast.makeText(getApplicationContext(), items.get(arg2),
				// 1).show();

				if (index != 0) {
					tvInfo.setText(build.get(index - 1).get_depart());
					tvLoc.setText(Double.toString(build.get(index - 1).get_latitude()) + ", "
							+ Double.toString(build.get(index - 1).get_longitude()));

				} else {
					tvInfo.setText("");
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		searchBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				extraBundle = new Bundle();
				extraBundle.putString("key1", "누름");

				intent = new Intent();
				intent.putExtras(extraBundle);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}
}
