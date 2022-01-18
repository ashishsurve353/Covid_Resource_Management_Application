package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    private int tabNumbers;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior,int tabs) {
        super(fm, behavior);
        this.tabNumbers=tabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AddFragment();
            case 1:
                return new CheckFragment();
            case 2:
                return new StatusFragment();
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return tabNumbers;
    }
}
