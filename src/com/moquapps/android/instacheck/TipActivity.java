package com.moquapps.android.instacheck;

import java.util.HashMap;

import com.google.cloud.backend.core.Consts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class TipActivity extends Activity {
	private SeekBar seekBar; 
	private TextView textViewProgress; 	
	Handler seekBarHandlerToChangeProgressColor;
	HashMap<String, Double> pTotalMap;
	
	public int mTipPercentSelected = 15; //default value
	
    //Radio buttons selection for Tip and Tax split
	public final int TIP_EQUAL_SPLIT_SELECTED = 1;
	public final int TIP_PROPORTIONAL_SPLIT_SELECTED = 2;
	public final int TAX_EQUAL_SPLIT_SELECTED = 3;
	public final int TAX_PROPORTIONAL_SPLIT_SELECTED = 4;	
	
	public int mTipSplitSelection = TIP_PROPORTIONAL_SPLIT_SELECTED;//default
	public int mTaxSplitSelection = TAX_PROPORTIONAL_SPLIT_SELECTED;//default
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.layout_tip_activity);
		
		Intent intent = getIntent();    
		//HashMap<String, Double> pTotalMap = (HashMap<String, Double>) intent.getSerializableExtra("hashMap");
		pTotalMap = (HashMap<String, Double>) intent.getSerializableExtra("hashMap");
		
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
			 public void onProgressChanged(SeekBar seekBar, int tipPercentParameter,
					                                boolean fromUser) {
			 
				seekBar.setProgress(tipPercentParameter);
				textViewProgress.setText("Tip: " + tipPercentParameter + "%");
				mTipPercentSelected = tipPercentParameter;
			 }		 
		 });
	    
		   
	     textViewProgress = (TextView)findViewById(R.id.textViewSeekBarProgressValue);
	    	   
	} 
	
	public void display(HashMap<String, Double> hMap) {
		
		for (String name: hMap.keySet()){

            String key = name.toString();
            String value = hMap.get(name).toString();  
            //System.out.println(key + " " + value);
            Log.v(Consts.TAG_AK, "TipActivity:display():key value = " + key + " " + value);
		} 
	}
	
	//"next" button handler
	public void onClickTipActivityNextBtn(View view){
		//start final tab activity; send hashMap pTotalMap with the intent
    	Intent intentToStartFinalTabActivity = new Intent(TipActivity.this, FinalTab.class);
    	intentToStartFinalTabActivity.putExtra("hashMap", pTotalMap);
    	startActivity(intentToStartFinalTabActivity);
    	//assign tip=split radio button selection 
    	RadioButton radioButtonTipPropSplit = (RadioButton)findViewById(R.id.radioButtonTipPropSplit);
    	if (radioButtonTipPropSplit.isChecked()){
    	   Log.v(Consts.TAG_AK, "TipActivity:onClickNexBtn():TipSplitPropotional selected");
    	   mTipSplitSelection = TIP_PROPORTIONAL_SPLIT_SELECTED;
    	}
    	else{
    		Log.v(Consts.TAG_AK, "TipActivity:onClickNexBtn():TipSplitEqual selected");
    		mTipSplitSelection = TIP_EQUAL_SPLIT_SELECTED;
    	}
    	//assign tax-split radio button selection 
    	RadioButton radioButtonTaxPropSplit = (RadioButton)findViewById(R.id.radioButtonTaxPropSplit);
    	if (radioButtonTaxPropSplit.isChecked()){
    	   Log.v(Consts.TAG_AK, "TipActivity:onClickNexBtn():TaxSplitPropotional selected");
    	   mTaxSplitSelection = TAX_PROPORTIONAL_SPLIT_SELECTED;
    	}
    	else{
    		Log.v(Consts.TAG_AK, "TipActivity:onClickNexBtn():TaxSplitEqual selected");
    		mTaxSplitSelection = TAX_EQUAL_SPLIT_SELECTED;
    	}
	}
}
