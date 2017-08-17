package com.example.tapan.dllogin.activity.adapter;

import android.hardware.Camera;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;

/**
 * Created by Tapan on 2/28/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{

    ArrayList <Fragment> fragments = new ArrayList<>();
    ArrayList <String> tabTitles = new ArrayList<>();

    public void addFragments (Fragment fragment, String titles){
        this.fragments.add(fragment);
        this.tabTitles.add(titles);
    }

    public ViewPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }
}

