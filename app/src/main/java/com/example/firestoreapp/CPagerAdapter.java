package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class CPagerAdapter extends FragmentPagerAdapter {
    int tabnum;
    public CPagerAdapter(@NonNull FragmentManager fm, int behavior,int tab) {
        super(fm, behavior);
        this.tabnum=tab;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new CMainFrag();
            case 1:
                return new CHistFrag();
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return tabnum;
    }
}
