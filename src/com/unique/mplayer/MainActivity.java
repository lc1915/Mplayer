package com.unique.mplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;

public class MainActivity extends FragmentActivity {
	private SlidingMenu mSlidingMenu;// 侧边栏的view
	private LeftFragment leftFragment; // 左侧边栏的碎片化view
	private SampleListFragment centerFragment;// 中间内容碎片化的view
	private FragmentTransaction ft; // 碎片化管理的事务
	static List<Mp3Info> mp3Infos;
	String TAG = "MainActivity";
	String duration;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题栏
		setContentView(R.layout.activity_main);
		// 使用布局文件来定义标题栏
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
		Log.e(TAG, "aa");
		Cursor cursor = getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		mp3Infos = new ArrayList<Mp3Info>();

		for (int i = 0; i < cursor.getCount(); i++) {
			Mp3Info mp3Info = new Mp3Info();
			cursor.moveToNext();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID)); // 音乐id
			String title = cursor.getString((cursor
					.getColumnIndex(MediaStore.Audio.Media.TITLE)));// 音乐标题
			String artist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST));// 艺术家
			long duration = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION));// 时长
			long size = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE)); // 文件大小
			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA)); // 文件路径
			int isMusic = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));// 是否为音乐
			if (isMusic != 0) { // 只把音乐添加到集合当中
				mp3Info.setId(id);
				mp3Info.setTitle(title);
				mp3Info.setArtist(artist);
				mp3Info.setDuration(duration);
				mp3Info.setSize(size);
				mp3Info.setUrl(url);
				Log.e(TAG, mp3Info.getTitle());
				mp3Infos.add(mp3Info);
			}
		}

		mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
		mSlidingMenu.setLeftView(getLayoutInflater().inflate(
				R.layout.left_frame, null));
		mSlidingMenu.setCenterView(getLayoutInflater().inflate(
				R.layout.center_frame, null));

		ft = this.getSupportFragmentManager().beginTransaction();
		leftFragment = new LeftFragment();
		ft.replace(R.id.left_frame, leftFragment);
		centerFragment = new SampleListFragment();
		ft.replace(R.id.center_frame, centerFragment);
		ft.commit();
	}

	public void llronclick(View v) {
		Log.e("aaa", "aaaa");
		switch (v.getId()) {
		case R.id.llr_energy_management:
			Intent intent = new Intent(this, DetailsActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	public void showLeft() {
		mSlidingMenu.showLeftView();
	}

	// 格式化时间，将毫秒转换为分:秒格式
	public static String formatTime(long time) {
		String min = time / (1000 * 60) + "";
		String sec = time % (1000 * 60) + "";
		if (min.length() < 2) {
			min = "0" + time / (1000 * 60) + "";
		} else {
			min = time / (1000 * 60) + "";
		}
		if (sec.length() == 4) {
			sec = "0" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 3) {
			sec = "00" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 2) {
			sec = "000" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 1) {
			sec = "0000" + (time % (1000 * 60)) + "";
		}
		return min + ":" + sec.trim().substring(0, 2);
	}

}
