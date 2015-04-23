package com.samuiinteractive.ati;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;


public class NewBookmark extends Activity implements OnClickListener {
	
	private TableLayout mView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newbookmark);

		mView = (TableLayout) findViewById(R.id.newbookmark);

		// look for data bundle
        String requestedUrl;
        String requestedTitle;
        try {
        	Bundle extras = getIntent().getExtras();
        	requestedUrl = extras.getString("url");
        	if (null != requestedUrl) {
        		System.out.println("[ATI] requested url is : " + requestedUrl);
        		EditText theUrl = (EditText) findViewById(R.id.editText1);
        		theUrl.setText(requestedUrl);
        	}
        	requestedTitle = extras.getString("title");
        	if (null != requestedTitle) {
        		System.out.println("[ATI] requested title is : " + requestedTitle);
        		EditText theTitle = (EditText) findViewById(R.id.editText2);
        		theTitle.setText(requestedTitle);
        	}
        } catch (Exception ignore) {}

        // button click listener
        Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(this);

	}

	//@Override
	public void onClick(View v) {
		// Save the new bookmark in the database
		BookmarkPojo newBookmark = new BookmarkPojo();
		String theTitle = ((EditText) findViewById(R.id.editText2)).getText().toString();
		String theUrl = ((EditText) findViewById(R.id.editText1)).getText().toString();
		newBookmark.setTitle(theTitle);
		newBookmark.setUrl(theUrl);
		insertBookmark(newBookmark);

//		// redirect to main web view
//		Intent startIntent = new Intent(NewBookmark.this, Start.class);
//    	// add data bundle with url
//    	Bundle dataBundle = new Bundle();
//		dataBundle.putString("url", theUrl);
//		startIntent.putExtras(dataBundle);
//		NewBookmark.this.startActivity(startIntent);
		this.finish();  // take this off the activity stack so we dont see it again if we his back

		// this activity has finishOnTaskLaunch = true so it wont be in the activity stack if you hit back button
		// .. but that alone does not seem to be enough without this.finish()

	}

	public void insertBookmark(BookmarkPojo bookmarkPojo){

		// First we have to open our DbHelper class by creating a new object of that
		BookmarkOpenHelper androidOpenDbHelperObj = new BookmarkOpenHelper(this);

		// Then we need to get a writable SQLite database, because we are going to insert some values
		// SQLiteDatabase has methods to create, delete, execute SQL commands, and perform other common database management tasks.
		SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();

		// ContentValues class is used to store a set of values that the ContentResolver can process.
		ContentValues contentValues = new ContentValues();

		// Get values from the POJO class and passing them to the ContentValues class
		contentValues.put(BookmarkOpenHelper.TITLE_COLUMN_NAME, bookmarkPojo.getTitle());
		contentValues.put(BookmarkOpenHelper.URL_COLUMN_NAME, bookmarkPojo.getUrl());

		// Now we can insert the data in to relevant table
		sqliteDatabase.insert(BookmarkOpenHelper.BOOKMARKS_TABLE_NAME, null, contentValues);

		// It is a good practice to close the database connections after you have done with it
		sqliteDatabase.close();

	}

	
}
