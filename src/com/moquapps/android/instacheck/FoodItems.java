
package com.moquapps.android.instacheck;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View.MeasureSpec;
import android.widget.Toast;



public class FoodItems implements Parcelable {
	//public for now. Make Private later on.
	public String orderCount;
	public String dishName;
	public String orderPrice;
	
	public void writeToParcel(Parcel out, int flags) {
		Log.v("FoodItems","Food items writeOut");
		out.writeString(orderCount);
		out.writeString(dishName);
		out.writeString(orderPrice);
	}
	
	public FoodItems() {
		orderCount = "";
		dishName = "";
		orderPrice = "";
	}
	 
	private FoodItems(Parcel in) {
		Log.v("FoodItems","Food items readIn");
		orderCount = in.readString();
		dishName = in.readString();
		orderPrice = in.readString();
	}
	public int describeContents() {
		return this.hashCode();
	}

	public static final Parcelable.Creator<FoodItems> CREATOR = new Parcelable.Creator<FoodItems>() {
		public FoodItems createFromParcel(Parcel in) {
			Log.v("Instacheck","Making the Food Items");
			return new FoodItems(in);
		}
		
		public FoodItems[] newArray(int size) {
			return new FoodItems[size];
		}
	};
}