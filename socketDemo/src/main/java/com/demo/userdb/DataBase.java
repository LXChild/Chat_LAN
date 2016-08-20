package com.demo.userdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
	public DataBase(Context context) {
		super(context, "db", null, 1);
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自动生成的方法存根
		db.execSQL("CREATE TABLE UserData("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "username TEXT DEFAULE \"\"," + "password TEXT DEFAULE \"\")");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO 自动生成的方法存根
	}
}
