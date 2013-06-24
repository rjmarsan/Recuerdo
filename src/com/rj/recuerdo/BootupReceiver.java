package com.rj.recuerdo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootupReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		//ProcessMonitorReciever.checkAndStartStop(context);
		Log.d("MemoMatic - Bootup", "Bootup intent! "+intent);
		Log.d("MemoMatic - Bootup", "Bootup intent extras: "+intent.getExtras().keySet());
		for (String key: intent.getExtras().keySet()) {
			Log.d("MemoMatic - Bootup", "Bootup intent key: "+key+" value: "+intent.getExtras().get(key));
		}
		try {
			boolean glassOnHead = intent.getExtras().getBoolean("is_donned", false);
			if (glassOnHead) {
				SchedulerReciever.actionUnpause(context);
			} else {
				SchedulerReciever.actionPause(context);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
