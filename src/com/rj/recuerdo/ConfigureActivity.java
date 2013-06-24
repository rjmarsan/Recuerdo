package com.rj.recuerdo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ConfigureActivity extends Activity {
	
	public final static int ACTIVITY_INTERVAL = 11;
	
	TextView enableText;
	TextView descriptionText;
	TextView lastRan;
	TextView changeInterval;
	Typeface robotoThin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configure);
		robotoThin = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
		enableText = (TextView)findViewById(R.id.enabledisable);
		descriptionText = (TextView)findViewById(R.id.description);
		lastRan = (TextView)findViewById(R.id.lastphoto);
		changeInterval = (TextView)findViewById(R.id.changeinterval);
		enableText.setTypeface(robotoThin);
		descriptionText.setTypeface(robotoThin);
		lastRan.setTypeface(robotoThin);
		changeInterval.setTypeface(robotoThin);

		initUi();
		updateUi();
		
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (ACTIVITY_INTERVAL == requestCode) {
			int minutes = data.getIntExtra("minutes", SchedulerReciever.getInterval(this));
			SchedulerReciever.setInterval(this, minutes);
			Log.d("MemoMatic - Configure", "new interval: "+minutes+" minutes");
			SchedulerReciever.startSelf(this, true);
			updateUi();
		}
	}
	
	void initUi() {
		descriptionText.setFocusable(true);
		descriptionText.setClickable(true);
		
		changeInterval.setFocusable(true);
		changeInterval.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(ConfigureActivity.this, IntervalPickerActivity.class), ACTIVITY_INTERVAL);
			}
		});
	}
	
	void updateText() {
		final boolean enabled = SchedulerReciever.isEnabled(this);
		
		if (enabled) {
			enableText.setText(R.string.config_on);
			enableText.setTextColor(getResources().getColorStateList(R.drawable.text_green_colorlist));
		} else {
			enableText.setText(R.string.config_off);
			enableText.setTextColor(getResources().getColorStateList(R.drawable.text_red_colorlist));
		}
		
		int interval_minutes = SchedulerReciever.getInterval(this);
		descriptionText.setText(getResources().getString(R.string.config_description, interval_minutes));
		
		final Context context = this;
		descriptionText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (enabled) {
					Log.d("MemoMatic - Configure", "UnScheduling Picture!");
			        SchedulerReciever.actionDisable(context);
				} else {
					Log.d("MemoMatic - Configure", "Scheduling Picture!");
			        SchedulerReciever.actionEnable(context);
				}
				updateUi();
			}
		});
		
		


		
	}
	
	void updateLastRan() {
		long now = System.currentTimeMillis();
		long nextScan = SchedulerReciever.getNextScan(this);
		boolean enabled = SchedulerReciever.isEnabled(this);
		long diff = (nextScan-now)/1000; //in seconds
		if (diff < 0 || enabled == false) {
			lastRan.setVisibility(View.INVISIBLE);
		} else {
			lastRan.setVisibility(View.VISIBLE);
		}
		if (diff < 60) {
			lastRan.setText("Next photo in "+diff+" seconds");
			return;
		} 
		diff = diff / 60;
		if (diff < 60) {
			lastRan.setText("Next photo in "+diff+" minutes");
			return;
		}
		diff = diff / 60;
		lastRan.setText("Next photo in "+diff+" hours");
	}
	
	void updateUi() {
		updateText();
		updateLastRan();
	}

	
	@Override
	protected void onStart() {
		super.onStart();
		updateUi();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updateUi();
	}
}
