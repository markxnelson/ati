package com.samuiinteractive.ati;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class Bookmarks extends Activity implements OnItemClickListener, OnItemLongClickListener {

	private ListView mListview;
	private ListAdapter mListAdapter;
	private ArrayList<BookmarkPojo> pojoArrayList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookmarks);

		mListview = (ListView) findViewById(R.id.bookmarks);
		mListview.setOnItemClickListener(this);
		mListview.setOnItemLongClickListener(this);

		pojoArrayList = new ArrayList<BookmarkPojo>();

		mListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, populateList());

		mListview.setAdapter(mListAdapter);
		Context context = getApplicationContext();
		CharSequence text = "Touch to open, hold to delete.";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();

	}

	private List<String> populateList() {
		// We have to return a List which contains only String values. Lets
		// create a List first
		List<String> bookmarksList = new ArrayList<String>();

		// First we need to make contact with the database we have created using
		// the DbHelper class
		BookmarkOpenHelper openHelperClass = new BookmarkOpenHelper(this);

		// Then we need to get a readable database
		SQLiteDatabase sqliteDatabase = openHelperClass.getReadableDatabase();

		// We need a a guy to read the database query. Cursor interface will do
		// it for us
		// (String table, String[] columns, String selection, String[]
		// selectionArgs, String groupBy, String having, String orderBy)
		Cursor cursor = sqliteDatabase.query(
				BookmarkOpenHelper.BOOKMARKS_TABLE_NAME, null, null, null,
				null, null, null);
		// Above given query, read all the columns and fields of the table

		startManagingCursor(cursor);

		// Cursor object read all the fields. So we make sure to check it will
		// not miss any by looping through a while loop
		while (cursor.moveToNext()) {
			// In one loop, cursor read one undergraduate all details
			// Assume, we also need to see all the details of each and every
			// undergraduate
			// What we have to do is in each loop, read all the values, pass
			// them to the POJO class
			// and create a ArrayList of undergraduates

			String title = cursor.getString(cursor
					.getColumnIndex(BookmarkOpenHelper.TITLE_COLUMN_NAME));
			String url = cursor.getString(cursor
					.getColumnIndex(BookmarkOpenHelper.URL_COLUMN_NAME));

			// Finish reading one raw, now we have to pass them to the POJO
			BookmarkPojo bookmarkPogo = new BookmarkPojo();
			bookmarkPogo.setTitle(title);
			bookmarkPogo.setUrl(url);

			// Lets pass that POJO to our ArrayList which contains
			// undergraduates as type
			pojoArrayList.add(bookmarkPogo);

			// But we need a List of String to display in the ListView also.
			// That is why we create "uGraduateNamesList"
			bookmarksList.add(title);
		}

		// If you don't close the database, you will get an error
		sqliteDatabase.close();

		return bookmarksList;
	}

	// On ListView you just see the name of the undergraduate, not any other
	// details
	// Here we provide the solution to that. When the user click on a list item,
	// he will redirect to a page where
	// he can see all the details of the undergraduate
	//@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		System.out.println("Clicked on :" + arg2);

		// We want to redirect to another Activity when the user click an item
		// on the ListView
		Intent startIntent = new Intent(this, Start.class);

		// We have to identify what object, does the user clicked, because we
		// are going to pass only clicked object details to the next activity
		// What we are going to do is, get the ID of the clicked item and get
		// the values from the ArrayList which has
		// same array id.
		BookmarkPojo clickedObject = pojoArrayList.get(arg2);

		// We have to bundle the data, which we want to pass to the other
		// activity from this activity
		Bundle dataBundle = new Bundle();
		dataBundle.putString("url", clickedObject.getUrl());
		
		// Attach the bundled data to the intent
		startIntent.putExtras(dataBundle);

		// Start the Activity
		startActivity(startIntent);
		this.finish(); // take this off the activity stack so we dont see out of date bookmarks (e.g. after a delete)

	}

	//@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		System.out.println("[ATI] we need to delete bookmark " + pojoArrayList.get(arg2).getTitle());
		Intent startIntent = new Intent(this, Start.class);
		BookmarkPojo clickedObject = pojoArrayList.get(arg2);
		// delete bookmark from db
		deleteBookmark(clickedObject.getUrl());
		Context context = getApplicationContext();
		CharSequence text = "Bookmark deleted";
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		// go back to web view
		Bundle dataBundle = new Bundle();
		dataBundle.putString("url", clickedObject.getUrl());
		startIntent.putExtras(dataBundle);
		startActivity(startIntent);
		this.finish();  // take this off the activity stack so we dont see out of date bookmarks (e.g. after a delete)
		return false;
	}
	
	public void deleteBookmark(String theUrl){

		// First we have to open our DbHelper class by creating a new object of that
		BookmarkOpenHelper androidOpenDbHelperObj = new BookmarkOpenHelper(this);

		// Then we need to get a writable SQLite database, because we are going to insert some values
		// SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
		SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();

		// Now we can insert the data in to relevant table
		String whereClause = BookmarkOpenHelper.URL_COLUMN_NAME + " = ? ";
		String[] whereArgs = { theUrl };
		sqliteDatabase.delete(BookmarkOpenHelper.BOOKMARKS_TABLE_NAME, whereClause, whereArgs);

		// It is a good practice to close the database connections after you have done with it
		sqliteDatabase.close();

	}

}
