// Copyright 2012, 2018, Mark Nelson. All rights reserved.

package com.samuiinteractive.ati;

import android.app.Activity;
import android.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.List;

public class Bookmarks extends Activity implements OnItemClickListener, OnItemLongClickListener {

	private ListView mListview;
	private ListAdapter mListAdapter;
	private ArrayList<BookmarkPojo> pojoArrayList;
    private String savedUrl = "";

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

		List<String> bookmarksList = new ArrayList<String>();

		BookmarkOpenHelper openHelperClass = new BookmarkOpenHelper(this);
		SQLiteDatabase sqliteDatabase = openHelperClass.getReadableDatabase();
		Cursor cursor = sqliteDatabase.query(
				BookmarkOpenHelper.BOOKMARKS_TABLE_NAME, null, null, null,
				null, null, null);
		startManagingCursor(cursor);

		while (cursor.moveToNext()) {

			String title = cursor.getString(cursor
					.getColumnIndex(BookmarkOpenHelper.TITLE_COLUMN_NAME));
			String url = cursor.getString(cursor
					.getColumnIndex(BookmarkOpenHelper.URL_COLUMN_NAME));

			BookmarkPojo bookmarkPogo = new BookmarkPojo();
			bookmarkPogo.setTitle(title);
			bookmarkPogo.setUrl(url);
			pojoArrayList.add(bookmarkPogo);
			bookmarksList.add(title);
		}

		sqliteDatabase.close();

		return bookmarksList;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

		System.out.println("Clicked on :" + arg2);
		Intent startIntent = new Intent(this, Start.class);
		BookmarkPojo clickedObject = pojoArrayList.get(arg2);
		Bundle dataBundle = new Bundle();
		dataBundle.putString("url", clickedObject.getUrl());
		startIntent.putExtras(dataBundle);

		startActivity(startIntent);
		this.finish();
		// take this off the activity stack so we don't see out of date bookmarks (e.g. after a delete)

	}

	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {

        System.out.println("[ATI] we need to delete bookmark " + pojoArrayList.get(arg2).getTitle());
        final BookmarkPojo clickedObject = pojoArrayList.get(arg2);
        savedUrl = clickedObject.getUrl();

        // confirmation popup
        new AlertDialog.Builder(this)
                .setTitle("Confirm")
                .setMessage("Do you really want to delete this bookmark?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // delete bookmark from db
                        deleteBookmark(clickedObject.getUrl());
                        Context context = getApplicationContext();
                        CharSequence text = "Bookmark deleted";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();

        // update bookmarks list
        mListview = (ListView) findViewById(R.id.bookmarks);
        mListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, populateList());
        mListview.setAdapter(mListAdapter);

        return false;
    }
	
	public void deleteBookmark(String theUrl){

		BookmarkOpenHelper androidOpenDbHelperObj = new BookmarkOpenHelper(this);
		SQLiteDatabase sqliteDatabase = androidOpenDbHelperObj.getWritableDatabase();

		String whereClause = BookmarkOpenHelper.URL_COLUMN_NAME + " = ? ";
		String[] whereArgs = { theUrl };
		sqliteDatabase.delete(BookmarkOpenHelper.BOOKMARKS_TABLE_NAME, whereClause, whereArgs);

		sqliteDatabase.close();

	}

}
