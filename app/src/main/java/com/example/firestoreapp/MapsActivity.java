package com.example.firestoreapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private FirebaseAuth mAuth;
    private static String id;
    private static List<String> list = new ArrayList<String>();
    private GoogleMap mMap,cmap;
    LocationManager locationManager;
    LocationListener locationListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static String strDate;
    private static ArrayList<Double> latt = new ArrayList<Double>();
    private static ArrayList<Double> lang = new ArrayList<Double>();
    private CollectionReference notebookRef = db.collection("Civilian");
    private CollectionReference NGORef = db.collection("Users");

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1)
        {
            if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
                {

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        latt.clear();
        lang.clear();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Grt the location of nearby NGO
        Date todaydate = new Date();
        SimpleDateFormat d=new SimpleDateFormat("yyyy-MM-dd");
        strDate = d.format(todaydate);


        NGORef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    id = documentSnapshot.getId();
                    list.add(id);//here i Stored all document of civilref into the list
                }
                for(int i=0;i<list.size();i++){

                    NGORef.document(list.get(i)).collection(strDate).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                            if (e != null) {
                                return;
                            }
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                //Toast.makeText(MapsActivity.this, strDate , Toast.LENGTH_LONG).show();
                                //Toast.makeText(MapsActivity.this, documentSnapshot.getString("Area"), Toast.LENGTH_LONG).show();
                                latt.add(documentSnapshot.getDouble("Lat"));
                                lang.add(documentSnapshot.getDouble("Long"));
                            }
                        }
                    });
                }
            }
        });

        mMap = googleMap;
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        // Add a marker in Sydney and move the camera
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mMap.clear();
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("Marker in UserLocation"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//               Toast.makeText(c_location.this, location.toString(), Toast.LENGTH_LONG).show();
                Map<String, Double> loc = new HashMap<>();
                loc.put("lat",location.getLatitude());
                loc.put("long",location.getLongitude());
//                String Id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                // Settings.Secure.ANDROID_ID);
                mAuth = FirebaseAuth.getInstance();

                String  user=mAuth.getCurrentUser().getEmail().toString();
//              updating location of customer
                notebookRef.document(user).update("lat",location.getLatitude());
                notebookRef.document(user).update("log",location.getLongitude());
                Double startLatitude,startLongitude;

                startLatitude=location.getLatitude();
                startLongitude=location.getLongitude();


                float[] results = new float[latt.size()];
                //getting distance of NGO from cutomer
                for (int i=0;i<latt.size();i++) {
                    if(latt.get(i)!=null && lang.get(i) != null) {
                        Location.distanceBetween(startLatitude, startLongitude,latt.get(i), lang.get(i), results);
                        //Toast.makeText(MapsActivity.this, "long" + lang.get(i), Toast.LENGTH_LONG).show();
                        //Toast.makeText(MapsActivity.this, "long" + lang.size(), Toast.LENGTH_LONG).show();
                        LatLng ngo = new LatLng(latt.get(i), lang.get(i));
                        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(HUE_BLUE)).position(ngo).title("Marker in NGOLocation"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(ngo));
                        float distance = results[i];
                        //Toast.makeText(MapsActivity.this, "NGO" + distance, Toast.LENGTH_LONG).show();
                    }
                }
                //


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
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            }
        }

    }
}