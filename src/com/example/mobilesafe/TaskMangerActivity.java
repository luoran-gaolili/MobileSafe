package com.example.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bean.TaskInfo;
import com.example.streamtools.AdminRunningProcess;
import com.example.streamtools.AdminTaskInfo;

public class TaskMangerActivity extends Activity {
	private static final String TAG = "TaskMangerActivity";
	TextView tv, tv1, tv2;
	List<TaskInfo> taskinfo;
	List<TaskInfo> userTask;
	List<TaskInfo> systemTask;
	ListView listview;
	MyAdapter adapter;
	RelativeLayout relative;
	ViewHolder holder;
	TaskInfo info;
	long processCount;
	long mem;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.taskmanger);
		tv = (TextView) findViewById(R.id.app_tv);
		tv1 = (TextView) findViewById(R.id.app_tvone);
		tv2 = (TextView) findViewById(R.id.app_tvthree);
		relative = (RelativeLayout) findViewById(R.id.app_linear);
		setTitle();
		listview = (ListView) findViewById(R.id.app_list);
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (userTask != null && systemTask != null) {
					tv2.setVisibility(View.VISIBLE);
					if (firstVisibleItem <= userTask.size()) {
						tv2.setText("用户进程" + userTask.size() + "个");
					} else {
						tv2.setText("系统进程" + systemTask.size() + "个");
					}
				}
			}
		});
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0 || position == userTask.size() + 1) {
					return;
				} else if (position <= userTask.size() + 1) {
					int newPosition = position - 1;
					info = userTask.get(newPosition);
				} else {
					int newPosition = position - 1 - userTask.size() - 1;
					info = systemTask.get(newPosition);
				}
				System.out.println(info.getAppName());
				// 从view中获取缓存对象
				holder = (ViewHolder) view.getTag();
				if (info.isChecked()) {
					info.setChecked(false);
					holder.checkbox.setChecked(false);
				} else {
					info.setChecked(true);
					holder.checkbox.setChecked(true);
				}

			}
		});
		relative.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				taskinfo = AdminTaskInfo.adminTaskInfo(TaskMangerActivity.this);
				userTask = new ArrayList<TaskInfo>();
				systemTask = new ArrayList<TaskInfo>();
				for (TaskInfo infos : taskinfo) {
					if (infos.isUserProcess()) {
						userTask.add(infos);
					} else {
						systemTask.add(infos);
					}
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter = new MyAdapter();
						listview.setAdapter(adapter);
						relative.setVisibility(View.INVISIBLE);

					}
				});

			}
		}).start();

	}

	private void setTitle() {
		processCount = AdminRunningProcess
				.adminProcessCount(TaskMangerActivity.this);
		mem = AdminRunningProcess.adminAvaiableMem(TaskMangerActivity.this);
		tv.setText("运行中的进程:" + processCount + "个");
		tv1.setText("剩余/总内存:"
				+ Formatter.formatFileSize(TaskMangerActivity.this, mem)
				+ "/"
				+ Formatter.formatFileSize(TaskMangerActivity.this,
						AdminRunningProcess.adminTotalMem()));
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return userTask.size() + systemTask.size() + 2;
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

			if (position == 0) {
				TextView tv = new TextView(TaskMangerActivity.this);
				tv.setTextColor(Color.BLACK);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("用户进程" + userTask.size() + "个");
				// 记得return
				return tv;
			} else if (position == (userTask.size() + 1)) {
				TextView tv = new TextView(TaskMangerActivity.this);
				tv.setTextColor(Color.BLACK);
				tv.setBackgroundColor(Color.GRAY);
				tv.setText("系统进程" + systemTask.size() + "个");
				return tv;
			} else if (position < userTask.size() + 1) {
				int newPosition = position - 1;
				info = userTask.get(newPosition);
			} else {
				int newPosition = position - userTask.size() - 1 - 1;
				info = systemTask.get(newPosition);

			}
			View view;

			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();

			} else {
				view = View.inflate(TaskMangerActivity.this,
						R.layout.task_item, null);
				holder = new ViewHolder();
				holder.imageview = (ImageView) view
						.findViewById(R.id.app_image);
				holder.tv = (TextView) view.findViewById(R.id.app_itemtv);
				holder.tv1 = (TextView) view.findViewById(R.id.app_itemtvone);
				holder.checkbox = (CheckBox) view
						.findViewById(R.id.app_task_checkbox);
				view.setTag(holder);
			}

			holder.imageview.setBackgroundDrawable(info.getIcon());
			holder.tv.setText(info.getAppName());
			holder.tv1.setText(Formatter.formatFileSize(
					TaskMangerActivity.this, info.getMemSize()) + "");
			if (info.getPackageName().equals(getPackageName())) {
				holder.checkbox.setVisibility(View.INVISIBLE);
			} else {
				holder.checkbox.setChecked(info.isChecked());
			}

			return view;
		}
	}

	class ViewHolder {
		CheckBox checkbox;
		ImageView imageview;
		TextView tv;
		TextView tv1;
	}

	/**
	 * 全选
	 * 
	 * @param view
	 */
	public void selectAll(View view) {
		Log.i(TAG, "全选按钮");
		for (TaskInfo infos : taskinfo) {
			if (!(infos.getPackageName().equals(getPackageName()))) {
				if (!(infos.isChecked())) {
					infos.setChecked(true);
				}
			}
		}
		// 记得刷新view
		adapter.notifyDataSetChanged();
	}

	/**
	 * 反选
	 * 
	 * @param view
	 */
	public void reverseSelect(View view) {
		Log.i(TAG, "反选按钮");
		for (TaskInfo infos : taskinfo) {
			if (infos.isChecked()) {
				infos.setChecked(false);
			} else {
				infos.setChecked(true);
			}
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 一键清理
	 * 
	 * @param view
	 */
	public void clearProcess(View view) {
		Log.i(TAG, "清理按钮");
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		int count = 0;
		int avaiable = 0;
		List<TaskInfo> killProsessMem = new ArrayList<TaskInfo>();
		for (TaskInfo infos : taskinfo) {
			if (infos.isChecked()) {
				if (infos.getPackageName().equals(getPackageName())) {
					return;
				} else {
					am.killBackgroundProcesses(infos.getPackageName());

				}
				if (infos.isUserProcess()) {
					userTask.remove(infos);
				} else {
					systemTask.remove(infos);
				}
				killProsessMem.add(infos);
				count++;
				avaiable += infos.getMemSize();
			}
		}
		taskinfo.removeAll(killProsessMem);
		adapter.notifyDataSetChanged();
		String size = Formatter.formatFileSize(TaskMangerActivity.this,
				avaiable);
		Toast.makeText(TaskMangerActivity.this,
				"已清理" + count + "个进程,共节省" + size + "内存", 0).show();
		processCount -= count;
		mem += avaiable;
		tv.setText("运行中的进程:" + processCount + "个");
		tv1.setText("剩余/总内存:"
				+ Formatter.formatFileSize(this, mem)
				+ "/"
				+ Formatter.formatFileSize(TaskMangerActivity.this,
						AdminRunningProcess.adminTotalMem()));
	}

	/**
	 * 设置
	 * 
	 * @param view
	 */
	public void Setup(View view) {
		Log.i(TAG, "设置按钮");
		Intent intent = new Intent(this, TaskSetupActivity.class);
		startActivity(intent);

	}

}
