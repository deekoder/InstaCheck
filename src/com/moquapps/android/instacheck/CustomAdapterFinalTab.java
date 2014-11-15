package com.moquapps.android.instacheck;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapterFinalTab extends BaseAdapter{
   private Activity mCallerActivityContext; 
   private ArrayList mArrayList;
   private static LayoutInflater layoutInflaterService = null;
   public Resources mResources;
   FinalTabEachRowItems mItemsInEachRowOfHashMap = null;
	   
   //constructor
   public CustomAdapterFinalTab(Activity callerActivityContext, ArrayList arrayList, Resources resources){
      mCallerActivityContext = callerActivityContext;
	  mArrayList = arrayList;
	  mResources = resources;        
	  //Layout inflator service to call external xml layout ()
	  layoutInflaterService = (LayoutInflater)
	       mCallerActivityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        
   }   
	
	public int getCount(){//Auto-generated to implement BaseAdapter
	   if (mArrayList.size() <= 0)
		  return 1;
	   return mArrayList.size();
	}
	
	public Object getItem(int position){//Auto-generated to implement BaseAdapter
		return position;
	}
	   
	public long getItemId(int position){//Auto-generated to implement BaseAdapter
		return position;
	}
	//************************
	// CLASS for ViewHolder 
	//************************
	//ViewHolder to contain inflated xml file elements
	public static class ViewHolderForEachRow{    	
	   public TextView textViewForPeople;
	   public TextView textViewForAmount;	        
	}
	//*************************
	//auto-generated to implement BaseAdapter
	public View getView(int position, View convertView, ViewGroup parent) {
	   //Log.i("ak","adapter:getView():enter");
	   View view = convertView;
	   ViewHolderForEachRow viewHolderForEachRow;
	   
	   if (convertView == null){
		  //inflate each_row_layout.xml file for each row
		  view = layoutInflaterService.inflate(R.layout.final_tab_each_row, null);
		  //viewHolderForEachRow object to contain each_row_layout file elements
		  viewHolderForEachRow = new ViewHolderForEachRow();
		  viewHolderForEachRow.textViewForPeople=(TextView)
				             view.findViewById(R.id.textViewPeople);
		  viewHolderForEachRow.textViewForAmount=(TextView)
		             view.findViewById(R.id.textViewAmount);
		  //Set viewToConvert value
		  view.setTag(viewHolderForEachRow);
	   }
	   else
		   viewHolderForEachRow = (ViewHolderForEachRow)view.getTag();
	   
	   if (mArrayList.size() <= 0){
		   viewHolderForEachRow.textViewForPeople.setText("No-Data:getView()");
	   }
	   else{//get each row item object from HashMapList
		  mItemsInEachRowOfHashMap = (FinalTabEachRowItems)mArrayList.get(position);
		  //put values in each Holder elements
	   	  viewHolderForEachRow.textViewForPeople.setText(mItemsInEachRowOfHashMap.getPeopleName());		  
	   	  viewHolderForEachRow.textViewForAmount.setText(mItemsInEachRowOfHashMap.getAmount()); 
	   }
	   return view;
	}
	
}