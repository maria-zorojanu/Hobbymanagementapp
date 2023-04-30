package com.example.hobbymanagementapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    EditText nameEditText;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        nameEditText = findViewById(R.id.editTextName);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.hobbymanagementapp", Context.MODE_PRIVATE);


    }

    public void register(View view) {
        Log.i("register pressed", "ok");

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String name = nameEditText.getText().toString();

        Log.i("email & pwd", email + " "+ password);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.i("task","successful");
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name).build();
                            user.updateProfile(profileUpdates);
                            id = user.getUid();
                            sharedPreferences.edit().putString("email", email).apply();
                            sharedPreferences.edit().putString("password", password).apply();
                            sharedPreferences.edit().putString("userName", name).apply();
                            sharedPreferences.edit().putString("uid", id).apply();

                            //Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                            //startActivity(mainMenuIntent);
                        }else{
                            Log.i("task","failure");
                            Toast toast = Toast.makeText(getApplicationContext(), "Could not register", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    }
                });
    }
}