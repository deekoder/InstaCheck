package com.moquapps.android.instacheck;

import android.content.Context;
import android.view.animation.AnticipateOvershootInterpolator;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;


public class FoodItem {

  String _orderCount;
  String _orderName;
  String _orderPrice;
  String _rating;
  int _categ;
 // Context context;
  
  int ID;
  
 // Date _created;
  
  String _restaurantName;
  String _totalSpent;
  
  public String getRating()
  {
	  return _rating;
	  
  }
  
  public int getID() {
	  return ID;
  }
  public String getOcount() {
    return _orderCount;
  }
  
  public String getOname() {
	    return _orderName;
  }
  
  public String getOprice() {
	    return _orderPrice;
  }
  
  public String getTSpend() {
	    return _totalSpent;
  }
  
  public FoodItem() {
	  
  }
  
  public FoodItem(String orderPrice, String orderName, String orderCount)//, String RName, String rating,int id) 
  {
 	  
 	  _orderCount = orderCount;
 	  _orderName = orderName;
 	  _orderPrice = orderPrice;
 //	 wheel.findViewById(R.id.country_name);
 //	 wheel.setViewAdapter(new NumericWheelAdapter(context, 0, 9));
  //   wheel.setCyclic(true);
   //  wheel.setInterpolator(new AnticipateOvershootInterpolator());
   //  wheel.addChangingListener(changedListener);
    // wheel.addScrollingListener(scrolledListener);
     
 	//  wheel.findViewById(R.id.wheel1);
 	 // wheel.setViewAdapter(new NumericWheelAdapter(ParseBill1.context, 0, 9));
	  //wheel.setCurrentItem(0);
	  
 	 
   }
   
   
 
	 
   
     
public String getRname() {
		return _restaurantName;
	}

}