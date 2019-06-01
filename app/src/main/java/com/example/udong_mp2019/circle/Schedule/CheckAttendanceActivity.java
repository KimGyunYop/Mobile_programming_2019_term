package com.example.udong_mp2019.circle.Schedule;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.udong_mp2019.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;

public class CheckAttendanceActivity extends AppCompatActivity {

    ArrayList<String> uid = new ArrayList<>();
    CutomAdapterChedkAttendance aad_displayAttendance;
    String path,circleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attendance);
        path = getIntent().getStringExtra("path");
        circleName = getIntent().getStringExtra("circleName");
        aad_displayAttendance = new CutomAdapterChedkAttendance(getApplicationContext(),uid,circleName,path);
        ListView lv_displayAttendance = findViewById(R.id.LV_checkAttendance);
        lv_displayAttendance.setAdapter(aad_displayAttendance);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference().child("circle/"+circleName+"/member").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uid.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    uid.add(postSnapshot.getKey());
                }
                aad_displayAttendance.reset(uid);
                aad_displayAttendance.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
