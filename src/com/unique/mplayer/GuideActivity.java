package com.unique.mplayer;

import java.util.ArrayList;  
import java.util.List;  
import android.app.Activity;  
import android.os.Bundle;  
import android.support.v4.view.ViewPager;  
import android.support.v4.view.ViewPager.OnPageChangeListener;  
import android.view.LayoutInflater;  
import android.view.View;   

public class GuideActivity extends Activity implements OnPageChangeListener {  
  
    private ViewPager vp;  
    private ViewPagerAdapter vpAdapter;  
    private List<View> views;  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.guide);  
  
        // 初始化页面  
        initViews();  
    }  
  
    private void initViews() {  
        LayoutInflater inflater = LayoutInflater.from(this);  
  
        views = new ArrayList<View>();  
        // 初始化引导图片列表  
        views.add(inflater.inflate(R.layout.guide_view1, null));  
        views.add(inflater.inflate(R.layout.guide_view2, null));  
        views.add(inflater.inflate(R.layout.guide_view3, null));  
        views.add(inflater.inflate(R.layout.guide_view0, null));  
  
        // 初始化Adapter  
        vpAdapter = new ViewPagerAdapter(views, this);  
          
        vp = (ViewPager) findViewById(R.id.viewpager);  
        vp.setAdapter(vpAdapter);  
        // 绑定回调  
        vp.setOnPageChangeListener(this);  
    }  
  
  
    // 当滑动状态改变时调用  
    @Override  
    public void onPageScrollStateChanged(int arg0) {  
    }  
  
    // 当当前页面被滑动时调用  
    @Override  
    public void onPageScrolled(int arg0, float arg1, int arg2) {  
    }  
  
    // 当新的页面被选中时调用  
    @Override  
    public void onPageSelected(int arg0) {  
        // 设置底部小点选中状态  
    }  
  
}  