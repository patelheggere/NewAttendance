package com.patelheggere.newattendance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText editTextPwd, editTextUserName;
    private Button mButton;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private boolean isInvalid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        mButton = findViewById(R.id.submit);
        editTextPwd = findViewById(R.id.editTextPassword);
        editTextUserName = findViewById(R.id.editTextUserName);
        progressBar = findViewById(R.id.progress_circular);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextPwd.getText().toString().isEmpty())
                {
                    editTextPwd.setError("Password should not be empty");
                    return;
                }
                if(editTextUserName.getText().toString().isEmpty())
                {
                    editTextUserName.setError("Username should not be empty");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                databaseReference = BaseApp.getFireBaseRef();
                databaseReference = databaseReference.child("login");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot: dataSnapshot.getChildren())
                        {
                            UserModel userModel = new UserModel();
                            userModel = snapshot.getValue(UserModel.class);
                            isInvalid = true;
                            if(editTextPwd.getText().toString().equalsIgnoreCase(userModel.getPassword()) &&
                            editTextUserName.getText().toString().equalsIgnoreCase(userModel.getUsername()))
                            {
                                if(!userModel.getUsername().equalsIgnoreCase("Admin")) {
                                    isInvalid = false;
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("DATA", userModel);
                                    startActivity(intent);
                                }
                                else{
                                    isInvalid = false;
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        isInvalid = false;
    }
}
