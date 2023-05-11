package com.example.bookevent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        Button register_ = findViewById(R.id.rsgter_);
        EditText email = findViewById(R.id.signemail);
        EditText pass = findViewById(R.id.pass);
        EditText name = findViewById(R.id.name);

        register_.setOnClickListener(view ->

        {
            String Name = name.getText().toString();
            String Email = email.getText().toString();

            Map<String, Object> data = new HashMap<>();
            data.put("Name", Name);
            data.put("Email", Email);
            data.put("Address", "California, USA, 100011");

            db.collection("Book_Event_Users").document(Email)
                            .set(data).addOnCompleteListener(
                                    task ->
                                    {
                                        change_home(email.getText().toString(), pass.getText().toString());
                                    }
                    ).addOnFailureListener(
                            task ->{

                            }
                    );
            }

            );

        TextView txt = findViewById(R.id.lgn_txt);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                change_login();
            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
    private void change_login() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void change_home(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        finish();
                        Intent intent = new Intent(this, HomePage.class);
                        startActivity(intent);
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(), "New User Created",
                                Toast.LENGTH_SHORT).show();
                    } else {

                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
