package com.example.streamtools;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class QueryNumberLocation {
	private static String path = "data/data/com.example.mobilesafe/files/address.db";

	/*
	 * ��ѯ���ݿ⣬��ȡ���������
	 */
	public static String queryLocation(String number) {
		String address = number;
		String regular = "^1[34568]\\d{9}$";
		SQLiteDatabase sdb = SQLiteDatabase.openDatabase(path, null,
				SQLiteDatabase.OPEN_READONLY);
		if (address.matches(regular)) {
			Cursor cursor = sdb
					.rawQuery(
							"select location from data2 where id = (select outkey from data1 where id = ?)",
							new String[] { number.substring(0, 7) });
			while (cursor.moveToNext()) {
				String location = cursor.getString(0);
				if (!TextUtils.isEmpty(location)) {
					address = location;
				}
			}
			cursor.close();
		} else {
			switch (address.length()) {
			case 3:
				// 110
				address = "�˾�����";
				break;
			case 4:
				// 5554
				address = "ģ����";
				break;
			case 5:
				// 10086
				address = "�ͷ��绰";
				break;
			case 7:
				//
				address = "���غ���";
				break;
			case 8:
				address = "���غ���";
				break;
			default:
				// /����;�绰 10
				if (address.length() >= 10 && address.startsWith("0")) {
					// 010-59790386
					Cursor cursor = sdb.rawQuery(
							"select location from data2 where area = ?",
							new String[] { address.substring(1, 3) });

					while (cursor.moveToNext()) {
						String location = cursor.getString(0);
						address = location.substring(0, location.length() - 2);
					}
					cursor.close();
					// 0855-59790386
					cursor = sdb.rawQuery(
							"select location from data2 where area = ?",
							new String[] { number.substring(1, 4) });
					while (cursor.moveToNext()) {
						String location = cursor.getString(0);
						address = location.substring(0, location.length() - 2);
					}
				}

				break;
			}
		}

		return address;
	}
}
