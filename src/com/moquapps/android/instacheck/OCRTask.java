
package com.moquapps.android.instacheck;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;
 

public class OCRTask extends AsyncTask<Void, Integer, String> {
  
  private ProgressDialog progress;
  static Context context;
  private Bitmap imageToProcess;
  private OCRCallback callback;
  private TessBaseAPI tesseract;
  private ParseBill1 pBill;
  int i=0;
  public static String mBill_ItemsToSendToCloud;
  
  public OCRTask(Context context, Bitmap bitmap, OCRCallback callback) {
    OCRTask.context = context;
    this.imageToProcess = bitmap.copy(Bitmap.Config.ARGB_8888, true);
    progress = new ProgressDialog(context);
    this.callback = callback;
    pBill = new ParseBill1(context);
  }
  
 
  @Override
  protected void onPreExecute() {
    FileManager manager = new FileManager(context);
    manager.writeRawToSD(FileManager.TESSERACT_PATH + "eng.traineddata",
        "tessdata/eng.traineddata");
    this.progress.setIndeterminate(true);
    this.progress.setIndeterminateDrawable(context.getResources().getDrawable(R.anim.spinner_loading));
    this.progress.setMessage("Recognizing using Tesseract, bro.");
    this.progress.setProgress(0);
    this.progress.show();
    tesseract = new TessBaseAPI();
    tesseract.setDebug(true);
    tesseract.init(FileManager.STORAGE_PATH, "eng");
    tesseract.setImage(imageToProcess);
  }
  
  @Override
  protected String doInBackground(Void... params) {
    String result = tesseract.getUTF8Text();
    mBill_ItemsToSendToCloud = result;
    //Log.v("Processed to", result);
    return result;
  }
  
  protected void onPostExecute(String finish) {
    if (progress.isShowing()) 
    		progress.dismiss();
    callback.onFinishRecognition(finish);
    tesseract.end();
    
   // Log.v("***Printing***", finish);
    i++;
   
    pBill.print(context,finish);
    Log.v("OCRTask:Post Exec","Completed Parsing");
    
    pBill.updateBill_ImagePathTableAndImageCount();      
    
    if( pBill.isParseGood(finish) == true )
    {        	
    	// proceed assuming bill is a good bill kick off to the next screen.
    	pBill.kickoff();
    }
    
    
   
 
    
  }
  
  
 

}
  	
