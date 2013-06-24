package com.rj.recuerdo;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class IntervalPickerActivity extends Activity {
	
	ListView list;
	int currentInterval;
	Typeface robotoThin;
	
	
	Map<String,Integer> intervals = new LinkedHashMap<String,Integer>() {{
		this.put("5 minute", 5);
		this.put("10 minute", 10);
		this.put("15 minute", 15);
		this.put("20 minute", 20);
		this.put("30 minute", 30);
		this.put("1 hour", 60);
	}};
	List<String> intervalsList = Arrays.asList(intervals.keySet().toArray(new String[]{}));
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_interval);
		robotoThin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		list = (ListView)findViewById(R.id.photo_intervals);
		
		currentInterval = SchedulerReciever.getInterval(this);
		
		setupList();
	}
	
	void setupList() {
		final IntervalAdapter adapter = new IntervalAdapter(this, R.layout.list_item, R.id.entry, intervalsList );
		
		
		list.setAdapter(adapter);
		list.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				//list.smoothScrollToPositionFromTop(position, 0);
				list.smoothScrollToPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				Intent result = new Intent();
				result.putExtra("minutes", (int)intervals.get(adapter.getItem(position)) );
				setResult(RESULT_OK, result);
				finish();

			}
		});
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		int index = 0;
		for (Entry<String, Integer> e : intervals.entrySet()) {
			if (e.getValue() == currentInterval) {
				list.setSelection(index);
				Log.d("IntervalPicker", "Picking index: "+index);
			}
			index += 1;
		}
	}
	
	private class IntervalAdapter extends ArrayAdapter<String> {

		public IntervalAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
			super(context, resource, textViewResourceId, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			TextView tv = (TextView)v;
			tv.setTypeface(robotoThin);
			tv.setBackgroundColor(Color.TRANSPARENT);
			parent.setBackgroundColor(Color.TRANSPARENT);
			
			return v;
		}
		
	};
	
	

	
}
