package com.gmail.benshaty.homemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class AdminActivity extends AppCompatActivity {
    private TextView Textv;
    private FirebaseAuth auth;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance("https://home-management-4b2b0.firebaseio.com/").getReference("users");
    final ArrayList<UserModel> usersList = new ArrayList<UserModel>();
    private ListView listiew;
    private UsersListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin);
        auth = FirebaseAuth.getInstance();
        Textv = (TextView) findViewById(R.id.textView3);
        listiew = (ListView) findViewById(R.id.users_list);
        Intent iin = getIntent();
        Bundle b = iin.getExtras();
        UserModel activeUser = null;
        if (b != null) {
            activeUser = (UserModel) b.get("user");
            if (activeUser.getUserLevel() == 1) {
                Intent intent = new Intent(AdminActivity.this, TenantActivity.class);
                intent.putExtra("user", activeUser);
                startActivity(intent);
                finish();
            }
            Textv.setText(activeUser.getUserName());
        }


        DatabaseReference uidRef = rootRef.child("users");
        ValueEventListener userEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    UserModel user = new UserModel(childDataSnapshot.child("userEmail").getValue(String.class),
                            childDataSnapshot.child("userName").getValue(String.class),
                            Integer.valueOf(childDataSnapshot.child("userLevel").getValue().toString()),
                            childDataSnapshot.getKey(),
                            Integer.valueOf(childDataSnapshot.child("totalTime").getValue().toString()),
                            Integer.valueOf(childDataSnapshot.child("timeToPay").getValue().toString()));
                    if (user != null) {

                        usersList.add(user);
                    }
                }
                updateUI();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        uidRef.addListenerForSingleValueEvent(userEventListener);
    }

    private void updateUI() {
        adapter = new UsersListAdapter(this, R.layout.admin_list_item,usersList);
        listiew.setAdapter(adapter);
        listiew.setItemsCanFocus(true);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                AdminActivity.this.recreate();
                handler.postDelayed(this, 30 * 1000);
            }
        }, 30 * 1000);
    }
    public void updateUserI(){
        AdminActivity.this.recreate();
        adapter.notifyDataSetChanged();

    }


    public void onBackPressed(){
        auth.signOut();
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}


