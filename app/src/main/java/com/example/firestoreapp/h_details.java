package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class h_details extends AppCompatActivity {
    private CollectionReference mref;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    String hid;
    TextView h_name,h_id,h_addr,cur_req;
    String requi;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h_details);

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        mref =  db.collection("hospital");
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user1=firebaseAuth.getCurrentUser();
        hid=user1.getUid();

        h_name=(TextView)findViewById(R.id.hname);
        h_addr=(TextView)findViewById(R.id.haddr);
        h_id=(TextView)findViewById(R.id.hoid);
        cur_req=(TextView)findViewById(R.id.req);

        mref.document(hid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {

                    DocumentSnapshot document  = task.getResult();
                    if(document.exists())
                    {
                        h_name.setText(document.get("name").toString());
                        h_addr.setText(document.get("address").toString());
                        h_id.setText(document.get("organization id").toString());
                        cur_req.setText(document.get("current requirement").toString());
                        requi=document.get("current requirement").toString();
                    }


                }
                else
                {
                    Log.i("Error","Document not present");
                }
            }
        });
    }

    public void update_req(View view) {
        Intent intent = new Intent(getApplicationContext(),update_req.class);
        intent.putExtra("hid",hid);
        intent.putExtra("requi",requi);
        startActivity(intent);


    }

    public void check_av(View view) {
        Intent intent = new Intent(getApplicationContext(),hlist.class);
        startActivity(intent);
    }

    public void Logout(View view) {
        firebaseAuth.getInstance().signOut();
        Intent intent=new Intent(h_details.this,MainActivity.class);
        startActivity(intent);
    }
}
