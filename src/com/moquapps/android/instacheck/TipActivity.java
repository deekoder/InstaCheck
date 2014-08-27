package com.moquapps.android.instacheck;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.os.Handler;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TipActivity extends Activity {
	private SeekBar seekBar;//declare SeekBar object variable
	private TextView textViewProgress, textViewAction;//declare text label obj	
	Handler seekBarHandlerToChangeProgressColor;
	final String TAG = "iCheck";//TAG for instaCheck
	//-------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.layout_tip_activity);
	      seekBar = (SeekBar)findViewById(R.id.seekBarTipPercentage);
		   seekBar.setMax(35);//Must be set before annonymous class is set ELSE CRASH!!
		   seekBar.setProgress(15);
		   //**********************************************
		   //define new annonymous inner class for OnSeekBarChangeListener
		   seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
	         //-------------------------	
		 	 @Override//must-have auto-method for this class
		 	 public void onStopTrackingTouch(SeekBar seekBar) {
				//TODO Auto-generated method stub
				//Log.i(TAG, "OnStopTrackingTouch");
		 		seekBar.setSecondaryProgress(seekBar.getProgress());
			 }
			 //--------------------------
			 @Override//must-have method for this class
			 public void onStartTrackingTouch(SeekBar seekBar) {
				//TODO Auto-generated method stub
				//Log.i(TAG, "OnStartTrackingTouch");
			 }
			 //--------------------------
			 @Override//must-have method for this class
			 public void onProgressChanged(SeekBar seekBar, int progress,
					                                boolean fromUser) {
				//TODO Auto-generated method stub
				//Change progress text label with current eekbar value
				seekBar.setProgress(progress);
				textViewProgress.setText("Tip: " + progress + "%");
			 }		 
		 });
	     //**********************************	   
	     textViewProgress = (TextView)findViewById(R.id.textViewSeekBarProgressValue);
	     textViewAction = (TextView)findViewById(R.id.textViewSeekBarAction);	   
	}//onCreate()
    //----------------------------------------------------
	public void onClickTipActivityNextBtn(View view){
		Log.v(TAG,"TipActivity:onClickTipActivityNextBtn() clicked." );
		//Start TipActivity - ak Aug 27,2014
    	Intent intentToStartFinalTabActivity = new Intent(TipActivity.this, FinalTab.class);
    	startActivity(intentToStartFinalTabActivity);
	}
}
