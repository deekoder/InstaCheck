package com.moquapps.android.instacheck;



import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class TodoAddBService extends Service {
    
    private final IBinder mBinder = new AddBinder();
     
    
    public class AddBinder extends Binder {
        TodoAddBService getService() {
           return TodoAddBService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
    	 
        return mBinder;
    }

    
    /*public void addTask(String task) {
    	
    	 ContentValues values = new ContentValues();
		 
		  
    	 
    }
    public void removeTask(String which) {
    	
   }*/
     
}