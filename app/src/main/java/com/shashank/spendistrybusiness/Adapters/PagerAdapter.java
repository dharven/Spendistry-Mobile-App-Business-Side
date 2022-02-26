package com.shashank.spendistrybusiness.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.shashank.spendistrybusiness.Fragments.ManualInventoryFragment;
import com.shashank.spendistrybusiness.Fragments.ScanInventoryFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private final int numOfTabs;
    public PagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, numOfTabs);
        this.numOfTabs = numOfTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ManualInventoryFragment();
            case 1:
                return new ScanInventoryFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
