package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class update_stock extends AppCompatActivity {
    private CollectionReference mref;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    String proid,avast;
    TextView av_stock;
    EditText newppe;
    String nppe;
    Integer newstock,curstock;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void update_stock()
    {
        String current=String.valueOf(curstock);
         mref.document(proid).update("available stock",current)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Log.d("Update stock","Success");
                            Toast.makeText(update_stock.this,"Stock updated Successfully",Toast.LENGTH_LONG).show();
                        }


                        else
                        {
                            Log.d("Update", "failed");
                        }
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_stock);
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        proid = intent.getStringExtra("proid");
        avast=intent.getStringExtra("avast");

        av_stock=(TextView)findViewById(R.id.textView8);
        av_stock.setText(avast);

    }

    public void update(View view) {


        newppe=(EditText)findViewById(R.id.ppe);
        nppe=newppe.getText().toString();
        newstock=Integer.parseInt(nppe);
        Log.i("newstock",nppe);
        mref =  db.collection("provider");
        firebaseAuth = FirebaseAuth.getInstance();

        mref.document(proid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {

                    DocumentSnapshot document  = task.getResult();
                    if(document.exists())
                    {

                        curstock=Integer.parseInt(document.get("available stock").toString());
                        curstock=curstock+newstock;
                        Log.i("curstock",String.valueOf(curstock));
                        update_stock();
                    }


                }
                else
                {
                    Log.i("Error","Document not present");
                }
            }
        });

    }
}
