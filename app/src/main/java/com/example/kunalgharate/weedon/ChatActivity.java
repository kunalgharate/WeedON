package com.example.kunalgharate.weedon;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.kunalgharate.weedon.Adapter.MessageAdapter;
import com.example.kunalgharate.weedon.Model.Messages;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final int TOTAL_ITEMS_TO_LOAD = 10;
    private static final int GALLERY_PICK = 1;
    private final List<Messages> messagesList = new ArrayList<>();
    Toolbar ctoolbar;
    String title;
    // TextView idview;
    DatabaseReference ChatDatabaseRef;
    RecyclerView messages_list;
    private SwipeRefreshLayout mRefreshLayout;
    private LinearLayoutManager mLinearLayout;
    private MessageAdapter mAdapter;
    private int mCurrentPage = 1;
    //New Solution
    private int itemPos = 0;
    private DatabaseReference mRootRef;
    private String mLastKey = "";
    private String mPrevKey = "";
    private StorageReference mImageStorage;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        title = getIntent().getStringExtra("chatname");


        ctoolbar = findViewById(R.id.chat_app_bar);
        setSupportActionBar(ctoolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        // idview = (TextView)findViewById(R.id.id_view);
        mLinearLayout = new LinearLayoutManager(this);
        messages_list = findViewById(R.id.messages_list);
        messages_list.setHasFixedSize(true);
        messages_list.setLayoutManager(mLinearLayout);
        String service_id = getIntent().getStringExtra("service_id");
        //------- IMAGE STORAGE ---------
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mRootRef.keepSynced(true);
        mRefreshLayout = findViewById(R.id.message_swipe_layout);
        //  idview.setText(service_id);

        ChatDatabaseRef = FirebaseDatabase.getInstance().getReference().child("chat").child(service_id);
        ChatDatabaseRef.keepSynced(true);

        mAdapter = new MessageAdapter(messagesList, service_id, getApplicationContext());
        messages_list.setAdapter(mAdapter);
        loadMessages();

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mCurrentPage++;
                //  itemPos = 0;
                loadMoreMessages();


            }
        });

    }

   /* @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerAdapter<Messages,ChatViewHolder> messagesChatViewHolderFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Messages, ChatViewHolder>(
                Messages.class,
                R.layout.message_single_layout,
                ChatViewHolder.class,
                ChatDatabaseRef
        ) {
            @Override
            protected void populateViewHolder(ChatViewHolder viewHolder, Messages model, int position) {

                viewHolder.setDisplayMsg(model.getMessage());
                //viewHolder.setDisplayName(title);

                viewHolder.setOnClickListener(new ChatViewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Toast.makeText(ChatActivity.this,"Text Copied",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });

            }
        };

        messages_list.setAdapter(messagesChatViewHolderFirebaseRecyclerAdapter);
    }*/


   /* public static class ChatViewHolder extends RecyclerView.ViewHolder {



        View mView;

        public ChatViewHolder(View itemView) {
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

        protected ChatActivity.ChatViewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

    public void setOnClickListener(ChatActivity.ChatViewHolder.ClickListener clickListener){
        mClickListener = clickListener;
    }



    *//*public void setDisplayName(String name){

        TextView userNameView = (TextView) mView.findViewById(R.id.name_text_layout);
        userNameView.setText(name);

    }*//*

        public void setDisplayMsg(String msg){

            TextView msgView = (TextView) mView.findViewById(R.id.message_text_layout);
            msgView.setText(msg);

        }


       *//* public void setUserStatus(String status){

            TextView userStatusView = (TextView) mView.findViewById(R.id.user_single_status);
            userStatusView.setText(status);


        }*//*

  *//*  public void setUserImage(String thumb_image, Context ctx){

        CircleImageView userImageView = (CircleImageView) mView.findViewById(R.id.user_single_image);

        Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.bg_card).into(userImageView);

    }
*//*


}*/


    private void loadMessages() {


        Query messageQuery = ChatDatabaseRef.limitToLast(mCurrentPage * TOTAL_ITEMS_TO_LOAD);
        messageQuery.keepSynced(true);


        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Messages message = dataSnapshot.getValue(Messages.class);

                itemPos++;

                if (itemPos == 1) {

                    String messageKey = dataSnapshot.getKey();

                    mLastKey = messageKey;
                    mPrevKey = messageKey;

                }

                messagesList.add(message);
                mAdapter.notifyDataSetChanged();

                messages_list.scrollToPosition(messagesList.size() - 1);

                mRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

         //   final String current_user_ref = "messages/" + mCurrentUserId + "/" + mChatUser;
         //   final String chat_user_ref = "messages/" + mChatUser + "/" + mCurrentUserId;

            DatabaseReference user_message_push = mRootRef.child("chat")
                .push();

            final String push_id = user_message_push.getKey();


            StorageReference filepath = mImageStorage.child("message_images").child( push_id + ".jpg");

            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if(task.isSuccessful()){

                        String download_url = task.getResult().getDownloadUrl().toString();


                        Map messageMap = new HashMap();
                        messageMap.put("message", download_url);
                        messageMap.put("seen", false);
                        messageMap.put("type", "image");
                        messageMap.put("time", ServerValue.TIMESTAMP);
                        messageMap.put("from", mCurrentUserId);

                        Map messageUserMap = new HashMap();
                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
                        messageUserMap.put(chat_user_ref + "/" + push_id, messageMap);

                        mChatMessageView.setText("");

                        mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                if(databaseError != null){

                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());

                                }

                            }
                        });


                    }

                }
            });

        }

    }*/

    private void loadMoreMessages() {


        Query messageQuery = ChatDatabaseRef.orderByKey().endAt(mLastKey).limitToLast(TOTAL_ITEMS_TO_LOAD);
        messageQuery.keepSynced(true);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                Messages message = dataSnapshot.getValue(Messages.class);
                String messageKey = dataSnapshot.getKey();

                if (!mPrevKey.equals(messageKey)) {

                    messagesList.add(itemPos++, message);

                } else {

                    mPrevKey = mLastKey;

                }


                if (itemPos == 1) {

                    mLastKey = messageKey;

                }


                Log.d("TOTALKEYS", "Last Key : " + mLastKey + " | Prev Key : " + mPrevKey + " | Message Key : " + messageKey);

                mAdapter.notifyDataSetChanged();

                mRefreshLayout.setRefreshing(false);

                mLinearLayout.scrollToPositionWithOffset(10, 0);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
