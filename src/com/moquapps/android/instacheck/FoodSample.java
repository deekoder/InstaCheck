package com.moquapps.android.instacheck;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;
/*This file excluded from build - ak - July7,2014*/
public class FoodSample implements Parcelable {
    int intValue;
    String stirngValue;
    private List<FoodItems> cpls;
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(intValue);
        dest.writeString(stirngValue);
        dest.writeTypedList(cpls);
        dest.writeList(cpls);
    }
    public static final Parcelable.Creator<FoodSample> CREATOR
                   = new Parcelable.Creator<FoodSample>() {
           public FoodSample createFromParcel(Parcel in) {
               return new FoodSample(in);
           }

      public FoodSample[] newArray(int size) {
          return new FoodSample[size];
      }
    };

    private FoodSample(Parcel in) {
        intValue = in.readInt();
        stirngValue=in.readString();
        in.readTypedList(cpls,FoodItems.CREATOR);
    }
    public FoodSample(int intValue, String stirngValue) {
        super();
        this.intValue = intValue;
        this.stirngValue = stirngValue;
    }

}