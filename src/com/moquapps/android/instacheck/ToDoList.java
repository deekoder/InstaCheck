package com.moquapps.android.instacheck;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.moquapps.android.instacheck.TodoAddBService.AddBinder;
 
public class ToDoList extends ListActivity {
	
  
  private ArrayList<FoodItem> foodItems;
  private ListView myListView;
  
  private static ToDoItemAdapter aa;
   
  TodoAddBService mService;
  boolean mBound = false;
  Spinner spinner;
  
  Cursor toDoListCursor;
  HashMap peepTotals;
 
  Button splitButton;
 
  Context cntxt;
   
  public ArrayList <PersonMath> pList;
  public ArrayList <PersonTotal> pTotal;
   
 
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);
   // myListView = (ListView)findViewById(R.id.);
    myListView = getListView();
    splitButton = (Button)findViewById(R.id.splitButton);
    foodItems = new ArrayList<FoodItem>();
    peepTotals = new HashMap();
    cntxt = this;
    int resID = R.layout.todolist_item;
    aa = new ToDoItemAdapter(this, resID, foodItems);
    myListView.setAdapter(aa);
      
    Intent intent = new Intent(ToDoList.this, TodoAddBService.class);
    bindService(intent, mConnection, Context.BIND_AUTO_CREATE); 
    populateTodoList(this);
    
     
    splitButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
	    	int cntChoice = myListView.getCount();
	    	Log.v(ParseBill1.TAG, "ToDoList():onCreate():splitBtnHandler():cntChoice="+cntChoice);
	    	
	    	String key;
	    	Double value;
	    	
	    	FoodItem fw;
	    	for(int i = 0; i < cntChoice; i++)
	    	{	    			    	
	    		fw = (FoodItem) myListView.getAdapter().getItem(i);
	    		 
	    		key = fw.selectedPerson;
	    		 
	    		value = (Double) peepTotals.get(key);
	    		if (value != null) {
	    			try {
	    				value += Double.parseDouble(fw._orderCount);
	    			}
	    			catch(Exception e) {
	    				value += (double) 0;
	    			}
	    		}
	    		else {
	    			
	    			try {
	    				value = Double.parseDouble(fw._orderCount);
	    			}
	    			catch(Exception e) {
	    				value = (double) 0;
	    			}
	    			 
	    		}
	    		 
	    		  
	    		 peepTotals.put(key, value);
	    		    		
	    		  	 
	    	}
	    	 
	  
	    	
	    	
	    	Intent intentToStartTipActivity = new Intent(ToDoList.this, TipActivity.class);
	    	intentToStartTipActivity.putExtra("hashMap", peepTotals);
	    	startActivity(intentToStartTipActivity);
	    	 
	    }    
 	});
    
     
  }
  
   
  
  private void populateTodoList(Context c) {
    updateArray(c);
    
  }
  
  private void updateArray(Context c) {
	 
	 
      Integer latestBill_ImageNumber = InstaDBService.mNumOfRecords_inBill_IFP_Table;
      Log.v(ParseBill1.TAG,"ToDoList:updateArray():latestBill_ImageNum = " + latestBill_ImageNumber);
	  toDoListCursor = getContentResolver().query(InstaProvider.CONTENT_URI, 
		  null,  
		  (InstaProvider.KEY_BILL_IMAGE_NUMBER + " = " + latestBill_ImageNumber),		 		  
	      null,  
		  null);  
	  	 
	  foodItems.clear();  
	  if (toDoListCursor.moveToFirst())
	    do {  
	      String orderCount = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_ORDERCOUNT)).trim();
	      String orderName = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_ORDERNAME)).trim();
	      String orderPrice = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_ORDERPRICE)).trim();
	      FoodItem fItem = new FoodItem(orderCount,orderName,orderPrice);
	      
	      foodItems.add(0, fItem);
	    } while(toDoListCursor.moveToNext());
	  aa.notifyDataSetChanged();
	   
	}
 
  @Override
  protected void onStop() {
      super.onStop();
       
  }
  
  private ServiceConnection mConnection = new ServiceConnection() {

	  
      @Override
      public void onServiceConnected(ComponentName className,
              IBinder service) {
           
          AddBinder binder = (AddBinder) service;
          mService = binder.getService();
          Log.v("Setting ->","true");
          mBound = true;
      }

      @Override
      public void onServiceDisconnected(ComponentName arg0) {
          mBound = false;
      }
  	};
 }

