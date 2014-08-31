package com.moquapps.android.instacheck;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
   
  
   
  
  public ArrayList <String> selectedPersons = new ArrayList<String>();
  
  List<String> list = new ArrayList<String>();
  
  
  public ToDoItemAdapter(Context _context, 
                             int _resource, 
                             List<FoodItem> _items) {
    super(_context, _resource, _items);
   
    resource = _resource;
    
    c =_context;
    list.add("P1");
    list.add("P2");
    list.add("P3");
    list.add("P4");
    list.add("P5");
    
  }
  public ArrayList<String> getSelectedPersons() {
	  return selectedPersons;
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
   Log.v("ConvertView", String.valueOf(position));
  
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
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            	   Object item = parent.getItemAtPosition(position);
            	   Log.v("Item is", "+: "+item.toString());    
            	   selectedPersons.add(position, item.toString());
            	       
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
   
   holder.personSpinner.setTag(item._orderName);
    
   holder.priceView.setText(item._orderPrice);
   holder.nameView.setText(item._orderName);
   holder.countView.setText(item._orderCount);
   
    
   
  
   return convertView;
  
  }
   
     
}

