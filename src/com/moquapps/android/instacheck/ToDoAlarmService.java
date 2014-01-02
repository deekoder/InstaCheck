package com.moquapps.android.instacheck;
import java.io.IOException;
import java.util.Date;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class ToDoAlarmService extends Service {
     
	 
	 Uri todoURI = InstaProvider.CONTENT_URI;
	 Cursor todoCursor;
	 
	 
	 private void notifyUser() {
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		 // Instantiate the notification
		 int icon = R.drawable.icon;
		 todoCursor = getContentResolver().query(InstaProvider.CONTENT_URI, null, null, null, null);
		 int count = todoCursor.getCount();
		 
		 CharSequence tickerText = "ToDoList Reminder : " +count;
		 long when = System.currentTimeMillis();
		 
		 Notification notification = new Notification(icon, tickerText, when);
		 // Define notification message and pending intent
		 Context context = getApplicationContext();
		 CharSequence contentTitle = "My notification";
		 CharSequence contentText;
		 if(count != 0)
			 contentText = "You have " +count+" pending todo items!";
		 else  {
			 contentText = "You have NO pending todo items!";
			 
		 }
		 Intent notificationIntent = new Intent(this, ToDoList.class);
		 PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		 notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		 // Notify the user
		  // unique id to modify/cancel notification later on*/
		 Log.v("Alarm Service->", "Notify User");
		 mNotificationManager.notify(1, notification);
	 }
	
	  public void onCreate() {
		  	CharSequence tickerText = "You have pending todo items";
		  	long when = System.currentTimeMillis();  	
			Log.v("Alarm Service->", "Service onCreate");
		}
		
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			
			  // String count = 	
			   notifyUser();
			   stopSelf();		   
			   Log.v("Alarm Service->", "Service running");
			   return Service.START_STICKY;
		}
		
		@Override
		public IBinder onBind(Intent arg0) {
			// TODO Auto-generated method stub
			Log.v("Service->", "Service onBind");
			return null;
		}
		
		@Override
		  public void onDestroy() {
			
			Log.v("Service->", "Service onDestroy");
			 
		}
	
}