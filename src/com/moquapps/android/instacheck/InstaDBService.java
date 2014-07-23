package com.moquapps.android.instacheck;

import kankan.wheel.widget.adapters.NumericWheelAdapter;
import kankan.wheel.widget.adapters.WheelViewAdapter;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class InstaDBService extends Service {
	public int mNumOfRecords_inBill_IFP_Table = 1;
	Cursor mCursor;
	final String TAG = "iCheck";//TAG for instaCheck
    //Binder given to clients	
    private final IBinder mBinder = new AddBinder();     
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class AddBinder extends Binder {
       InstaDBService getService() {
         //Return this instance of LocalService so clients can call public methods
         //Log.v("getService","In get Service");
    	 Log.i(TAG,"InstaDBService:AddBinder()");
         return InstaDBService.this;
       }
    }
    //---------------------------------------------
    @Override
    public IBinder onBind(Intent intent) {
       //Log.v("InstaService","bindng");
       Log.i(TAG,"InstaDBService:OnBind()");
       return mBinder;
    }
    //---------------------------------------------
    /** method for clients */
    public void addTask(String orderPrice, String orderName, String orderCount)//, String totalSpent)//, String restName, String rating, int ID) 
    {    	
    	//Log.v("InstaDBService","Inserting food items from bill");
    	//Log.v(TAG, "InstaDBService:addTask():Inserting food items from bill");
    	ContentValues values = new ContentValues();
	    FoodItem newItem = new FoodItem(orderPrice, orderName, orderCount); //, totalSpent); // restName, rating, ID);
		 
	    // fItem.wheel.findViewById(R.id.wheel1);
		values.put(InstaProvider.KEY_ORDERCOUNT,newItem.getOcount());
		values.put(InstaProvider.KEY_ORDERNAME, newItem.getOname()); 
		values.put(InstaProvider.KEY_ORDERPRICE,newItem.getOprice());
		values.put(InstaProvider.KEY_BILL_IMAGE_NUMBER, mNumOfRecords_inBill_IFP_Table);
		 
		// values.put(InstaProvider.KEY_TOTALSPENT, newItem.getTSpend()); 
		// values.put(InstaProvider.KEY_RATING, newItem.getRating()); 
		// values.put(InstaProvider.KEY_RESTNAME, newItem.getRname()); 
		//Log.v("instDBService", "In addTask");
		Log.i(TAG,"InstaDBService:addTask():price = "+orderPrice+
				              " name= "+orderName+" count="+orderCount);
		Uri uri = getContentResolver().insert(InstaProvider.CONTENT_URI, values);
		//Toast.makeText(this,"Added Food Item", Toast.LENGTH_SHORT).show();		 
    }
    //------------------------------------------------------------
    //ak-July17,2014
	//This method adds an entry, for every new image user takes, to the newly added 
	// table whose URI is defined as CONTENT_UTI_BILL_IFP_TABLE in InstaProvider class file. 
	// The new entry is for the file-path-name-for-the-image. This method is called
    // from ParseBill1.java:print() method. 
    public void addBill_ImageFilePathName(String bill_ImageFilePathName){
       Log.i(TAG,"InstaDBService:addBillImageFilePathName():path = "+
                                                            bill_ImageFilePathName);
       ContentValues cValues = new ContentValues();
       cValues.put(InstaProvider.KEY_BILL_IMAGE_FILE_PATH, bill_ImageFilePathName);       
       Uri uri = getContentResolver().insert(InstaProvider.CONTENT_URI_BILL_IFP_TABLE,cValues);       
    }
    //-------------------------------------------
    //ak-July19,2014
    //This method called once from ParseBill1:print() method after new image of bill 
    //is taken to update the count with total number of images taken by user. 
    public void updateCount_numOfRecords_in_Bill_IFP_Table(){
    	mNumOfRecords_inBill_IFP_Table = 1;    	
    	//Get actual count of number of records in Bill_IFP_Table 
    	//Ref: ToDoList:updateArray()
 
    	//query to get all records from the new table
    	mCursor = getContentResolver().query(InstaProvider.CONTENT_URI_BILL_IFP_TABLE,		 
    		null, //ColumnsToReturn - projection - null mean all columns 
    	    null,//selection - which rows to return - null mean all rows; (WHERE clause)
    	    null,//selectionArgs
    	    null);//sortOrder - null man dafault order which can be unordered! 
    	/**********************************	
    	//No need to loop thru to get the count of records in the table. 
        if (mCursor.moveToFirst()){
    		do {  
    	   	   mNumOfRecords_inBill_IFP_Table++;    	   	   
    		}while(mCursor.moveToNext());
    	}
    	*******************************/    
    	mNumOfRecords_inBill_IFP_Table = mCursor.getCount();//get count of total num of records
        Log.v(TAG, "InstaDBService:updateCount_numOfRecords..IFP_Table():mNumOfRecords = "
			       + mNumOfRecords_inBill_IFP_Table);
    }
    //----------------------------------------------------
    //need not be used
    public void removeTask(String which){    	
	   getContentResolver().delete(Uri.parse(InstaProvider.CONTENT_URI +"/"+which),null,null);
	   Toast.makeText(this,"Removed Food Task", Toast.LENGTH_SHORT).show();
	   Log.i(TAG,"InstaDBService:removeTask()");
   }
   //---------------------------------------------
}