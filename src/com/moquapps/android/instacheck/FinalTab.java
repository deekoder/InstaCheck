package com.moquapps.android.instacheck;
/**
 * This class does following:
 * (1) Display final tab to user
 * (2) Sends bill items to google-Cloud from onResume() using Service
 * (3) It inits Cloud (hence implements OnListener) to perform above step
 * Dec13,2014
 * (1) Remove Google Cloud (will be replaced by ParseCloud)
 *     (a)remove google-cloud Started-Service from onResume()
 *     (b)remove google-cloud init from here as well - do not implement OnListener 
 */
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.parse.ParseFile;
import com.parse.ParseObject;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class FinalTab extends Activity {

   HashMap<String, Double> mHashMapOfPeoplesFinalTab;
   CustomAdapterFinalTab customAdapterForFinalTab;
   ListView listViewInFinalTabItems;
   public ArrayList<FinalTabEachRowItems>arrayListOfFinalTabItems =
		                   new ArrayList<FinalTabEachRowItems>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_final_tab);
		
		Intent intent = getIntent();    
		mHashMapOfPeoplesFinalTab = (HashMap<String, Double>) intent.getSerializableExtra("hashMap");
		
		displayHashMapEntries(mHashMapOfPeoplesFinalTab);
	}

	public void displayHashMapEntries(HashMap<String, Double> hashMapOfFinalTab){
		//Sort HashMap entries based on keys (P1, P2 etc)
		SortedSet<String>sortedSetOfFinalTab = new TreeSet<String>(hashMapOfFinalTab.keySet());

		//Copy each key, value from above sortedSetOfFinalTabItems in to the ArrayList
		for (String key : sortedSetOfFinalTab){
			String amount = hashMapOfFinalTab.get(key).toString();
            Log.v(ParseBill1.TAG, "FinalTab:dispHashMapEnt():key & value = " + key + " " + amount);
            //create a new row to add to arraylist
            FinalTabEachRowItems itemsInEachRow = new FinalTabEachRowItems();
            //assign values to new-row creaed above
            itemsInEachRow.setPeopleName(key);
            itemsInEachRow.setAmount(amount);
            arrayListOfFinalTabItems.add(itemsInEachRow);
		 }//for
		 //Display the arrayList items on device
		 listViewInFinalTabItems = (ListView)findViewById(R.id.listViewForFinalTabItems);
	     //create custom adapter
		 customAdapterForFinalTab = new CustomAdapterFinalTab(
				            this, arrayListOfFinalTabItems, getResources());
		 listViewInFinalTabItems.setAdapter(customAdapterForFinalTab);
	}//displayHaskMapEntries()
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.final_tab, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	 
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onResume(){
	   super.onResume();
	   Log.v(ParseBill1.TAG, "FinalTab:onResume():send data to ParseCloud");
	   //**Remove Google-Cloud and include Paser-Cloud 
	   //start CloudStartedService; This service will send all bill items to Cloud
	   //Intent i = new Intent(FinalTab.this, CloudStartedService.class);
	   //startService(i);
	   //Create a ParseObject and send to ParseCloud
	   ParseObject iCheckParseObject = new ParseObject("InstaCheckObject");
	   iCheckParseObject.put("email_of_sender"/*key*/, /*value*/MainActivity.mEmailAddrOfThisMobile);
	   iCheckParseObject.put("instaCheckItems", OCRTask.mBill_ItemsToSendToCloud);	  
	   //------------------------------------------------------
	   //Store "Image" of check as a field of iCheckParseObject
	   //-------------------------------------------------------
	   ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	   //compress-and-copy bitmap image to byteArrayOutputStream
	   int compressQuality = 20;
	   MainActivity.photo.compress(CompressFormat.JPEG, compressQuality,byteArrayOutputStream);   
	   //get outputStream to a new byteArray
	   byte[] byteArrayImage = byteArrayOutputStream.toByteArray();
	   //use byteArray of image to create new Parsefile to save in ParseCloud.
	   ParseFile newParseFileForImage = new ParseFile("iCheck_Image.jpg", byteArrayImage);
	   newParseFileForImage.saveInBackground();
	   //assign parseCloud saved file to the instaCheck-ParseObject.
	   iCheckParseObject.put("InstaCheckImage", newParseFileForImage);
	   
	   //now, save the whole iCheckObject to Parse-Cloud
	   //iCheckParseObject.saveEventually();//not work or crashes
	   iCheckParseObject.saveInBackground();//works!
	}	
}
