package com.moquapps.android.instacheck;
/**
 * This class does following:
 * (1) Display final tab to user
 * (2) Sends bill items to google-Cloud from onResume() using Service
 * (3) It inits Cloud (hence implements OnListener) to perform above step
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.cloud.backend.core.CloudBackendFragment;
import com.google.cloud.backend.core.Consts;
import com.google.cloud.backend.core.CloudBackendFragment.OnListener;
import com.google.cloud.backend.core.CloudEntity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class FinalTab extends Activity implements OnListener {

  //Cloud variables
   private static final String PROCESSING_FRAGMENT_TAG = "BACKEND_FRAGMENT"; 
   private FragmentManager mFragmentManager;
   public static CloudBackendFragment mCloudBackendFragment;	

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
		
		//Cloud-Init requirement(Note:FragementManager Not allowed in Cloud-Service file)
        mFragmentManager = getFragmentManager();//CloudBackend-requirement.Note:FragmentManager NOT allowed in Service
        initiateCloudBackendFragments();//CloudBackend-requirement
	}

	public void displayHashMapEntries(HashMap<String, Double> hashMapOfFinalTab){
		//Sort HashMap entries based on keys (P1, P2 etc)
		SortedSet<String>sortedSetOfFinalTab = new TreeSet<String>(hashMapOfFinalTab.keySet());

		//Copy each key, value from above sortedSetOfFinalTabItems in to the ArrayList
		for (String key : sortedSetOfFinalTab){
			String amount = hashMapOfFinalTab.get(key).toString();
            Log.v(Consts.TAG_AK, "FinalTab:dispHashMapEnt():key & value = " + key + " " + amount);
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
	   //start CloudStartedService; This service will send all bill items to Cloud
	   Intent i = new Intent(FinalTab.this, CloudStartedService.class);
	   startService(i);
	}	

    /*****************************************************
     * CLOUD Backend - Init & OnListener interface methods
     * Note: Following CloudBackendFragment() method NOT allowed in 
     * the Cloud "Service" file CloudStartedService.java -- It MUST be
     * in an "Activity" file. 
	 *****************************************************/
     private void initiateCloudBackendFragments() {
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
	    //Check to see if we have retained the fragment which handles
	    //asynchronous backend calls
	    mCloudBackendFragment = (CloudBackendFragment) mFragmentManager.
	                                    findFragmentByTag(PROCESSING_FRAGMENT_TAG);
		//If not retained (or first time running), create a new one
		if (mCloudBackendFragment == null) {
		    mCloudBackendFragment = new CloudBackendFragment();
		    mCloudBackendFragment.setRetainInstance(true);
		    fragmentTransaction.add(mCloudBackendFragment, PROCESSING_FRAGMENT_TAG);
	     }       
	     fragmentTransaction.commit();        
	 }
	
	@Override
	public void onCreateFinished() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onBroadcastMessageReceived(List<CloudEntity> message) {
		// TODO Auto-generated method stub
		
	}
}
