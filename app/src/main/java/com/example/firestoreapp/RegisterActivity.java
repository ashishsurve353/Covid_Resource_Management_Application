package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mName,mEmail,mAddress,mPhone,mHeadname,mPassword,mRpassword;
    Button mRegister;
    TextView mLogindetails,mLogin;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID,xmail;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName     =   findViewById(R.id.Name);
        mEmail    =   findViewById(R.id.Email);
        mAddress  =   findViewById(R.id.Address);
        mPhone    =   findViewById(R.id.Phone);
        mHeadname =   findViewById(R.id.InchargeName);
        mPassword =   findViewById(R.id.Password);
        mRpassword=   findViewById(R.id.RePassword);
        mRegister =   findViewById(R.id.Register);
        mLogindetails=findViewById(R.id.Logindetail);
        mLogin   =   findViewById(R.id.lLogin);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if( fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mName.getText().toString().trim();
                final String email = mEmail.getText().toString().trim();
                final String address = mAddress.getText().toString().trim();
                final String phone = mPhone.getText().toString().trim();
                final String incharge = mHeadname.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String repassword = mRpassword.getText().toString().trim();

                if(TextUtils.isEmpty(name))
                {
                    mName.setError("Name is Required");
                    return;
                }
                if(TextUtils.isEmpty(email))
                {
                    mEmail.setError("Email is Required");
                    return;
                }
                if(TextUtils.isEmpty(address))
                {
                    mAddress.setError("Address is Required");
                    return;
                }
                if(TextUtils.isEmpty(phone))
                {
                    mPhone.setError("Phone Number is Required");
                    return;
                }
                if(phone.length() != 10)
                {
                    mPhone.setError("Enter a valid Phone Number");
                    return;
                }
                if(TextUtils.isEmpty(incharge))
                {
                    mHeadname.setError("Name of the Incharge is Required");
                    return;
                }
                if(TextUtils.isEmpty(password))
                {
                    mPassword.setError("Password is Required");
                    return;
                }
                if(TextUtils.isEmpty(repassword))
                {
                    mRpassword.setError("This field is Required");
                    return;
                }
                if(password.length() < 6)
                {
                    mPassword.setError("Password must be of 6 characters atleast");
                    return;
                }
                if(!(password.contentEquals(repassword)))
                {
                    mRpassword.setError("The password is not same as above");
                    return;
                }

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this,"User created", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            xmail=fAuth.getCurrentUser().getEmail();
                            DocumentReference documentReference = fStore.collection("Users").document(xmail);
                            Map<String,Object> user = new HashMap<>();
                            user.put("Name of the Organization",name);
                            user.put("Email id",email);
                            user.put("Address",address);
                            user.put("Phone",phone);
                            user.put("Incharge Name",incharge);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG,"user is created for"+userID);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG,"onfailure:"+e.toString());
                                }
                            });

                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }
                        else {
                            Toast.makeText(RegisterActivity.this,"Error in creating the user " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }



        });
    }

}
