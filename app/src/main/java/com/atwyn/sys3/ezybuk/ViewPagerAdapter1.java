package com.atwyn.sys3.ezybuk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by SYS3 on 6/15/2017.
 */

class ViewPagerAdapter1 extends FragmentPagerAdapter {

    public ViewPagerAdapter1(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        if (position ==0) {

            return new LogIn();

        }   return new Registration();
    }

    @Override
    public int getCount() {
        return 2;
    }
}