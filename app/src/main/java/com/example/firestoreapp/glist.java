package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class glist extends AppCompatActivity {
    FirebaseFirestore db;
    private CollectionReference mref,mref1;
    FirebaseAuth firebaseAuth;
    ArrayList<promodel> prolist;
    Intent intent;
    RecyclerView recyclerView;

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
        setContentView(R.layout.activity_glist);

        intent=getIntent();

        db = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String proid = firebaseAuth.getCurrentUser().getUid();
        mref1 = db.collection("hospital");

        recyclerView = findViewById(R.id.rv2);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLiLayoutManager = layoutManager;
        recyclerView.setLayoutManager(rvLiLayoutManager);
        prolist=new ArrayList<promodel>();

        mref1.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    for(QueryDocumentSnapshot document : task.getResult())
                    {
                        prolist.add(new promodel(document.get("name").toString(),
                                document.get("current requirement").toString(),
                                document.getId().toString()

                        ));



                    }
                    Log.d("Status","added to applist");
                    pro_adapter padapter = new pro_adapter(glist.this,prolist);
                    recyclerView.setAdapter(padapter);
                    padapter.setOnItemClickListener(new pro_adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {

                            // Toast.makeText(getApplicationContext(), "ppe requested", Toast.LENGTH_LONG).show();
                            //We put uid of doctor in intent so that it is easier to extract doctor info
                            // Intent intent1 = new Intent(hlist.this,hospdetail.class);
                            //intent1.putExtra("docid",hosplist.get(position).getId());
                            //startActivity(intent1);


                        }
                    });

                }
                else
                {

                    Log.d("Status","failed");
                }
            }

        });
    }
}
