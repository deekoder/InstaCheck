package com.moquapps.android.instacheck;

import com.parse.Parse;
import com.parse.ParseACL;//ACL=Access Control List
import com.parse.ParseUser;

import android.app.Application;
import android.util.Log;

public class ModuleToSetupParseCloud extends Application {
	@Override
	public void onCreate(){
	   super.onCreate();
	   //Log.i(ParseBill1.TAG,"ModuleToSetupParseCloud:onCreate()");
	   //*****************************
	   //Init Parse Cloud needed items
	   //*****************************
	   //Project Name on Parse: InstaCheckCloudStorage: Init with parse.com
	   // generated and our project specific APPLICATION_ID and CLIENT_KEY
	   //Parse.initialize(this, "g9IFQfi4SM5c28jByj8yZuIvmir8OhvDL6Shddy5", 
	   //                      "PLtQRiItSEEp17cKTkBZFFNlyh5Trww2clbxRzFC");
	   //New project name on Parse.com: InstaCheckParse - Jan 10,2015
	   Parse.initialize(this, "XjmunI0wcM09uDzVhB7luFuirs5ucvWZrqU4P8bD", 
                              "gizkBrHe6SnHpZXwHi7gX0XlkkL1JJdJ6taHDPfy");
	    ParseUser.enableAutomaticUser();
	    ParseACL defaultACL = new ParseACL();//ACL=Access Control List
	      
	    //For all objects to be private by default, remove this line.
	    //defaultACL.setPublicReadAccess(true);
	    
	    ParseACL.setDefaultACL(defaultACL, true);//ACL=Access Control List  
	}
	
}
