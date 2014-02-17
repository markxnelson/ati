package com.samuiinteractive.ati;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BookmarkOpenHelper  extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
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
        //db.execSQL("INSERT INTO BOOKMARKS (TITLE, URL) VALUES ('TEST BOOKMARK','file:///android_asset/tipitaka/an/an01/an01.047.than.html')");
    }

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}