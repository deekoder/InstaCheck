package com.moquapps.android.instacheck;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

//import kankan.wheel.widget.OnWheelChangedListener;
//import kankan.wheel.widget.OnWheelScrollListener;
//import kankan.wheel.widget.WheelView;
//import kankan.wheel.widget.adapters.AbstractWheelAdapter;
//import kankan.wheel.widget.adapters.NumericWheelAdapter;
import com.moquapps.android.instacheck.TodoAddBService.AddBinder;
 
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ToDoList extends Activity {
	
  static final private int ADD_NEW_TODO = Menu.FIRST;
  static final private int REMOVE_TODO = Menu.FIRST + 1;
  static final private int UPDATE_TODO = Menu.FIRST + 2;
  
  private static final String TEXT_ENTRY_KEY = "TEXT_ENTRY_KEY";
  private static final String ADDING_ITEM_KEY = "ADDING_ITEM_KEY";
  private static final String SELECTED_INDEX_KEY = "SELECTED_INDEX_KEY";
  
  private boolean addingNew = false;
  private ArrayList<ToDoItem> todoItems;
  private ArrayList<FoodItem> foodItems;
  private ListView myListView;
  private EditText myEditText;
  
  private static ToDoItemAdapter aa;
  
  TodoAddBService mService;
  boolean mBound = false;
  Spinner spinner;
  //ToDoDBAdapter toDoDBAdapter;
  Cursor toDoListCursor;
  ToDoAlarmManager tAM;
  ArrayAdapter<String> adapter;
  EditText myPerson;
  Button splitButton;
  Context cntxt;
  final String TAG = "iCheck";//TAG for instaCheck
  
  /** Called when the activity is first created. */
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.main);

    myListView = (ListView)findViewById(R.id.myListView);
    myEditText = (EditText)findViewById(R.id.myEditText);
    //myPerson = (EditText)findViewById(R.id.person);
    splitButton = (Button)findViewById(R.id.splitButton);
    
  //  todoItems = new ArrayList<ToDoItem>();
    foodItems = new ArrayList<FoodItem>();
  //  myPerson.setCurrentItem(0);
    cntxt = this;
    int resID = R.layout.todolist_item;
    aa = new ToDoItemAdapter(this, resID, foodItems);
    myListView.setAdapter(aa);
     
    Intent intent = new Intent(ToDoList.this, TodoAddBService.class);
    bindService(intent, mConnection, Context.BIND_AUTO_CREATE); 
    populateTodoList(this);
    
    myListView.setOnItemSelectedListener(new OnItemSelectedListener() {

        
        public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
            view.setSelected(true);
            
            
        }

		@Override
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			 Log.v("List","Selected"+arg2);
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub
			
		}
    });
    splitButton.setOnClickListener(new View.OnClickListener() {
	    public void onClick(View v) {
	    	int cntChoice = myListView.getCount();
	    	Log.v(TAG, "ToDoList():onCreate():splitBtnHandler():cntChoice="+cntChoice);
	    	for(int i = 0; i < cntChoice; i++)
	    	{	    			    	
	    		FoodItem fw = (FoodItem) myListView.getAdapter().getItem(i);
	    		//fw._categ = myListView.
	    		 
	    		//Log.v("Item","==>"+myListView.getItemAtPosition(i).toString());
	    		//Log.v("From Adapter","-->"+myListView.getAdapter().getItem(i));
	    		// Call the tip screen from here.
	    		 	    		 
	    	}
	    	//Start TipActivity - ak Aug 27,2014
	    	Intent intentToStartTipActivity = new Intent(ToDoList.this, TipActivity.class);
	    	startActivity(intentToStartTipActivity);
	    	Log.v(TAG, "ToDoList:onCreate():SplitBtnHandler():startActivity(intentToStartTipActivity)");
	    	//Log.v(TAG, "ToDoList:onCreate():SplitBtnHandler():call finish()");
	    	//finish();
	    }    
 	});
    
    
   
    }
  
  
  private void populateTodoList(Context c) {
    updateArray(c);
    
  }
  //---------------------------------------------------------------------
  //ak-Aug28,2014
  //This method is edited to query for all-items from the latest bill only. It replaces 
  // old-query which get all items from all bills in the todoItems db-table. 
  //
  private void updateArray(Context c) {
	  //toDoListCursor.requery();

	  //Old query which gets all-items from todoItem table, commented out - ak Aug 28,2014 
	  //old query--> toDoListCursor = getContentResolver().query(InstaProvider.CONTENT_URI, null, null, null, null);//Aug28,14
	  //ak-Aug28,2014 - Above old-query-line gets ALL entries from the db table
	  //which get displayed on the 'Split-button' screen.
	  //ak-Aug28,2014
	  //To display only the latest bill item, we need to query the db for the latest bill only
	  //To do this is 2 step process. 
	  //  Step-1 Get the total number of entries in the new table. It is also the latestBill_ImageNumber
	  //  Step-2 Get rows from old table which match the latestBill_ImageNumber
	 
      Integer latestBill_ImageNumber = InstaDBService.mNumOfRecords_inBill_IFP_Table;
      Log.v(TAG,"ToDoList:updateArray():latestBill_ImageNum = " + latestBill_ImageNumber);
	  toDoListCursor = getContentResolver().query(InstaProvider.CONTENT_URI, 
		  null, //projection i.e. which columns to return - null returns all columns as before
		  (InstaProvider.KEY_BILL_IMAGE_NUMBER + " = " + latestBill_ImageNumber),		 		  
	      null,  //selection-Args
		  null); //sort order
	  
	  //ak-That's it - later logic is same as before. But now, only items for latest bill
	  //   is displayed because the above new-query is constructed to get items from the 
	  //   latest bill in todoItems db-table.
	  
	//  todoItems.clear();
	  foodItems.clear();  
	  if (toDoListCursor.moveToFirst())
	    do {  
	      String orderCount = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_ORDERCOUNT)).trim();
	      String orderName = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_ORDERNAME)).trim();
	      String orderPrice = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_ORDERPRICE)).trim();
		  
	     // ToDoItem newItem = new ToDoItem(task, new Date(created), taskid);
	      FoodItem fItem = new FoodItem(orderCount,orderName,orderPrice);
	      //fItem.wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
	     // fItem.wheel.findViewById(R.id.wheel1);
	      //Log.v("Wheel", "-->"+fItem.wheel.getCurrentItem());
	   //   fItem.wheel.setCurrentItem(0);
	      foodItems.add(0, fItem);
	    } while(toDoListCursor.moveToNext());
	  aa.notifyDataSetChanged();
	  //if(mBound != true) spinner.setAdapter(adapter);
	}
  //-----------------------------------------------------------------
  private void restoreUIState() {
    // Get the activity preferences object.
    SharedPreferences settings = getPreferences(0);

    // Read the UI state values, specifying default values.
    String text = settings.getString(TEXT_ENTRY_KEY, "");
    Boolean adding = settings.getBoolean(ADDING_ITEM_KEY, false);

    // Restore the UI to the previous state.
    if (adding) {
      addNewItem();
      //myEditText.setText(text);
    }
  }
  
   
  
  @Override
  public void onSaveInstanceState(Bundle outState) {
    outState.putInt(SELECTED_INDEX_KEY, myListView.getSelectedItemPosition());

    super.onSaveInstanceState(outState);
    //tAM.cancelAlarm();
  }
  
  
  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState) {
    int pos = -1;

    if (savedInstanceState != null)
      if (savedInstanceState.containsKey(SELECTED_INDEX_KEY))
        pos = savedInstanceState.getInt(SELECTED_INDEX_KEY, -1);

    myListView.setSelection(pos);
    tAM.cancelAlarm();
  }
  
  /*@Override
  protected void onPause() {
    super.onPause();
    
    // Get the activity preferences object.
    SharedPreferences uiState = getPreferences(0);
    // Get the preferences editor.
    SharedPreferences.Editor editor = uiState.edit();

    // Add the UI state preference values.
    editor.putString(TEXT_ENTRY_KEY, myEditText.getText().toString());
    editor.putBoolean(ADDING_ITEM_KEY, addingNew);
    // Commit the preferences.
    editor.commit();
  }*/
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);

    // Create and add new menu items.
    MenuItem itemAdd = menu.add(0, ADD_NEW_TODO, Menu.NONE,
                                R.string.add_new);
    MenuItem itemRem = menu.add(0, REMOVE_TODO, Menu.NONE,
                                R.string.remove);

    // Assign icons
    itemAdd.setIcon(R.drawable.add_new_item);
    itemRem.setIcon(R.drawable.remove_item);

    // Allocate shortcuts to each of them.
    itemAdd.setShortcut('0', 'a');
    itemRem.setShortcut('1', 'r');

    return true;
  }

  @Override
  public boolean onPrepareOptionsMenu(Menu menu) {
    super.onPrepareOptionsMenu(menu);

    int idx = myListView.getSelectedItemPosition();

    String removeTitle = getString(addingNew ? 
                                   R.string.cancel : R.string.remove);

    MenuItem removeItem = menu.findItem(REMOVE_TODO);
    removeItem.setTitle(removeTitle);
    removeItem.setVisible(addingNew || idx > -1);

    return true;
  }
  
  @Override
  public void onCreateContextMenu(ContextMenu menu, 
                                  View v, 
                                  ContextMenu.ContextMenuInfo menuInfo) {
    super.onCreateContextMenu(menu, v, menuInfo);

    menu.setHeaderTitle("Selected To Do Item");
    menu.add(0, REMOVE_TODO, Menu.NONE, R.string.remove);
    menu.add(1, UPDATE_TODO, Menu.NONE,R.string.update);
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    super.onOptionsItemSelected(item);

    int index = myListView.getSelectedItemPosition();

    switch (item.getItemId()) {
      case (REMOVE_TODO): {
        if (addingNew) {
          cancelAdd();
        } 
        else {
        //  removeItem(index);
        }
        return true;
      }
      case (ADD_NEW_TODO): { 
        addNewItem();
        return true; 
      }
      case (UPDATE_TODO): {
    //	  updateNewItem(index);
    	  return true;
      }
    }

    return false;
  }
  
  @Override
  public boolean onContextItemSelected(MenuItem item) {  
    super.onContextItemSelected(item);
    switch (item.getItemId()) {
      case (REMOVE_TODO): {
        AdapterView.AdapterContextMenuInfo menuInfo;
        menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index = menuInfo.position;

     //   removeItem(index);
        return true;
      }
      case (UPDATE_TODO): {
    	  AdapterView.AdapterContextMenuInfo menuInfo;
          menuInfo =(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
          int index = menuInfo.position;
    //	  updateNewItem(index);
    	  return true;
      }
    }
    return false;
  }
  
  @Override
  public void onDestroy() {
	  
    super.onDestroy();
    tAM.cancelAlarm();  
    if (mBound) {
        unbindService(mConnection);
        mBound = false;
    }
    // Close the database
    //toDoDBAdapter.close();
  }
  
  private void cancelAdd() {
    addingNew = false;
    myEditText.setVisibility(View.GONE);
  }

  private void addNewItem() {
    addingNew = true;
  //  myEditText.setVisibility(View.VISIBLE);
   // myEditText.requestFocus(); 
  }

  private void removeItem(int _index, Context c) {
    // Items are added to the listview in reverse order, so invert the index.
    //toDoDBAdapter.removeTask(todoItems.size()-_index);
     ToDoItem item = todoItems.get(_index);
     final long selectedId = item.getTaskId();
     
     
	 Intent intent = new Intent(ToDoList.this, TodoAddBService.class);
     bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
     
     if (mBound) {
         // Call a method from the LocalService.
         // However, if this call were something that might hang, then this request should
         // occur in a separate thread to avoid slowing down the activity performance.
 	    Log.v("In On click","Hey2");
         mService.removeTask(String.valueOf(item.getTaskId()));
         updateArray(c);
         aa.notifyDataSetChanged();
          
     }
  
	 updateArray(c);
  }
   
  
  private void updateNewItem(final int _index, Context c) {
	    // Items are added to the listview in reverse order, so invert the index.
	    //toDoDBAdapter.removeTask(todoItems.size()-_index);
	  
	     ToDoItem item = todoItems.get(_index);
	     
	     final long selectedId = item.getTaskId();
	     
	      
	     Log.v("updateNewItem",item.getTask());  
	     final EditText tempText = new EditText(this);
	     tempText.setText(item.getTask());
	     AlertDialog.Builder alert = new AlertDialog.Builder(this); 
	     
	     alert.setTitle("Please update the item"); 
	        //alert.setMessage("Enter your email and password"); 
	        // Set an EditText view to get user input  
	     alert.setView(tempText); 
	     //AlertDialog loginPrompt = alert.create();

	          
	        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() { 
	        public void onClick(DialogInterface dialog, int whichButton) { 
	            String input = tempText.getText().toString();  
	            Intent intent = new Intent(ToDoList.this, TodoAddBService.class);
	            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	   	     
	   	     	if (mBound) {
	   	         // Call a method from the LocalService.
	   	         // However, if this call were something that might hang, then this request should
	   	         // occur in a separate thread to avoid slowing down the activity performance.
	   	     		Log.v("In On click","Hey2");
	   	     		mService.addTask(input);
	   	     		mService.removeTask(String.valueOf(selectedId));
	   	     		//updateArray(c);
	   	     		aa.notifyDataSetChanged();
	   	          
	   	     }
	   	  
	   		// updateArray(c);

	        } 
	        }); 

	        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() { 
	          public void onClick(DialogInterface dialog, int whichButton) { 
	            // Canceled. 
	          } 
	        }); 
	        AlertDialog ad = alert.create();
	        ad.show(); 
	     
	  
		
	  }
  
  @Override
  public void onResume()
  {
       super.onResume();
  }
 
  @Override
  protected void onStop() {
      super.onStop();
      // Unbind from the service
      /*if (mBound) {
          unbindService(mConnection);
          mBound = false;
      }*/
  }
  
  private ServiceConnection mConnection = new ServiceConnection() {

	  //Log.v("Setting",String.valueOf(mBound));
      @Override
      public void onServiceConnected(ComponentName className,
              IBinder service) {
          // We've bound to LocalService, cast the IBinder and get LocalService instance
    	  
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

