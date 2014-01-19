package com.unique.mplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class SampleListFragment extends ListFragment {

	ListView musicListView;
	private SimpleAdapter mAdapter;
	static int listPosition = 0;
	static int i;
	String duration;
	static List<Mp3Info> mp3Infos;
	String TAG = "SampleListFragment";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.list, null);
		musicListView = (ListView) mView.findViewById(android.R.id.list);
		return mView;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mp3Infos = MainActivity.mp3Infos;

		List<HashMap<String, String>> mp3list = new ArrayList<HashMap<String, String>>();
		for (@SuppressWarnings("rawtypes")
		Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("title", mp3Info.getTitle());
			map.put("artist", mp3Info.getArtist());
			duration = formatTime(mp3Info.getDuration());
			map.put("duration", duration);
			map.put("size", String.valueOf(mp3Info.getSize()));
			map.put("url", mp3Info.getUrl());
			mp3list.add(map);
		}

		mAdapter = new SimpleAdapter(getActivity(), mp3list,
				android.R.layout.simple_list_item_2, new String[] { "title",
						"duration" }, new int[] { android.R.id.text1,
						android.R.id.text2 });
		musicListView.setAdapter(mAdapter);

		// 给listview的每一个item添加点击事件
		musicListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				listPosition = position;
				playMusic(listPosition);
			}
		});

	}

	public void playMusic(int listPosition) {
		if (mp3Infos != null) {
			Mp3Info mp3Info = mp3Infos.get(listPosition);
			Intent intent = new Intent(getActivity(), PlayActivity0.class);
			intent.putExtra("title", mp3Info.getTitle());
			intent.putExtra("url", mp3Info.getUrl());
			intent.putExtra("artist", mp3Info.getArtist());
			intent.putExtra("listPosition", listPosition);
			intent.putExtra("changed", 0);
			intent.putExtra("MSG", Constant.PlayerMsg.PLAY_MSG);
			startActivity(intent);
		}
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
