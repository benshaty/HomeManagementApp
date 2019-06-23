package com.gmail.benshaty.homemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class TenantActivity extends AppCompatActivity {
    private TextView Textv;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance("https://home-management-4b2b0.firebaseio.com/").getReference("users");
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvLevel;
    private TextView tvTotalTime;
    private TextView tvTimeToPay;
    private Button btnMonthToPay;
    private EditText etMonthToPay;
    private UserModel activeUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant);
        Textv = (TextView)findViewById(R.id.textView1);
        tvEmail = (TextView) findViewById(R.id.user_email_t);
        tvLevel = (TextView) findViewById(R.id.user_level_t);
        tvTotalTime = (TextView) findViewById(R.id.listTotalTime_t);
        tvTimeToPay = (TextView) findViewById(R.id.listTimeToPay_t);
        btnMonthToPay    = (Button)findViewById(R.id.listPayBtn);
        etMonthToPay   = (EditText) findViewById(R.id.listNumberEditText_t);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();

        if(b!=null)
        {
            activeUser = (UserModel) b.get("user");
            if (activeUser.getUserLevel() == 2){
                Intent intent = new Intent(TenantActivity.this, AdminActivity.class);
                intent.putExtra("user",activeUser);
                startActivity(intent);
                finish();
            }

        } else {
            Intent intent = new Intent(TenantActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        Textv.setText(activeUser.getUserName());
        tvEmail.setText(activeUser.getUserEmail());
        tvLevel.setText(String.valueOf(activeUser.getUserLevel()));
        tvTimeToPay.setText(String.valueOf(activeUser.getTimeToPay()));
        tvTotalTime.setText(String.valueOf(activeUser.getTotalTime()));
        btnMonthToPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Query query = rootRef.child("users").child(activeUser.getUid());
                ValueEventListener valueEventListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            int number;

                            String uid = activeUser.getUid();
                            etMonthToPay = (EditText) findViewById(R.id.listNumberEditText_t);
                            try{
                                number= Integer.parseInt(etMonthToPay.getText().toString());
                                activeUser.setTimeToPay(number);
                            } catch (NumberFormatException ex) {

                                Log.d("bbb_err",ex.getMessage());
                            }
                            etMonthToPay.setText("");
                            dataSnapshot.getRef().child("timeToPay").setValue(activeUser.getTimeToPay());
                            tvTimeToPay = (TextView) findViewById(R.id.listTimeToPay_t);
                            tvTimeToPay.setText(String.valueOf(activeUser.getTimeToPay()));
                            tvTotalTime = (TextView) findViewById(R.id.listTotalTime_t);
                            tvTotalTime.setText(String.valueOf(activeUser.getTotalTime()));
                        } catch (IndexOutOfBoundsException ex) {
                            etMonthToPay = (EditText) findViewById(R.id.listNumberEditText_t);
                            etMonthToPay.setText("");
                            Toast.makeText(TenantActivity.this,ex.getMessage(),Toast.LENGTH_SHORT).show();
                        } catch (Exception ex) {
                            Log.d("bbb_err",ex.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                query.addListenerForSingleValueEvent(valueEventListener);

            }

        });
    }
    public void onBackPressed(){
        Intent intent = new Intent(TenantActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}