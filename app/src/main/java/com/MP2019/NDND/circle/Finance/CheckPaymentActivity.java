package com.MP2019.NDND.circle.Finance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.MP2019.NDND.R;
import com.MP2019.NDND.login.UserInfoForDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class CheckPaymentActivity extends AppCompatActivity {
    ListView LV_members;
    CustomAdapterFinanceChange cafc_aad;

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef = mRootRef.child("circle");
    private DatabaseReference userRef = mRootRef.child("user");

    ArrayList<String> uid = new ArrayList<>();
    String circleName, planName, due;
    String user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_payment);

        Intent receive =getIntent();
        circleName=receive.getStringExtra("circleName");
        planName = receive.getStringExtra("planName");
        due = receive.getStringExtra("date");

        LV_members=(ListView) findViewById(R.id.LV_memberList);

        cafc_aad= new CustomAdapterFinanceChange(getApplicationContext(),uid,circleName,due,planName);
        LV_members.setAdapter(cafc_aad);

    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference().child("circle/"+circleName+"/member").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uid.clear();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    uid.add(postSnapshot.getKey());
                }
                Log.d("paymentChange",uid.toString());
                cafc_aad.reset(getApplicationContext(),uid);
                cafc_aad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
