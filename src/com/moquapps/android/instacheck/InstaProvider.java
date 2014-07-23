package com.moquapps.android.instacheck;

import android.content.*;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class InstaProvider extends ContentProvider {
  private static final String AUTHORITY = "com.moquapps.instacheck.provider.todo"; //this match 
                                 //with provider entry in Manifest file. **Do not change**
  //public static final Uri CONTENT_URI = Uri.parse("content://com.moquapps.instacheck.provider.todo/todo");
  public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/todo");
  public static final Uri CONTENT_URI_BILL_IFP_TABLE = Uri.parse("content://" + AUTHORITY + "/bill_ImageFilePath");
  //Note: Content-Uris have syntax ==> content://authority/path/id
  private static final String TAG = "iCheck";//TAG for instaCheck
  //----------------------------------------------------
  @Override
  public boolean onCreate() {
    Context context = getContext();
    //Log.v("InstaProvider", "onCreate");
    Log.v(TAG, "--InstaProvider:onCreate()");
    ToDoDatabaseHelper dbHelper = new ToDoDatabaseHelper(context,  
                                                   DATABASE_NAME, //tList.db
                                                   null, //CursorFactory null = default
                                                   DATABASE_VERSION);//1
    todoDB = dbHelper.getWritableDatabase();
    return (todoDB == null) ? false : true;
  }
  //-------------------------------------  
  @Override
  public Cursor query(Uri uri, 
                      String[] projection, 
                      String selection, 
                      String[] selectionArgs, 
                      String sort) {
    
	//Log.v("TodoProvider", "query");
	Log.v(TAG, "InstaProvider:query()-uri=" + uri);
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

    if (uri == CONTENT_URI){
       qb.setTables(TODO_TABLE);
       Log.v(TAG, "InstaProvider:query()-setTables(TODO_TABLE)");
    }
    else if (uri == CONTENT_URI_BILL_IFP_TABLE){
       qb.setTables(BILL_IFP_TABLE);
       Log.v(TAG, "InstaProvider:query()-setTables(BILL_IFP_TABLE)");
    }
    else{
       qb.setTables(TODO_TABLE);
       Log.v(TAG, "**InstaProvider:query()*NO-URI-MATCH**setTables(TODO_TABLE)");
    }
    
    Log.v(TAG, "InstaProvider:query():uriMatcher.match = " + uriMatcher.match(uri));
    Log.v(TAG, "InstaProvider:query():uri.getPathSegments()=" + uri.getPathSegments());
    // If this is a row query, limit the result set to the passed in row. (!!)
    /*******************************************
     This is NOT needed. ak - Jul 19,2014
    switch (uriMatcher.match(uri)) {      
      case TODO_ID:     	  
    	  Log.v(TAG, "InstaProvider:query():uriMatcher.match = TODO_ID");
    	  String whereClause = KEY_ID + "=" + uri.getPathSegments().get(1);
    	  Log.v(TAG, "InstaProvider:query():TODO_ID WHERE-Clause = " + whereClause);
    	  qb.appendWhere(whereClause);
    	  //qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
          break;
      case BILL_IFP_ID: 
    	  Log.v(TAG, "InstaProvider:query():BILL_IFP_ID WHERE-Clause = " + 
    			       KEY_ID_BILL_IMAGE_COUNT_AUTO_INCR + "=" + uri.getPathSegments().get(1));
    	  qb.appendWhere(KEY_ID_BILL_IMAGE_COUNT_AUTO_INCR+"=" + uri.getPathSegments().get(1));
                     break;
      default: 
    	  Log.v(TAG, "InstaProvider:query():default - No uriMatcher.match = ");    	  
    	  break;
    }
    **********************************/
    //If no sort order is specified sort by date / time
    String orderBy;
    /**********
    if (TextUtils.isEmpty(sort)) {
      orderBy = KEY_ORDERCOUNT;
    } else {
    ********/
      orderBy = sort;
    //}

    // Apply query to underlying database.
    Cursor c = qb.query(todoDB,//DB to query on 
               projection,//List of which columns to return - null means all columns
               selection, //filter - which rows to return - SQL-WHERE clause - null mean ALL rows for given URL
               selectionArgs,//the '?' in above "selection" replaced by these "Args" !
               null, //SQL-Group by - null means no grouping of rows
               null, //SQL-having clause - null means incude all row groups                       
               orderBy);//sort order as SQL ORDER BY - null mean default sort order
    // Register the contexts ContentResolver to be notified if
    // the cursor result set changes. 
    c.setNotificationUri(getContext().getContentResolver(), uri);
    Log.v(TAG, "InstaProvider:query()Cusor c.Count =" + c.getCount());
    // Return a cursor to the query result.
    return c;
  }
  //---------------------------------------------
  @Override
  //old -> public Uri insert(Uri _uri, ContentValues _initialValues) {
  public Uri insert(Uri uriParameter, ContentValues cValues) {
    // Insert the new row, will return the row number if 
    // successful.
	//Log.v(TAG, "--InstaProvider:insert():uriParameter = " + uriParameter);  
	Uri uriWithAppendedId = null; //Content-Uris have syntax content://authority/path/id
	int k = uriMatcher.match(uriParameter);
	switch(k){
	  case TODO:
		 //Log.v(TAG, "InstaProvider:insert:TODOTABLE:cValue = " + cValues);
	     long rowID = todoDB.insert(TODO_TABLE, "notes", cValues);
	     //Log.v("DB", "Inserting :: " + _initialValues);      
	    
	     if (rowID > 0){
	        uriWithAppendedId = ContentUris.withAppendedId(CONTENT_URI, rowID);
	        getContext().getContentResolver().notifyChange(uriWithAppendedId, null);          		
	     }
	     break;
	  case BILL_IFP:
		 Log.v(TAG, "InstaProvider:insert:BILL_IFP_TABLE:cValue = " + cValues);
		 long rowID2 = todoDB.insert(BILL_IFP_TABLE, "nullColumnHack", cValues);		       
		 if (rowID2 > 0) {
		    uriWithAppendedId = ContentUris.withAppendedId(CONTENT_URI_BILL_IFP_TABLE, rowID2);
	        getContext().getContentResolver().notifyChange(uriWithAppendedId, null);          		
		 }
		 break;	   	    	    
      default:
    	 Log.v(TAG,"InstaProvider:insert:TODOTABLE:k = " + k );
    	 throw new SQLException("InstaProvider:insert():Failed to insert row into " + uriParameter);	    
	}//switch    
	//Log.v(TAG, "--InstaProvider:insert():uriWithAppendedId = " + uriWithAppendedId);
    return uriWithAppendedId;//Return URI of newly inserted row on success
  }
  //-----------------------------------------------------
  @Override
  public int delete(Uri uri, String where, String[] whereArgs) {
    int count;
    Log.v(TAG, "--InstaProvider:delete()");
    switch (uriMatcher.match(uri)) {
      case TODO:
        count = todoDB.delete(TODO_TABLE, where, whereArgs);
        break;

      case TODO_ID:
        String segment = uri.getPathSegments().get(1);
        //Toast.makeText(this, segment, Toast.LENGTH_SHORT).show();
        //Log.v("Delete->>",segment);
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
  //------------------------------------------------
  @Override
  public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
    int count;
    Log.v(TAG, "--InstaProvider:update()");
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
  //---------------------------------------------
  /*Implement this method to handle requests for the MIME type of 
   *the data at the given URI. The returned MIME type should start with 
   *vnd.android.cursor.item for a single record, or vnd.android.cursor.dir/ 
   *for multiple items. This method can be called from multiple threads. 
   */
  @Override
  public String getType(Uri uri) {
	Log.v(TAG, "--InstaProvider:getType()-WHO call this and Why ?");
    switch (uriMatcher.match(uri)) {
      case TODO: return "vnd.android.cursor.dir/vnd.examples.todo";
      case TODO_ID: return "vnd.android.cursor.item/vnd.examples.todo";
      case BILL_IFP: return "vnd.android.cursor.dir/vnd.examples.bill_ImageFilePath";
      case BILL_IFP_ID: return "vnd.android.cursor.item/vnd.examples.bill_ImageFilePath";      
      default: throw new IllegalArgumentException("InstaProvider:getType()Unsupported URI: " + uri);
    }
  }
  //---------------------------------------------------
  //Create the constants used to differentiate between the different URI 
  //requests.
  private static final int TODO = 1;
  private static final int TODO_ID = 2;
  private static final int BILL_IFP = 3;   //IFP = ImageFilePath !
  private static final int BILL_IFP_ID = 4;
  
  private static final UriMatcher uriMatcher;
  // Allocate the UriMatcher object, where a URI ending in 'earthquakes' will
  // correspond to a request for all earthquakes, and 'earthquakes' with a 
  // trailing '/[rowID]' will represent a single earthquake row.
  static {
   uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
   //uriMatcher.addURI("com.examples.provider.todo", "todo", TODO);
   //uriMatcher.addURI("com.examples.provider.todo", "todo/#", TODO_ID);
   uriMatcher.addURI(AUTHORITY, "todo", TODO);
   uriMatcher.addURI(AUTHORITY, "todo/#", TODO_ID);
   uriMatcher.addURI(AUTHORITY, "bill_ImageFilePath", BILL_IFP);
   uriMatcher.addURI(AUTHORITY, "bill_ImageFilePath/#", BILL_IFP_ID);
  }
  //*****************************************************************
  //        The underlying database
  //*****************************************************************
  private SQLiteDatabase todoDB;
  //private static final String TAG = "ToDoProvider";
  private static final String DATABASE_NAME = "tList.db";
  private static final int DATABASE_VERSION = 1;
  
  private static final String TODO_TABLE = "todoItems";
  private static final String BILL_IFP_TABLE = "bill_ImageFilePathTable";
  
  // Column Names in this TODO_TABLE table
  public static final String KEY_ID = "_id";//android guideline require "_id" in all tables
  public static final String KEY_BILL_IMAGE_NUMBER = "bill_ImageNumber";//Jul19,14 ak
  public static final String KEY_ORDERPRICE = "orderPrice";
  public static final String KEY_ORDERNAME = "orderName";
  public static final String KEY_ORDERCOUNT = "orderCount";
 // public static final String KEY_RESTNAME = "restName";
 // public static final String KEY_TOTALSPENT = "totalSpent";
 // public static final String KEY_RATING = "rating";
  
  // Column indexes
  public static final int ORDERCOUNT_COLUMN = 1;
  public static final int ORDERNAME_COLUMN = 2;
  public static final int ORDERPRICE_COLUMN = 3;
  //public static final int ORDERATING_COLUMN = 4;
  
 // public static final int RESTNAME_COLUMN = 5;
 // public static final int TOTALSPENT_COLUMN = 6;
  
  //Column names in BILL_COUNT_TABLE 
  public static final String KEY_ID_BILL_IMAGE_COUNT_AUTO_INCR = "_id";//Android guideline require "_id" in every table
  public static final String KEY_BILL_IMAGE_FILE_PATH = "bill_ImageFilePath";
  
  //public static final int LONGITUDE_COLUMN = 3;
  //public static final int LATITUDE_COLUMN = 4;
  //public static final int MAGNITUDE_COLUMN = 5;
  //public static final int LINK_COLUMN = 6;
      
  // Helper class for opening, creating, and managing database version control
  private static class ToDoDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_CREATE = //its Table-create NOT DB -create !! ak July18,14
            "create table " + TODO_TABLE + " (" 
            + KEY_BILL_IMAGE_NUMBER + " integer, "
            + KEY_ORDERPRICE + " VARCHAR, "
            + KEY_ORDERNAME + " VARCHAR, "
            + KEY_ORDERCOUNT + " VARCHAR, "
            // + KEY_TOTALSPENT + " VARCHAR, "  
            // + KEY_RESTNAME + " VARCHAR, " 
            // + KEY_RATING + " VARCHAR, "
            + KEY_ID + " integer primary key autoincrement);";
    
    private static final String CREATE_BILL_IMAGE_FILE_PATH_TABLE = 
    	    "create table " + BILL_IFP_TABLE + " (" 
    	    	 + KEY_ID_BILL_IMAGE_COUNT_AUTO_INCR + " integer primary key autoincrement, "
    	    	 + KEY_BILL_IMAGE_FILE_PATH + " VARCHAR);" ;		
    		
    //-----------------------------------------------------       
    public ToDoDatabaseHelper(Context context, 
    		                  String name,//name of database file; null for in-memory db
                              CursorFactory factory, int version) {
      super(context, name, factory, version);
      //Log.v("InstaProvider", "Constructor....");
      Log.v(TAG, "--InstaProvider:ToDoDatabaseHelper():constructor()");
    }
    //-------------------------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {
      //Log.v("Creating DB with : ", ":"+DATABASE_CREATE);
      Log.v(TAG, "InstaProvider:ToDoDatabaseHelper:onCreate():Create todoList table w - "+DATABASE_CREATE);
      db.execSQL(DATABASE_CREATE); 
      Log.v(TAG, "--InstaProvider:ToDoDatabaseHelper:onCreate():todoItem table created");
      Log.v(TAG, "InstaProvider:ToDoDatabaseHelper:onCreate():Creating billCountTable w - "+
                                                       CREATE_BILL_IMAGE_FILE_PATH_TABLE);
      db.execSQL(CREATE_BILL_IMAGE_FILE_PATH_TABLE);
      Log.v(TAG, "--InstaProvider:ToDoDatabaseHelper:onCreate():bill_ImageFilePathTable created");
    }
    //-----------------------------------------------------
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.w(TAG, "InstaProvider:ToDoDatabaseHelper:onUpgrade()--Upgrading database from version " + oldVersion + " to "
                  + newVersion + ", which will destroy all old data");
              
      db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
      db.execSQL("DROP TABLE IF EXISTS " + BILL_IFP_TABLE);
      onCreate(db);
    }
    //----------------------------------------------------
  }//class ToDoDatabaseHelper
  //******************************************************************
  //** new calss for 
}//class InstaProvider