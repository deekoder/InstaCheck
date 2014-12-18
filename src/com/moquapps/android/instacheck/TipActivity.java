package com.moquapps.android.instacheck;

import java.util.HashMap;
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
	
	public double mTipPercentSelected = 15; //default value
	
    //Radio buttons selection for Tip and Tax split
	public final int TIP_EQUAL_SPLIT_SELECTED = 1;
	public final int TIP_PROPORTIONAL_SPLIT_SELECTED = 2;
	public final int TAX_EQUAL_SPLIT_SELECTED = 3;
	public final int TAX_PROPORTIONAL_SPLIT_SELECTED = 4;	
	
	public int mTipSplitSelection = TIP_PROPORTIONAL_SPLIT_SELECTED;//default
	public int mTaxSplitSelection = TAX_PROPORTIONAL_SPLIT_SELECTED;//default
	
	double mSubTotalFromParseBill_temp = ParseBill1.subTotal;//Get value from ParseBill1.java when 
	                                           //it works - ak - Oct24,14 
	double mTaxFromParseBill = ParseBill1.tax;
	
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
		
		String key;
		String value;
		for (String name: hMap.keySet()){

             key = name.toString();
             value = hMap.get(name).toString();  
            //System.out.println(key + " " + value);
            Log.v(ParseBill1.TAG, "TipActivity:display():key value = " + key + " " + value);
		} 
	}
	
	public void computeTipTax(){
		
		String key;
		double value, tip, tax;
		double mTipPct;
		mTipPct = mTipPercentSelected/100;
		 
		if(mTipSplitSelection == TIP_PROPORTIONAL_SPLIT_SELECTED) {
			//Log.v("Proptional", "Tip");
			for (String name: pTotalMap.keySet()){

	             key = name.toString();
	             value = pTotalMap.get(key);
	             tip = mTipPct * value;
	            // Log.v("Value is - ",String.valueOf(value));
	            // Log.v(key,String.valueOf(mTipPct) + " : "+String.valueOf(tip));
	             pTotalMap.put(key, value+tip);
	            
	            
			}
			
			
		}
		if(mTipSplitSelection == TIP_EQUAL_SPLIT_SELECTED) {
			
			//Log.v("Equal", "Tip");
			tip = (mTipPct * mSubTotalFromParseBill_temp)/pTotalMap.size();
			
			//Log.v("Tip now has",Double.toString(tip));
			for (String name: pTotalMap.keySet()){

	             key = name.toString();
	             value = pTotalMap.get(key);
	             //Log.v(key," : "+String.valueOf(tip));
	            
	             pTotalMap.put(key, value+tip);
	            
	            
			}
			
		}
		
		if(mTaxSplitSelection == TAX_PROPORTIONAL_SPLIT_SELECTED) {
			//Log.v("Proptional", "Tax");
			for (String name: pTotalMap.keySet()){

	             key = name.toString();
	             value = pTotalMap.get(name);
	             //Log.v("Value is :",String.valueOf(value));
	             tax =  ( (value/mSubTotalFromParseBill_temp)) * mTaxFromParseBill;
	             //Log.v(key," : "+String.valueOf(tax));
	             pTotalMap.put(name, value+tax);
	            
	            
			}
			
			
			
		}
		if(mTaxSplitSelection == TAX_EQUAL_SPLIT_SELECTED) {
			
			tax =   mTaxFromParseBill/pTotalMap.size();
			//Log.v("Equal", "Tax");
			for (String name: pTotalMap.keySet()){

	             key = name.toString();
	             value = pTotalMap.get(name);
	             //Log.v(key," : "+String.valueOf(tax));
	             pTotalMap.put(name, value+tax);
	            
	            
			}
		}
		
		
	}
	
	//"next" button handler
	public void onClickTipActivityNextBtn(View view){
    	//assign tip=split radio button selection 
    	RadioButton radioButtonTipPropSplit = (RadioButton)findViewById(R.id.radioButtonTipPropSplit);
    	if (radioButtonTipPropSplit.isChecked()){
    	   Log.v(ParseBill1.TAG, "TipActivity:onClickNexBtn():TipSplitPropotional selected");
    	   mTipSplitSelection = TIP_PROPORTIONAL_SPLIT_SELECTED;
    	}
    	else{
    		Log.v(ParseBill1.TAG, "TipActivity:onClickNexBtn():TipSplitEqual selected");
    		mTipSplitSelection = TIP_EQUAL_SPLIT_SELECTED;
    	}
    	//assign tax-split radio button selection 
    	RadioButton radioButtonTaxPropSplit = (RadioButton)findViewById(R.id.radioButtonTaxPropSplit);
    	if (radioButtonTaxPropSplit.isChecked()){
    	   Log.v(ParseBill1.TAG, "TipActivity:onClickNexBtn():TaxSplitPropotional selected");
    	   mTaxSplitSelection = TAX_PROPORTIONAL_SPLIT_SELECTED;
    	}
    	else{
    		Log.v(ParseBill1.TAG, "TipActivity:onClickNexBtn():TaxSplitEqual selected");
    		mTaxSplitSelection = TAX_EQUAL_SPLIT_SELECTED;
    	}
    	computeTipTax();
		//start final tab activity; send hashMap pTotalMap with the intent
    	Intent intentToStartFinalTabActivity = new Intent(TipActivity.this, FinalTab.class);
    	intentToStartFinalTabActivity.putExtra("hashMap", pTotalMap);
    	startActivity(intentToStartFinalTabActivity);
	}
}
