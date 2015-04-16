package com.example.mobilesafe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class SelectContactActivity extends Activity {

	private ListView list_select_contact;
	String phone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_contact);
		list_select_contact = (ListView) findViewById(R.id.list_select_contact);
		final List<Map<String, String>> data = getContactInfo();
		list_select_contact.setAdapter(new SimpleAdapter(this, data,
				R.layout.contact_list, new String[] { "name", "phone" },
				new int[] { R.id.tv_name, R.id.tv_phone }) {

		});
		list_select_contact.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				phone = data.get(position).get("phone");
				Intent data = new Intent();
				data.putExtra("phone", phone);
				setResult(0, data);
				// ��ǰҳ��رյ�
				finish();
			}
		});
	}

	private List<Map<String, String>> getContactInfo() {

		// �����е���ϵ��
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();

		// �õ�һ�����ݽ�����
		ContentResolver resolver = getContentResolver();
		// raw_contacts uri
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri uriData = Uri.parse("content://com.android.contacts/data");

		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);

		while (cursor.moveToNext()) {
			String contact_id = cursor.getString(0);

			if (contact_id != null) {
				// �����ĳһ����ϵ��
				Map<String, String> map = new HashMap<String, String>();

				Cursor dataCursor = resolver.query(uriData, new String[] {
						"data1", "mimetype" }, "contact_id=?",
						new String[] { contact_id }, null);

				while (dataCursor.moveToNext()) {
					String data1 = dataCursor.getString(0);
					String mimetype = dataCursor.getString(1);
					System.out.println("data1==" + data1 + "==mimetype=="
							+ mimetype);

					if ("vnd.android.cursor.item/name".equals(mimetype)) {
						// ��ϵ�˵�����
						map.put("name", data1);
					} else if ("vnd.android.cursor.item/phone_v2"
							.equals(mimetype)) {
						// ��ϵ�˵ĵ绰����
						map.put("phone", data1);
					}

				}

				list.add(map);
				dataCursor.close();

			}

		}

		cursor.close();
		return list;
	}

}
