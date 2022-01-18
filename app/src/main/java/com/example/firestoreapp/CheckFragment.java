package com.example.firestoreapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class CheckFragment extends Fragment {

    public CheckFragment() {
        // Required empty public constructor
    }
    EditText cName,cGoods,ddate,dquantity,darea;
    Button Check;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String DocName,email;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    private CollectionReference civilref = db.collection("Civilian");


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_check, container, false);
        cName = view.findViewById(R.id.NameCivil);
        cGoods = view.findViewById(R.id.Goods);
        ddate = view.findViewById(R.id.date);
        dquantity = view.findViewById(R.id.quant);
        darea = view.findViewById(R.id.Carea);
        Check = view.findViewById(R.id.Checkbtn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        email = fAuth.getCurrentUser().getEmail();


        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goods = cGoods.getText().toString().trim();
                final String name = cName.getText().toString().trim();
                String date = ddate.getText().toString().trim();
                String quantity = dquantity.getText().toString().trim();
                String area = darea.getText().toString().trim();


                if (TextUtils.isEmpty(name)) {
                    cName.setError("Email id is Required");
                    return;
                }

                if (TextUtils.isEmpty(goods)) {
                    cGoods.setError("Goods is Required");
                    return;
                }
                if (TextUtils.isEmpty(quantity)) {
                    dquantity.setError("Quantity is Required");
                    return;
                }
                if (TextUtils.isEmpty(date)) {
                    ddate.setError("Date is Required");
                    return;
                }

                if (TextUtils.isEmpty(area)) {
                    darea.setError("Place is Required");
                    return;
                }

                final Map<String,Object> user = new HashMap<>();

                civilref.document(name).collection("Receive").document(goods).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {

                            String date=documentSnapshot.getString("Till Date");
                            SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");

                            Date todaydate = new Date();
                            String strDate = d.format(todaydate);
                            String name = cName.getText().toString().trim();
                            String quantity=dquantity.getText().toString();
                            String goods = cGoods.getText().toString().trim();
                            String xdate = ddate.getText().toString().trim();
                            String area = darea.getText().toString().trim();
                            Map<String, String> user = new HashMap<>();
                            goods = goods.toLowerCase();

                            user.put("Quantity",quantity);
                            user.put("Till Date",xdate);
                            user.put("Goods",goods);
                            user.put("Org Email",email);
                            user.put("Civilian Email",name);
                            user.put("Place of Donation",area);
                            user.put("Received Date",strDate);
                            try {
                                Date date1=d.parse(date);
                                if(todaydate.compareTo(date1)>0)
                                {

                                    Toast.makeText(getContext(),"donation Successful", Toast.LENGTH_SHORT).show();
                                    civilref.document(name).collection("Receive").document(goods).set(user);
                                }
                                else
                                {

                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String date=documentSnapshot.getString("Till Date");
                            SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");

                            Date todaydate = new Date();
                            String strDate = d.format(todaydate);
                            String name = cName.getText().toString().trim();
                            String quantity=dquantity.getText().toString();
                            String area = darea.getText().toString().trim();
                            String goods = cGoods.getText().toString().trim();
                            String xdate = ddate.getText().toString().trim();
                            Map<String, String> user = new HashMap<>();

                            user.put("Quantity",quantity);
                            user.put("Till Date",xdate);
                            user.put("Goods",goods);
                            user.put("Org Email",email);
                            user.put("Civilian Email",name);
                            user.put("Place of Donation",area);
                            user.put("Received Date",strDate);
                            Toast.makeText(getContext(),"donation Successful", Toast.LENGTH_SHORT).show();
                            civilref.document(name).collection("Receive").document(goods).set(user);
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        return view;
    }
}
