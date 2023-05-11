package com.example.bookevent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Booking extends AppCompatActivity {

    // Retrieve the string variable "myVariable" from the Intent

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking);

        Calendar calendar = Calendar.getInstance();
        Button pay = findViewById(R.id.pay);

        TextView txt = findViewById(R.id.textView25);
        TextView txt1 = findViewById(R.id.textView26);
        ImageView back = findViewById(R.id.imageView2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txt.setText("Sam's Music Event");
        txt1.setText("A long awaited Music concert right at your hometown. Join with your freinds and family and enjoy the show.");


        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user_email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
                Map<String, Object> data = new HashMap<>();
                data.put("Email",user_email);
                data.put("Event_Name", "Sam's Music Event");
                data.put("Date", calendar.getTime().toString());
                data.put("Payment", "Done");


                db.collection("Booking").document(user_email)
                        .set(data)
                        .addOnSuccessListener(aVoid -> {
                            // Handle the success case
                            Toast.makeText(Booking.this, "Booking Done", Toast.LENGTH_LONG).show();
                        })
                        .addOnFailureListener(e -> {
                            // Handle the error case
                        });


                change_payement();
            }
        });
    }


    private void change_payement() {
        Intent intent = new Intent(this, Payment.class);
        startActivity(intent);
    }
}
