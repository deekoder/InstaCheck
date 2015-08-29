package com.moquapps.android.instacheck;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class ToDoItemAdapter extends ArrayAdapter<FoodItem> {
  int resource;
  Context c;
  public FoodItem fw;
  public String selectedPersons;
  
  List<String> list = new ArrayList<String>();
  
  public ToDoItemAdapter(Context _context, 
                             int _resource, 
                             List<FoodItem> _items) {
    super(_context, _resource, _items);
    resource = _resource;
    c =_context;
    //list.add("P1");
    //list.add("P2");
    //list.add("P3");
    //list.add("P4");
    //list.add("P5");
    //list.add("assign...");
    
    
    //-------------------------------------------------
    //get names from Contact-List in android Phone - ak
    //-------------------------------------------------
	String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP;
	String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " ASC";
	ContentResolver contentResolver = c.getContentResolver();
	Cursor cursorResultSet = contentResolver.query(
			           ContactsContract.Contacts.CONTENT_URI,
                       null,//projection
                       selection,
                       null,//selectionArgs,
                       sortOrder);
	int numberOfContacts = cursorResultSet.getCount();
	Log.i("ak", "Number of Contact = " + numberOfContacts);
	if (numberOfContacts > 0){
	   //display all names in the Contact List
	   while (cursorResultSet.moveToNext()){
		  String nameOfEachContact = cursorResultSet.getString
                                     (cursorResultSet.getColumnIndex(
		  ContactsContract.Contacts.DISPLAY_NAME));//columnName
		  //Log.i("ak", "ContactName = " + nameOfEachContact);
		  //add nameOfEachContact to ArrayList
	      list.add(nameOfEachContact);
	  }
	}
	else {
		list.add("P1");
	    list.add("P2");
	    list.add("P3");
	    list.add("P4");
	    list.add("P5");
	}
  }

  public  String getSelectedPersons() {
	  return selectedPersons;
  }
  
  public FoodItem getAssociatedFWItem() {
	  return fw;
  }
  private class ViewHolder {
	    
	   Spinner personSpinner;
	   TextView priceView;
	   TextView nameView;
	   TextView countView;
	   
   }

  
  
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
  
    ViewHolder holder = null;
   
  
   if (convertView == null) {
   LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   convertView = vi.inflate(R.layout.todolist_item, null);
  
   holder = new ViewHolder();
   holder.priceView = (TextView)convertView.findViewById(R.id.rowDate);
   holder.nameView = (TextView)convertView.findViewById(R.id.row);
   holder.countView = (TextView)convertView.findViewById(R.id.rowPrice); 
   holder.personSpinner = (Spinner)convertView.findViewById(R.id.spinner2);
   
    
   convertView.setTag(holder);
  
   holder.personSpinner.setOnItemSelectedListener(
           new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	   Object item = parent.getItemAtPosition(pos);
            	  
            	   selectedPersons = item.toString();
            	   
            	   Log.v("Chosen spiner", selectedPersons);
            	   fw = (FoodItem) parent.getTag();  
            	  /* Log.v("FoodItem corresponding to spinner: " , fw._orderName);
            	   Log.v("FoodItem corresponding to spinner: " , fw._orderPrice);
            	   Log.v("FoodItem corresponding to spinner: " , fw._orderCount);*/
            	   fw.selectedPerson = selectedPersons;
            	       
               }
               @Override
               public void onNothingSelected(AdapterView<?> parent) {
                   
               }
           }
       );

     
   }
   else {
    holder = (ViewHolder) convertView.getTag();
   }
  
   holder.personSpinner = (Spinner)convertView.findViewById(R.id.spinner2);
   ArrayAdapter<String> adapter = new ArrayAdapter<String>(c, android.R.layout.simple_spinner_item, list);
   holder.personSpinner.setAdapter(adapter);
   FoodItem item = getItem(position);
   
   holder.personSpinner.setTag(item);
    
   holder.priceView.setText(item._orderPrice);
   holder.nameView.setText(item._orderName);
   holder.countView.setText(item._orderCount);
   
    
   
  
   return convertView;
  
  }
   
     
}

