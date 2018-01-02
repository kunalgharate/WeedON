package com.example.kunalgharate.weedon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class SubscribeActivity extends AppCompatActivity{
    Context context;
    CoustomTextView coustomTextView;
    String display_name;
    Toolbar toolbar;
    String title;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView serviceSubImg;
    private TextView mServiceName, mServiceDesc, mServiceCount;
    private DatabaseReference mServicesDatabase;
    private ProgressDialog mProgressDialog;
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
        toolbar = findViewById(R.id.toolbar1);
        //  setSupportActionBar(toolbar);
        //  getSupportActionBar().setTitle(display_name);
        setSupportActionBar(toolbar);
        title = getIntent().getStringExtra("title");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        user_id = getIntent().getStringExtra("user_id");
        mRootRef = FirebaseDatabase.getInstance().getReference();
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.coll_toolbar_title);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.exp_toolbar_title);

      /*  AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle("Title");
                    isShow = true;
                } else if(isShow) {
                    collapsingToolbarLayout.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });*/

        mServicesDatabase = FirebaseDatabase.getInstance().getReference().child("all_services").child(user_id);
        mServicesDatabase.keepSynced(true);
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("subscribers");
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        serviceSubImg = findViewById(R.id.expandedImage);
        //mServiceName = (TextView) findViewById(R.id.display_name);
        mServiceDesc = findViewById(R.id.desc_details);
        //  mServiceCount = (TextView) findViewById(R.id.total_subscribers);
        coustomTextView = findViewById(R.id.button_subscribe);

        mCurrent_state = "unsubscribed";


        if (mCurrent_state == "subscribed") {
            coustomTextView.setText("Unsubsribe");
        }



        mServicesDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    display_name = dataSnapshot.child("service_name").getValue().toString();
                    String desc = dataSnapshot.child("service_desc").getValue().toString();
                    String image = dataSnapshot.child("service_image").getValue().toString();

                    //     mServiceName.setText(display_name);
                    mServiceDesc.setText(desc);

                    Picasso picasso = Picasso.with(getApplicationContext());
                    picasso.setIndicatorsEnabled(false); //Or remove picasso.setIndicatorsEnabled(true);
                    Picasso.with(SubscribeActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.android_logo).into(serviceSubImg);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        coustomTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//final String curr_date = DateFormat.getDateTimeInstance().format(new Date());

                final String my_string = "subscribed";
                if (mCurrent_state == "unsubscribed") {

                    mFriendDatabase.child(mCurrent_user.getUid()).child(user_id).child(my_string).setValue(my_string).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendDatabase.child(user_id).child(mCurrent_user.getUid()).child(my_string).setValue(my_string).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    mCurrent_state = "subscribed";
                                    coustomTextView.setText("Unsubsribe");

                                    Intent subscribeIntent = new Intent(SubscribeActivity.this, MainActivity.class);
                                    startActivity(subscribeIntent);



                                }


                            });

                        }

                    });
                }
            }
        });


    }


}
