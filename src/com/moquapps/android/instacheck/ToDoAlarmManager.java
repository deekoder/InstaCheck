package com.moquapps.android.instacheck;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class ToDoAlarmManager {
	
	AlarmManager am;
	PendingIntent sender;
	 

	void setAlarm(Context c, long AlarmTime, String Message, Boolean Set) {
		
		
	    Intent intent = new Intent(c, ToDoBroadCastReceiever.class);
	     
	    //intent.putExtra("ItemID", ItemID);
	    Log.v("Alarms Manager", "Setting Alarm");
	    sender = PendingIntent.getBroadcast(c, 8192, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	   
	    // Get the AlarmManager service
	    am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
	    if (Set) {
	    	Log.v("Alarms Manager", "Repeat Alarm");
	        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*20, sender);
	    } else {
	        am.cancel(sender);
	    }
	}
	
	void cancelAlarm() {
		Log.v("AlarmsManager","Cancel alarm");
		
		am.cancel(sender);
		//stopService());
	}
}