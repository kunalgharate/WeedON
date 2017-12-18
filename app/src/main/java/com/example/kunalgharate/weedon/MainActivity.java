package com.example.kunalgharate.weedon;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    RecyclerView mSubsList;
    DatabaseReference mServicesDatabases;
    DatabaseReference mUsersDatabase;
    private String mCurrent_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
         getSupportActionBar().setTitle("WeedON");
        mAuth = FirebaseAuth.getInstance();
        mCurrent_user_id = mAuth.getCurrentUser().getUid();
        mSubsList =findViewById(R.id.subscribes_list);
        mSubsList.setHasFixedSize(true);
        mSubsList.setLayoutManager(new LinearLayoutManager(this));


        mServicesDatabases = FirebaseDatabase.getInstance().getReference().child("Subscribers").child(mCurrent_user_id);
        mServicesDatabases.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUsersDatabase.keepSynced(true);




    }

    private void signOut() {
        mAuth.signOut();
    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Friends,MainViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Friends,MainViewHolder>(


                Friends.class,
                R.layout.single_service_layout,
                MainViewHolder.class,
                mServicesDatabases
                )
        {
            @Override
            protected void populateViewHolder(MainViewHolder viewHolder, Friends model, int position) {



            }
        };

       mSubsList.setAdapter(firebaseRecyclerAdapter);
        }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_logout_btn){

          //  mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

            signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
         //   sendToStart();

        }

        if(item.getItemId() == R.id.main_settings_btn){

            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(settingsIntent);

        }

        if(item.getItemId() == R.id.main_all_btn){

            Intent settingsIntent = new Intent(MainActivity.this, AllServicesActivity.class);
            startActivity(settingsIntent);

        }

        return true;
    }

    public static class MainViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MainViewHolder(View itemView) {
            super(itemView);

            mView =itemView;
        }


    }



}
