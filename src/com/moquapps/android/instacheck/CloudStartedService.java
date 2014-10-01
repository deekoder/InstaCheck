package com.moquapps.android.instacheck;

import java.io.IOException;

import com.google.cloud.backend.core.CloudCallbackHandler;
import com.google.cloud.backend.core.CloudEntity;
import com.google.cloud.backend.core.Consts;

import android.app.FragmentManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class CloudStartedService extends Service {
	int mServiceStartCount = 0;
	int mServiceDestroyCount = 0;	
	private static final String PROCESSING_FRAGMENT_TAG = "BACKEND_FRAGMENT";
    private FragmentManager mFragmentManager;
    
	public CloudStartedService() {//Auto-generated
	}

	@Override
	public void onCreate() {				
		super.onCreate();
	}
	
	@Override//android call this method when client call startService()
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		//Log.v(Consts.TAG_AK, "SvcToSendToCloud:onStartCommand(): Service_Start_Count = " + ++mServiceStartCount);
		
        //create a CloudEntity with the new post on the Cloud of a new-CloudEntity
        //CloudEntity newPost = new CloudEntity("Guestbook");//ak- new _kindName "instaCheck"
        //CloudEntity newPost = new CloudEntity("instaCheck");//Add email-param tobe used by CloudEntity constructor
    	CloudEntity newCloudEntity = new CloudEntity("instaCheck", MainActivity.mEmailAddrOfThisMobile);
    	
        //ak-send items-strings to CLOUD; use items from MainActivity instead of calling getAllBill_ItemsIn_a_string()
        //newCloudEntity.put("message", "using-StartedSERVICE_via_MainActivity: "+getAllBill_ItemsIn_a_string());
    	newCloudEntity.put("message", OCRTask.mBill_ItemsToSendToCloud);//"message" key do NOT change; used in Cloud Backend - ak
        		        		        
        //create a response handler that will receive the result or an error
        CloudCallbackHandler<CloudEntity> handler = new CloudCallbackHandler<CloudEntity>() {
            @Override
            public void onComplete(final CloudEntity result){//Cloud-Entity posted on the cloud.
                //ak-Do not display on device as before - do nothing here
            }
            //----------
            @Override
            public void onError(final IOException ioe) {
                handleEndpointException(ioe);
            }
        };
        //INSERT this new user-entered-text to Cloud using newCloudEntity - ak
        //Log.v(Consts.TAG_AK, "Started_SERVICE_via_MainActivityToUseCloud:onClick..using_SERVICE()");
        
        //mCloudBackendFragment.getCloudBackend().insert(newCloudEntity, handler);
        FinalTab.mCloudBackendFragment.getCloudBackend().insert(newCloudEntity, handler);		
		return super.onStartCommand(intent, flags, startId);
	}	
	
	private void handleEndpointException(IOException ioe) {
	   ioe.getStackTrace();
	}
	
	@Override//Service life cycle method
	public void onDestroy() {
		super.onDestroy();
		Log.i(Consts.TAG_AK, "SvcToSendToCloud:onDestroy(): Service_Stopped_Count = " + ++mServiceDestroyCount);		
	}
	
	@Override//Auto-generated
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
