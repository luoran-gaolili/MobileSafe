package com.example.test;

import java.util.List;
import java.util.Random;

import android.test.AndroidTestCase;
import android.text.format.Formatter;

import com.example.bean.BlackNumberBean;
import com.example.bean.TaskInfo;
import com.example.db.BlackListInfo;
import com.example.db.MySQLiteOpenHelper;
import com.example.streamtools.AdminRunningProcess;
import com.example.streamtools.AdminTaskInfo;

public class ProjectTest extends AndroidTestCase {
	public void testOncrete() {
		MySQLiteOpenHelper openHelper = new MySQLiteOpenHelper(getContext(),
				"blacklist.db", null, 1);
		openHelper.getWritableDatabase();
	}

	public void testAdd() {
		BlackListInfo info = new BlackListInfo(getContext());
		long l = 13500000000l;
		Random random = new Random();
		for (int i = 0; i < 100; i++) {
			info.add(String.valueOf(l + i),
					String.valueOf(random.nextInt(3) + 1));
		}

	}

	public void testFind() {
		BlackListInfo info = new BlackListInfo(getContext());
		boolean bool = info.find("110");
		assertEquals(true, bool);

	}

	public void testFindAll() {
		BlackListInfo info = new BlackListInfo(getContext());
		List<BlackNumberBean> bean = info.findAll();
		for (BlackNumberBean info1 : bean) {
			System.out.println(info1.toString());
		}
	}

	public void testFindMode() {
		BlackListInfo info = new BlackListInfo(getContext());
		System.out.println(info.findMode("18119503697"));
	}

	public void test() {
		AdminRunningProcess a = new AdminRunningProcess();
		System.out.println(Formatter.formatFileSize(getContext(),
				a.adminTotalMem()));
	}

	public void testInfo() {
		AdminTaskInfo info = new AdminTaskInfo();
		List<TaskInfo> list = info.adminTaskInfo(getContext());
		System.out.println(list.toString());
	}
}
