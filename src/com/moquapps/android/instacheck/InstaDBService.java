package com.moquapps.android.instacheck;

 
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class InstaDBService extends Service {
	public static int mNumOfRecords_inBill_IFP_Table = 1;
	Cursor mCursor; 
	
    private final IBinder mBinder = new AddBinder();     
    
    public class AddBinder extends Binder {
       InstaDBService getService() {
       //Log.v(ParseBill1.TAG,"InstaDBService:AddBinder()");
         return InstaDBService.this;
       }
    }   
     
    @Override
    public IBinder onBind(Intent intent) {
       //Log.v(ParseBill1.TAG,"InstaDBService:OnBind()");
       return mBinder;
    }
    
    public void addTask(String orderPrice, String orderName, String orderCount) {
    	 
    	ContentValues values = new ContentValues();
	    FoodItem newItem = new FoodItem(orderPrice, orderName, orderCount); 
		 
	    
		values.put(InstaProvider.KEY_ORDERCOUNT,newItem.getOcount());
		values.put(InstaProvider.KEY_ORDERNAME, newItem.getOname()); 
		values.put(InstaProvider.KEY_ORDERPRICE,newItem.getOprice());
		values.put(InstaProvider.KEY_BILL_IMAGE_NUMBER, mNumOfRecords_inBill_IFP_Table);
		 
		 
		//Log.v(ParseBill1.TAG,"InstaDBService:addTask():price = "+orderPrice+
		//		              " name= "+orderName+" count="+orderCount);
		getContentResolver().insert(InstaProvider.CONTENT_URI, values);
		 		 
    }
     
    public void addBill_ImageFilePathName(String bill_ImageFilePathName){
       //Log.v(ParseBill1.TAG,"InstaDBService:addBillImageFilePathName():path = "+
       //                                      bill_ImageFilePathName);
       ContentValues cValues = new ContentValues();
       cValues.put(InstaProvider.KEY_BILL_IMAGE_FILE_PATH, bill_ImageFilePathName);       
       getContentResolver().insert(InstaProvider.CONTENT_URI_BILL_IFP_TABLE,cValues);       
    }
      
    public void updateCount_numOfRecords_in_Bill_IFP_Table(){
    	mNumOfRecords_inBill_IFP_Table = 1;    	
    	 
    	mCursor = getContentResolver().query(InstaProvider.CONTENT_URI_BILL_IFP_TABLE,		 
    		null, 
    	    null, 
    	    null, 
    	    null);  
    	   
    	mNumOfRecords_inBill_IFP_Table = mCursor.getCount();
        //Log.v(ParseBill1.TAG, "InstaDBService:updateCount_numOfRecords..IFP_Table():mNumOfRecords = "
		//	       + mNumOfRecords_inBill_IFP_Table);
    }
    
    public void removeTask(String which){    	
	   getContentResolver().delete(Uri.parse(InstaProvider.CONTENT_URI +"/"+which),null,null);
	    
	   //Log.v(ParseBill1.TAG,"InstaDBService:removeTask()");
   }
    
}