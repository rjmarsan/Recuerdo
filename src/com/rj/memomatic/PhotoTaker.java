package com.rj.memomatic;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PhotoTaker {
	public static void takePhoto(Context context) {
		Log.d("MemoMatic - Phototaker", "Scheduling Picture!");
		Intent localIntent = new Intent("com.google.glass.action.TAKE_PICTURE_FROM_SCREEN_OFF");
		localIntent.putExtra("should_finish_turn_screen_off", true);
		localIntent.putExtra("should_play_initial_sound", false);
		localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(localIntent);
	}
}
