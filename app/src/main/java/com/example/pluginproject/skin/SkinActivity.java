package com.example.pluginproject.skin;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.pluginproject.BaseActivity;
import com.example.pluginproject.R;
import com.example.pluginproject.skin.fragment.MusicFragment;
import com.example.pluginproject.skin.fragment.MyFragmentPagerAdapter;
import com.example.pluginproject.skin.fragment.RadioFragment;
import com.example.pluginproject.skin.fragment.VideoFragment;
import com.example.pluginproject.skin.widget.MyTabLayout;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

public class SkinActivity extends BaseActivity {

    @BindView(R.id.tabLayout)
    MyTabLayout mMyTabLayout;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    @Override
    public int bindLayout() {
        return R.layout.activity_skin;
    }

    @Override
    public void initView() {
        List<Fragment> list = new ArrayList<>();
        list.add(new MusicFragment());
        list.add(new VideoFragment());
        list.add(new RadioFragment());
        List<String> listTitle = new ArrayList<>();
        listTitle.add("音乐");
        listTitle.add("视频");
        listTitle.add("电台");
        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), list, listTitle);
        mViewPager.setAdapter(myFragmentPagerAdapter);
        mMyTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void doBusiness(Context context) {

    }

    public void skinSelect(View view) {
        Intent intent = new Intent(this, SelectSkinActivity.class);
        startActivity(intent);
    }
}
