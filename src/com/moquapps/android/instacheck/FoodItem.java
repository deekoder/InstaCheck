package com.moquapps.android.instacheck;

import java.util.ArrayList;
 
public class FoodItem {

  ArrayList<String> personList;	
  String _orderCount;
  String _orderName;
  String _orderPrice;
  String _rating;
  int _categ;
  String selectedPerson;
 
  int ID;
   
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
 	  
 	  personList = new ArrayList<String>();
 	  personList.add("P1");
 	  personList.add("P2");
 	  personList.add("P3");
 	  personList.add("P4");
 	  personList.add("P5");
	  personList.add("P6");
	  personList.add("P7");
	  personList.add("P8");
	  personList.add("P9");
 	  personList.add("P10");
 	  personList.add("P11");
 	  personList.add("P12");
 	  personList.add("P13");
	  personList.add("P14");
	  personList.add("P15");
	  personList.add("P16");

	  
 	 
   }
  
public String getRname() {
		return _restaurantName;
	}

}