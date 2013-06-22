package com.rj.memomatic;

import java.util.Calendar;
import java.util.Date;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class SchedulerReciever extends BroadcastReceiver {
	public final static String PROCESS_SHARED_PREFS = SchedulerReciever.class.getPackage().toString() + ".PROCESS_SHARED_PREFS";
	public final static String LASTSCANNED_SHARED_PREFS = SchedulerReciever.class.getPackage().toString() + ".LASTSCANNED_SHARED_PREFS";
	public final static String TIME_OF_LAST_SCAN = SchedulerReciever.class.getPackage().toString() + ".LAST_SCAN";
	public final static String NEXT_SCHEDULED_SCAN = SchedulerReciever.class.getPackage().toString() + ".NEXT_SCHEDULED_SCAN";
	public final static String INTERVAL = SchedulerReciever.class.getPackage().toString() + ".INTERVAL";
	public final static String ENABLED = SchedulerReciever.class.getPackage().toString() + ".ENABLED";
	public final static String PAUSED = SchedulerReciever.class.getPackage().toString() + ".PAUSED";
	public final static String TIMER_ENABLED = SchedulerReciever.class.getPackage().toString() + ".TIMER_ENABLED";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			PhotoTaker.takePhoto(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			saveTimestamp(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		startSelf(context, true);
	}
	
	
	private void saveTimestamp(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(LASTSCANNED_SHARED_PREFS, 0);
		Editor edit = prefs.edit();
		edit.putLong(TIME_OF_LAST_SCAN, System.currentTimeMillis());
		edit.putLong(NEXT_SCHEDULED_SCAN, System.currentTimeMillis()+getInterval(context));
		edit.commit(); 
	}
	private static void setNextScheduledScan(Context context, long time) {
		SharedPreferences prefs = context.getSharedPreferences(LASTSCANNED_SHARED_PREFS, 0);
		Editor edit = prefs.edit();
		edit.putLong(NEXT_SCHEDULED_SCAN, time);
		edit.commit(); 
	}
	
	public static long getLastScan(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(LASTSCANNED_SHARED_PREFS, 0);
		return prefs.getLong(TIME_OF_LAST_SCAN, -1);
	}
	public static long getNextScan(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(LASTSCANNED_SHARED_PREFS, 0);
		return prefs.getLong(NEXT_SCHEDULED_SCAN, -1);
	}

	
	
	
	
	public static long calculateNextScanTime(Context context) {
		int minute_interval = getInterval(context);
		Calendar cal_target = Calendar.getInstance();
		cal_target.add(Calendar.MINUTE, minute_interval);
		int minute = cal_target.get(Calendar.MINUTE);
		minute = minute_interval*(minute / minute_interval);
		cal_target.set(Calendar.MINUTE, minute);
		
		return cal_target.getTimeInMillis();
	}
	
	
	
	
	public static void startSelf(Context context, boolean overrideSchedule) {
		startStopSelf(context, true, overrideSchedule);
	}
	public static void stopSelf(Context context) {
		startStopSelf(context, false, false);
	}
	public static void startStopSelf(Context context, boolean start, boolean overrideSchedule) {
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        
        Intent i = new Intent(context, SchedulerReciever.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        
        Intent timer_i = new Intent(context, TimerAlertReciever.class);
        PendingIntent timer_pi = PendingIntent.getBroadcast(context, 0, timer_i, 0);

    	mgr.cancel(pi);
    	mgr.cancel(timer_pi);

    	long now = System.currentTimeMillis();
    	long scheduledTime = getNextScan(context);
    	//if we missed this photo, skip it and schedule another soon.
    	//ideally: schedule on the :00s, :20s and :40s.
    	//maybe change the schedule if the person is away from home?
    	if (now > scheduledTime || overrideSchedule) {
    		scheduledTime = calculateNextScanTime(context);
    		Log.d("MemoMatic - Scheduler", "Missed the photo. new one in "+((scheduledTime-now)/1000)+" seconds in the future: "+start);
    		setNextScheduledScan(context, scheduledTime);
    	}
		Log.d("MemoMatic - Scheduler", "Scheduling Picture for "+((scheduledTime-now)/1000)+" seconds in the future: "+start);
		if (isPaused(context) == true || isEnabled(context) == false) {
			Log.d("MemoMatic - Scheduler", "either paused or disabled. not scheduling");
			return;
		}
        if (start) {
        	//mgr.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), repeatTime, pi);
        	mgr.set(AlarmManager.RTC, scheduledTime, pi);
        	mgr.set(AlarmManager.RTC, scheduledTime-5000, timer_pi);
        }

	}

	
	
	public static boolean isEnabled(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		return prefs.getBoolean(ENABLED, false);
	}
	
	
	public static void setEnabled(Context context, boolean running) {        
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		Editor edit = prefs.edit();
    	edit.putBoolean(ENABLED, running);
        edit.apply();
	}
	
	public static boolean isPaused(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		return prefs.getBoolean(PAUSED, false);
	}
	
	
	public static void setPaused(Context context, boolean paused) {        
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		Editor edit = prefs.edit();
    	edit.putBoolean(PAUSED, paused);
        edit.apply();
	}



	
	/**
	 * The interval between photos, in minutes.
	 * @param context
	 * @return
	 */
	public static int getInterval(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		return prefs.getInt(INTERVAL, 20);
	}
	
	public static void setInterval(Context context, long value) {
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		Editor edit = prefs.edit();
		edit.putLong(INTERVAL, value);
		edit.apply();
	}
	
	public static boolean getTimerSoundEnabled(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		return prefs.getBoolean(TIMER_ENABLED, true);
	}
	
	public static void setTimerSoundEnabled(Context context, boolean value) {
		SharedPreferences prefs = context.getSharedPreferences(PROCESS_SHARED_PREFS, 0);
		Editor edit = prefs.edit();
		edit.putBoolean(TIMER_ENABLED, value);
		edit.apply();
	}
	
	
	
	
	
	
	public static void actionPause(Context context) {
		Log.d("MemoMatic - Scheduler", "Pausing");
		setPaused(context, true);
		stopSelf(context);
	}
	
	public static void actionUnpause(Context context) {
		Log.d("MemoMatic - Scheduler", "Unpausing");
		setPaused(context, false);
		startSelf(context, false);
	}

	
	public static void actionEnable(Context context) {
		Log.d("MemoMatic - Scheduler", "Enabling");
		setEnabled(context, true);
		startSelf(context, true);
	}
	
	public static void actionDisable(Context context) {
		Log.d("MemoMatic - Scheduler", "Disabling");
		setEnabled(context, false);
		stopSelf(context);
	}
	

	

}
