package com.example.sofiane.esiee_drive.Adapter;

/**
 * Created by Sofiane on 18/01/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.sofiane.esiee_drive.Fragments.editUserEmailFragment;
import com.example.sofiane.esiee_drive.Fragments.editUserInfoFragment;
import com.example.sofiane.esiee_drive.Fragments.editUserPwdFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                editUserInfoFragment tab3 = new editUserInfoFragment();
                return tab3;

            case 1:
                editUserEmailFragment tab1 = new editUserEmailFragment();
                return tab1;
            case 2:
                editUserPwdFragment tab2 = new editUserPwdFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
