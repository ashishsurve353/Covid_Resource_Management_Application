package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class g_register extends AppCompatActivity {

    private CollectionReference mref;
    private FirebaseAuth mAuth;

    EditText g_name,g_addr,g_orgid,g_uname,g_pass;
    String name,addr,orgid,uname,pass;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_register);
    }

    public void register(View view) {
        mAuth = FirebaseAuth.getInstance();
        g_name=(EditText)findViewById(R.id.name);
        g_addr=(EditText)findViewById(R.id.addr);
        g_orgid=(EditText)findViewById(R.id.orgid);
        g_uname=(EditText)findViewById(R.id.uname);
        g_pass=(EditText)findViewById(R.id.pwd);

        mref=db.collection("provider");

        name=g_name.getText().toString();
        addr=g_addr.getText().toString();
        orgid=g_orgid.getText().toString();
        uname=g_uname.getText().toString();
        pass=g_pass.getText().toString();

        mAuth.createUserWithEmailAndPassword(uname,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {

                            FirebaseUser user1 = mAuth.getCurrentUser();
                            String user_uid = user1.getUid();

                            Map<String,Object> data_user = new HashMap<>();
                            data_user.put("name",name);
                            data_user.put("uid",user_uid);
                            data_user.put("address",addr);
                            data_user.put("organization id",orgid);
                            data_user.put("available stock",0);



                            mref.document(user_uid).set(data_user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {


                                            Log.d("Current","Insertion completed");

                                            Toast.makeText(g_register.this,"Registration SUCCESS",Toast.LENGTH_LONG);
                                            Intent intent = new Intent(g_register.this,g_details.class);
                                            startActivity(intent);
                                            finish();


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Log.d("Current","Insertion failed");
                                            Toast.makeText(g_register.this,"Registration Failed",Toast.LENGTH_LONG);
                                        }
                                    });


                        }

                        else
                        {
                            Toast.makeText(g_register.this,"REGISTRATION FAILED",Toast.LENGTH_LONG).show();
                            Log.d("Register","User creation Failed");
                        }
                    }
                });



    }
}
