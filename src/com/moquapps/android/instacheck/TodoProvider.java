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
/*This file excluded from build - ak - July7,2014*/
public class TodoProvider extends ContentProvider {

  public static final Uri CONTENT_URI = Uri.parse("content://com.examples.provider.todo/todo");
  
  @Override
  public boolean onCreate() {
    Context context = getContext();
    Log.v("TodoDBProvider", "onCreate");
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
    
	  Log.v("TodoProvider", "query");
    SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

    qb.setTables(TODO_TABLE);

    // If this is a row query, limit the result set to the passed in row. 
    switch (uriMatcher.match(uri)) {
      case TODO_ID: qb.appendWhere(KEY_ID + "=" + uri.getPathSegments().get(1));
                     break;
      default      : break;
    }

    // If no sort order is specified sort by date / time
    String orderBy;
    if (TextUtils.isEmpty(sort)) {
      orderBy = KEY_DATE;
    } else {
      orderBy = sort;
    }

    // Apply the query to the underlying database.
    Cursor c = qb.query(todoDB, 
                        projection, 
                        selection, selectionArgs, 
                        null, null, 
                        orderBy);

    // Register the contexts ContentResolver to be notified if
    // the cursor result set changes. 
    c.setNotificationUri(getContext().getContentResolver(), uri);
    
    // Return a cursor to the query result.
    return c;
  }

  @Override
  public Uri insert(Uri _uri, ContentValues _initialValues) {
    // Insert the new row, will return the row number if 
    // successful.
    long rowID = todoDB.insert(TODO_TABLE, "notes", _initialValues);
          
    // Return a URI to the newly inserted row on success.
    if (rowID > 0) {
      Uri uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
      getContext().getContentResolver().notifyChange(uri, null);
      return uri;
    }
    throw new SQLException("Failed to insert row into " + _uri);
  }

  @Override
  public int delete(Uri uri, String where, String[] whereArgs) {
    int count;
    
    switch (uriMatcher.match(uri)) {
      case TODO:
        count = todoDB.delete(TODO_TABLE, where, whereArgs);
        break;

      case TODO_ID:
        String segment = uri.getPathSegments().get(1);
        //Toast.makeText(this, segment, Toast.LENGTH_SHORT).show();
        Log.v("Delete->>",segment);
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

      default: throw new IllegalArgumentException("Unknown URI " + uri);
    }

    getContext().getContentResolver().notifyChange(uri, null);
    return count;
  }
  
  @Override
  public String getType(Uri uri) {
    switch (uriMatcher.match(uri)) {
      case TODO: return "vnd.android.cursor.dir/vnd.examples.todo";
      case TODO_ID: return "vnd.android.cursor.item/vnd.examples.todo";
      default: throw new IllegalArgumentException("Unsupported URI: " + uri);
    }
  }
  
  // Create the constants used to differentiate between the different URI 
  // requests.
  private static final int TODO = 1;
  private static final int TODO_ID = 2;

  private static final UriMatcher uriMatcher;

  // Allocate the UriMatcher object, where a URI ending in 'earthquakes' will
  // correspond to a request for all earthquakes, and 'earthquakes' with a 
  // trailing '/[rowID]' will represent a single earthquake row.
  static {
   uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
   uriMatcher.addURI("com.examples.provider.todo", "todo", TODO);
   uriMatcher.addURI("com.examples.provider.todo", "todo/#", TODO_ID);
  }
  
  //The underlying database
  private SQLiteDatabase todoDB;

  private static final String TAG = "ToDoProvider";
  private static final String DATABASE_NAME = "tList.db";
  private static final int DATABASE_VERSION = 1;
  private static final String TODO_TABLE = "todoItems";

  // Column Names
  public static final String KEY_ID = "_id";
  public static final String KEY_DATE = "creation_date";
  public static final String KEY_DETAILS = "task";
 // public static final String KEY_LOCATION_LAT = "latitude";
  //public static final String KEY_LOCATION_LNG = "longitude";
  //public static final String KEY_MAGNITUDE = "magnitude";
  //public static final String KEY_LINK = "link";

  // Column indexes
  public static final int DATE_COLUMN = 1;
  public static final int DETAILS_COLUMN = 2;
  //public static final int LONGITUDE_COLUMN = 3;
  //public static final int LATITUDE_COLUMN = 4;
  //public static final int MAGNITUDE_COLUMN = 5;
  //public static final int LINK_COLUMN = 6;
      
  // Helper class for opening, creating, and managing database version control
  private static class ToDoDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_CREATE =
      "create table " + TODO_TABLE + " (" 
      + KEY_ID + " integer primary key autoincrement, "
      + KEY_DATE + " INTEGER, "
      + KEY_DETAILS + " VARCHAR);";
        
    public ToDoDatabaseHelper(Context context, String name,
                                    CursorFactory factory, int version) {
      super(context, name, factory, version);
      Log.v("TodoProvider", "Constructor....");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      Log.v("TodoProvider", "Creating DB....");
      db.execSQL(DATABASE_CREATE);           
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
      Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                  + newVersion + ", which will destroy all old data");
              
      db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
      onCreate(db);
    }
  }
}