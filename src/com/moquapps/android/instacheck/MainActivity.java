
package com.moquapps.android.instacheck;

import java.io.InputStream;

import android.app.Activity;
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
	 

	protected void onCreate(Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.activity_main);
		 
		InputStream is = getResources().openRawResource(R.drawable.cameraiconmd); 
		bmp = BitmapFactory.decodeStream(is);
		pic = (ImageView) findViewById(R.id.imageView1); 
		
		snapButton = (Button) findViewById(R.id.button2);
		 
		 
		
		snapButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View view) {
		        
		    	
		    	i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(i,cameraData);
		       
		    }
		});

		
		 
}
	
	
	

 
protected void onActivityResult(int requestCode, int resultCode, Intent data)
{
	super.onActivityResult(requestCode, resultCode, data);
	 
	if(resultCode == RESULT_OK)
	try {
	     //TODO: We need to recyle unused bitmaps
	     if (bmp != null) {
	          bmp.recycle();
	     }
	     //Bitmap photo = (Bitmap) data.getExtras().get("data"); 
	     Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.bill12);//Good-bill
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
