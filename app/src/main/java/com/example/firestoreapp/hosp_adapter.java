package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.firestoreapp.App.CHANNEL_1_ID;

public class hosp_adapter extends RecyclerView.Adapter<hosp_adapter.ViewHolder>{

private Context mcontext;
private ArrayList<hospmodel> mlist;
private OnItemClickListener mlistener;
        Intent intent;
        String h_id,pro_id,hname,hreq;
        FirebaseFirestore db;
private CollectionReference mref,mref1;
        FirebaseAuth firebaseAuth;
    private NotificationManagerCompat notificationManager;


public interface OnItemClickListener {
    void onItemClick(int position);

}

    public void setOnItemClickListener(OnItemClickListener listener) {
        mlistener = listener;
    }


    hosp_adapter(Context context, ArrayList<hospmodel> list) {
        mcontext = context;
        mlist = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);

        View view = layoutInflater.inflate(R.layout.activity_hosp_adapter, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final hospmodel hitem = mlist.get(position);

        final TextView pname = holder.pname;
        TextView avail = holder.avail;
        Button request = holder.request;

        pname.setText("Provider Name: " + hitem.getpname());
        avail.setText("Current Availabilty: " + hitem.getavail());




            request.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final View view1 = view;
                   db = FirebaseFirestore.getInstance();
                    firebaseAuth = FirebaseAuth.getInstance();
                    final String user = firebaseAuth.getCurrentUser().getUid();
                    final String proid=hitem.getId();
                    mref1 = db.collection("hospital");

                    final DocumentReference dref = mref1.document(user).collection("order").document();
                    final DocumentReference dref1 = db.collection("provider").document(proid)
                            .collection("order").document();

                    h_id = dref.getId();
                    pro_id = dref1.getId();

                    mref1.document(user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {

                                    hname=document.getString("name");
                                    hreq=document.getString("current requirement");

                                    Map<String,String> orderdata = new HashMap<>();

                                    orderdata.put("hospitaluid",user);
                                    orderdata.put("hospitalname",hname);
                                    //Status implies if appointment is accepted by doctor

                                    orderdata.put("status","requested");
                                    orderdata.put("providerid",proid);
                                    orderdata.put("providername",hitem.getpname());

                                    //completed tells if appointment is completed or yet to be completed

                                    orderdata.put("hospdocref",h_id);

                                    orderdata.put("prodocref",pro_id);
                                    orderdata.put("requirement",hreq);

                                    dref1.set(orderdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful())
                                                    {
                                                        Log.d("Status","Success");
                                                        Toast.makeText(view1.getContext(),"PPE kits requested Successfully",Toast.LENGTH_LONG).show();
                                                    }
                                                    else
                                                    {
                                                        Log.d("Status","failed");
                                                    }


                                                }
                                            });

                                    dref.set(orderdata)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        Log.d("Status","Success");
                                                    }
                                                    else
                                                    {
                                                        Log.d("Status","failed");
                                                    }
                                                }
                                            });
                                } else {
                                    Log.d("LOGGER", "No such document");
                                }
                            } else {
                                Log.d("LOGGER", "No such document");
                            }



                        }
                    });



                }
            });


        }



    @Override
    public int getItemCount() {
        return mlist.size();
    }

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView pname,avail;
    Button request;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);


        pname = itemView.findViewById(R.id.pname);
        avail = itemView.findViewById(R.id.avail);

        request = itemView.findViewById(R.id.button7);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mlistener != null) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mlistener.onItemClick(position);
                    }
                }


            }
        });

    }
}
}


