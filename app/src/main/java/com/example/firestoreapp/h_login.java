package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class h_login extends AppCompatActivity {
    private FirebaseAuth mAuth;

    TextView uname,password;
    String username,pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_login);
    }

    public void login(View view) {
        mAuth = FirebaseAuth.getInstance();

        uname = (TextView) findViewById(R.id.uname);
        password = (EditText) findViewById(R.id.pwd);
        username = uname.getText().toString();
        pass  = password.getText().toString();

        mAuth.signInWithEmailAndPassword(username,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            Log.d("LOGIN", "signInWithEmail:success");
                            Toast.makeText(h_login.this,"Login Successful",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(h_login.this,h_details.class);
                            startActivity(intent);
                            finish();
                        }

                        else
                        {
                            Log.d("LOGIN", "Failed");
                            Toast.makeText(h_login.this,"Login Denied",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }
}
