package com.example.firestoreapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment {

    public StatusFragment() {
        // Required empty public constructor
    }

    TextView textViewData;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String DocName, email;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference civilref = db.collection("Civilian");
    private static List<String> list = new ArrayList<String>();
    private static String data = "";
    private static String s;
    private static String id;
    private CollectionReference notebookRef = db.collection("Users");
    private CollectionReference CustomerRef = db.collection("Civilian");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        textViewData = view.findViewById(R.id.data);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        email = fAuth.getCurrentUser().getEmail();
        list = new ArrayList<String>();
        data="";
        CustomerRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    id = documentSnapshot.getId();
                    list.add(id);//here i Stored all document of civilref into the list
                }
                for (int i = 0; i < list.size(); i++) {
                    CustomerRef.document(list.get(i)).collection("Receive").whereEqualTo("Org Email", email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                String good = documentSnapshot.getString("Goods");
                                String description = documentSnapshot.getString("Quantity");
                                String xName = documentSnapshot.getString("Civilian Email");
                                String gD = documentSnapshot.getString("Received Date");
                                String Area = documentSnapshot.getString("Place of Donation");

                                data += "Area: " + Area + "\nGoods : " + good + "\t     Quantity : " + description + "\nReceiver : " + xName + "\nGiven Date : " + gD +"\n\n";
                            }
                            textViewData.setText(data);
                        }
                    });
                }
            }
        });
        return view;
    }
}
