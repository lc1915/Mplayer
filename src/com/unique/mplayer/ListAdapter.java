package com.unique.mplayer;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {
	private Context mContext;
	private View mLastView;
	private int mLastPosition;

	public ListAdapter(Context context) {
		this.mContext = context;
	}

	@Override
	public int getCount() {
		return MainActivity.mp3Infos.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(mContext);
			convertView = inflater.inflate(R.layout.list_item, null);
			holder = new Holder();
			holder.textView = (TextView) convertView
					.findViewById(R.id.title);
			holder.textView0=(TextView)convertView.findViewById(R.id.duration);
			holder.imageView=(ImageView)convertView.findViewById(R.id.picture);
			holder.UEFAView = (Button) convertView
					.findViewById(R.id.shoucang);
			holder.mascotView = (Button) convertView
					.findViewById(R.id.xiangxi);
			holder.hint = convertView.findViewById(R.id.hint_image);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		Mp3Info map=MainActivity.mp3Infos.get(position);
		String title=map.getTitle();
		String duration=formatTime(map.getDuration());
		holder.textView.setText(title);
		holder.textView0.setText(duration);
		
		return convertView;
	}

	class Holder {
		TextView textView;
		TextView textView0;
		ImageView imageView;
		Button UEFAView;
		Button mascotView;
		View hint;
	}

	public void changeImageVisable(View view, int position) {
		if (mLastView != null && mLastPosition != position) {
			Holder holder = (Holder) mLastView.getTag();
			switch (holder.hint.getVisibility()) {
			case View.VISIBLE:
				holder.hint.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		}
		mLastPosition = position;
		mLastView = view;
		Holder holder = (Holder) view.getTag();
		switch (holder.hint.getVisibility()) {
		case View.GONE:
			holder.hint.setVisibility(View.VISIBLE);
			break;
		case View.VISIBLE:
			holder.hint.setVisibility(View.GONE);
			break;
		}
	}
	
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
