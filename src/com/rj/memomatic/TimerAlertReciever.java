package com.rj.memomatic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.Log;

public class TimerAlertReciever extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d("SOUNDS", "Trying to play sound");
		try {
			MediaPlayer mp = MediaPlayer.create(context, R.raw.timer);
	        mp.setOnCompletionListener(new OnCompletionListener() {
	
	            @Override
	            public void onCompletion(MediaPlayer mp) {
	                mp.release();
	        		Log.d("SOUNDS", "finished platig sound");
	            }
	
	        });   
	        mp.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
