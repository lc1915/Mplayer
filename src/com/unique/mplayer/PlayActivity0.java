package com.unique.mplayer;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class PlayActivity0 extends Activity {
	private TextView musicTitle = null;
	private TextView musicArtist = null;
	private ImageButton previousBtn; // 上一首
	private Button repeatBtn; // 重复（单曲循环、全部循环）
	private ImageButton playBtn; // 播放（播放、暂停）
	private Button shuffleBtn; // 随机播放
	private ImageButton nextBtn; // 下一首
	private Button queueBtn; // 播放列表
	ImageButton mButton;
	private SeekBar music_progressBar; // 歌曲进度
	private TextView currentProgress; // 当前进度消耗的时间
	private TextView finalProgress; // 歌曲时间

	private static String title; // 歌曲标题
	private static String artist; // 歌曲艺术家
	static String url; // 歌曲路径
	private static int listPosition; // 播放歌曲在mp3Infos的位置
	private int currentTime; // 当前歌曲播放时间
	private int duration; // 歌曲长度
	private int flag; // 播放标识

	private final int isCurrentRepeat = 1; // 单曲循环
	private final int isAllRepeat = 2; // 全部循环
	private final int isNoneRepeat = 3; // 无重复播放
	private int repeatState = isNoneRepeat; // 初始状态为无重复播放状态;
	private boolean isNoneShuffle = true; // 顺序播放
	private boolean isShuffle = false; // 随机播放
	private boolean isPlaying; // 正在播放
	private boolean isPause; // 暂停
	private static int changed = 0;

	private List<Mp3Info> mp3Infos;
	public static LrcView lrcView; // 自定义歌词视图
	String TAG = "PlayActivity0";
	Intent intent;

	private PlayerReceiver playerReceiver;
	public static final String UPDATE_ACTION = "com.unique.action.UPDATE_ACTION"; // 更新动作
	public static final String CONTROL_ACTION = "com.unique.action.CONTROL_ACTION"; // 控制动作
	public static final String MUSIC_CURRENT = "com.unique.action.MUSIC_CURRENT"; // 音乐当前时间改变动作
	public static final String MUSIC_DURATION = "com.unique.action.MUSIC_DURATION";// 音乐播放长度改变动作
	public static final String MUSIC_PLAYING = "com.unique.action.MUSIC_PLAYING"; // 音乐正在播放动作
	public static final String REPEAT_ACTION = "com.unique.action.REPEAT_ACTION"; // 音乐重复播放动作
	public static final String SHUFFLE_ACTION = "com.unique.action.SHUFFLE_ACTION";// 音乐随机播放动作
	public static final String SHOW_LRC = "com.unique.action.SHOW_LRC"; // 通知显示歌词

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);// 去标题栏
		setContentView(R.layout.play_music0);
		// 使用布局文件来定义标题栏
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title0);
		
		mButton = (ImageButton)this.findViewById(R.id.imageButton1);
        //按钮监听
        mButton.setOnClickListener(new View.OnClickListener() 
        {
            
            @Override
            public void onClick(View v) 
            {
                // TODO Auto-generated method stub
                finish();
            }
        });

		musicTitle = (TextView) findViewById(R.id.title);
		musicArtist = (TextView) findViewById(R.id.artist);

		findViewById();
		setViewOnclickListener();
		mp3Infos = MainActivity.mp3Infos;
		playerReceiver = new PlayerReceiver();

		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_ACTION);
		filter.addAction(MUSIC_CURRENT);
		filter.addAction(MUSIC_DURATION);
		registerReceiver(playerReceiver, filter);
	}

	@SuppressWarnings("deprecation")
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onStart();

		if (url == null) {
			intent = getIntent();
			Log.e(TAG, intent.getStringExtra("title"));
			title = intent.getStringExtra("title");
			artist = intent.getStringExtra("artist");
			url = intent.getStringExtra("url");
			Log.e(TAG, "AAA" + url);
			listPosition = intent.getIntExtra("listPosition", listPosition);
			Log.e(TAG, "AAA" + listPosition);
			int MSG = 0;
			flag = intent.getIntExtra("MSG", MSG);
		} else {
			Log.e(TAG, "BBB" + url);

			listPosition = SampleListFragment.listPosition;
			Mp3Info mp3Info = MainActivity.mp3Infos.get(listPosition);
			url = mp3Info.getUrl();

		}

		try {
			// 获取通知管理器
			NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
					intent, 0);

			// 创建Notification 即通知
			Notification notification = new Notification();
			// 设置通知图标
			notification.icon = R.drawable.ic_launcher;
			// 设置通知栏显示的信息与点击发出的PendingIntent，其实就是自身播放器这个activity。
			notification.setLatestEventInfo(this, "正在播放音乐", title,
					pendingIntent);
			// 发出Notification
			notificationManager.notify(0, notification);
		} catch (Exception e) {
			if (changed == 0) {

				listPosition = MusicListActivity0.listPosition;
				Mp3Info mp3Info = MusicListActivity0.mp3Infos.get(listPosition);
				url = mp3Info.getUrl();
				Intent intent = new Intent(PlayActivity0.this,
						PlayService0.class);
				intent.putExtra("title", mp3Info.getTitle());
				intent.putExtra("url", mp3Info.getUrl());
				Log.e(TAG, mp3Info.getUrl());
				Log.e(TAG, changed + "");
				intent.putExtra("artist", mp3Info.getArtist());
				intent.putExtra("listPosition", listPosition);
				intent.putExtra("progress", currentTime);
				intent.putExtra("MSG", Constant.PlayerMsg.PLAY_MSG);
				startService(intent);
			} else {
				Intent intent = new Intent(PlayActivity0.this,
						PlayService0.class);
				intent.putExtra("title", title);
				intent.putExtra("url", url);
				Log.e(TAG + "111", url);
				Log.e(TAG, changed + "");
				intent.putExtra("artist", artist);
				Log.e(TAG, "listposition=" + listPosition);
				intent.putExtra("listPosition", listPosition);
				intent.putExtra("progress", currentTime);
				intent.putExtra("MSG", Constant.PlayerMsg.PLAY_MSG);
				startService(intent);
			}
		}

		initView();

		play();
		Log.e("a", "进入service");
	}

	// 从界面上根据id获取按钮
	private void findViewById() {
		previousBtn = (ImageButton) findViewById(R.id.last);
		//repeatBtn = (Button) findViewById(R.id.repeat_music);
		playBtn = (ImageButton) findViewById(R.id.pause_or_play);
		//shuffleBtn = (Button) findViewById(R.id.shuffle_music);
		nextBtn = (ImageButton) findViewById(R.id.next);
		//queueBtn = (Button) findViewById(R.id.play_queue);
		music_progressBar = (SeekBar) findViewById(R.id.audioTrack);
		//currentProgress = (TextView) findViewById(R.id.current_progress);
		//finalProgress = (TextView) findViewById(R.id.final_progress);
		lrcView = (LrcView) findViewById(R.id.lrcShowView);
	}

	// 给每一个按钮设置监听器
	private void setViewOnclickListener() {
		ViewOnclickListener ViewOnClickListener = new ViewOnclickListener();
		previousBtn.setOnClickListener(ViewOnClickListener);
		//repeatBtn.setOnClickListener(ViewOnClickListener);
		playBtn.setOnClickListener(ViewOnClickListener);
		//shuffleBtn.setOnClickListener(ViewOnClickListener);
		nextBtn.setOnClickListener(ViewOnClickListener);
		//queueBtn.setOnClickListener(ViewOnClickListener);
		music_progressBar
				.setOnSeekBarChangeListener(new SeekBarChangeListener());
	}

	// 初始化界面
	public void initView() {
		musicTitle.setText(title);
		musicArtist.setText(artist);
		music_progressBar.setProgress(currentTime);
		music_progressBar.setMax(duration);
		/*
		switch (repeatState) {
		case isCurrentRepeat: // 单曲循环
			Log.e("aaa", "单曲循环啊啊啊");
			shuffleBtn.setClickable(false);
			//repeatBtn.setBackgroundResource(R.drawable.repeat_current_selector);
			break;
		case isAllRepeat: // 全部循环
			Log.e("aaa", "全部循环啊啊啊");
			shuffleBtn.setClickable(false);
			//repeatBtn.setBackgroundResource(R.drawable.repeat_all_selector);
			break;
		case isNoneRepeat: // 无重复
			Log.e("aaa", "无重复啊啊啊");
			shuffleBtn.setClickable(true);
			//repeatBtn.setBackgroundResource(R.drawable.repeat_none_selector);
			break;
		}
		if (isShuffle) {
			isNoneShuffle = false;
			//shuffleBtn.setBackgroundResource(R.drawable.shuffle_selector);
			repeatBtn.setClickable(false);
		} else {
			isNoneShuffle = true;
			//shuffleBtn.setBackgroundResource(R.drawable.shuffle_none_selector);
			repeatBtn.setClickable(true);
		}*/

		playBtn.setBackgroundResource(R.drawable.pause_selector);
		isPlaying = true;
		isPause = false;
	}

	// 反注册广播
	@Override
	protected void onStop() {
		super.onStop();
		try {
			unregisterReceiver(playerReceiver);
		} catch (Exception e) {

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// 控件点击事件
	private class ViewOnclickListener implements OnClickListener {
		Intent intent = new Intent(PlayActivity0.this,
				PlayService0.class);
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.pause_or_play:
				if (isPlaying) {
					playBtn.setBackgroundResource(R.drawable.play_selector);
					intent.setAction("com.unique.media.MUSIC_SERVICE");
					intent.putExtra("MSG", Constant.PlayerMsg.PAUSE_MSG);
					startService(intent);
					isPlaying = false;
					isPause = true;

				} else if (isPause) {
					playBtn.setBackgroundResource(R.drawable.pause_selector);
					intent.setAction("com.unique.media.MUSIC_SERVICE");
					intent.putExtra("MSG", Constant.PlayerMsg.CONTINUE_MSG);
					startService(intent);
					isPause = false;
					isPlaying = true;
				}
				break;
			case R.id.last: // 上一首歌曲
				isPlaying = true;
				isPause = false;
				changed = 1;
				previous_music();
				break;
			case R.id.next: // 下一首歌曲
				isPlaying = true;
				isPause = false;
				changed = 1;
				next_music();
				break;
			case R.id.repeat_music: // 重复播放音乐
				if (repeatState == isNoneRepeat) {
					repeat_one();
					shuffleBtn.setClickable(false); // 是随机播放变为不可点击状态
					repeatState = isCurrentRepeat;
				} else if (repeatState == isCurrentRepeat) {
					repeat_all();
					shuffleBtn.setClickable(false);
					repeatState = isAllRepeat;
				} else if (repeatState == isAllRepeat) {
					repeat_none();
					shuffleBtn.setClickable(true);
					repeatState = isNoneRepeat;
				}
				Intent intent = new Intent(REPEAT_ACTION);
				switch (repeatState) {
				case isCurrentRepeat: // 单曲循环
					//repeatBtn
							//.setBackgroundResource(R.drawable.repeat_current_selector);
					Toast.makeText(PlayActivity0.this, "repeat_current",
							Toast.LENGTH_SHORT).show();

					intent.putExtra("repeatState", isCurrentRepeat);
					sendBroadcast(intent);
					break;
				case isAllRepeat: // 全部循环
					//repeatBtn
							//.setBackgroundResource(R.drawable.repeat_all_selector);
					Toast.makeText(PlayActivity0.this, "repeat_all",
							Toast.LENGTH_SHORT).show();
					intent.putExtra("repeatState", isAllRepeat);
					sendBroadcast(intent);
					break;
				case isNoneRepeat: // 无重复
					//repeatBtn
							//.setBackgroundResource(R.drawable.repeat_none_selector);
					Toast.makeText(PlayActivity0.this, "repeat_none",
							Toast.LENGTH_SHORT).show();
					intent.putExtra("repeatState", isNoneRepeat);
					break;
				}
				break;
			case R.id.shuffle_music: // 随机播放状态
				Intent shuffleIntent = new Intent(SHUFFLE_ACTION);
				if (isNoneShuffle) { // 如果当前状态为非随机播放，点击按钮之后改变状态为随机播放
					//shuffleBtn
							//.setBackgroundResource(R.drawable.shuffle_selector);
					Toast.makeText(PlayActivity0.this, "shuffle",
							Toast.LENGTH_SHORT).show();
					isNoneShuffle = false;
					isShuffle = true;
					shuffleMusic();
					repeatBtn.setClickable(false);
					shuffleIntent.putExtra("shuffleState", true);
					sendBroadcast(shuffleIntent);
				} else if (isShuffle) {
					//shuffleBtn
							//.setBackgroundResource(R.drawable.shuffle_none_selector);
					Toast.makeText(PlayActivity0.this, "shuffle_none",
							Toast.LENGTH_SHORT).show();
					isShuffle = false;
					isNoneShuffle = true;
					repeatBtn.setClickable(true);
					shuffleIntent.putExtra("shuffleState", false);
					sendBroadcast(shuffleIntent);
				}
				break;
			case R.id.play_queue:// 播放列表
				Intent intent0 = new Intent();
				intent0.setClass(PlayActivity0.this, MusicListActivity0.class);
				startActivity(intent0);
				finish();
				break;
			}
		}
	}

	// 实现监听Seekbar的类
	private class SeekBarChangeListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (fromUser) {
				audioTrackChange(progress); // 用户控制进度的改变
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}

	}

	// 播放音乐
	public void play() {
		// 开始播放的时候为顺序播放
		repeat_none();
		Intent intent = new Intent(PlayActivity0.this,
				PlayService0.class);
		intent.setAction("com.unique.media.MUSIC_SERVICE");
		intent.putExtra("url", url);
		intent.putExtra("listPosition", listPosition);
		intent.putExtra("MSG", flag);
		startService(intent);
	}

	// 随机播放
	public void shuffleMusic() {
		Intent intent = new Intent(CONTROL_ACTION);
		intent.putExtra("control", 4);
		sendBroadcast(intent);
	}

	public void audioTrackChange(int progress) {
		Intent intent = new Intent(PlayActivity0.this,
				PlayService0.class);
		intent.setAction("com.unique.media.MUSIC_SERVICE");
		intent.putExtra("url", url);
		intent.putExtra("listPosition", listPosition);
		if (isPause) {
			playBtn.setBackgroundResource(R.drawable.pause_selector);
			intent.putExtra("MSG", Constant.PlayerMsg.PROGRESS_CHANGE);
			isPause = false;
			isPlaying = true;
		} else {
			intent.putExtra("MSG", Constant.PlayerMsg.PROGRESS_CHANGE);
		}
		intent.putExtra("progress", progress);
		startService(intent);
	}

	// 单曲循环
	public void repeat_one() {
		Intent intent = new Intent(CONTROL_ACTION);
		intent.putExtra("control", 1);
		sendBroadcast(intent);
	}

	// 全部循环
	public void repeat_all() {
		Intent intent = new Intent(CONTROL_ACTION);
		intent.putExtra("control", 2);
		sendBroadcast(intent);
	}

	// 顺序播放
	public void repeat_none() {
		Intent intent = new Intent(CONTROL_ACTION);
		intent.putExtra("control", 3);
		sendBroadcast(intent);
	}

	// 上一首
	public void previous_music() {
		playBtn.setBackgroundResource(R.drawable.pause_selector);
		listPosition--;
		changed = 1;
		if (listPosition >= 0) {
			Mp3Info mp3Info = mp3Infos.get(listPosition); // 上一首MP3
			musicTitle.setText(mp3Info.getTitle());
			musicArtist.setText(mp3Info.getArtist());
			url = mp3Info.getUrl();
			Intent intent = new Intent(PlayActivity0.this,
					PlayService0.class);
			intent.setAction("com.unique.media.MUSIC_SERVICE");
			intent.putExtra("url", mp3Info.getUrl());
			intent.putExtra("listPosition", listPosition);
			intent.putExtra("MSG", Constant.PlayerMsg.PRIVIOUS_MSG);
			startService(intent);
		} else {
			Toast.makeText(PlayActivity0.this, "没有上一首了", Toast.LENGTH_SHORT)
					.show();
			listPosition++;
		}
	}

	// 下一首
	public void next_music() {
		playBtn.setBackgroundResource(R.drawable.pause_selector);
		listPosition++;
		if (listPosition <= mp3Infos.size() - 1) {
			Mp3Info mp3Info = mp3Infos.get(listPosition);
			url = mp3Info.getUrl();
			musicTitle.setText(mp3Info.getTitle());
			musicArtist.setText(mp3Info.getArtist());
			Intent intent = new Intent(PlayActivity0.this,
					PlayService0.class);
			intent.setAction("com.unique.media.MUSIC_SERVICE");
			intent.putExtra("url", mp3Info.getUrl());
			intent.putExtra("listPosition", listPosition);
			intent.putExtra("MSG", Constant.PlayerMsg.NEXT_MSG);
			startService(intent);
		} else {
			Toast.makeText(PlayActivity0.this, "没有下一首了", Toast.LENGTH_SHORT)
					.show();
			listPosition--;
		}
	}

	/* 按返回键弹出对话框确定退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {

			new AlertDialog.Builder(this)
					.setIcon(R.drawable.ic_launcher)
					.setTitle("退出")
					.setMessage("你确定要退出音乐播放器吗？")
					.setNegativeButton("取消", null)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish();
									Intent intent = new Intent(
											PlayActivity0.this,
											PlayService0.class);
									// unregisterReceiver(playerReceiver);
									stopService(intent); // 停止后台服务
								}
							}).show();

		}
		return super.onKeyDown(keyCode, event);
	}*/

	// 用来接收从service传回来的广播的内部类
	public class PlayerReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(MUSIC_CURRENT)) {
				currentTime = intent.getIntExtra("currentTime", -1);
				//currentProgress.setText(MainActivity
						//.formatTime(currentTime));
				music_progressBar.setProgress(currentTime);
			} else if (action.equals(MUSIC_DURATION)) {
				Log.e("PlayActivity0", "接受duration广播");
				int duration = intent.getIntExtra("duration", -1);
				music_progressBar.setMax(duration);
				//finalProgress.setText(MainActivity.formatTime(duration));
			} else if (action.equals(UPDATE_ACTION)) {
				// 获取Intent中的current消息，current代表当前正在播放的歌曲
				listPosition = intent.getIntExtra("current", -1);
				Log.e(TAG, listPosition + "");
				url = mp3Infos.get(listPosition).getUrl();
				Log.e(TAG, url);
				if (listPosition >= 0) {
					musicTitle.setText(mp3Infos.get(listPosition).getTitle());
					musicArtist.setText(mp3Infos.get(listPosition).getArtist());
				}
				if (listPosition == 0) {
					//finalProgress.setText(MainActivity
							//.formatTime(mp3Infos.get(listPosition)
									//.getDuration()));
					playBtn.setBackgroundResource(R.drawable.pause_selector);
					isPause = true;
				}
			}
		}

	}

}