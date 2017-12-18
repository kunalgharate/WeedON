package com.example.kunalgharate.weedon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;

public class SubscribeActivity extends AppCompatActivity{
    Context context;

    private ImageView serviceSubImg;
    private TextView mServiceName, mServiceDesc, mServiceCount;
    private Button SendReqBtn;

    private DatabaseReference mServicesDatabase;

    private ProgressDialog mProgressDialog;

    private DatabaseReference mFriendReqDatabase;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mNotificationDatabase;

    private DatabaseReference mRootRef;

    private FirebaseUser mCurrent_user;
    private String user_id;
    private String mCurrent_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscribe);

        user_id = getIntent().getStringExtra("user_id");
        mRootRef = FirebaseDatabase.getInstance().getReference();


        mServicesDatabase = FirebaseDatabase.getInstance().getReference().child("all_services").child(user_id);
        mServicesDatabase.keepSynced(true);
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Subscribers");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        serviceSubImg = (ImageView) findViewById(R.id.profile_image);
        mServiceName = (TextView) findViewById(R.id.display_name);
        mServiceDesc = (TextView) findViewById(R.id.desc_details);
        mServiceCount = (TextView) findViewById(R.id.total_subscribers);
        SendReqBtn = (Button) findViewById(R.id.button_subscribe);

        mCurrent_state = "unsubscribed";

        mServicesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("service_name").getValue().toString();
                String status = dataSnapshot.child("service_desc").getValue().toString();
                String image = dataSnapshot.child("service_image").getValue().toString();

                mServiceName.setText(display_name);
                mServiceDesc.setText(status);

                Picasso.with(SubscribeActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.bg_card).into(serviceSubImg);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        SendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//final String curr_date = DateFormat.getDateTimeInstance().format(new Date());

                final String curs = "Hello Service Reply";
                if (mCurrent_state == "unsubscribed") {
                    mFriendDatabase.child(mCurrent_user.getUid()).child(user_id).setValue(curs).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendDatabase.child(user_id).child(mCurrent_user.getUid()).setValue(curs).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    mCurrent_state = "subscribed";
                                    SendReqBtn.setText("Unsubscribe");

                                    Intent mainIntent = new Intent(SubscribeActivity.this,MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);

                                }

                            });
                        }
                    });
                }
            }
        });

    }



}
