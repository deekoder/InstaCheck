package com.moquapps.android.instacheck;
/**
 * This class does following:
 * (1) Display final tab to user
 * (2) Sends bill items, tip and final tab info to 'instaCheck' CLOUD.
 *     from onResume() to test BUT change its location when finalTab coding is done. 
 * (3) It inits Cloud (hense implement OnListener) to perform above step
 */
import java.util.List;

import com.google.cloud.backend.core.CloudBackendFragment;
import com.google.cloud.backend.core.CloudBackendFragment.OnListener;
import com.google.cloud.backend.core.CloudEntity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FinalTab extends Activity implements OnListener {

  //Cloud variables
   private static final String PROCESSING_FRAGMENT_TAG = "BACKEND_FRAGMENT"; 
   private FragmentManager mFragmentManager;
   public static CloudBackendFragment mCloudBackendFragment;	
			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_final_tab);
		
        mFragmentManager = getFragmentManager();//CloudBackend-requirement.Note:FragmentManager NOT allowed in Service
        initiateCloudBackendFragments();//CloudBackend-requirement
	}

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
