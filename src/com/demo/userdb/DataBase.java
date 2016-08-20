package com.demo.userdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
	public DataBase(Context context) {
		super(context, "db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE UserData("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "username TEXT DEFAULE \"\"," + "password TEXT DEFAULE \"\")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	}
}
