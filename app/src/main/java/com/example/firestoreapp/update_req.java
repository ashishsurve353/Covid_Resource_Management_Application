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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class update_req extends AppCompatActivity {
    private CollectionReference mref;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    String hid,requi;
    TextView cur_req;
    EditText newreq;
    String nreq;
    Integer newstock,curstock;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void update_req()
    {
        String current=String.valueOf(curstock);
        mref.document(hid).update("current requirement",current)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Log.d("Update stock","Success");
                            Toast.makeText(update_req.this,"Stock updated Successfully",Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_update_req);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        hid = intent.getStringExtra("hid");
        requi=intent.getStringExtra("requi");

        cur_req=(TextView)findViewById(R.id.textView8);
        cur_req.setText(requi);
    }

    public void update(View view) {


        newreq=(EditText)findViewById(R.id.req);
        nreq=newreq.getText().toString();
        newstock=Integer.parseInt(nreq);
        Log.i("newstock",nreq);
        mref =  db.collection("hospital");
        firebaseAuth = FirebaseAuth.getInstance();

        mref.document(hid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {

                    DocumentSnapshot document  = task.getResult();
                    if(document.exists())
                    {

                        curstock=Integer.parseInt(document.get("current requirement").toString());
                        curstock=curstock+newstock;
                        Log.i("curstock",String.valueOf(curstock));
                        update_req();
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
