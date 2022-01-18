package com.example.firestoreapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


/**
 * A simple {@link Fragment} subclass.
 */
public class CMainFrag extends Fragment {
    TextView PName,FAM;
    Button pLink,pNear;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String email;


    public CMainFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_c_main, container, false);

        PName = view.findViewById(R.id.NamePerson);
        FAM = view.findViewById(R.id.NoFM);
        pNear = view.findViewById(R.id.NearBtn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        email = fAuth.getCurrentUser().getEmail();

        DocumentReference documentReference = fStore.collection("Civilian").document(email);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                PName.setText(documentSnapshot.getString("Name"));
                FAM.setText(documentSnapshot.getLong("Number of Family Members").toString());
            }
        });
        pNear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(),MapsActivity.class));
            }
        });
        return view;
    }
}
