package com.example.test;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static String DB_PATH = "/data/data/com.example.test/databases/";
	
	/** 
	 * this will be on shared preferences (default en_US) 
	 * */
	private static String DB_NAME = "DB_en_US";
	
	private SQLiteDatabase mDataBase;
	private final Context mContext;
	
	
	public DatabaseHelper(Context context) {
		
		super(context, DB_NAME, null, 1);
		this.mContext = context;
	}
	
	/**
     * Creates an empty database on the system and rewrites it with your own database.
     * */
	
    public void createDataBase() throws IOException{
 
    	//CHANGE ON RELEASE!!!!
    	//boolean dbExist = checkDataBase();
    	boolean dbExist = false;
    	
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		/** By calling this method and empty database will be created into the default system path
               of your application so we are gonna be able to overwrite that database with our database. */
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
	

	/**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
	private boolean checkDataBase() {
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		//database does't exist yet.
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }
    
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */

	private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = mContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
	
    public void openDataBase() throws SQLException{
    	 
        String myPath = DB_PATH + DB_NAME;
    	mDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
    
    public synchronized void close() {
    	 
	    if(mDataBase != null)
		    mDataBase.close();
	    super.close();
    }
    
    
    public Cursor allOverlays() {
		String table = "subcategory";
		String[] columns = new String[] { 
				"category", "name", "longitude", "latitude", "image_url", "description_url" 
				};
		
    	Cursor cursor = mDataBase.query(table, columns, null, null, null, null, "name");
    	
    	if(cursor != null)
    	{
    		cursor.moveToFirst();
    	}
    	
    	return cursor;
    	
    }
    
    
    /** 
     * return the cursors of category "church" 
     * */
    public Cursor churchOverlays() {
    	String table = "subcategory";
    	String[] columns = new String[] { 
				"category", "name", "longitude", "latitude", "image_url", "description_url" 
				};
    	String where = "category = 'church'";
		
    	Cursor cursor = mDataBase.query(table, columns , where ,null,null,null,null);
    	
    	if(cursor != null)
    	{
    		cursor.moveToFirst();
    	}
    	
    	return cursor;
    }
    
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
