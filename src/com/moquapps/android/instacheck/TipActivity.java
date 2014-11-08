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
	public final int TIP_PERCENT_MIN = 0;
	public final int TIP_PERCENT_MAX = 30;
	public final int TIP_PERCENT_DEFAULT = 15;
	
	private SeekBar seekBar; 
	private TextView textViewTipPercent,
	                 textViewTipAmount; 	
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
	
	double mSubTotalFromParseBill_temp = 32.11;//Get value from ParseBill1.java when 
	                                           //it works - ak - Oct24,14 
	Double mTipAmount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.layout_tip_activity);
	    textViewTipPercent = (TextView)findViewById(R.id.textViewSeekBarProgressValue);
	    textViewTipAmount = (TextView)findViewById(R.id.textViewTipAmount); 
		Intent intent = getIntent();    
		//HashMap<String, Double> pTotalMap = (HashMap<String, Double>) intent.getSerializableExtra("hashMap");
		pTotalMap = (HashMap<String, Double>) intent.getSerializableExtra("hashMap");
		
		display(pTotalMap);
		
	    seekBar = (SeekBar)findViewById(R.id.seekBarTipPercentage);
		seekBar.setMax(TIP_PERCENT_MAX);
		seekBar.setProgress(TIP_PERCENT_DEFAULT);
		//Display initial Tip Percentage
		textViewTipPercent.setText(" " + TIP_PERCENT_DEFAULT + "%");
		//display initial $ default Tip Amount for default 15% Tip; round to 2 decimals
		mTipAmount = (double)Math.round(mSubTotalFromParseBill_temp *
				                                       TIP_PERCENT_DEFAULT);
		mTipAmount /= 100;
		textViewTipAmount.setText(" $" + mTipAmount.toString()+ ")");
		
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
	         
			   
		 	 @Override 
		 	 public void onStopTrackingTouch(SeekBar seekBar) {
				 
		 		seekBar.setSecondaryProgress(seekBar.getProgress());
			 }
			 
			 @Override 
			 public void onStartTrackingTouch(SeekBar seekBar) {
				 
			 }
			 
			 @Override 
			 public void onProgressChanged(SeekBar seekBar, int progressTipPercent,
					                                boolean fromUser) {
			 
				seekBar.setProgress(progressTipPercent);
				textViewTipPercent.setText(" " + progressTipPercent + "%");
				mTipPercentSelected = progressTipPercent;
				//disp Tip Amount; round to 2 decimals
				mTipAmount = (double)Math.round(mSubTotalFromParseBill_temp *
						                                     progressTipPercent);
				mTipAmount /= 100;
				String mTipAmountStr = String.format(" $" + "%.2f", mTipAmount);					
				textViewTipAmount.setText(mTipAmountStr + ")");								
			 }		 
		 });
	    
		   
	
	    	   
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
