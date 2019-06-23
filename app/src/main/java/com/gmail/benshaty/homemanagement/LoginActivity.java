package com.gmail.benshaty.homemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    //private Firebase databaseReference;
    private ProgressBar progressBar;
    private Button btnLogin;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance("https://home-management-4b2b0.firebaseio.com/").getReference("users");
    private UserModel activeUser = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        //TODO: remove comment to autologin;
        /*
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, TenantActivity.class));
            finish();
        }
        */

        setContentView(R.layout.activity_login);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnLogin = (Button) findViewById(R.id.btn_login);
        ImageView simpleImageView=(ImageView) findViewById(R.id.imageView1);
        simpleImageView.setImageResource(R.drawable.building);//set the source in java class

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = inputEmail.getText().toString();
                final String password = inputPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        auth.createUserWithEmailAndPassword(email, password)
                                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        progressBar.setVisibility(View.GONE);
                                                        if (!task.isSuccessful()) {
                                                            Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException(),
                                                                    Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            checkIfUserExistInDB();
                                                        }
                                                    }
                                                });
                                    }
                                } else {
                                    checkIfUserExistInDB();
                                }
                            }
                        });
            }
        });
    }

    private boolean addUserToDb() {
        try {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String[] userNameSplit = inputEmail.getText().toString().split("@");
            activeUser = new UserModel(inputEmail.getText().toString(), userNameSplit[0], 1, uid);
            DatabaseReference usersRef = rootRef.child("users");
            usersRef.child(uid).setValue(activeUser);
            return true;
        } catch (Exception ex) {
            return false;
        }

    }

    private void checkIfUserExistInDB() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference uidRef = rootRef.child("users").child(uid);
        ValueEventListener userEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    if (addUserToDb()) {
                        Intent intent = new Intent(LoginActivity.this, TenantActivity.class);
                        intent.putExtra("user", activeUser);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    try {
                        UserModel user = new UserModel(dataSnapshot.child("userEmail").getValue(String.class),
                                dataSnapshot.child("userName").getValue(String.class),
                                dataSnapshot.child("userLevel").getValue(Integer.class),
                                dataSnapshot.getKey(),
                                dataSnapshot.child("totalTime").getValue(Integer.class),
                                dataSnapshot.child("timeToPay").getValue(Integer.class));
                        Intent intent = new Intent(LoginActivity.this, TenantActivity.class);
                        intent.putExtra("user", user);
                        startActivity(intent);
                        finish();
                    } catch (Exception ex) {
                        inputEmail.getText().clear();
                        inputPassword.getText().clear();
                        Toast.makeText(LoginActivity.this, "Something got wrong!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        uidRef.addListenerForSingleValueEvent(userEventListener);

    }

}
