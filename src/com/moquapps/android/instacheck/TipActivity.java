package com.moquapps.android.instacheck;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TipActivity extends Activity {
	private SeekBar seekBar; 
	private TextView textViewProgress; 	
	Handler seekBarHandlerToChangeProgressColor;
  
 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.layout_tip_activity);
		
		Intent intent = getIntent();    
		HashMap<String, Double> pTotalMap = (HashMap<String, Double>) intent.getSerializableExtra("hashMap");
		
		display(pTotalMap);
		
	      seekBar = (SeekBar)findViewById(R.id.seekBarTipPercentage);
		   seekBar.setMax(35);
		   seekBar.setProgress(15);
		    
		   seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
	         
			   
		 	 @Override 
		 	 public void onStopTrackingTouch(SeekBar seekBar) {
				 
		 		seekBar.setSecondaryProgress(seekBar.getProgress());
			 }
			 
			 @Override 
			 public void onStartTrackingTouch(SeekBar seekBar) {
				 
			 }
			 
			 @Override 
			 public void onProgressChanged(SeekBar seekBar, int progress,
					                                boolean fromUser) {
			 
				seekBar.setProgress(progress);
				textViewProgress.setText("Tip: " + progress + "%");
			 }		 
		 });
	    
		   
	     textViewProgress = (TextView)findViewById(R.id.textViewSeekBarProgressValue);
	    	   
	} 
	
	public void display(HashMap<String, Double> hMap) {
		
		for (String name: hMap.keySet()){

            String key = name.toString();
            String value = hMap.get(name).toString();  
            System.out.println(key + " " + value);  
		} 
	}
	
	public void onClickTipActivityNextBtn(View view){
		 
    	Intent intentToStartFinalTabActivity = new Intent(TipActivity.this, FinalTab.class);
    	startActivity(intentToStartFinalTabActivity);
	}
}
