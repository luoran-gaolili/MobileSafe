package com.example.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bean.App;
import com.example.streamtools.AdminAppInfo;
import com.example.streamtools.DensityUtil;

public class AppMangerActivity extends Activity implements OnClickListener {
	TextView tv, tvone, tvthree;
	ListView listview;
	List<App> info;
	List<App> userapp;
	List<App> systemapp;
	RelativeLayout layout;
	App appinfo;
	PopupWindow window;
	LinearLayout linear, linearone, lineartwo;
	MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.appmanger);
		tv = (TextView) findViewById(R.id.app_tv);
		tvone = (TextView) findViewById(R.id.app_tvone);
		tvthree = (TextView) findViewById(R.id.app_tvthree);
		listview = (ListView) findViewById(R.id.app_list);
		layout = (RelativeLayout) findViewById(R.id.app_linear);

		adminData();

		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				dismissWindow();
				if (userapp != null && systemapp != null) {
					tvthree.setVisibility(View.VISIBLE);
					if (firstVisibleItem > userapp.size()) {
						tvthree.setText("ϵͳ����" + systemapp.size() + "��");
					} else {
						tvthree.setText("�û�����" + userapp.size() + "��");
					}
				}

			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				dismissWindow();
				// ��ȷ����¼���λ��
				if (position == 0 || position == userapp.size() + 1) {
					return;
				} else if (position <= userapp.size()) {
					int newposition = position - 1;
					appinfo = userapp.get(newposition);
				} else {
					int newposition = position - userapp.size() - 1 - 1;
					appinfo = systemapp.get(newposition);

				}
				System.out.println(appinfo.getPackName());
				View vw = View.inflate(AppMangerActivity.this,
						R.layout.pop_laoout, null);
				linear = (LinearLayout) vw.findViewById(R.id.pop_linear);
				linearone = (LinearLayout) vw.findViewById(R.id.pop_linearone);
				lineartwo = (LinearLayout) vw.findViewById(R.id.pop_lineartwo);
				linear.setOnClickListener(AppMangerActivity.this);
				linearone.setOnClickListener(AppMangerActivity.this);
				lineartwo.setOnClickListener(AppMangerActivity.this);
				// -2��ʾ�����������
				window = new PopupWindow(vw, -2, -2);
				// ����Ч���Ĳ��ű���Ҫ�󵯳������б�����ɫ
				window.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				int[] location = new int[2];
				view.getLocationInWindow(location);
				int dp = DensityUtil.dip2px(AppMangerActivity.this, 50);
				window.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, dp,
						location[1]);
				ScaleAnimation scale = new ScaleAnimation(0.3f, 1.0f, 0.3f,
						1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
						Animation.RELATIVE_TO_SELF, 0.5f);
				scale.setDuration(300);
				AlphaAnimation alpha = new AlphaAnimation(0.3f, 1.0f);
				alpha.setDuration(300);
				AnimationSet set = new AnimationSet(false);
				set.addAnimation(scale);
				set.addAnimation(alpha);
				vw.startAnimation(set);

			}

		});

		long l = retriveRoom(Environment.getDataDirectory().getAbsolutePath());
		long d = retriveRoom(Environment.getExternalStorageDirectory()
				.getAbsolutePath());
		// ��ʽ���ڴ��С
		String ls = Formatter.formatFileSize(this, l);
		tv.setText("�ڴ����:" + ls);
		String ld = Formatter.formatFileSize(this, d);
		tvone.setText("sd������:" + ld);

	}

	private void adminData() {
		layout.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				info = AdminAppInfo.appInfo(AppMangerActivity.this);
				userapp = new ArrayList<App>();
				systemapp = new ArrayList<App>();
				for (App infos : info) {
					if (infos.isUserApp()) {
						userapp.add(infos);
					} else {
						systemapp.add(infos);
					}
				}
				runOnUiThread(new Runnable() {

					public void run() {
						// TODO Auto-generated method stub

						if (adapter == null) {
							adapter = new MyAdapter();
							listview.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}
						layout.setVisibility(View.INVISIBLE);

					}
				});

			};
		}.start();
	}

	private void dismissWindow() {
		if (window != null && window.isShowing()) {
			window.dismiss();
		}
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userapp.size() + systemapp.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			// ��ȷ����¼���λ��
			if (position == 0) {
				TextView tv = new TextView(AppMangerActivity.this);
				tv.setTextColor(Color.BLACK);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("�û�����" + userapp.size() + "��");
				// �ǵ�return
				return tv;
			} else if (position == (userapp.size() + 1)) {
				TextView tv = new TextView(AppMangerActivity.this);
				tv.setTextColor(Color.BLACK);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("ϵͳ����" + systemapp.size() + "��");
				return tv;
			} else if (position <= userapp.size()) {
				int newposition = position - 1;
				appinfo = userapp.get(newposition);
			} else {
				int newposition = position - 1 - userapp.size() - 1;
				appinfo = systemapp.get(newposition);
			}

			View view = View.inflate(AppMangerActivity.this,
					R.layout.manage_item, null);
			ImageView image = (ImageView) view.findViewById(R.id.app_image);
			TextView tv = (TextView) view.findViewById(R.id.app_itemtv);
			TextView tvone = (TextView) view.findViewById(R.id.app_itemtvone);
			tv.setText(appinfo.getAppName());
			if (appinfo.isRom()) {
				tvone.setText("�ֻ��ڴ�");
			} else {
				tvone.setText("�ⲿ�洢");
			}
			image.setImageDrawable(appinfo.getIcon());
			return view;
		}
	}

	/**
	 * ��ȡ�ڴ�ռ�Ĵ�С
	 * 
	 * @param path
	 *            ·��
	 * @return
	 */
	private long retriveRoom(String path) {
		StatFs fs = new StatFs(path);
		// ��ȡ�ڴ�ռ�ĸ���
		fs.getBlockCount();
		// ��ȡ�ռ�Ĵ�С
		long l = fs.getBlockSize();
		// ��ȡ���õ��ڴ����
		long s = fs.getAvailableBlocks();
		return l * s;

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dismissWindow();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.pop_linear:
			dismissWindow();
			if (appinfo.isUserApp()) {
				uniInstall();
			} else {
				Toast.makeText(AppMangerActivity.this, "�޷�ж��ϵͳ����",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.pop_linearone:
			dismissWindow();

			intentActivity();
			break;
		case R.id.pop_lineartwo:
			shareApp();
			break;
		default:
			break;
		}
	}

	/**
	 * �������
	 */
	private void shareApp() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,
				"�Ƽ���ʹ��һ����õ����,�������:" + appinfo.getAppName());
		startActivity(intent);
	}

	/**
	 * ж�����
	 */
	private void uniInstall() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.setAction("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + appinfo.getPackName()));
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			adminData();
		}

	}

	/**
	 * �������
	 */
	private void intentActivity() {
		// TODO Auto-generated method stub
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(appinfo.getPackName());
		// �ж�intent�Ƿ�Ϊ��
		if (intent != null) {
			startActivity(intent);
		}
	}

}
