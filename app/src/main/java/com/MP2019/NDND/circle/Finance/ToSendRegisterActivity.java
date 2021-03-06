package com.MP2019.NDND.circle.Finance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.MP2019.NDND.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class ToSendRegisterActivity extends AppCompatActivity {
    Button btn_register;

    EditText et_name;
    EditText et_amount;

    DatePicker dp;
    FirebaseUser user;

    private static final Pattern INTEGER_PATTERN =  Pattern.compile("(^[0-9]*$)");

    private DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference circleRef = mRootRef.child("circle");

    private DatabaseReference toSendRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_send_register);
        Log.d("toSendActivity","startToSendActivity");
        et_name = (EditText) findViewById(R.id.et_name);
        et_amount = (EditText) findViewById(R.id.et_amount);
        dp=(DatePicker) findViewById(R.id.datepicker);
        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        String circlename = ((Intent) intent).getStringExtra("circleName");

        btn_register = (Button) findViewById(R.id.btn_tosend_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!INTEGER_PATTERN.matcher(et_amount.getText().toString()).matches() || et_amount.getText().toString().equals("")) {
                    Toast.makeText(ToSendRegisterActivity.this,"금액은 숫자로 입력해주세요.",Toast.LENGTH_LONG).show();
                }
                else {
                    toSendRef = mRootRef.child("circle/" + circlename + "/schedule/tosend");
                    int month= 1+dp.getMonth();
                    String date = dp.getYear() + "-" + month + "-" + dp.getDayOfMonth();
                    Log.d(et_name.getText().toString(), "ET_NAME");
                    toSendRef.child(date).child(et_name.getText().toString()).child("amount").setValue(et_amount.getText().toString());
                    setCheckFinanceFirebaseDatabase(date);
                }
            }

            void setCheckFinanceFirebaseDatabase(String date){
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("toSendActivity","startsetMemeber");
                        for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                            String key = postSnapshot.getKey();
                            toSendRef.child(date).child(et_name.getText().toString()+"/member/"+key).setValue(false);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                };
                Log.d("tosends","circle/"+circlename+"/member");
                Query qurey = FirebaseDatabase.getInstance().getReference().child("circle/"+circlename+"/member").orderByKey();
                qurey.addListenerForSingleValueEvent(postListener);
            }
        });
    }
}