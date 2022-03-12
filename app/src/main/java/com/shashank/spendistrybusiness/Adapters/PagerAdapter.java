package com.shashank.spendistrybusiness.Adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.shashank.spendistrybusiness.Fragments.ManualInventoryFragment;
import com.shashank.spendistrybusiness.Fragments.ManualInvoiceFragment;
import com.shashank.spendistrybusiness.Fragments.ScanInventoryFragment;
import com.shashank.spendistrybusiness.Fragments.ScanInvoiceFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private final int numOfTabs;
    private ManualInvoiceFragment manualInvoiceFragment;
    private ScanInvoiceFragment scanInvoiceFragment;
    public PagerAdapter(@NonNull FragmentManager fm, int numOfTabs, ManualInvoiceFragment manualInvoiceFragment, ScanInvoiceFragment scanInvoiceFragment) {
        super(fm, numOfTabs);
        this.numOfTabs = numOfTabs;
        this.manualInvoiceFragment = manualInvoiceFragment;
        this.scanInvoiceFragment = scanInvoiceFragment;
    }

    public PagerAdapter(@NonNull FragmentManager fm, int numOfTabs, ManualInvoiceFragment manualInvoiceFragment) {
        super(fm, numOfTabs);
        this.numOfTabs = numOfTabs;
        this.manualInvoiceFragment = manualInvoiceFragment;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return manualInvoiceFragment;
            case 1:
                return scanInvoiceFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
