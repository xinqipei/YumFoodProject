package com.example.yummfoodapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yummfoodapp.Adapter.CommentsAdapter;
import com.example.yummfoodapp.Model.CommentModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailsActivity extends AppCompatActivity {

    //to get details of user and post
    String myUid, myEmail, myName, myDp,postId, pLikes;
    boolean mProcessComment= false;
    boolean mProcessLike =false;


    //views

    CircleImageView profileImage;
    ImageView pImageIv;
    TextView name, pTimeTv, pTitleTv, pDescriptionTv, pLikesTv, pCommentsTv, pNameTV;
    ImageButton moreBtn;
    Button likeBtn, shareBtn;
    RecyclerView recyclerView;
    List<CommentModel> commentModelList;
    CommentsAdapter commentsAdapter;


    //add comments views
    EditText commentEt;
    ImageButton sendBtn;
    ImageView aAvatarIv;

    //progress bar
    ProgressDialog pd;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);




        //get id of post using intent

        Intent intent = getIntent();
        postId = intent.getStringExtra("postId");

        //init views
        pDescriptionTv = findViewById(R.id.postDescription);
        aAvatarIv = findViewById(R.id.cAvatarIv);
        sendBtn = findViewById(R.id.sendBtn);
        likeBtn = findViewById(R.id.likeBtn);
        commentEt = findViewById(R.id.commentEt);
        shareBtn = findViewById(R.id.shareBtn);
        moreBtn = findViewById(R.id.moreBtn);
        pTimeTv = findViewById(R.id.pTime);
        name = findViewById(R.id.name);
        pNameTV= findViewById(R.id.nameTv);
        pLikesTv = findViewById(R.id.pLikesTv);
        profileImage = findViewById(R.id.profile_image);
        pTitleTv = findViewById(R.id.postTitle);
        pImageIv = findViewById(R.id.postImage);
        pCommentsTv = findViewById(R.id.pCommentTv);
        recyclerView = findViewById(R.id.recyclerView);
        loadPostInfo();
        checkUSerStatus();
        loadUserInfo();
        setLikes();
        
        loadComments();
        //send comment button click
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostComment();

            }
        });
        
        //like button click handle
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likePost();
            }
        });
        // more button click handle
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreOptions();
            }
        });
        name.setText(getIntent().getStringExtra("name"));
        Picasso.get().load(getIntent().getStringExtra("dp")).into(aAvatarIv);
        Picasso.get().load(getIntent().getStringExtra("dp")).into(profileImage);
        Picasso.get().load(getIntent().getStringExtra("pimg")).into(pImageIv);

        //share button click handle
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pTitle= pTitleTv.getText().toString().trim();
                String pDescr= pDescriptionTv.getText().toString().trim();

                //get image from imageView
                BitmapDrawable bitmapDrawable = (BitmapDrawable)pImageIv.getDrawable();
                if(bitmapDrawable == null){
                    //post without image
                    shareTextOnly( pTitle,pDescr);



                }
                else{

                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    shareImageAndText( pTitle,pDescr, bitmap);
                }

            }
        });
    }

    private void shareTextOnly(String title, String description) {
        String shareBody = title+"\n"+ description;
        //share intent
        Intent sIntent = new Intent(Intent.ACTION_SEND);
        sIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        sIntent.setType("text/plain");
       startActivity(Intent.createChooser(sIntent,"Share Via"));
    }

    private void shareImageAndText(String title, String description, Bitmap bitmap) {

        //concatenate title and description to share
        String shareBody = title+"\n"+ description;

        //first save this image in cache, get the saved image uri
        Uri uri = saveImageToShare(bitmap);
        //share intent
        Intent sIntent = new Intent(Intent.ACTION_SEND);
        sIntent.putExtra(Intent.EXTRA_STREAM, uri);
        sIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        sIntent.setType("image/png");
       startActivity(Intent.createChooser(sIntent,"Share Via"));

    }

    private Uri saveImageToShare(Bitmap bitmap) {
        File imageFolder = new File(getCacheDir(), "images");

        Uri uri = null;
        try {
            imageFolder.mkdirs();//create if not exists
            File file = new File(imageFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(this, "com.example.yummfoodapp.fileprovider", file);

        }
        catch(Exception e){
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return uri;

    }

    private void loadComments() {
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        //set layout to recyclerview
        recyclerView.setLayoutManager(mLayoutManager);



        //init comments list

        commentModelList =new ArrayList<>();

        //path of the post to get its comments
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("comments");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                commentModelList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){

                    myName=""+ds.child("uName").getValue();
                    myDp=""+ds.child("pImage").getValue();
                    myEmail= ""+ds.child("uEmail").getValue();

                    //set data
                    try{
                        //if image is received then set
                        Picasso.get().load(myDp).placeholder(R.drawable.ic_edit_24).into(aAvatarIv);
                    }
                    catch(Exception e){
                        Picasso.get().load(R.drawable.ic_edit_24).into(aAvatarIv);

                    }

                    CommentModel commentModel = ds.getValue(CommentModel.class);

                    commentModelList.add(commentModel);

                    //pass mu Uid and postId as parameter of constructor of comment Adapter


                    //set up data

                    commentsAdapter = new CommentsAdapter(getApplicationContext(), commentModelList, myUid, postId);
                    //set adapter
                    recyclerView.setAdapter(commentsAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showMoreOptions() {
        
    }

    private void setLikes() {
       // when the details of post is loading, also check if current user has liked it ot not
        final DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postId).hasChild(myUid)){
                    //user has liked this post, change text from like to liked, change icon color
                    likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_24, 0,0,0);
                    likeBtn.setText("Like");
                }
                else{
                    //user has not like post
                   likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_black, 0,0,0);
                   likeBtn.setText("Liked");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void likePost() {
        //get total number of likes for the post
        //if currently signed in user has not liked it before
        // increase value by 1, otherwise decrease value by one

        mProcessLike =true;

        //get id of the post clicked
        final DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference().child("Likes");
        final DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mProcessLike) {
                    if(snapshot.child(postId).hasChild(myUid)) {
                        //already liked so remove like
                        postsRef.child(postId).child("pLikes").setValue(""+(Integer.parseInt(pLikes)-1));
                        likeRef.child(postId).child(myUid).removeValue();
                        mProcessLike = false;

                        likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_24, 0,0,0);
                        likeBtn.setText("Like");
                    }
                    else {
                        //not liked, like it
                        postsRef.child(postId).child("pLikes").setValue(""+(Integer.parseInt(pLikes)+1));
                        likeRef.child(postId).child(myUid).setValue("Liked");
                        mProcessLike = false;


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void PostComment() {
        pd= new ProgressDialog(this);
        pd.setMessage("Adding Comment...");

        //get data from comment edit text
        final String comment = commentEt.getText().toString().trim();

        //validate
        if(TextUtils.isEmpty(comment)){
            //no value is entered

            Toast.makeText(this,"comment is empty...", Toast.LENGTH_SHORT).show();
            return;
        }

        String timestamp = String.valueOf(System.currentTimeMillis());
        //each post will have a child "comment" that will contain the comments of that post
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("comments");

        HashMap<String, Object> hashMap = new HashMap<>();

        String cid= String.valueOf(System.currentTimeMillis());
        //put info in hashmap
        hashMap.put("cId", cid);
        hashMap.put("timeStamp", timestamp);
        hashMap.put("comment", comment);
        hashMap.put("uid", myUid);
        hashMap.put("uEmail", myEmail);
        hashMap.put("uDp", myDp);
        hashMap.put("uName", myName);
        //put this data in db
        ref.child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    //add
                        pd.dismiss();

                        Toast.makeText(PostDetailsActivity.this,"comment added...", Toast.LENGTH_SHORT).show();
                        commentEt.setText("");
                       // updateCommentCount();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    //failed , not added
                        pd.dismiss();
                        Toast.makeText(PostDetailsActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateCommentCount() {

        //whenever user adds comment increase the comment count as the likes function
        mProcessComment = true;
       final DatabaseReference ref =  FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(mProcessComment){
                    /*String comments = ""+snapshot.child("pComments").getValue();
//                    int newCommentVal = Integer.parseInt(comments) + 1;
                    ref.child("pComments").setValue(""+newCommentVal);
                    mProcessComment=false;*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }

    private void loadUserInfo() {
        //get current user info
        Query myRef = FirebaseDatabase.getInstance().getReference("Users");
        myRef.orderByChild("uid").equalTo(myUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    myName=""+ds.child("name").getValue();
                    myDp=""+ds.child("image").getValue();
                    myEmail= ""+ds.child("email").getValue();

                    //set data
                    try{
                        //if image is received then set
                        Picasso.get().load(myDp).placeholder(R.drawable.ic_edit_24).into(aAvatarIv);
                    }
                    catch(Exception e){
                        Picasso.get().load(R.drawable.ic_edit_24).into(aAvatarIv);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkUSerStatus() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!= null){
            //user is signed in
            myEmail = user.getEmail();
            myUid = user.getUid();

        }
        else{
            //user not signed in , go to main activity
            startActivity( new Intent(PostDetailsActivity.this, MainActivity.class));

        }
    }

    private void loadPostInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = ref.orderByChild("pId").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //keep checking the posts until get  the required post
                for(DataSnapshot ds: snapshot.getChildren()){
                    //get data
                    String pTitle = ""+ds.child("pTitle").getValue();
                    String pDescr = ""+ds.child("pDescripition").getValue();
                    String pImage = ""+ds.child("image").getValue();
                    String pTimeStamp = ""+ds.child("pTime").getValue();
                    String uid= ""+ds.child("uid").getValue();
                    String uEmail = ""+ds.child("uEmail").getValue();
                    pLikes = ""+ds.child("pLikes").getValue();
                    String  commentCount = ""+ds.child("pComments").getValue();

                    // convert timestamp to dd/mm/yyyy  hh:mm am/pm
                   /* Calendar calendar = Calendar.getInstance(Locale.getDefault());
                    calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
                    String pTime = DateFormat.format("dd/mm/yyyy hh:mm aa", calendar).toString();*/
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    String dateString = formatter.format(new Date(Long.parseLong(pTimeStamp)));

                   pTimeTv.setText(dateString);
                   pTitleTv.setText(pTitle);
                    pLikesTv.setText(pLikes+"Likes");
                    pDescriptionTv.setText(pDescr);
                    pCommentsTv.setText(commentCount + "Comments");



                    if(pImage.equals("Image")){

                        pImageIv.setVisibility(View.VISIBLE);

                        try{
                            Picasso.get().load(pImage).into(pImageIv);
                        }
                        catch(Exception e){


                        }
                    }




                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}