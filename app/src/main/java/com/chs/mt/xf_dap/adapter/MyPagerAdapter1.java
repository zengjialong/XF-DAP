package com.chs.mt.xf_dap.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class MyPagerAdapter1 extends FragmentPagerAdapter {
    private Context mscontext;
    Fragment currentFragment;
    private List<Fragment> list;

    public MyPagerAdapter1(FragmentManager fm, Context mcontext, List<Fragment> aList) {
        super(fm);
        mscontext = mcontext;
        this.list = aList;
    }

    public MyPagerAdapter1(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        return fragment;
    }



    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        currentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }
    public Fragment getCurrentFragment() {
        return currentFragment;
    }
}
