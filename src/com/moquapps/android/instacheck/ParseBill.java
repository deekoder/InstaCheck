
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
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.Toast;

 
public class ParseBill implements Parcelable {
	
		 
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
		private FoodItems f;
		private ArrayList <FoodItems> foodList;
		public Context context;
	
	 
	public ParseBill() {
		 parseString = "";
		 totalString="0.0";
		 foodList = new ArrayList<FoodItems>();
		  	
	}
	  
	public void parse() {
		
	}
	public void print(Context c, String s) {
		//foodList = new ArrayList<FoodItems>();
		context=c;
		System.out.println("\n*********Assembled String********\n");
		System.out.println(s);
		parseString = s;
		extractTotal(s);
		extractItems(s);
		
		
	}
	public void fireIntent() {
		Intent parcelIntent = new Intent();//context, SplitActivity.class);
		parcelIntent.setClass(OCRTask.context, SplitActivity.class);
		Log.v("ParseBill:FireIntent","Putting the parsel object");
		parcelIntent.putExtra("Parse", this);
		//parcelIntent.putParcelableArrayListExtra("Custom Parcel", this);
		OCRTask.context.startActivity(parcelIntent);
	}
	
	private void extractTotal(String s) {
		String delim = "\n";
		String output="",temp="";
		StringTokenizer tok = new StringTokenizer(s,delim,true);
		while (tok.hasMoreTokens()) {
			String token = tok.nextToken();
			if(token.contains("TOTAL")) {
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
		/*Intent intent = new Intent();
        intent.setClass(context, SplitActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/

		 
	}
	
	private void extractItems(String s) {
		f = new FoodItems();
		f.dishName = "Paneer Chili";
		f.orderCount = "1";
		f.orderPrice = "4.99";
		foodList.add(f);
	}
	
	public void writeToParcel(Parcel out, int flags) {
		Log.v("ParseBill","PBill writeOut");
		out.writeString(totalString);
		out.writeTypedList(foodList);
	}
	 
	private ParseBill(Parcel in) {
		Log.v("ParseBill","PBill readIn");
		totalString = in.readString();
		in.readTypedList(foodList, FoodItems.CREATOR);
	}
	public int describeContents() {
		return this.hashCode();
	}

	public static final Parcelable.Creator<ParseBill> CREATOR = new Parcelable.Creator<ParseBill>() {
		public ParseBill createFromParcel(Parcel in) {
			Log.v("Instacheck","Making the bill parcel");
			return new ParseBill(in);
		}
		
		public ParseBill[] newArray(int size) {
			return new ParseBill[size];
		}
	};
}