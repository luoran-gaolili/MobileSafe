package com.example.mobilesafe;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bean.BlackNumberBean;
import com.example.db.BlackListInfo;

public class BlackNumberActivity extends Activity {
	List<BlackNumberBean> list;
	List<BlackNumberBean> listone;

	BlackListInfo info;
	MyAdapter adapter;
	Button button;
	ProgressBar bar;
	LinearLayout linear;
	int offset = 0;
	int max = 20;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.blacknumber);
		info = new BlackListInfo(this);
		listone = info.findAll();
		System.out.println(listone.size());
		final ListView listview = (ListView) findViewById(R.id.black_listview);
		bar = (ProgressBar) findViewById(R.id.progress_ber);
		linear = (LinearLayout) findViewById(R.id.linear);
		retireData(listview);
		listview.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				switch (scrollState) {
				case OnScrollListener.SCROLL_STATE_IDLE:
					int position = view.getLastVisiblePosition();
					Log.d("log", position + "");
					if (position == list.size() - 1) {
						offset += max;
						retireData(listview);
					}

					if (position == listone.size() - 1) {
						Log.e("log", "恭喜你，数据加载完成");
						linear.setVisibility(View.INVISIBLE);
						Toast.makeText(BlackNumberActivity.this, "数据加载已完成",
								Toast.LENGTH_SHORT).show();
					}

					break;
				case OnScrollListener.SCROLL_STATE_FLING:
					break;
				case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					break;
				default:
					break;
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (visibleItemCount + firstVisibleItem == totalItemCount) {
					Log.d("log", "滑到底部");
				}

			}
		});

		button = (Button) findViewById(R.id.black_button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder dialog = new Builder(
						BlackNumberActivity.this);
				final AlertDialog alert = dialog.create();
				dialog.setMessage("请输入黑名单号码");
				dialog.setCancelable(true);
				View view = View.inflate(BlackNumberActivity.this,
						R.layout.addblacknumber, null);
				Button button = (Button) view.findViewById(R.id.confim_button);
				Button buttonone = (Button) view
						.findViewById(R.id.confim_buttontwo);
				final CheckBox box = (CheckBox) view
						.findViewById(R.id.confim_box);
				final CheckBox boxone = (CheckBox) view
						.findViewById(R.id.confim_boxone);
				final EditText text = (EditText) view
						.findViewById(R.id.confim_text);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 保存黑名单信息
						String number = text.getText().toString().trim();
						if (TextUtils.isEmpty(number)) {
							Toast.makeText(BlackNumberActivity.this,
									"请输入要拦截的号码", Toast.LENGTH_SHORT).show();
						}
						String mode;
						if (box.isChecked() && boxone.isChecked()) {
							mode = "3";
						} else if (box.isChecked()) {
							mode = "1";
						} else if (boxone.isChecked()) {
							mode = "2";
						} else {
							Toast.makeText(BlackNumberActivity.this, "请选择拦截模式",
									Toast.LENGTH_SHORT).show();
							return;
						}
						BlackListInfo info = new BlackListInfo(
								BlackNumberActivity.this);
						info.add(number, mode);
						BlackNumberBean bean = new BlackNumberBean();
						bean.setNumber(number);
						bean.setMode(mode);
						list.add(0, bean);
						adapter.notifyDataSetChanged();
						alert.dismiss();

					}
				});
				buttonone.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						alert.dismiss();
					}
				});
				alert.setView(view, 0, 0, 0, 0);
				alert.show();
			}
		});

	}

	private void retireData(final ListView listview) {
		linear.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				if (list == null) {
					list = info.findApart(offset, max);
				} else {
					list.addAll(info.findApart(offset, max));
				}

				runOnUiThread(new Runnable() {
					public void run() {
						// TODO Auto-generated method stub
						linear.setVisibility(View.INVISIBLE);
						if (adapter == null) {
							adapter = new MyAdapter();
							listview.setAdapter(adapter);
						} else {
							adapter.notifyDataSetChanged();
						}

					}
				});

			};
		}.start();
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
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
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				view = View.inflate(BlackNumberActivity.this,
						R.layout.blacknumber_item, null);
				holder.textview = (TextView) view
						.findViewById(R.id.tv_black_number);
				holder.textviewone = (TextView) view
						.findViewById(R.id.tv_block_mode);
				holder.imageview = (ImageView) view
						.findViewById(R.id.iv_delete);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}

			String number = list.get(position).getNumber();
			holder.textview.setText(number);
			String mode = list.get(position).getMode();
			if (mode.equals("1")) {
				holder.textviewone.setText("短信拦截");
			}
			if (mode.equals("2")) {
				holder.textviewone.setText("电话拦截");
			}
			if (mode.equals("3")) {
				holder.textviewone.setText("全部拦截");
			}
			holder.imageview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// 删除黑名单号码
					AlertDialog.Builder builder = new Builder(
							BlackNumberActivity.this);
					builder.setTitle("警告");
					builder.setMessage("确定要删除这条记录么？");
					builder.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 删除数据库的内容
									info.delete(list.get(position).getNumber());
									// 更新界面。
									list.remove(position);
									// 通知listview数据适配器更新
									adapter.notifyDataSetChanged();
								}
							});
					builder.setNegativeButton("取消", null);
					builder.show();
				}
			});
			return view;
		}

		class ViewHolder {
			TextView textview;
			TextView textviewone;
			ImageView imageview;
		}

	}

}
