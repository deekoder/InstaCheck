package com.moquapps.android.instacheck;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;



public class ToDoBroadCastReceiever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			 Bundle bundle = intent.getExtras();
			 //String count = bundle.getString("count");
			 int ItemID = bundle.getInt("ItemID");
			 Log.v("Alarm Broadcast", "On Receive");
			 
			 Intent scheduledIntent = new Intent(context, ToDoAlarmService.class);
			// scheduledIntent.putExtra("count", count);
			 scheduledIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 context.startService(scheduledIntent);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}