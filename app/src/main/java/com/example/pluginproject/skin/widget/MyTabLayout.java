package com.example.pluginproject.skin.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.example.pluginproject.R;
import com.google.android.material.tabs.TabLayout;


public class MyTabLayout extends TabLayout {

    private int mTabIndicatorColorResId;
    private int mTabTextColorResId;

    public MyTabLayout(Context context) {
        this(context, null, 0);
    }

    public MyTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout, defStyleAttr, 0);
        mTabIndicatorColorResId = a.getResourceId(R.styleable.TabLayout_tabIndicatorColor, 0);
        mTabTextColorResId = a.getResourceId(R.styleable.TabLayout_tabTextColor, 0);
        a.recycle();
    }


}
