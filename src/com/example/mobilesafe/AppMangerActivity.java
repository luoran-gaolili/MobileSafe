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
						tvthree.setText("系统程序" + systemapp.size() + "个");
					} else {
						tvthree.setText("用户程序" + userapp.size() + "个");
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
				// 明确点击事件的位置
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
				// -2表示窗体包裹内容
				window = new PopupWindow(vw, -2, -2);
				// 动画效果的播放必须要求弹出窗体有背景颜色
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
		// 格式化内存大小
		String ls = Formatter.formatFileSize(this, l);
		tv.setText("内存可用:" + ls);
		String ld = Formatter.formatFileSize(this, d);
		tvone.setText("sd卡可用:" + ld);

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
			// 明确点击事件的位置
			if (position == 0) {
				TextView tv = new TextView(AppMangerActivity.this);
				tv.setTextColor(Color.BLACK);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("用户程序" + userapp.size() + "个");
				// 记得return
				return tv;
			} else if (position == (userapp.size() + 1)) {
				TextView tv = new TextView(AppMangerActivity.this);
				tv.setTextColor(Color.BLACK);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("系统程序" + systemapp.size() + "个");
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
				tvone.setText("手机内存");
			} else {
				tvone.setText("外部存储");
			}
			image.setImageDrawable(appinfo.getIcon());
			return view;
		}
	}

	/**
	 * 获取内存空间的大小
	 * 
	 * @param path
	 *            路径
	 * @return
	 */
	private long retriveRoom(String path) {
		StatFs fs = new StatFs(path);
		// 获取内存空间的个数
		fs.getBlockCount();
		// 获取空间的大小
		long l = fs.getBlockSize();
		// 获取可用的内存个数
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
				Toast.makeText(AppMangerActivity.this, "无法卸载系统程序",
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
	 * 分享软件
	 */
	private void shareApp() {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setAction("android.intent.action.SEND");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT,
				"推荐您使用一款好用的软件,软件名叫:" + appinfo.getAppName());
		startActivity(intent);
	}

	/**
	 * 卸载软件
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
	 * 启动软件
	 */
	private void intentActivity() {
		// TODO Auto-generated method stub
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(appinfo.getPackName());
		// 判断intent是否为空
		if (intent != null) {
			startActivity(intent);
		}
	}

}
