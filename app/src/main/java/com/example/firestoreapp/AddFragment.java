package com.example.firestoreapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddFragment extends Fragment {

    public AddFragment() {
        // Required empty public constructor
    }

    public static final String TAG = "TAG";
    TextView Name;
    EditText goods,quantity,area;
    Button add,finish;
    GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    FirebaseAuth fAuth,mAuth;
    FirebaseFirestore fStore;
    String userID,email;
    FirebaseFirestore db= FirebaseFirestore.getInstance();

    private CollectionReference ngoref = db.collection("Users");
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(getContext(),android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        Name = view.findViewById(R.id.aName);
        goods = view.findViewById(R.id.aDonate);
        quantity = view.findViewById(R.id.aQuantity);
        area = view.findViewById(R.id.AddArea);
        add = view.findViewById(R.id.Addbt);
        finish = view.findViewById(R.id.FinishBtn);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        email = fAuth.getCurrentUser().getEmail();
        mAuth = FirebaseAuth.getInstance();


        DocumentReference documentReference = fStore.collection("Users").document(email);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Name.setText(documentSnapshot.getString("Name of the Organization"));
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  user=mAuth.getCurrentUser().getEmail().toString();
                Date todaydate = new Date();
                SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");
                String strDate = d.format(todaydate);
                String Area = area.getText().toString().trim();
                ngoref.document(user).collection(strDate).document(Area).update("Lat",null);
                ngoref.document(user).collection(strDate).document(Area).update("Long",null);
            }

        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                locationManager = (LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
                // Add a marker in Sydney and move the camera
                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {


                        mAuth = FirebaseAuth.getInstance();
                        String  user=mAuth.getCurrentUser().getEmail().toString();

                        Date todaydate = new Date();
                        SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");
                        String strDate = d.format(todaydate);
                        String Agoods = goods.getText().toString().trim();
                        String Aquantity = quantity.getText().toString().trim();
                        String Area = area.getText().toString().trim();
                        Agoods = Agoods.toLowerCase();

                        if (TextUtils.isEmpty(Agoods)) {
                            goods.setError("Goods is Required");
                            return;
                        }
                        if (TextUtils.isEmpty(Aquantity)) {
                            quantity.setError("Quantity is Required");
                            return;
                        }
                        if (TextUtils.isEmpty(Area)) {
                            area.setError("Area is Required");
                            return;
                        }

                        Map<String,Object> suser = new HashMap<>();
                        suser.put("Goods",Agoods);
                        suser.put("Quantity",Aquantity);
                        suser.put("Area",Area);
                        //suser.put("lat",location.getLatitude());
                        //suser.put("log",location.getLongitude());
                        ngoref.document(user).collection(strDate).document(Area).set(suser);
                        ngoref.document(user).collection(strDate).document(Area).update("Lat",location.getLatitude());
                        ngoref.document(user).collection(strDate).document(Area).update("Long",location.getLongitude());
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {

                    }
                };
                if (Build.VERSION.SDK_INT < 23) {
                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
                else {

                    if(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

                        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                    }
                    else {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                    }
                }


            }
        });
        return view;
    }
}