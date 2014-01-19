package com.unique.mplayer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends BaseAdapter {  
    private Context mContext;  
    private View mLastView;  
    private int mLastPosition;  
      
    public ListAdapter(Context sampleListFragment) {  
        this.mContext = sampleListFragment;  
    }  
  
    public ListAdapter(SampleListFragment sampleListFragment) {
		// TODO Auto-generated constructor stub
	}

	@Override  
    public int getCount() {  
        return 8;  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return null;  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return 0;  
    }  
  
    public View getView1(int position, View convertView, ViewGroup parent) {  
        Holder holder;  
        if(convertView == null ) {  
            LayoutInflater inflater = LayoutInflater.from(mContext);  
            convertView = inflater.inflate(R.layout.list_item, null);  
            holder =new Holder();  
            holder.textView = (TextView)convertView.findViewById(R.id.textView);  
            holder.UEFAView = (ImageView)convertView.findViewById(R.id.image_uefa);  
            holder.mascotView = (ImageView)convertView.findViewById(R.id.image_mascot);  
            holder.hint = convertView.findViewById(R.id.hint_image);  
            convertView.setTag(holder);  
        } else {  
            holder = (Holder) convertView.getTag();  
        }  
        holder.textView.setText("Hello,It is " + position);  
        return convertView;  
    }  
      
    class Holder {  
        TextView textView;  
        ImageView UEFAView;  
        ImageView mascotView;  
        View hint;  
    }  
      
    public void changeImageVisable(View view,int position) {  
        if(mLastView != null && mLastPosition != position ) {  
            Holder holder = (Holder) mLastView.getTag();  
            switch(holder.hint.getVisibility()) {  
            case View.VISIBLE:  
                holder.hint.setVisibility(View.GONE);  
                break;  
            default :  
                break;  
            }  
        }  
        mLastPosition = position;  
        mLastView = view;  
        Holder holder = (Holder) view.getTag();  
        switch(holder.hint.getVisibility()) {  
        case View.GONE:  
            holder.hint.setVisibility(View.VISIBLE);  
            break;  
        case View.VISIBLE:  
            holder.hint.setVisibility(View.GONE);  
            break;  
        }  
    }

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		return null;
	}  
  
}  