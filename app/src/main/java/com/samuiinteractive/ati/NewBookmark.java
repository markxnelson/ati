// Copyright 2012, 2018, Mark Nelson. All rights reserved.

package com.samuiinteractive.ati;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;


public class NewBookmark extends Activity implements OnClickListener {
	
	private TableLayout mView;

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

	public void onClick(View v) {
		// Save the new bookmark in the database
		BookmarkPojo newBookmark = new BookmarkPojo();
		String theTitle = ((EditText) findViewById(R.id.editText2)).getText().toString();
		String theUrl = ((EditText) findViewById(R.id.editText1)).getText().toString();
		newBookmark.setTitle(theTitle);
		newBookmark.setUrl(theUrl);
		insertBookmark(newBookmark);

		this.finish();
		// take this off the activity stack so we don't see it again if we hit back

	}

	public void insertBookmark(BookmarkPojo bookmarkPojo){

		BookmarkOpenHelper androidOpenDbHelperObj = new BookmarkOpenHelper(this);
		SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();

		ContentValues contentValues = new ContentValues();
		contentValues.put(BookmarkOpenHelper.TITLE_COLUMN_NAME, bookmarkPojo.getTitle());
		contentValues.put(BookmarkOpenHelper.URL_COLUMN_NAME, bookmarkPojo.getUrl());

		sqliteDatabase.insert(BookmarkOpenHelper.BOOKMARKS_TABLE_NAME, null, contentValues);

		sqliteDatabase.close();

	}

	
}
