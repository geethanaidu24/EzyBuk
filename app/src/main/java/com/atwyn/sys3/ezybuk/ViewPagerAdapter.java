package com.atwyn.sys3.ezybuk;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by SYS3 on 6/6/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }



    @Override
    public Fragment getItem(int position) {
        if (position ==0) {

            return new NowShowing();

        }   return new ComingSoon();
    }

    @Override
    public int getCount() {
        return 2;
    }
}