package com.moquapps.android.instacheck;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class InstaProvider extends ContentProvider {
  private static final String AUTHORITY = "com.moquapps.instacheck.provider.todo"; 
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/todo");
  public static final Uri CONTENT_URI_BILL_IFP_TABLE = Uri.parse("content://" + AUTHORITY + "/bill_ImageFilePath");
   
  @Override
  public boolean onCreate() {
    Context context = getContext();
     
    //Log.v(ParseBill1.TAG, "--InstaProvider:onCreate()");
    ToDoDatabaseHelper dbHelper = new ToDoDatabaseHelper(context,  
                                                   DATABASE_NAME,  
                                                   null, 
                                                   DATABASE_VERSION); 
    todoDB = dbHelper.getWritableDatabase();
    return (todoDB == null) ? false : true;
  }
  
  @Override
  public Cursor query(Uri uri, 
                      String[] projection, 
                      String selection, 
                      String[] selectionArgs, 
                      String sort) {
    
	 
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

    if (uri == CONTENT_URI){
       qb.setTables(TODO_TABLE);
       //Log.v(ParseBill1.TAG, "InstaProvider:query()-setTables(TODO_TABLE)");
    }
    else if (uri == CONTENT_URI_BILL_IFP_TABLE){
       qb.setTables(BILL_IFP_TABLE);
       //Log.v(ParseBill1.TAG, "InstaProvider:query()-setTables(BILL_IFP_TABLE)");
    }
    else{
       qb.setTables(TODO_TABLE);
       //Log.v(ParseBill1.TAG, "**InstaProvider:query()*NO-URI-MATCH**setTables(TODO_TABLE)");
    }
    
    //Log.v(ParseBill1.TAG, "InstaProvider:query():uriMatcher.match = " + uriMatcher.match(uri));
    //Log.v(ParseBill1.TAG, "InstaProvider:query():uri.getPathSegments()=" + uri.getPathSegments());
     
    String orderBy;
    orderBy = sort;
     
    Cursor c = qb.query(todoDB, 
               projection, 
               selection,  
               selectionArgs, 
               null,  
               null,                         
               orderBy); 
      
    c.setNotificationUri(getContext().getContentResolver(), uri);
    //Log.v(ParseBill1.TAG, "InstaProvider:query()Cusor c.Count =" + c.getCount());
    
    return c;
  }
   
  @Override
   
  public Uri insert(Uri uriParameter, ContentValues cValues) {
      
	Uri uriWithAppendedId = null;  
	int k = uriMatcher.match(uriParameter);
	switch(k){
	  case TODO:
		  
	     long rowID = todoDB.insert(TODO_TABLE, "notes", cValues);
	     if (rowID > 0){
	        uriWithAppendedId = ContentUris.withAppendedId(CONTENT_URI, rowID);
	        getContext().getContentResolver().notifyChange(uriWithAppendedId, null);          		
	     }
	     break;
	  case BILL_IFP:
		 //Log.v(ParseBill1.TAG, "InstaProvider:insert:BILL_IFP_TABLE:cValue = " + cValues);
		 long rowID2 = todoDB.insert(BILL_IFP_TABLE, "nullColumnHack", cValues);		       
		 if (rowID2 > 0) {
		    uriWithAppendedId = ContentUris.withAppendedId(CONTENT_URI_BILL_IFP_TABLE, rowID2);
	        getContext().getContentResolver().notifyChange(uriWithAppendedId, null);          		
		 }
		 break;	   	    	    
      default:
    	 //Log.v(ParseBill1.TAG,"InstaProvider:insert:TODOTABLE:k = " + k );
    	 throw new SQLException("InstaProvider:insert():Failed to insert row into " + uriParameter);	    
	}
    return uriWithAppendedId; 
  }
   
  @Override
  public int delete(Uri uri, String where, String[] whereArgs) {
    int count;
    //Log.v(ParseBill1.TAG, "--InstaProvider:delete()");
    switch (uriMatcher.match(uri)) {
      case TODO:
        count = todoDB.delete(TODO_TABLE, where, whereArgs);
        break;

      case TODO_ID:
        String segment = uri.getPathSegments().get(1);
         
        count = todoDB.delete(TODO_TABLE, KEY_ID + "="
                                    + segment
                                    + (!TextUtils.isEmpty(where) ? " AND (" 
                                    + where + ')' : ""), whereArgs);
        break;

      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }
  
  
  @Override
  public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
    int count;
    //Log.v(ParseBill1.TAG, "--InstaProvider:update()");
    switch (uriMatcher.match(uri)) {
      case TODO: count = todoDB.update(TODO_TABLE, values, 
                                               where, whereArgs);
                   break;
      case TODO_ID: String segment = uri.getPathSegments().get(1);
                     count = todoDB.update(TODO_TABLE, values, KEY_ID 
                             + "=" + segment 
                             + (!TextUtils.isEmpty(where) ? " AND (" 
                             + where + ')' : ""), whereArgs);
                     break;
      case BILL_IFP: count = todoDB.update(BILL_IFP_TABLE, values, 
                                             where, whereArgs);
                      break;
      case BILL_IFP_ID: String segment2 = uri.getPathSegments().get(1);
                     count = todoDB.update(TODO_TABLE, values, KEY_ID_BILL_IMAGE_COUNT_AUTO_INCR 
                             + "=" + segment2 
                             + (!TextUtils.isEmpty(where) ? " AND (" 
                             + where + ')' : ""), whereArgs);
                     break;                                    
      default: throw new IllegalArgumentException("Unknown URI " + uri);
    }
    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }
   
  @Override
  public String getType(Uri uri) {
	//Log.v(ParseBill1.TAG, "--InstaProvider:getType()-WHO call this and Why ?");
    switch (uriMatcher.match(uri)) {
      case TODO: return "vnd.android.cursor.dir/vnd.examples.todo";
      case TODO_ID: return "vnd.android.cursor.item/vnd.examples.todo";
      case BILL_IFP: return "vnd.android.cursor.dir/vnd.examples.bill_ImageFilePath";
      case BILL_IFP_ID: return "vnd.android.cursor.item/vnd.examples.bill_ImageFilePath";      
      default: throw new IllegalArgumentException("InstaProvider:getType()Unsupported URI: " + uri);
    }
  }
  
  private static final int TODO = 1;
  private static final int TODO_ID = 2;
  private static final int BILL_IFP = 3;    
  private static final int BILL_IFP_ID = 4;
  
  private static final UriMatcher uriMatcher;
  
  static {
   uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
  
   uriMatcher.addURI(AUTHORITY, "todo", TODO);
   uriMatcher.addURI(AUTHORITY, "todo/#", TODO_ID);
   uriMatcher.addURI(AUTHORITY, "bill_ImageFilePath", BILL_IFP);
   uriMatcher.addURI(AUTHORITY, "bill_ImageFilePath/#", BILL_IFP_ID);
  }
   
  private SQLiteDatabase todoDB;
   
  private static final String DATABASE_NAME = "tList.db";
  private static final int DATABASE_VERSION = 1;
  
  private static final String TODO_TABLE = "todoItems";
  private static final String BILL_IFP_TABLE = "bill_ImageFilePathTable";
  
  
  public static final String KEY_ID = "_id"; 
  public static final String KEY_BILL_IMAGE_NUMBER = "bill_ImageNumber"; 
  public static final String KEY_ORDERPRICE = "orderPrice";
  public static final String KEY_ORDERNAME = "orderName";
  public static final String KEY_ORDERCOUNT = "orderCount";
  
  
   
  public static final int ORDERCOUNT_COLUMN = 1;
  public static final int ORDERNAME_COLUMN = 2;
  public static final int ORDERPRICE_COLUMN = 3;
   
  public static final String KEY_ID_BILL_IMAGE_COUNT_AUTO_INCR = "_id"; 
  public static final String KEY_BILL_IMAGE_FILE_PATH = "bill_ImageFilePath";
  
   
   
  private static class ToDoDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_CREATE = 
            "create table " + TODO_TABLE + " (" 
            + KEY_BILL_IMAGE_NUMBER + " integer, "
            + KEY_ORDERPRICE + " VARCHAR, "
            + KEY_ORDERNAME + " VARCHAR, "
            + KEY_ORDERCOUNT + " VARCHAR, "
            + KEY_ID + " integer primary key autoincrement);";
    
    private static final String CREATE_BILL_IMAGE_FILE_PATH_TABLE = 
    	    "create table " + BILL_IFP_TABLE + " (" 
    	    	 + KEY_ID_BILL_IMAGE_COUNT_AUTO_INCR + " integer primary key autoincrement, "
    	    	 + KEY_BILL_IMAGE_FILE_PATH + " VARCHAR);" ;		
    		
    
    public ToDoDatabaseHelper(Context context, 
    		                  String name, 
                              CursorFactory factory, int version) {
      super(context, name, factory, version);
       
      //Log.v(ParseBill1.TAG, "--InstaProvider:ToDoDatabaseHelper():constructor()");
    }
     
    
    @Override
    public void onCreate(SQLiteDatabase db) {
       
      //Log.v(ParseBill1.TAG, "InstaProvider:ToDoDatabaseHelper:onCreate():Create todoList table w - "+DATABASE_CREATE);
      db.execSQL(DATABASE_CREATE); 
      //Log.v(ParseBill1.TAG, "--InstaProvider:ToDoDatabaseHelper:onCreate():todoItem table created");
      //Log.v(ParseBill1.TAG, "InstaProvider:ToDoDatabaseHelper:onCreate():Creating billCountTable w - "+
      //                                        CREATE_BILL_IMAGE_FILE_PATH_TABLE);
      db.execSQL(CREATE_BILL_IMAGE_FILE_PATH_TABLE);
      //Log.v(ParseBill1.TAG, "--InstaProvider:ToDoDatabaseHelper:onCreate():bill_ImageFilePathTable created");
    }
    
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      //Log.w(ParseBill1.TAG, "InstaProvider:ToDoDatabaseHelper:onUpgrade()--Upgrading database from version " + oldVersion + " to "
      //           + newVersion + ", which will destroy all old data");
              
      db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
      db.execSQL("DROP TABLE IF EXISTS " + BILL_IFP_TABLE);
      onCreate(db);
    }
     
  } 
  
} 