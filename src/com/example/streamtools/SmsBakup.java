package com.example.streamtools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.StringReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.util.Xml;

/**
 * 短信的备份
 * 
 * @author Administrator
 * 
 */

public class SmsBakup {

	/**
	 * 接口回调
	 * 
	 * @author Administrator
	 * 
	 */

	public interface BakupFace {
		public abstract void SmsSetMax(int max);

		public abstract void SmsSetProgress(int progress);
	}

	public static void smsBakup(Context context, BakupFace face)
			throws Exception {
		ContentResolver resolver = context.getContentResolver();
		File file = new File(Environment.getExternalStorageDirectory(),
				"backup.xml");
		FileOutputStream fps = new FileOutputStream(file);
		// 获取序列化生成器
		XmlSerializer xml = Xml.newSerializer();
		// 关联输出流
		xml.setOutput(fps, "utf-8");
		xml.startDocument("utf-8", true);
		xml.startTag(null, "smss");
		Uri uri = Uri.parse("content://sms/");
		Cursor cursor = resolver.query(uri, new String[] { "body", "address",
				"type", "date" }, null, null, null);
		int max = cursor.getCount();
		xml.attribute(null, "max", max + "");
		face.SmsSetMax(max);
		/*
		 * dialog.setMax(max); bar.setMax(max);
		 */
		int progress = 0;
		while (cursor.moveToNext()) {
			String body = cursor.getString(0);
			String address = cursor.getString(1);
			String type = cursor.getString(2);
			String date = cursor.getString(3);
			xml.startTag(null, "sms");
			xml.startTag(null, "totalcount");
			xml.text(String.valueOf(max));
			xml.endTag(null, "totalcount");
			xml.startTag(null, "body");
			xml.text(body);
			xml.endTag(null, "body");
			xml.startTag(null, "address");
			xml.text(address);
			xml.endTag(null, "address");
			xml.startTag(null, "type");
			xml.text(type);
			xml.endTag(null, "type");
			xml.startTag(null, "date");
			xml.text(date);
			xml.endTag(null, "date");
			xml.endTag(null, "sms");
			progress++;
			/*
			 * dialog.setProgress(progress); bar.setProgress(progress);
			 */
			face.SmsSetProgress(progress);
		}
		xml.endTag(null, "smss");
		xml.endDocument();
	}

	/**
	 * 短信的还原
	 * 
	 * @param context
	 *            上下文
	 * @param face
	 *            接口回调
	 */
	public static void smsRestore(Context context, boolean flag, BakupFace face) {

		Uri uri = Uri.parse("content://sms/");
		if (flag) {
			context.getContentResolver().delete(uri, null, null);
		}
		try {
			// 读取文件
			File file = new File(Environment.getExternalStorageDirectory(),
					"backup.xml");
			FileInputStream stream = new FileInputStream(file);
			String str = StreamTools.getString(stream);
			// 生成解析器
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(str));
			int eventType = parser.getEventType();
			String body = "";
			String address = "";
			String type = "";
			String date = "";
			String totalcount = "";
			String nodeName = null;
			int count = 0;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_TAG:
					nodeName = parser.getName();
					{
						if ("totalcount".equals(nodeName)) {
							totalcount = parser.nextText();
							face.SmsSetMax(Integer.parseInt(totalcount));

						}
						if ("body".equals(nodeName)) {
							body = parser.nextText();
						} else if ("address".equals(nodeName)) {
							address = parser.nextText();
						} else if ("type".equals(nodeName)) {
							type = parser.nextText();
						} else if ("date".equals(nodeName)) {
							date = parser.nextText();
						}
						break;
					}
				case XmlPullParser.END_TAG: {
					nodeName = parser.getName();
					if ("sms".equals(nodeName)) {
						ContentResolver resolver = context.getContentResolver();
						ContentValues values = new ContentValues();
						values.put("body", body);
						values.put("address", address);
						values.put("type", type);
						values.put("date", date);
						resolver.insert(uri, values);
						count++;
						face.SmsSetProgress(count);
					}

				}

					break;

				default:
					break;
				}
				eventType = parser.next();

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
