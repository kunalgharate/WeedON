package com.example.kunalgharate.weedon.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.kunalgharate.weedon.CoustomTextView;
import com.example.kunalgharate.weedon.Model.Messages;
import com.example.kunalgharate.weedon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by AkshayeJH on 24/07/17.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {


    Context context;
    Intent intent = new Intent();
    String service_id;
    private List<Messages> mMessageList;
    private DatabaseReference ChatDatabaseRef;

    public MessageAdapter(List<Messages> mMessageList, String serviceId, Context context) {

        this.mMessageList = mMessageList;
        this.service_id = serviceId;
        this.context = context;

    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_single_layout, parent, false);

        return new MessageViewHolder(v);

    }

    @Override
    public void onBindViewHolder(final MessageViewHolder viewHolder, int i) {

        Messages c = mMessageList.get(i);

        String from_user = c.getFrom();
        String message_type = c.getType();


        ChatDatabaseRef = FirebaseDatabase.getInstance().getReference().child("chat").child(service_id);
        ChatDatabaseRef.keepSynced(true);
        // mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(from_user);

        ChatDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //String name = dataSnapshot.child("name").getValue().toString();
                // String image = dataSnapshot.child("thumb_image").getValue().toString();

                // viewHolder.displayName.setText(name);

                // Picasso.with(viewHolder.profileImage.getContext()).load(image)
                //         .placeholder(R.drawable.avatar_background).into(viewHolder.profileImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if (message_type.equals("text")) {

            viewHolder.messageText.setText(c.getMessage());
            viewHolder.messageImage.setVisibility(View.INVISIBLE);


        } else {

            viewHolder.messageText.setVisibility(View.INVISIBLE);
            viewHolder.linearLayout.setVisibility(View.INVISIBLE);
            viewHolder.chatImageBg.setVisibility(View.INVISIBLE);

         /*   Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(false);
            Picasso.with(viewHolder.messageImage.getContext()).load(c.getMessage())
                    .placeholder(R.drawable.logo).into(viewHolder.messageImage);
*/

            Glide.with(context).load(c.getMessage())
                    .fitCenter()
                    .placeholder(R.drawable.android_logo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(viewHolder.messageImage);

        }

    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public CoustomTextView messageText;
        public CircleImageView profileImage;
        //public TextView messageText;
        public ImageView messageImage;
        public ImageView chatImageBg;
        public LinearLayout linearLayout;

        public MessageViewHolder(View view) {
            super(view);


            messageText = view.findViewById(R.id.message_text_layout);
            linearLayout = view.findViewById(R.id.linearLayout2);
            // profileImage = (CircleImageView) view.findViewById(R.id.message_profile_layout);
            // messageText = (TextView) view.findViewById(R.id.message_text_layout);
            messageImage = view.findViewById(R.id.message_image_layout);
            chatImageBg = view.findViewById(R.id.chat_bgimage);

        }
    }


}
