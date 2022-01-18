package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

public class pro_adapter extends RecyclerView.Adapter<pro_adapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<promodel> mlist;
    private pro_adapter.OnItemClickListener mlistener;
    Intent intent;
    String h_id,pro_id,avail,curreq;
    FirebaseFirestore db;
    private CollectionReference mref,mref1;
    FirebaseAuth firebaseAuth;
    int av,curr;

    public interface OnItemClickListener {
        void onItemClick(int position);

    }

    public void setOnItemClickListener(pro_adapter.OnItemClickListener listener) {
        mlistener = listener;
    }


    pro_adapter(Context context, ArrayList<promodel> list) {
        mcontext = context;
        mlist = list;
    }

    @NonNull
    @Override
    public pro_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mcontext);

        View view = layoutInflater.inflate(R.layout.activity_pro_adapter, parent, false);

        pro_adapter.ViewHolder viewHolder = new pro_adapter.ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull pro_adapter.ViewHolder holder, int position) {
        final promodel pitem = mlist.get(position);

        final TextView hname = holder.hname;
        TextView req = holder.req;
        Button respond = holder.respond;

        hname.setText("Hospital Name: " + pitem.gethname());
        req.setText("Current requirement: " + pitem.getreq());




        respond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View view1 = view;
                db = FirebaseFirestore.getInstance();
                firebaseAuth = FirebaseAuth.getInstance();
                final String user = firebaseAuth.getCurrentUser().getUid();
                final String hid = pitem.getId();
                mref1 = db.collection("provider");

                final DocumentReference dref = mref1.document(user).collection("order").document();
                final DocumentReference dref1 = db.collection("hospital").document(hid)
                        .collection("order").document();

                //pro_id = dref.getId();
               // h_id = dref1.getId();


                mref1.document(user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                avail=document.getString("available stock");
                                av=Integer.parseInt(avail);
                                curreq=pitem.getreq();
                                curr=Integer.parseInt(curreq);
                                if(curr<=av)
                                {
                                    av=av-curr;
                                    dref.update("status","completed")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        Toast.makeText(view1.getContext(),"Request fulfilled Successfully",Toast.LENGTH_LONG).show();
                                                        Log.d("Status","Success");
                                                    }
                                                    else
                                                    {
                                                        Log.d("Status","failed");
                                                    }
                                                }
                                            });

                                    dref1.update("status","completed")
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful())
                                                    {
                                                        //Toast.makeText(view1.getContext(),"Request fulfilled Successfully",Toast.LENGTH_LONG).show();
                                                        Log.d("Status","Success");
                                                    }
                                                    else
                                                    {
                                                        Log.d("Status","failed");
                                                    }
                                                }
                                            });
                                }
                                else
                                {
                                    Toast.makeText(view1.getContext(),"Not enough stock",Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Log.d("LOGGER", "No such document");
                            }
                        } else {

                        Log.d("LOGGER", "No such document");
                    }


                    }
                });

               /* mref1.document(user).update("available stock",String.valueOf(av)).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            //Toast.makeText(view1.getContext(),"Appointement confirmed",Toast.LENGTH_LONG).show();
                            Log.d("LOGGER", "stock updated");
                        }

                    }
                });*/

            }


        });


    }



    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView hname,req;
        Button respond;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            hname = itemView.findViewById(R.id.hname);
            req = itemView.findViewById(R.id.req);

            respond = itemView.findViewById(R.id.respond);

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
