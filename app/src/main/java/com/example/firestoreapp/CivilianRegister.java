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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class CivilianRegister extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText mName,mEmail,mAddress,mPhone,mAadharNum,mFamName,mPassword,mRpassword;
    Button mRegister;
    TextView mSignIn;
    private static String []Aadhar ;
    private static int i=0;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    public Map<String,Object> user;
    public Map<String,Object> abc;
    FirebaseFirestore db= FirebaseFirestore.getInstance();

    private CollectionReference civilref = db.collection("Civilian");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_civilian_register);
        mName     =   findViewById(R.id.cName);
        mEmail    =   findViewById(R.id.cEmail);
        mAddress  =   findViewById(R.id.cAddress);
        mPhone    =   findViewById(R.id.cPhone);
        mAadharNum =  findViewById(R.id.AadharNumbers);
        mPassword =   findViewById(R.id.cPassword);
        mRpassword=   findViewById(R.id.cRePassword);
        mRegister =   findViewById(R.id.cRegister);
        mSignIn   =   findViewById(R.id.SignIn);
        i=0;

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if( fAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(getApplicationContext(),CivilianLogin.class));
            finish();
        }

        mSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CivilianLogin.class));
            }
        });


        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = mName.getText().toString().trim();
                final String email = mEmail.getText().toString().trim();
                final String address = mAddress.getText().toString().trim();
                final String phone = mPhone.getText().toString().trim();
                String FAN = mAadharNum.getText().toString().trim();
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
                if(TextUtils.isEmpty(FAN))
                {
                    mAadharNum.setError("Aadhar number is Required");
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
                if(((FAN.length()+1)%13)!=0)
                {
                    mAadharNum.setError("Enter valid Aadhar Number");
                    return;
                }
                if(phone.length()!=10)
                {
                    mPhone.setError("Enter valid Phone Number");
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
                            String FAN = mAadharNum.getText().toString().trim();
                            Aadhar = FAN.split("\\s*,\\s*");
                            //userID = fAuth.getCurrentUser().getUid();
                            //DocumentReference documentReference = fStore.collection("Civilian").document(userID);
                            user = new HashMap<>();
                            abc= new HashMap<>();

                            user.put("Name",name);
                            user.put("Email id",email);
                            user.put("Address",address);
                            user.put("Phone",phone);
                            for(final String u : Aadhar){
                                boolean a;
                                civilref.whereEqualTo(u,"Aadhar").addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                                        if(queryDocumentSnapshots.isEmpty()) {

                                            user.put(u, "Aadhar");

                                            civilref.document(email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.e(TAG, "user is created for" + userID);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.e(TAG, "onfailure:" + e.toString());
                                                }
                                            });
                                            /*civilref.document(email).set(abc, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG,"user is created for"+userID);

                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.d(TAG,"onfailure:"+e.toString());
                                                }
                                            });*/
                                            //Log.e( "Aadhar",user.get("Aadhar").toString());
                                            Toast.makeText(CivilianRegister.this, "user created", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                i++;

                            }
                            user.put("Number of Family Members",i);
                            i=0;
                            startActivity(new Intent(getApplicationContext(),CivilianLogin.class));
                        }
                        else {
                            Toast.makeText(CivilianRegister.this,"Error in creating the user " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}