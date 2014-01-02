package com.moquapps.android.instacheck;

import java.util.ArrayList;
import java.util.Date;

import com.moquapps.android.instacheck.InstaDBService.AddBinder;

 
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class InstaActivity extends Activity {
	
  static final private int ADD_NEW_TODO = Menu.FIRST;
  static final private int REMOVE_TODO = Menu.FIRST + 1;
  static final private int UPDATE_TODO = Menu.FIRST + 2;
  
  private static final String TEXT_ENTRY_KEY = "TEXT_ENTRY_KEY";
  private static final String ADDING_ITEM_KEY = "ADDING_ITEM_KEY";
  private static final String SELECTED_INDEX_KEY = "SELECTED_INDEX_KEY";
  
  private boolean addingNew = false;
  private ArrayList<FoodItem> foodItems;
  
  private Button button1; 
  
    Cursor toDoListCursor;
   /** Called when the activity is first created. */
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.split_interim);

    button1 = (Button)findViewById(R.id.button1);
     
    foodItems = new ArrayList<FoodItem>();
    
    
    button1.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
        	
        }
    });
    
  }  
}

