package com.example.hobbymanagementapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;


public class LogInActivity extends AppCompatActivity {

    EditText emailEditText;
    EditText passwordEditText;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;
    String displayName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.hobbymanagementapp", Context.MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        String password = sharedPreferences.getString("password", "");
        //String username = sharedPreferences.getString("userName", "");
        if(email.length()>0 && password.length()>0)
        {
            Log.i("shared preferences:", email +' '+ password);
            firebaseLogIn(email, password);
        }
    }

    public void register(View view) {
        Log.i("register pressed", "ok");

        Intent intentRegister = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intentRegister);
    }

    public void login(View view) {
        Log.i("login pressed", "ok");

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Log.i("email & pwd", email + " "+ password);
        if(email.length()>0 && password.length()>0) {
            firebaseLogIn(email, password);
        }
    }
    public void firebaseLogIn(String email,String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d("task", "success");
                            sharedPreferences.edit().putString("email", email).apply();
                            sharedPreferences.edit().putString("password", password).apply();
                            FirebaseUser user = mAuth.getCurrentUser();
                            displayName = user.getDisplayName();

                            for (UserInfo userInfo : user.getProviderData()) {
                                if (displayName == null && userInfo.getDisplayName() != null) {
                                    displayName = userInfo.getDisplayName();
                                }
                            }

                            String id = "";
                            if(displayName!= null)
                            {
                                Log.i("name", displayName);
                            }
                            id = user.getUid();
                            Log.i("id", id);
                            Intent mainMenuIntent = new Intent(getApplicationContext(), HobbyActivity.class);
                            sharedPreferences.edit().putString("userName", displayName).apply();
                            sharedPreferences.edit().putString("uid", id).apply();
                            startActivity(mainMenuIntent);
                        }else{
                            Log.d("log in", "fail");
                        }
                    }
                });
    }
}