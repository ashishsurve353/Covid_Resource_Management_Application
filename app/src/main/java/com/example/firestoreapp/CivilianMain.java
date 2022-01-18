package com.example.firestoreapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class CivilianMain extends AppCompatActivity {
    TabLayout cTablayout;
    ViewPager cviewPager;
    TabItem cfirstitem,cseconditem;
    CPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civilian_main);
        cTablayout = findViewById(R.id.TabLay);
        cviewPager = findViewById(R.id.Vpager);
        cfirstitem = findViewById(R.id.mainItem);
        cseconditem = findViewById(R.id.HistItem);

        adapter = new CPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,cTablayout.getTabCount());
        cviewPager.setAdapter(adapter);


        cTablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                cviewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        cviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(cTablayout));
    }

    public void Logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),CivilianLogin.class));
    }
}


