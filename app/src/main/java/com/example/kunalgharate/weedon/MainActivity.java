package com.example.kunalgharate.weedon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kunalgharate.weedon.Model.Friends;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    RecyclerView mSubsList;
    DatabaseReference mFriendsDatabase;
    DatabaseReference mMainSubcribers;
    DatabaseReference mUsersDatabase;
    private FirebaseAuth mAuth;
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

        mMainSubcribers = FirebaseDatabase.getInstance().getReference().child("all_services");
        mFriendsDatabase = FirebaseDatabase.getInstance().getReference().child("subscribers").child(mCurrent_user_id);
        mFriendsDatabase.keepSynced(true);
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
                mFriendsDatabase
                )
        {
            @Override
            protected void populateViewHolder(final MainViewHolder viewHolder, Friends model, final int position) {


                final String list_service_id = getRef(position).getKey().toString();

                mMainSubcribers.child(list_service_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final String serviceName = dataSnapshot.child("service_name").getValue().toString();
                        String thumbImage = dataSnapshot.child("service_thumbImage").getValue().toString();

                        viewHolder.setDisplayName(serviceName);
                        viewHolder.setUserImage(thumbImage, getApplicationContext());


                        viewHolder.setOnClickListener(new MainViewHolder.ClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Intent msgIntent = new Intent(MainActivity.this, ChatActivity.class);
                                msgIntent.putExtra("chatname", serviceName);
                                msgIntent.putExtra("service_id", list_service_id);
                                startActivity(msgIntent);

                            }

                            @Override
                            public void onItemLongClick(View view, int position) {

                                Toast.makeText(MainActivity.this, "Working In Progrress", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






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

        protected MainViewHolder.ClickListener mClickListener;
        View mView;

        public MainViewHolder(View itemView) {
            super(itemView);

            mView =itemView;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });

        }

        public void setOnClickListener(MainViewHolder.ClickListener clickListener) {
            mClickListener = clickListener;
        }

        public void setDisplayName(String name) {

            TextView userNameView = mView.findViewById(R.id.user_single_name);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx) {

            CircleImageView userImageView = mView.findViewById(R.id.user_single_image);

            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.bg_card).into(userImageView);

        }

       /* public void setUserStatus(String status){

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);


        }*/

        //Interface to send callbacks...
        public interface ClickListener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }



    }



}
