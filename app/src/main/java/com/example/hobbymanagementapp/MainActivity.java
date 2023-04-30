package com.example.hobbymanagementapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent i = new Intent(getApplicationContext(), LogInActivity.class);
        startActivity(i);
    }

    public void login(View view) {
    }

    public void register(View view) {
    }
}