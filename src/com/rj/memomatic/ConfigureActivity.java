package com.rj.memomatic;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ConfigureActivity extends Activity {
	
	TextView enableText;
	TextView lastRan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_configure);
		enableText = (TextView)findViewById(R.id.enabledisable);
		updateText();
	}
	
	
	void updateText() {
		final boolean enabled = SchedulerReciever.isEnabled(this);
		
		if (enabled) {
			enableText.setText(R.string.config_on);
			enableText.setTextColor(getResources().getColor(R.color.state_green));
		} else {
			enableText.setText(R.string.config_off);
			enableText.setTextColor(getResources().getColor(R.color.state_red));
		}
		
		
		final Context context = this;
		enableText.setOnClickListener(new OnClickListener() {
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
		
	}
	
	void updateUi() {
		updateText();
		updateLastRan();
	}

}
