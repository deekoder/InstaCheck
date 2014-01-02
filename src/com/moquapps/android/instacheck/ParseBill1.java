
package com.moquapps.android.instacheck;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;


import com.moquapps.android.instacheck.InstaDBService;
import com.moquapps.android.instacheck.InstaDBService.AddBinder;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.Toast;

 
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
	//	private ArrayList <FoodItems> foodList;
		public Context context;
		InstaDBService mService;
		boolean mBound = false;

	 
	public ParseBill1(Context c) {
		context=c;
		Intent intent = new Intent(context, InstaDBService.class);
		context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		    
		 parseString = "";
		 totalString="0.0";
		 
		 
		  	
	}
	  
	public void kickoff() {
		Intent intent = new Intent();
        //intent.setClass(context, SplitActivity.class);
		intent.setClass(context,ToDoList.class);
		//intent.setClass(context,SplitActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
	}
	public void print(Context c, String s) {
		//foodList = new ArrayList<FoodItems>();
		context=c;
		parseString = s;

		System.out.println("\n*********Assembled String********\n");
		System.out.println(s);
		
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
	
	//iteration 1 : parse 
	public void extractLineItems(String s) {
	
		String delim = "\n";
		String output="",temp="";
		String tmp="";
		FoodItem i = new FoodItem();
		StringBuilder stringBuilder;
		
		StringTokenizer tok = new StringTokenizer(s,delim,true);
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			//if(token.contains(" ")) {
				Log.v("Token per line ::: ",token);
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
					if(isNumeric(i._orderCount)) {
						Log.v("Order Count : ", "OC - "+i._orderCount);
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
					
					Log.v("Oder Dish", "is - "+i._orderName);
					
					 
					//if(toks.hasMoreTokens()) {
					i._orderPrice = tmp;
						if(isNumeric(i._orderPrice) == true) {
							Log.v("Order Price","is - "+i._orderPrice);
						}
					if(i._orderName.length() > 1)	
						extractItems(i._orderPrice, i._orderName, i._orderCount); //, "Side Bar Grill", "2", 1);
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
				Log.v("Tax ::: ",token);
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
		System.out.printf("Extracted total %s is",output);
		
		try {
			if(output.length() > 1) { 
				Log.v("Output:"+output+"***","trial");
				total = Double.parseDouble(output);
				System.out.printf("\n Total is: %2f",total);
				 
			}
		}  catch (NumberFormatException nfe) {
            Log.v("Number Error","Input must be a number.");
         }
		 
	}
	
	public void extractSubTotal(String s) {
		String delim = "\n";
		String output="",temp="";
		StringTokenizer tok = new StringTokenizer(s,delim,true);
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if(token.contains("Sub Tot")) {
				Log.v("Sub Total ::: ",token);
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
		System.out.printf("Extracted total %s is",output);
		
		try {
			if(output.length() > 1) { 
				Log.v("Output:"+output+"***","trial");
				total = Double.parseDouble(output);
				System.out.printf("\n Total is: %2f",total);
				 
			}
		}  catch (NumberFormatException nfe) {
            Log.v("Number Error","Input must be a number.");
         }
		 
	}
	public void extractTotal(String s) {
		String delim = "\n";
		String output="",temp="";
		StringTokenizer tok = new StringTokenizer(s,delim,true);
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if(token.contains("Total")) {
				Log.v("Total found",token);
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
		System.out.printf("Extracted total %s is",output);
		
		try {
			if(output.length() > 1) { 
				Log.v("Output:"+output+"***","trial");
				total = Double.parseDouble(output);
				System.out.printf("\n Total is: %2f",total);
				 
			}
		}  catch (NumberFormatException nfe) {
            Log.v("Number Error","Input must be a number.");
         }
		 
		 
	}
	
	public void extractItems(String orderPrice, String orderName, String orderCount)//, String totalSpent)//, String restName, String rating, int ID) 
	{
		  		
				 
	   	     	Log.v("ParseBill Mbound", "is"+mBound);
	   	     	
	   	     	if (mBound) {
	   	      
	   	     		mService.addTask(orderPrice, orderName, orderCount); //, totalSpent);//, restName, rating, ID);
	   	     	     
	   	     }
	}

	private ServiceConnection mConnection = new ServiceConnection() {

		 
	      @Override
	      public void onServiceConnected(ComponentName className,
	              IBinder service) {
	          // We've bound to LocalService, cast the IBinder and get LocalService instance
	    	  
	          AddBinder binder = (AddBinder) service;
	          mService = binder.getService();
	          Log.v("Setting ->","true");
	          mBound = true;
	      }

	      @Override
	      public void onServiceDisconnected(ComponentName arg0) {
	          mBound = false;
	      }
	  	};
	
	
	 
}