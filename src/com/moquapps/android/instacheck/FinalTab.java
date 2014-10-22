package com.moquapps.android.instacheck;
/**
 * This class does following:
 * (1) Display final tab to user
 * (2) Sends bill items, tip and final tab info to 'instaCheck' CLOUD.
 *     from onResume() to test BUT change its location when finalTab coding is done. 
 * (3) It inits Cloud (hense implement OnListener) to perform above step
 */
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
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class FinalTab extends Activity implements OnListener {

  //Cloud variables
   private static final String PROCESSING_FRAGMENT_TAG = "BACKEND_FRAGMENT"; 
   private FragmentManager mFragmentManager;
   public static CloudBackendFragment mCloudBackendFragment;	
   HashMap<String, Double> mHashMapOfPeoplesFinalTab;		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_final_tab);
		
		Intent intent = getIntent();    
		mHashMapOfPeoplesFinalTab = (HashMap<String, Double>) intent.getSerializableExtra("hashMap");
		
		displayHashMapEntries(mHashMapOfPeoplesFinalTab);
		
        mFragmentManager = getFragmentManager();//CloudBackend-requirement.Note:FragmentManager NOT allowed in Service
        initiateCloudBackendFragments();//CloudBackend-requirement
	}

	public void displayHashMapEntries(HashMap<String, Double> hashMap) {
		int loopCounter = 0;
		final RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayoutInner);
		
		//Lets sort the HashMap entries based on keys (P1, P2 etc)
		SortedSet<String>keys = new TreeSet<String>(hashMap.keySet());
		
		//for (String hashMapEntry: hashMap.keySet()){
		for (String key : keys){
            //String key = hashMapEntry.toString();
            //String value = hashMap.get(hashMapEntry).toString();
			String value = hashMap.get(key).toString();
            Log.v(Consts.TAG_AK, "FinalTab:dispHashMapEnt():key value = " + key + " " + value);
            
            //display key string P1, P2 etc
			RelativeLayout.LayoutParams layoutParamsForKey = new RelativeLayout.LayoutParams
					((int)LayoutParams.WRAP_CONTENT, (int)LayoutParams.WRAP_CONTENT);
			layoutParamsForKey.leftMargin= 40;
			layoutParamsForKey.topMargin = loopCounter*50;
			TextView tViewKey = new TextView(this);	
			tViewKey.setText(key);
			tViewKey.setTextSize((float) 16); 		
			tViewKey.setTextColor(Color.YELLOW);
			tViewKey.setLayoutParams(layoutParamsForKey);
			relativeLayout.addView(tViewKey);            
			
			//display '$' char
			RelativeLayout.LayoutParams layoutParamsFor$ = new RelativeLayout.LayoutParams
					((int)LayoutParams.WRAP_CONTENT, (int)LayoutParams.WRAP_CONTENT);
			layoutParamsFor$.leftMargin=130;
			layoutParamsFor$.topMargin = loopCounter*50;
			TextView tViewFor$ = new TextView(this);	
			tViewFor$.setText("$");
			tViewFor$.setTextSize((float) 16);
			tViewFor$.setTextColor(Color.YELLOW);
			tViewFor$.setLayoutParams(layoutParamsFor$);
			relativeLayout.addView(tViewFor$);						
			
			//display value string 24.75, 2.90 etc.
			RelativeLayout.LayoutParams layoutParamsForValue = new RelativeLayout.LayoutParams
					         ((int)LayoutParams.WRAP_CONTENT, (int)LayoutParams.WRAP_CONTENT);
			TextView tViewValue = new TextView(this);
			layoutParamsForValue.leftMargin= 150;
			layoutParamsForValue.topMargin = loopCounter*50;
			tViewValue.setText(value.toString());
			tViewValue.setTextSize((float)16);		
			tViewValue.setTextColor(Color.YELLOW);
			tViewValue.setLayoutParams(layoutParamsForValue);
			relativeLayout.addView(tViewValue);
			
			loopCounter++;
		}//for								 
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
	   //start Started-Service here to have it send the items w Tip and FinalTab to Cloud
	   Intent i = new Intent(FinalTab.this, CloudStartedService.class);
	   startService(i);
	}	

    /*****************************************************
     * CLOUD Backend - Init & OnListener interface methods
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
