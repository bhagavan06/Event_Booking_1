package com.example.bookevent;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;


public class HomePage extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public String Name;
    public String Place;
    public String Date;
    public DocumentSnapshot data;

    public TextView loc;
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // Convert location into address
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();

                // Show address in TextView or other UI component
                loc.setText(address);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);


        TextView txt1 = findViewById(R.id.textView15);
        TextView txt2 = findViewById(R.id.textView17);
        TextView txt3 = findViewById(R.id.textView9);

        TextView txt4 = findViewById(R.id.textView16);
        TextView txt5 = findViewById(R.id.textView18);
        TextView txt6 = findViewById(R.id.textView10);

        DocumentReference documentRef = db.collection("Events").document("Event_1");
        documentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Name = document.getString("Name");
                        Place = document.getString("Place");
                        Date = document.getString("Date");

                        txt1.setText(Name);
                        txt2.setText(Place);
                        txt3.setText(Date);

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        DocumentReference documentRef2 = db.collection("Events").document("Event_2");
        documentRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Name = document.getString("Name");
                        Place = document.getString("Place");
                        Date = document.getString("Date");

                        txt4.setText(Name);
                        txt5.setText(Place);
                        txt6.setText(Date);

                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        ImageView pro = findViewById(R.id.profile);
        pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_profile();
            }
        });

        Button bk_btn1 = findViewById(R.id.bk1);
        bk_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_book();
            }
        });

        Button bk_btn2 = findViewById(R.id.bk2);
        bk_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_book1();
            }
        });

        ImageView logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signOut();
                Intent intent = new Intent(HomePage.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        loc = findViewById(R.id.loc);
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            Toast.makeText(this, "Please enable the location permision!", Toast.LENGTH_SHORT).show();
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
// Request location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        TextView book = findViewById(R.id.textView30);
        String user_email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        db.collection("Booking").document(user_email)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String Name = document.getString("Event_Name");
                            String Date = document.getString("Date");
                            book.setText(Name + "\n" + Date);
                        } else {



                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                });


    }

    private void change_book1() {

        Intent intent = new Intent(this, Booking1.class);
        startActivity(intent);

    }

    private void change_book() {
        Intent intent = new Intent(this, Booking.class);
        startActivity(intent);
    }
    // Create a new Intent to start ActivityB

    public void change_profile() {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}