package com.example.kunalgharate.weedon;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllServicesActivity extends AppCompatActivity {


    private Toolbar mToolbar;

    private RecyclerView mServicesList;

    private DatabaseReference mServicesDatabase;

  //  private LinearLayoutManager mLayoutManager;
    private  GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_services);

        mToolbar = (Toolbar) findViewById(R.id.allservices_toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("All Services");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

      /*  FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String uid = user.getUid();*/

        mServicesDatabase = FirebaseDatabase.getInstance().getReference().child("all_services");
       // mLayoutManager = new LinearLayoutManager(this);

        gridLayoutManager = new GridLayoutManager(this,3);

        mServicesList = (RecyclerView) findViewById(R.id.allservices_list);
        mServicesList.setHasFixedSize(true);
        mServicesList.setLayoutManager(gridLayoutManager);


    }


    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<ServicePojo, ServicesViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ServicePojo, ServicesViewHolder>(

                ServicePojo.class,
                R.layout.grid_single_service,
                ServicesViewHolder.class,
                mServicesDatabase

        ) {
            @Override
            protected void populateViewHolder(ServicesViewHolder servicesViewHolder, final ServicePojo serModel, int position) {

                final String user_id = getRef(position).getKey();

             //   servicesViewHolder.setDisplayName("Lets Up");
                servicesViewHolder.setDisplayName(serModel.getName());
              //  Toast.makeText(getApplicationContext(),serModel.getName().toString(),Toast.LENGTH_LONG).show();
             //   servicesViewHolder.setUserStatus(smodel.getImage());
               servicesViewHolder.setUserImage(serModel.getImage(), getApplicationContext());

              //  final String user_id = getRef(position).getKey();

                servicesViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Intent subsIntent = new Intent(AllServicesActivity.this,SubscribeActivity.class);
                        subsIntent.putExtra("user_id", user_id);

                        Log.e("userid",user_id);

                        startActivity(subsIntent);



                       // Toast.makeText(getApplicationContext(),"Working",Toast.LENGTH_LONG).show();

                      //  Intent profileIntent = new Intent(AllServicesActivity.this, .class);
                     //   profileIntent.putExtra("user_id", user_id);
                     //   startActivity(profileIntent);

                    }
                });

            }
        };


        mServicesList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class ServicesViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ServicesViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }

        public void setDisplayName(String name){

            TextView userNameView = (TextView) mView.findViewById(R.id.service_single_name);
            userNameView.setText(name);

        }

       /* public void setUserStatus(String status){

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);


        }*/

        public void setUserImage(String thumb_image, Context ctx){

            CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.service_photo);

            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.bg_card).into(userImageView);

        }


    }
}
