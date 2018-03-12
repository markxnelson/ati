// Copyright 2012, 2018, Mark Nelson. All rights reserved.

package com.samuiinteractive.ati;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookmarkOpenHelper  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;
    private static final String DATABASE_NAME = "bookmarks";
    public static final String BOOKMARKS_TABLE_NAME = "bookmarks";
    public static final String TITLE_COLUMN_NAME = "title";
    public static final String URL_COLUMN_NAME = "url";
    private static final String BOOKMARKS_TABLE_CREATE =
                "CREATE TABLE " + BOOKMARKS_TABLE_NAME + " (" +
                TITLE_COLUMN_NAME + " TEXT, " +
                URL_COLUMN_NAME + " TEXT);";

    BookmarkOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BOOKMARKS_TABLE_CREATE);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion < 5) {
            // do upgrade (nothing to do)
        }
	}
}