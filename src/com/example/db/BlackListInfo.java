package com.example.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.bean.BlackNumberBean;

public class BlackListInfo {
	MySQLiteOpenHelper openHelper;

	public BlackListInfo(Context context) {
		openHelper = new MySQLiteOpenHelper(context, "blacklist.db", null, 1);

	}

	public String findMode(String number) {
		String mode = null;
		SQLiteDatabase database = openHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(
				"select mode from blacklist where number=?",
				new String[] { number });
		while (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}
		database.close();
		cursor.close();
		return mode;
	}

	public void add(String number, String mode) {
		SQLiteDatabase database = openHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		values.put("mode", mode);
		database.insert("blacklist", null, values);
		database.close();
	}

	public boolean find(String number) {
		boolean bool = false;
		SQLiteDatabase database = openHelper.getReadableDatabase();
		Cursor cursor = database.rawQuery(
				"select * from blacklist where number=?",
				new String[] { number });
		while (cursor.moveToNext()) {
			bool = true;
		}
		database.close();
		cursor.close();
		return bool;

	}

	public List<BlackNumberBean> findApart(int offset, int maxNumber) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SQLiteDatabase database = openHelper.getReadableDatabase();
		List<BlackNumberBean> list = new ArrayList<BlackNumberBean>();
		Cursor cursor = database
				.rawQuery(
						"select number,mode from blacklist order by _id desc limit ? offset ?",
						new String[] { String.valueOf(maxNumber),
								String.valueOf(offset) });
		while (cursor.moveToNext()) {
			BlackNumberBean bean = new BlackNumberBean();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			bean.setMode(mode);
			bean.setNumber(number);
			list.add(bean);
		}
		cursor.close();
		database.close();
		return list;

	}

	public List<BlackNumberBean> findAll() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SQLiteDatabase database = openHelper.getReadableDatabase();
		List<BlackNumberBean> list = new ArrayList<BlackNumberBean>();
		Cursor cursor = database.rawQuery(
				"select number,mode from blacklist order by _id desc", null);
		while (cursor.moveToNext()) {
			BlackNumberBean bean = new BlackNumberBean();
			String number = cursor.getString(0);
			String mode = cursor.getString(1);
			bean.setMode(mode);
			bean.setNumber(number);
			list.add(bean);
		}
		cursor.close();
		database.close();
		return list;

	}

	public void delete(String number) {
		SQLiteDatabase database = openHelper.getReadableDatabase();
		database.delete("blacklist", "number=?", new String[] { number });
	}
}
