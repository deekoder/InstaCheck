
package com.moquapps.android.instacheck;

import java.io.IOException;
import java.io.InputStream;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.leptonica.android.Pixa;
import com.googlecode.tesseract.android.ResultIterator;
import com.googlecode.tesseract.android.TessBaseAPI;
import com.googlecode.tesseract.android.TessBaseAPI.PageIteratorLevel;

public class MainActivity extends Activity implements OCRCallback {

	Button snapButton;
	private ImageView pic;
	Intent i;
	int cameraData = 0;
	private Bitmap bmp;
	private TextView noteView;
	public static String checkString;
	//ParseBill pBill; 

	protected void onCreate(Bundle b)
	{
		super.onCreate(b);
		setContentView(R.layout.activity_main);
		//pBill = new ParseBill();
		InputStream is = getResources().openRawResource(R.drawable.cameraiconmd); 
		bmp = BitmapFactory.decodeStream(is);
		pic = (ImageView) findViewById(R.id.imageView1); 
		
		snapButton = (Button) findViewById(R.id.button2);
		 
		 
		
		snapButton.setOnClickListener(new OnClickListener() {
		    public void onClick(View view) {
		        // Start new activity
		    	
		    	i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(i,cameraData);
		       
		    }
		});

		
		 
}
	
	
	

 
protected void onActivityResult(int requestCode, int resultCode, Intent data)
{
	super.onActivityResult(requestCode, resultCode, data);
	InputStream stream = null;
	if(resultCode == RESULT_OK)
	try {
	        // We need to recyle unused bitmaps
	     if (bmp != null) {
	          bmp.recycle();
	     }
	     //Bitmap photo = (Bitmap) data.getExtras().get("data"); 
	     Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.bill12);
	     int mPhotoWidth = photo.getWidth();
	     int mPhotoHeight = photo.getHeight();
         pic.setImageBitmap(photo);
         new OCRTask(this, photo, this).execute();
         Log.v("Finally","Control here");
	    // stream = getContentResolver().openInputStream(data.getData());
	     //bmp = BitmapFactory.decodeStream(stream);
	    // pic.setImageBitmap(bmp);
	} catch (Exception e) {
	        e.printStackTrace();
	} 		
  }

@Override
public void onFinishRecognition(String recognizedText) {

 // noteView.setText(noteView.getText() + " " + recognizedText);
	//checkString.append(" "+recognizedText);
	//Log.v("Output",recognizedText);
	//pBill.assemble(recognizedText);
	
	}

}
