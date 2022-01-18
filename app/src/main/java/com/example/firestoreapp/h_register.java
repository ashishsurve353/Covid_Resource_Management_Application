package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class h_register extends AppCompatActivity {
    private CollectionReference mref;
    private FirebaseAuth mAuth;

    EditText h_name,h_addr,h_orgid,h_uname,h_pass;
    String name,addr,orgid,uname,pass;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_register);
    }

    public void register(View view) {
        mAuth = FirebaseAuth.getInstance();
        h_name=(EditText)findViewById(R.id.name);
        h_addr=(EditText)findViewById(R.id.addr);
        h_orgid=(EditText)findViewById(R.id.orgid);
        h_uname=(EditText)findViewById(R.id.uname);
        h_pass=(EditText)findViewById(R.id.pwd);

        mref=db.collection("hospital");

        name=h_name.getText().toString();
        addr=h_addr.getText().toString();
        orgid=h_orgid.getText().toString();
        uname=h_uname.getText().toString();
        pass=h_pass.getText().toString();

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
                            data_user.put("current requirement",0);



                            mref.document(user_uid).set(data_user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {


                                            Log.d("Current","Insertion completed");

                                            Toast.makeText(h_register.this,"Registration SUCCESS",Toast.LENGTH_LONG);
                                            Intent intent = new Intent(h_register.this,h_details.class);
                                            startActivity(intent);
                                            finish();


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Log.d("Current","Insertion failed");
                                            Toast.makeText(h_register.this,"Registration Failed",Toast.LENGTH_LONG);
                                        }
                                    });


                        }

                        else
                        {
                            Toast.makeText(h_register.this,"REGISTRATION FAILED",Toast.LENGTH_LONG).show();
                            Log.d("Register","User creation Failed");
                        }
                    }
                });

    }

}
