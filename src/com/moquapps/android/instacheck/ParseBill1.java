
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
import android.content.ContentValues;

import com.moquapps.android.instacheck.InstaDBService;
import com.moquapps.android.instacheck.InstaDBService.AddBinder;

import android.content.ComponentName;
//import android.content.Context;
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
/** ak - July9,2014
 * This class 
 *  - Start InstaDBService in its constructor 
 *  - parses the bill string into tokens 
 *  - Adds token items into the Content Provider using 
 *    mService.addTask(orderPrice, orderName, orderCount) in extractItems() method
 *  - The print() methos is called once for each image taken by user. This method is 
 *    also used to update the new table for image_file_path and update the count of 
 *    racords in this new table. 
 */
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
		//InstaDBService mService;
		InstaDBService mInstaDBService;
		boolean mBound = false;
		final String TAG = "iCheck";//TAG for instaCheck
	//--------------------------------------------
	//constructor 
	public ParseBill1(Context c) {
		context=c; 
		Intent intent = new Intent(context, InstaDBService.class);
		context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		    
		 parseString = "";
		 totalString="0.0";		 		 		  
	}
	//----------------------------------------  
	public void kickoff() {
		Intent intent = new Intent();
        //intent.setClass(context, SplitActivity.class);
		intent.setClass(context,ToDoList.class);
		//intent.setClass(context,SplitActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
	}
	//-----------------------------------------
	public void print(Context c, String s) {		
		//store filePath_of_imageFromCamera to new table billImageTable - ak;July19,14
	    mInstaDBService.addBill_ImageFilePathName("storage.emulated.0.DCIM.Camera");//Jul 23,14	
		
		 //Query this new table for number of records
	     // That number is also the latest Bill number that go on every 
	     //record of the todoItems table. 
	     //Ref: ToDoList:updateArray() 
         mInstaDBService.updateCount_numOfRecords_in_Bill_IFP_Table();//Jul 23,14
         
        //foodList = new ArrayList<FoodItems>();  
		context = c;
		parseString = s;
		//System.out.println("\n*********Assembled String********\n");
		//System.out.println(s);
		//Log.i(TAG,"ParseBill:print()**Assembled String**");
		Log.v(TAG,"ParseBill:print():s = "+s);
		Log.v(TAG,"ParseBill:print():-- finished printing long-String-s above");
	}
	//-------------------------------------
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
	//--------------------------------------
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
	//---------------------------------------
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
		    //Log.v("Token per line ::: ",token);
			//Log.v(TAG,"ParseBill1:extractLineItems():token = " + token);
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
					//Log.v("Order Count : ", "OC - "+i._orderCount);
					//Log.v(TAG,"ParseBill:extractLineItems():OrderCount = "+i._orderCount);
				}
				stringBuilder = new StringBuilder();
					
				while (toks.hasMoreTokens()) {
						if(isNumeric(tmp = toks.nextToken()) != true) 
						{						
							stringBuilder.append(tmp);
							stringBuilder.append(" ");							
						}
				}//while	
				i._orderName = stringBuilder.toString();					
				//Log.v("Oder Dish", "is - "+i._orderName);
				//Log.v(TAG,"ParseBill:extractLineItems():OrderName = "+i._orderName);
										 
				//if(toks.hasMoreTokens()) {
				i._orderPrice = tmp;
				if (isNumeric(i._orderPrice) == true) {
				   //Log.v("Order Price","is - "+i._orderPrice);
				   //Log.v(TAG,"ParseBill:extractLineItems():OrderPrice = "+i._orderPrice);
				}
				if(i._orderName.length() > 1)	
					extractItems(i._orderPrice, i._orderName, i._orderCount);//,"Side Bar Grill", "2", 1);
				tmp="";					
			}//while
		}
    }
	//----------------------------------- 
	public void extractTax(String s) {
		String delim = "\n";
		String output="",temp="";
		StringTokenizer tok = new StringTokenizer(s,delim,true);
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if(token.contains("Tax")) {
				//Log.v("Tax ::: ",token);
				//Log.i(TAG,"ParseBill:extractTax(): Tax = " + token);
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
		//System.out.printf("Extracted total %s is",output);
		//Log.v(TAG,"ParseBill:extractTax():Extracted Total = " + output);
		try {
			if(output.length() > 1) { 
				//Log.v("Output:"+output+"***","trial");
				total = Double.parseDouble(output);
				System.out.printf("\n Total is: %2f",total);
				//Log.v(TAG,"ParseBill:extractTax():Total = " + output);
			}
		}  catch (NumberFormatException nfe) {
            //Log.v("Number Error","Input must be a number.");
			Log.v(TAG,"ParseBill:extractTax():Num Error-Input must be Num");
        }		 
	}
	//----------------------------------------
	public void extractSubTotal(String s) {
		String delim = "\n";
		String output="",temp="";
		StringTokenizer tok = new StringTokenizer(s,delim,true);
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if (token.contains("Sub Tot")) {
			   //Log.v("Sub Total ::: ",token);
			   //Log.v(TAG,"ParseBill:extractSubTotal():SubTotal = " + token);
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
		//System.out.printf("Extracted total %s is",output);
		//Log.v(TAG,"ParseBill:extractSubTotal():Extracted total = "+output);
		try {
			if(output.length() > 1) { 
				//Log.v("Output:"+output+"***","trial");
				total = Double.parseDouble(output);
				System.out.printf("\n Total is: %2f",total);
				//Log.v(TAG,"ParseBill:extractSubTotal():Total = "+total);				 
			}
		}  catch (NumberFormatException nfe) {
           //Log.v("Number Error","Input must be a number.");
           //Log.v(TAG,"ParseBill:extractSubTotal():Error:Input must be a number");
        }		 
	}
	//----------------------------------------
	public void extractTotal(String s) {
		String delim = "\n";
		String output="",temp="";
		StringTokenizer tok = new StringTokenizer(s,delim,true);
		boolean noTotalFoundInToken = false;
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if(token.contains("Total")) {
				//Log.v("Total found",token);
				//Log.v(TAG,"ParseBill:extractTotal():Total found = "+token);
				StringTokenizer toks = new StringTokenizer(token,":");
				while (toks.hasMoreTokens()) {
					temp = toks.nextToken();
				}
				String outputs = temp.replaceAll("\\s+",""); 
				totalString = outputs;
				output = outputs.replaceAll("\\n","");
				break;
			}
			else{ //added by ak - July 9,2014
			   if (noTotalFoundInToken == false)
			      //Log.v(TAG,"ParseBill:extractTotal():'Total' str NOT found in token.");
			   noTotalFoundInToken = true;
			}
		}//while
		//System.out.printf("Extracted total %s is",output);
		//Log.v(TAG,"ParseBill:extractTotal():output.length = "+output.length());
		try {
			if(output.length() > 1) { 
				//Log.v("Output:"+output+"***","trial");
				//Log.v(TAG,"ParseBill:extractTotal():Extracted total="+output);
				total = Double.parseDouble(output);
				//System.out.printf("\n Total is: %2f",total);
				//Log.v(TAG,"ParseBill:extractTotal():Total="+total); 
			}
		}  catch (NumberFormatException nfe) {
           //Log.v("Number Error","Input must be a number.");
		   Log.v(TAG,"ParseBill:extractTotal():Error:Input must be a number ");
        }		 		 
	}
	//-------------------------------------------------------
	public void extractItems(String orderPrice, String orderName, String orderCount)//, String totalSpent)//, String restName, String rating, int ID) 
	{		  						 
	   	//Log.v("ParseBill Mbound", "is"+mBound);
	   	//Log.v(TAG,"ParseBill:extractItems():Mbound="+mBound);
	   	if (mBound) {      
	   	   mInstaDBService.addTask(orderPrice, orderName, orderCount);//,totalSpent);//,restName, rating, ID);
	   	  
	   	}
	}
    //----------------------------------------------------------
	private ServiceConnection mConnection = new ServiceConnection() {
		
	      @Override
	      public void onServiceConnected(ComponentName className,
	              IBinder service) {
	          // We've bound to LocalService, cast the IBinder and get LocalService instance	    	  
	          AddBinder binder = (AddBinder) service;
	          mInstaDBService = binder.getService();
	          //Log.v("Setting ->","true");
	          Log.v(TAG,"ParseBill:ServiceConnection:0nServiceConnected()");
	          mBound = true;
	      }
          //---------------------
	      @Override
	      public void onServiceDisconnected(ComponentName arg0) {
	          mBound = false;
	      }
	      //--------------------
	  	};
	  	//-------------------
}