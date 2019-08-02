package com.huajie.readbook.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 首页适配器
 * viewpager
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Fragment> mList;
    private FragmentManager mManager;

    public ViewPagerAdapter(Context context, List<Fragment> list, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.mContext = context;
        this.mList = list;
        this.mManager = fragmentManager;
    }
    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
