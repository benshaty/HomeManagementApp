package com.gmail.benshaty.homemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class TenantActivity extends AppCompatActivity {
    private TextView Textv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant);
        Textv = (TextView)findViewById(R.id.textView1);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        UserModel activeUser = null;
        if(b!=null)
        {
            activeUser = (UserModel) b.get("user");
            if (activeUser.getUserLevel() == 2){
                Intent intent = new Intent(TenantActivity.this, AdminActivity.class);
                intent.putExtra("user",activeUser);
                startActivity(intent);
                finish();
            }
            Textv.setText(activeUser.getUserName());
        }
    }
}
