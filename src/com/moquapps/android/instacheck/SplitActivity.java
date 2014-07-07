package com.moquapps.android.instacheck;

import java.util.ArrayList;
import java.util.Date;

import com.moquapps.android.instacheck.FoodItem;
import com.moquapps.android.instacheck.InstaProvider;
import com.moquapps.android.instacheck.R;



//import kankan.wheel.widget.OnWheelChangedListener;
//import kankan.wheel.widget.OnWheelScrollListener;
//import kankan.wheel.widget.WheelView;



import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/*This file excluded from build - ak - July7,2014*/
@SuppressLint("NewApi")
public class SplitActivity extends Activity {

private TextView option1, option2, option3, choice1, choice2, choice3;
public CharSequence dragData;
private EditText myEditText;
Cursor toDoListCursor;
private ArrayList<FoodItem> foodList;

// Scrolling flag
private boolean scrolling = false;
 
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi") 

protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
   
    setContentView(R.layout.split);

    //get both sets of text views
    myEditText = (EditText) findViewById(R.id.myEditText);
    
    //views to drag
    option1 = (TextView)findViewById(R.id.option_1);
    option2 = (TextView)findViewById(R.id.option_2);
    option3 = (TextView)findViewById(R.id.option_3);

    
     
    savedInstanceState = getIntent().getExtras();
    //ParseBill pBill = (ParseBill) savedInstanceState.getSerializable("SomeUniqueKey");
    //views to drop onto
    choice1 = (TextView)findViewById(R.id.choice_1);
    choice2 = (TextView)findViewById(R.id.choice_2);
    choice3 = (TextView)findViewById(R.id.choice_3);

    //option1.append(String.valueOf(pBill.total));
    toDoListCursor = getContentResolver().query(InstaProvider.CONTENT_URI, null, null, null, null);  
    foodList = new ArrayList<FoodItem>();
    myEditText.setText("");
    
	/*if (toDoListCursor.moveToFirst())
	    do { 
	      String orderName = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_ORDERNAME));
	      String orderCount = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_ORDERCOUNT));
	      String orderPrice = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_ORDERPRICE));
	      String restName = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_RESTNAME));
	      String totalSpent = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_TOTALSPENT));
	      String rating = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_RATING));
	      myEditText.append(orderName);
	      myEditText.append("   ");
	      myEditText.append(orderCount);
	      myEditText.append("   ");
	      myEditText.append(orderPrice);
	      myEditText.append("   ");
	 
	      FoodItems newItem = new FoodItems();
	      foodList.add(newItem);
	    } while(toDoListCursor.moveToNext());*/
    
    getItems();
     
    //set touch listeners
    option1.setOnTouchListener(new ChoiceTouchListener());
    option2.setOnTouchListener(new ChoiceTouchListener());
    option3.setOnTouchListener(new ChoiceTouchListener());
    myEditText.setOnTouchListener(new ChoiceTouchListener());

    //set drag listeners
    choice1.setOnDragListener(new ChoiceDragListener());
    choice2.setOnDragListener(new ChoiceDragListener());
    choice3.setOnDragListener(new ChoiceDragListener());
    
    
     

   
}

void getItems(){
	//toDoListCursor = getContentResolver().query(InstaProvider.CONTENT_URI, null, null, null, null);  
     
    myEditText.setText("");
    if(toDoListCursor.moveToNext()){
	   
    	String orderName = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_ORDERNAME));
		String orderCount = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_ORDERCOUNT));
		String orderPrice = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_ORDERPRICE));
		//String restName = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_RESTNAME));
	//	String totalSpent = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_TOTALSPENT));
		//String rating = toDoListCursor.getString(toDoListCursor.getColumnIndex(InstaProvider.KEY_RATING));
	
		myEditText.append(orderName);
		myEditText.append("  ");
		myEditText.append(orderCount);
		myEditText.append("  ");
		myEditText.append(orderPrice);
		myEditText.append("  ");
	//	myEditText.append(restName);
	//	myEditText.append(totalSpent);
		//myEditText.append(rating);
    }
	//FoodItems newItem = new FoodItems();
	//return newItem;
    
}

/**
 * ChoiceTouchListener will handle touch events on draggable views
 *
 */
private final class ChoiceTouchListener implements OnTouchListener {
    @SuppressLint("NewApi")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            /*
             * Drag details: we only need default behavior
             * - clip data could be set to pass data as part of drag
             * - shadow can be tailored
             */
            ClipData data = ClipData.newPlainText("", "");
            DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            //start dragging the item touched
            view.startDrag(data, shadowBuilder, view, 0);
            return true;
        } else {
            return false;
        }
    }
} 

/**
 * DragListener will handle dragged views being dropped on the drop area
 * - only the drop action will have processing added to it as we are not
 * - amending the default behavior for other parts of the drag process
 *
 */
@SuppressLint("NewApi")
private class ChoiceDragListener implements OnDragListener {

    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch (event.getAction()) {
        case DragEvent.ACTION_DRAG_STARTED:
            //no action necessary
            break;
        case DragEvent.ACTION_DRAG_ENTERED:
            //no action necessary
            break;
        case DragEvent.ACTION_DRAG_EXITED:        
            //no action necessary
            break;
        case DragEvent.ACTION_DROP:

            //handle the dragged view being dropped over a drop view
            View view = (View) event.getLocalState();
            //view dragged item is being dropped on
            TextView dropTarget = (TextView) v;
            //view being dragged and dropped
            TextView dropped = (TextView) view;
            //checking whether first character of dropTarget equals first character of dropped
           // if(dropTarget.getText().toString().charAt(0) == dropped.getText().toString().charAt(0))
            {
                //stop displaying the view where it was before it was dragged
                view.setVisibility(View.INVISIBLE);
                //update the text in the target view to reflect the data being dropped
                dropTarget.setText(dropTarget.getText().toString() + dropped.getText().toString());
                //make it bold to highlight the fact that an item has been dropped
                dropTarget.setTypeface(Typeface.DEFAULT_BOLD);
                //if an item has already been dropped here, there will be a tag
                Object tag = dropTarget.getTag();
                //if there is already an item here, set it back visible in its original place
                if(tag!=null)
                {
                    //the tag is the view id already dropped here
                    int existingID = (Integer)tag;
                    //set the original view visible again
                    findViewById(existingID).setVisibility(View.VISIBLE);
                }
                //set the tag in the target view being dropped on - to the ID of the view being dropped
                dropTarget.setTag(dropped.getId());
                //remove setOnDragListener by setting OnDragListener to null, so that no further drag & dropping on this TextView can be done
                dropTarget.setOnDragListener(null);
               // reset();
                getItems();
            }
           // else
                //displays message if first character of dropTarget is not equal to first character of dropped
                Toast.makeText(SplitActivity.this, dropTarget.getText().toString() + "is not " + dropped.getText().toString(), Toast.LENGTH_LONG).show();
            break;
        case DragEvent.ACTION_DRAG_ENDED:
            //no action necessary
            break;
        default:
            break;
        }
        return true;
    }
} 

public void reset(View view)
{
    /*option1.setVisibility(TextView.VISIBLE);
    option2.setVisibility(TextView.VISIBLE);
    option3.setVisibility(TextView.VISIBLE);
    myEditText.setVisibility(EditText.VISIBLE);
    
    choice1.setText("A for ");
    choice2.setText("O for ");
    choice3.setText("B for ");
   // getLayout(myEditText);
    myEditText.setText("Blah blah");
    
    choice1.setTag(null);
    choice2.setTag(null);
    choice3.setTag(null);

    choice1.setTypeface(Typeface.DEFAULT);
    choice2.setTypeface(Typeface.DEFAULT);
    choice3.setTypeface(Typeface.DEFAULT);

    choice1.setOnDragListener(new ChoiceDragListener());
    choice2.setOnDragListener(new ChoiceDragListener());
    choice3.setOnDragListener(new ChoiceDragListener());
    myEditText.setOnDragListener(new ChoiceDragListener());*/
	finito();
}

@SuppressLint("NewApi")
private void finito()
{
	
	    option1.setVisibility(TextView.VISIBLE);
	    option2.setVisibility(TextView.VISIBLE);
	    option3.setVisibility(TextView.VISIBLE);
	    myEditText.setVisibility(EditText.VISIBLE);
	    
	    choice1.setText("A for ");
	    choice2.setText("O for ");
	    choice3.setText("B for ");
	   
	    getItems();
	    choice1.setTag(null);
	    choice2.setTag(null);
	    choice3.setTag(null);

	    choice1.setTypeface(Typeface.DEFAULT);
	    choice2.setTypeface(Typeface.DEFAULT);
	    choice3.setTypeface(Typeface.DEFAULT);

	    choice1.setOnDragListener(new ChoiceDragListener());
	    choice2.setOnDragListener(new ChoiceDragListener());
	    choice3.setOnDragListener(new ChoiceDragListener());
	    myEditText.setOnDragListener(new ChoiceDragListener());

	   // Intent mainIntent = new Intent(this, LoginActivity.class);
		//startActivity(mainIntent);
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
}


	
}