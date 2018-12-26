package com.developer.sarthak.businessportal;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.developer.sarthak.businessportal.Tab1;
import com.developer.sarthak.businessportal.Tab2;
import com.developer.sarthak.businessportal.Tab3;

/**
 * Created by ASUS on 23-07-2018.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNoofTabs;

    public PagerAdapter(FragmentManager fm, int Nooftabs) {
        super(fm);
        this.mNoofTabs=Nooftabs;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Tab1 tab1=new Tab1();
                return tab1;
            case 1:
                Tab2 tab2=new Tab2();
                return tab2;
            case 2:
                Tab3 tab3=new Tab3();
                return tab3;
            default:
            return null;
        }
    }

    @Override
    public int getCount() {
        return mNoofTabs;
    }
}
