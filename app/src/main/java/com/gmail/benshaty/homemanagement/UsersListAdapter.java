package com.gmail.benshaty.homemanagement;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

class UsersListAdapter extends ArrayAdapter<UserModel> {
    private Context userContext;
    private int userResource;
    private DatabaseReference rootRef = FirebaseDatabase.getInstance("https://home-management-4b2b0.firebaseio.com/").getReference("users");
    private TextView tvUsername;
    private TextView tvEmail;
    private TextView tvLevel;
    private TextView tvTotlaTime;
    private TextView tvTimeToPay;
    private TextView tvTimeToPayFinal;
    private Button btnMonthToAdd;
    private EditText etMonthToAdd;

    public UsersListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<UserModel> objects) {
        super(context, resource, objects);
        this.userContext = context;
        this.userResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String username = getItem(position).getUserName();
        final String email = getItem(position).getUserEmail();
        String level = String.valueOf(getItem(position).getUserLevel());
        String timeToPay = String.valueOf(getItem(position).getTimeToPay());
        String totalTime = String.valueOf(getItem(position).getTotalTime());
        final int positionFinal = position;
        LayoutInflater inflater = LayoutInflater.from(userContext);
        convertView = inflater.inflate(userResource,parent,false);
        final View convertViewFinal = convertView;

        tvUsername = (TextView) convertView.findViewById(R.id.user_username);
        tvEmail = (TextView) convertView.findViewById(R.id.user_email);
        tvLevel = (TextView) convertView.findViewById(R.id.user_level);
        tvTotlaTime = (TextView) convertView.findViewById(R.id.listTotalTime);
        tvTimeToPay = (TextView) convertView.findViewById(R.id.listTimeToPay);
        tvTimeToPayFinal = (TextView) convertViewFinal.findViewById(R.id.listTimeToPay);
        btnMonthToAdd    = (Button)convertView.findViewById(R.id.listAddBtn);
        etMonthToAdd   = (EditText) convertView.findViewById(R.id.listNumberEditText);

        btnMonthToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Query query = rootRef.child("users").child(getItem(positionFinal).getUid());
                ValueEventListener valueEventListener = new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {

                            UserModel user = getItem(positionFinal);
                            String uid = user.getUid();
                            Log.d("benshatyerr",uid);
                            etMonthToAdd = (EditText) convertViewFinal.findViewById(R.id.listNumberEditText);
                            int number = Integer.parseInt(etMonthToAdd.getText().toString());
                            etMonthToAdd.setText("0");
                            user.setTotalTime(number,1234);
                            user.addTimeToPay(number);
                            Log.d("benshatyerr",String.valueOf(number));
                            Log.d("benshatyerr total time",String.valueOf(user.getTotalTime()));
                            dataSnapshot.getRef().child("totalTime").setValue(user.getTotalTime());
                            dataSnapshot.getRef().child("timeToPay").setValue(user.getTimeToPay());
                        notifyDataSetChanged();
                            tvTimeToPay = (TextView) convertViewFinal.findViewById(R.id.listTimeToPay);;
                            tvTimeToPay.setText(String.valueOf(user.getTimeToPay()));
                            tvTotlaTime = (TextView) convertViewFinal.findViewById(R.id.listTotalTime);;
                            tvTotlaTime.setText(String.valueOf(user.getTotalTime()));
                        }

                    @Override
                    public void onCancelled(DatabaseError databaseError)
                    {

                    }
                };
                query.addListenerForSingleValueEvent(valueEventListener);

            }
        });
        tvUsername.setText(username.trim());
        tvEmail.setText(email.trim());
        tvLevel.setText(level.trim());
        tvTotlaTime.setText(totalTime.trim());
        tvTimeToPay.setText(timeToPay.trim());

        return convertView;
    }
    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
