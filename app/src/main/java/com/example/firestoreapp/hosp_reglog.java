package com.example.firestoreapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class hosp_reglog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosp_reglog);
    }

    public void register(View view) {
        Intent intent = new Intent(getApplicationContext(),h_register.class);
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent = new Intent(getApplicationContext(),h_login.class);
        startActivity(intent);
    }
}
