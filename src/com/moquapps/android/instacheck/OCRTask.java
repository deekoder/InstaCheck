
package com.moquapps.android.instacheck;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
  
  public OCRTask(Context context, Bitmap bitmap, OCRCallback callback) {
    this.context = context;
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
    //pBill.extractItems("4.99", "Paneer Chilli", "2", "34.99", "Side Bar Grill", "3", 1);
    //pBill.extractItems("2.99", "Red Lobsters", "1", "34.99", "Side Bar Grill", "2", 1);
    pBill.extractTotal(finish);
    pBill.extractTax(finish);
    pBill.extractSubTotal(finish);
    pBill.extractLineItems(finish);
    pBill.kickoff();
   // Intent parcelIntent = new Intent();
    //context, SplitActivity.class);
	//parcelIntent.setClass(context, SplitActivity.class);
	//Log.v("OCRTask","Putting the parsel object");
	//parcelIntent.putExtra("Parse", pBill);
	//parcelIntent.putParcelableArrayListExtra("Custom Parcel", this);
	//context.startActivity(parcelIntent);
    
    //ParseBill.context = context;
   
    //ParseBill.verify(context,finish); 
   
/*	 
    pBill.extractTotal(finish);
	pBill.extractItems("4.99", "Paneer Chilli", "2", "34.99", "Side Bar Grill", "3", 1);
	pBill.extractItems("3.99", "Malai Kofta", "1", "34.99", "Side Bar Grill", "0", 2);
	pBill.extractItems("2.99", "Samosa Chaat", "1", "34.99", "Side Bar Grill", "2", 3);
	pBill.extractItems("1.99", "Dosa Sambhar", "1", "34.99", "Side Bar Grill", "1", 4);
	pBill.extractItems("0.99", "Gulab Jamun", "1", "34.99", "Side Bar Grill", "5", 5);
	*/
    
  }
  
  
 

}
  	
