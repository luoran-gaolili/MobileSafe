package com.example.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class Setup1Activity extends BaseActivity {
	Button button;
	GestureDetector gesture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setup1);
		button = (Button) findViewById(R.id.nextbutton1);
		gesture = new GestureDetector(this, new SimpleOnGestureListener() {

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				// TODO Auto-generated method stub
				if (Math.abs(velocityX) < 200) {
					Toast.makeText(getApplicationContext(), "������̫����", 0).show();
					return true;
				}

				// ����б���������
				if (Math.abs((e2.getRawY() - e1.getRawY())) > 100) {
					Toast.makeText(getApplicationContext(), "����������", 0).show();

					return true;
				}

				if ((e2.getRawX() - e1.getRawX()) > 200) {
					// ��ʾ��һ��ҳ�棺�������һ���
					System.out.println("��ʾ��һ��ҳ�棺�������һ���");

					return true;

				}

				if ((e1.getRawX() - e2.getRawX()) > 200) {
					// ��ʾ��һ��ҳ�棺�������󻬶�
					System.out.println("��ʾ��һ��ҳ�棺�������󻬶�");
					showNext();
					return true;
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}

		});

	}

	protected void showNext() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.right, R.anim.right_move);

	}

	@Override
	protected void showPre() {
		// TODO Auto-generated method stub
		
	}

}
