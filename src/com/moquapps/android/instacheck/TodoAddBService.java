package com.moquapps.android.instacheck;



import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class TodoAddBService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new AddBinder();
    // Random number generator
    //private int factResult=1;
     
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class AddBinder extends Binder {
        TodoAddBService getService() {
            // Return this instance of LocalService so clients can call public methods
        	Log.v("getService","In get Service");
            return TodoAddBService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
    	Log.v("AddBService","bindng");
        return mBinder;
    }

    /** method for clients */
    public void addTask(String task) {
    	
    	 ContentValues values = new ContentValues();
		/* ToDoItem newItem = new ToDoItem(task, 0);
		 values.put(InstaProvider.KEY_ORDERCOUNT,newItem.getTask());
		 values.put(InstaProvider.KEY_DATE, newItem.getTaskId());        
		 Uri uri = getContentResolver().insert(TodoProvider.CONTENT_URI, values);
		 Toast.makeText(this,"Added Todo Task", Toast.LENGTH_SHORT).show();*/
		 
		  
    	 
    }
    public void removeTask(String which) {
    	
    	//getContentResolver().delete(todoURI + "/2",which, null);
		//getContentResolver().delete(Uri.parse(TodoProvider.CONTENT_URI +"/"+which),null,null);
		Toast.makeText(this,"Removed Todo Task", Toast.LENGTH_SHORT).show();
				
		 
   	 
   }
     
}