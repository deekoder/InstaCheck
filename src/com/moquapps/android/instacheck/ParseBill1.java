 
package com.moquapps.android.instacheck;

import java.util.StringTokenizer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.moquapps.android.instacheck.InstaDBService.AddBinder;
//import android.content.Context;
 
public class ParseBill1   {		 
		private static String parseString;
		
		private String restaurantName;
		private String restaurantAddress;
		private String restaurantPhone;
		private String checkNo;
		private String serverName;
		private String tableNo;
		private String date;
		private String time;
		private String clientCount; 
		private String totalString;
		public static double total=0.0;
		private FoodItem f;
	 
		public Context context;
	 
		InstaDBService mInstaDBService;
		boolean mBound = false;
		final String TAG = "iCheck"; 
	 
	public ParseBill1(Context c) {
		context=c; 
		Intent intent = new Intent(context, InstaDBService.class);
		context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		    
		 parseString = "";
		 totalString="0.0";		 		 		  
	}
	  
	public void kickoff() {
		Intent intent = new Intent();
       
		intent.setClass(context,ToDoList.class);
	 
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
	}
	 
	public void print(Context c, String s) {		
		 
		//putting this here is a hack. Please remove.
	    mInstaDBService.addBill_ImageFilePathName("storage.emulated.0.DCIM.Camera"); 
        mInstaDBService.updateCount_numOfRecords_in_Bill_IFP_Table(); 
         
       
		context = c;
		parseString = s;
		 
		Log.v(TAG,"ParseBill:print():s = "+s);
		Log.v(TAG,"ParseBill:print():-- finished printing long-String-s above");
	}
	 
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	 
	public double retNumeric(String str) {
		double d=0;
		try  
		  {  
		    d = Double.parseDouble(str);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return 0;  
		  }  
		  return d;
	}
	
	public boolean isParseGood(String s) {
	    
		 extractTotal(s);
		 extractTax(s);
		 extractSubTotal(s);
		 extractLineItems(s);
		 return true; 
		
	}
	 
	public void extractLineItems(String s) {
		String delim = "\n";
		String output="",temp="";
		String tmp="";
		FoodItem i = new FoodItem();
		StringBuilder stringBuilder;
		
		StringTokenizer tok = new StringTokenizer(s,delim,true);
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			 
			StringTokenizer toks = new StringTokenizer(token," ");
			while (toks.hasMoreTokens()) {					
				if(token.contains("Sub Tot")) break;
				if(token.contains("Subtot")) break;
					if(token.contains("total")) break;
					if(token.contains("SubTot")) break;
					if(token.contains("Tax")) break;
					if(token.contains("Total")) break;
					if(token.contains("Due")) break;
					
				i._orderCount = toks.nextToken();
				if (isNumeric(i._orderCount)) {
					 
				}
				stringBuilder = new StringBuilder();
					
				while (toks.hasMoreTokens()) {
						if(isNumeric(tmp = toks.nextToken()) != true) 
						{						
							stringBuilder.append(tmp);
							stringBuilder.append(" ");							
						}
				} 
				i._orderName = stringBuilder.toString();					
				 
				i._orderPrice = tmp;
				if (isNumeric(i._orderPrice) == true) {
				    
				}
				if(i._orderName.length() > 1)	
					extractItems(i._orderPrice, i._orderName, i._orderCount); 
				tmp="";					
			} 
		}
    }
	 
	
	public void extractTax(String s) {
		String delim = "\n";
		String output="",temp="";
		StringTokenizer tok = new StringTokenizer(s,delim,true);
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if(token.contains("Tax")) {
				 
				StringTokenizer toks = new StringTokenizer(token,":");
				while (toks.hasMoreTokens()) {
					temp = toks.nextToken();
				}
				String outputs = temp.replaceAll("\\s+",""); 
				totalString = outputs;
				output = outputs.replaceAll("\\n","");
				break;
			}
		}
		 
		try {
			if(output.length() > 1) { 
				 
				total = Double.parseDouble(output);
				System.out.printf("\n Total is: %2f",total);
				 
			}
		}  catch (NumberFormatException nfe) {
            
			Log.v(TAG,"ParseBill:extractTax():Num Error-Input must be Num");
        }		 
	}
	
	private boolean isWord()
	{
		
		 
		return true;
	}
	
	private boolean isPrice() {
		
		return true;
	}
	 
	
	public void extractSubTotal(String s) {
		String delim = "\n";
		String output="",temp="";
		StringTokenizer tok = new StringTokenizer(s,delim,true);
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if (token.contains("Sub Tot")) {
			   
			   StringTokenizer toks = new StringTokenizer(token,":");
			   while (toks.hasMoreTokens()) {
					temp = toks.nextToken();
				}
				String outputs = temp.replaceAll("\\s+",""); 
				totalString = outputs;
				output = outputs.replaceAll("\\n","");
				break;
			}
		}
	 
		try {
			if(output.length() > 1) { 
			 
				total = Double.parseDouble(output);
				System.out.printf("\n Total is: %2f",total);
			 			 
			}
		}  catch (NumberFormatException nfe) {
            
        }		 
	}
	 
	
	public void extractTotal(String s) {
		String delim = "\n";
		String output="",temp="";
		StringTokenizer tok = new StringTokenizer(s,delim,true);
		boolean noTotalFoundInToken = false;
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if(token.contains("Total")) {
				 
				StringTokenizer toks = new StringTokenizer(token,":");
				while (toks.hasMoreTokens()) {
					temp = toks.nextToken();
				}
				String outputs = temp.replaceAll("\\s+",""); 
				totalString = outputs;
				output = outputs.replaceAll("\\n","");
				break;
			}
			else{  
			   if (noTotalFoundInToken == false)
			      
			   noTotalFoundInToken = true;
			}
		} 
		try {
			if(output.length() > 1) { 
				 
				total = Double.parseDouble(output);
				 
			}
		}  catch (NumberFormatException nfe) {
            
		   Log.v(TAG,"ParseBill:extractTotal():Error:Input must be a number ");
        }		 		 
	}
	 
	
	public void extractItems(String orderPrice, String orderName, String orderCount) 
	{		  						 
	    
	   	if (mBound) {      
	   	   mInstaDBService.addTask(orderPrice, orderName, orderCount);
	   	  
	   	}
	}
    
	
	private ServiceConnection mConnection = new ServiceConnection() {
		
	      @Override
	      public void onServiceConnected(ComponentName className,
	              IBinder service) {
	              	  
	          AddBinder binder = (AddBinder) service;
	          mInstaDBService = binder.getService();
	         
	          Log.v(TAG,"ParseBill:ServiceConnection:0nServiceConnected()");
	          mBound = true;
	      }
           
	      @Override
	      public void onServiceDisconnected(ComponentName arg0) {
	          mBound = false;
	      }
	      
	  	};
	   
}