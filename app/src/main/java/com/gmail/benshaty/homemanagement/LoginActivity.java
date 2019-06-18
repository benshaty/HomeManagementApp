package com.gmail.benshaty.homemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    //private Firebase databaseReference;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

/*
        if (auth.getCurrentUser() != null) {
            //TODO: fix
            // startActivity(new Intent(LoginActivity.this, MainActivity.class));
            checkIfUserExistInDB();
            //finish();
        }
*/
        // set the view now
        setContentView(R.layout.activity_login);

        //TODO: fix
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);


        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();
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
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        inputPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    checkIfUserExistInDB();
                                    //TODO: fix
                                    // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    // startActivity(intent);
                                    Toast.makeText(LoginActivity.this,"go to main activity",Toast.LENGTH_LONG);
                                    //finish();
                                }
                            }
                        });
            }
        });
    }
/*
    @Override
    public void onStart(){
        super.onStart();
        databaseReference = new Firebase("https://home-management-4b2b0.firebaseio.com/");

    }
*/

    private boolean addUserToDb(){
        try {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String[] userNameSplit = inputEmail.getText().toString().split("@");
            UserModel userModel = new UserModel( inputEmail.getText().toString(), userNameSplit[0] , 1);
            DatabaseReference usersRef = rootRef.child("users");
            usersRef.child(uid).setValue(userModel);
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
                if(!dataSnapshot.exists()) {
                    addUserToDb() ;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        uidRef.addListenerForSingleValueEvent(userEventListener);
    }

/*
    private void checkUserType() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("Users").child(auth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                int userType = (userProfile.getUsertype());

                switch (userType) {
                    case 0:
                        startActivity(new Intent(Login.this, DoctorActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(Login.this, MainActivity.class));
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    */
}
