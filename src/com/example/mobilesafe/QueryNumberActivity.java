package com.example.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.streamtools.QueryNumberLocation;

public class QueryNumberActivity extends Activity {
	private EditText edittext;
	private TextView textview;
	Vibrator virator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.querynumber_layout);
		edittext = (EditText) findViewById(R.id.query_edittext);
		textview = (TextView) findViewById(R.id.query_textview);
		virator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		edittext.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				String numberLocation = QueryNumberLocation.queryLocation(s
						.toString());
				textview.setText(numberLocation);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void queryLocation(View view) {
		String numberText = edittext.getText().toString().trim();
		if (TextUtils.isEmpty(numberText)) {
			long[] pattern = { 200, 200, 300, 300, 2000, 2000 };
			virator.vibrate(pattern, -1);
			Toast.makeText(this, "ÄúÊäÈëµÄºÅÂëÎª¿Õ£¬Çë¼ì²é", Toast.LENGTH_SHORT).show();

		} else {
			String queryLocation = QueryNumberLocation
					.queryLocation(numberText);
			textview.setText(queryLocation);

		}
	}

}
