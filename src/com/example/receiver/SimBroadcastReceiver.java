package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class SimBroadcastReceiver extends BroadcastReceiver {
	private SharedPreferences sp;
	private TelephonyManager tm;

	@Override
	public void onReceive(Context context, Intent intent) {

		sp = context.getSharedPreferences("configd", Context.MODE_PRIVATE);

		boolean protecting = sp.getBoolean("setupd", false);
		if (protecting) {
			// ��������������ִ������ط�
			tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);

			// ��ȡ֮ǰ�����SiM��Ϣ��
			String saveSim = sp.getString("SIM", "") + "afu";

			// ��ȡ��ǰ��sim����Ϣ
			String realSim = tm.getSimSerialNumber();

			// �Ƚ��Ƿ�һ��
			if (saveSim.equals(realSim)) {
				// simû�б��������ͬһ������
				Toast.makeText(context, "SIM��û�б���������ʹ��", 0).show();
			} else {
				// sim �Ѿ���� ��һ�����Ÿ���ȫ����
				System.out.println("sim �Ѿ����");
				Toast.makeText(context, "sim �Ѿ����", 1).show();
				SmsManager.getDefault().sendTextMessage(
						sp.getString("safenumber", ""), null,
						"sim changing....", null, null);
			}

		}

	}
}
