
package com.moquapps.android.instacheck;

import java.io.InputStream;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

 
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
 

public class MainActivity extends Activity implements OCRCallback {

	Button snapButton;
	private ImageView pic;
	Intent i;
	int cameraData = 0;
	private Bitmap bmp;
	 
	public static String checkString;	 
	public static String mEmailAddrOfThisMobile = "emailAddrOfCode@gmail.com";//ak
	public static Bitmap photo; //made photo public to access in FinalTab-ak 12/14/2014
	
	private final int REQUEST_CODE_GET_USERS_EMAIL_ADDR = 25;
	
	protected void onCreate(Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.activity_main);
		 
		InputStream is = getResources().openRawResource(R.drawable.cameraiconmd); 
		bmp = BitmapFactory.decodeStream(is);
		pic = (ImageView) findViewById(R.id.imageView1); 
		
		snapButton = (Button) findViewById(R.id.button2);
		 
		startActForResultToGetEmailAddrOfThisMobile();
		
		snapButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View view) {
		        
		    	
		    	i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(i,cameraData);
		       
		    }
		});

		
		 
}
	
/**
 * get Email address of this device.
 */
private void startActForResultToGetEmailAddrOfThisMobile(){
    try {
	   Intent intent = AccountPicker.newChooseAccountIntent(null, null,
	       new String[] {GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE},false,null,null,null,null);
	   startActivityForResult(intent, REQUEST_CODE_GET_USERS_EMAIL_ADDR);
	} catch (ActivityNotFoundException e) {	        
	  	 e.printStackTrace();
	}    	    	    				
}	
	
protected void onActivityResult(int requestCode, int resultCode, Intent data)
{
	super.onActivityResult(requestCode, resultCode, data);
	 
	if(resultCode == RESULT_OK)
		
	   if (requestCode == REQUEST_CODE_GET_USERS_EMAIL_ADDR)
		  mEmailAddrOfThisMobile = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
	   else
		   
	try {
	     //TODO: We need to recyle unused bitmaps
	     if (bmp != null) {
	          bmp.recycle();
	     }
	     //Bitmap photo = (Bitmap) data.getExtras().get("data"); old pre-existing line
	     //photo = (Bitmap) data.getExtras().get("data"); //get pic from camera
	     photo = BitmapFactory.decodeResource(getResources(), R.drawable.bill12);//Good-bill
	     //Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.bill2);//Fatal-Signal-11
	     //Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.bill15);//BAD-Infinite-loop
	     //Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.bill14);//BAD-Infinite-Loop
	     //Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.bill10);//Fatal-Signal 11
	     
         pic.setImageBitmap(photo);
         new OCRTask(this, photo, this).execute();
         Log.v("Finally","Control here");
	    
	} catch (Exception e) {
	        e.printStackTrace();
	} 		
  }

@Override
public void onFinishRecognition(String recognizedText) {

   
	
	}

}
