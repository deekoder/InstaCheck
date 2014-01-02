package com.moquapps.android.instacheck;



import kankan.wheel.widget.adapters.NumericWheelAdapter;
import kankan.wheel.widget.adapters.WheelViewAdapter;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;


public class InstaDBService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new AddBinder();
     
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class AddBinder extends Binder {
        InstaDBService getService() {
            // Return this instance of LocalService so clients can call public methods
        	Log.v("getService","In get Service");
            return InstaDBService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
    	Log.v("InstaService","bindng");
        return mBinder;
    }

    /** method for clients */
    public void addTask(String orderPrice, String orderName, String orderCount)//, String totalSpent)//, String restName, String rating, int ID) 
    {
    	
    	 Log.v("InstaDBService","Inserting food items from bill");
    	 ContentValues values = new ContentValues();
		 FoodItem newItem = new FoodItem(orderPrice, orderName, orderCount); //, totalSpent); // restName, rating, ID);
		 
	     // fItem.wheel.findViewById(R.id.wheel1);
		 values.put(InstaProvider.KEY_ORDERCOUNT,newItem.getOcount());
		 values.put(InstaProvider.KEY_ORDERNAME, newItem.getOname()); 
		 values.put(InstaProvider.KEY_ORDERPRICE,newItem.getOprice());
		 
		// values.put(InstaProvider.KEY_TOTALSPENT, newItem.getTSpend()); 
		// values.put(InstaProvider.KEY_RATING, newItem.getRating()); 
		// values.put(InstaProvider.KEY_RESTNAME, newItem.getRname()); 
		 Log.v("instDBService", "In addTask");
		 Uri uri = getContentResolver().insert(InstaProvider.CONTENT_URI, values);
		 Toast.makeText(this,"Added Food Item", Toast.LENGTH_SHORT).show();
		 
		  
    	 
    }
   
    //need not be used
    public void removeTask(String which) {
    	
    	 
		getContentResolver().delete(Uri.parse(InstaProvider.CONTENT_URI +"/"+which),null,null);
		Toast.makeText(this,"Removed Food Task", Toast.LENGTH_SHORT).show();
   	 
   }
     
}