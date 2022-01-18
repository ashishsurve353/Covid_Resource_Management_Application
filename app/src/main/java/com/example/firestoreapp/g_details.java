package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static com.example.firestoreapp.App.CHANNEL_1_ID;

public class g_details extends AppCompatActivity {
    private CollectionReference mref,mref1;
    private NotificationManagerCompat notificationManager;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    String proid;
    TextView pro_name,pro_id,av_stock;
    String avast;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void sendnotification(String status,String hname)
    {
        if(status.equals("requested"))
        {
            notificationManager = NotificationManagerCompat.from(g_details.this);
            Notification notification = new NotificationCompat.Builder(g_details.this, CHANNEL_1_ID).setSmallIcon(R.drawable.ic_menu_camera)
                    .setContentTitle("PPE KIT REQUEST").setContentText( hname + " has placed ppe kit order").setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE).build();
            notificationManager.notify(1, notification);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_details);

        final FirebaseStorage storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        mref =  db.collection("provider");
        firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser user1=firebaseAuth.getCurrentUser();
        proid=user1.getUid();


        mref1 = db.collection("provider").document(proid).collection("order");

        Query query = mref.whereEqualTo("status","requested");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    Log.d("Status","Successful");



                    for(QueryDocumentSnapshot document: task.getResult())
                    {

                        Log.d("Status","Doc retreived");

                        //Adding listener
                        String id = document.get("prodocref").toString();
                        DocumentReference docref = mref.document(id);

                        docref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {
                                if (e != null) {
                                    Log.w("Status", "Listen failed.", e);
                                    return;
                                }

                                String source = snapshot != null && snapshot.getMetadata().hasPendingWrites()
                                        ? "Local" : "Server";

                                if (snapshot != null && snapshot.exists()) {
                                    Log.d("Status", source + " data: " + snapshot.getData());
                                    //Notification function
                                    //String docname = snapshot.get("docname").toString();
                                    String status = snapshot.get("status").toString();
                                    String hname = snapshot.get("hospitalname").toString();
                                    sendnotification(status,hname);

                                } else {
                                    Log.d("status", source + " data: null");
                                }
                            }
                        });



                    }
                }
                else
                {

                    Log.d("Status","UNSuccessful");

                }

            }
        });



        pro_name=(TextView)findViewById(R.id.textView2);
        pro_id=(TextView)findViewById(R.id.textView4);
        av_stock=(TextView)findViewById(R.id.textView6);

        mref.document(proid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {

                    DocumentSnapshot document  = task.getResult();
                    if(document.exists())
                    {
                        pro_name.setText(document.get("name").toString());
                        pro_id.setText(document.get("organization id").toString());
                        av_stock.setText(document.get("available stock").toString());
                        avast=document.get("available stock").toString();
                    }


                }
                else
                {
                    Log.i("Error","Document not present");
                }
            }
        });
    }

    public void update(View view) {

        Intent intent = new Intent(getApplicationContext(),update_stock.class);
        intent.putExtra("proid",proid);
        intent.putExtra("avast",avast);
        startActivity(intent);

    }

    public void check_req(View view) {
        Intent intent = new Intent(getApplicationContext(),glist.class);
        startActivity(intent);
    }

    public void Logout(View view) {
        firebaseAuth.getInstance().signOut();
        Intent intent=new Intent(g_details.this,MainActivity.class);
        startActivity(intent);
    }
}
