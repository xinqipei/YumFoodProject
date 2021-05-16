package com.example.yummfoodapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yummfoodapp.Model.PostModel;
import com.example.yummfoodapp.PostDetailsActivity;
import com.example.yummfoodapp.R;
import com.example.yummfoodapp.ThereProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyHolder> {
    Context context;
    List<PostModel> postModelList;
    String myUid;
    private DatabaseReference likeRef;
    private DatabaseReference postsRef;
    boolean mProcessLike = false;



    public PostAdapter(Context context, List<PostModel> postModelList) {
        this.context = context;
        this.postModelList = postModelList;
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        likeRef= FirebaseDatabase.getInstance().getReference().child("Likes");
        postsRef= FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.demo , parent , false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        final String uid = postModelList.get(position).getUid();
        final String pId = postModelList.get(position).getpId();
        String uEmail = postModelList.get(position).getuEmail();
        final String uName = postModelList.get(position).getuName();
        final String uDp = postModelList.get(position).getuDp();
        String pTimeStamp = postModelList.get(position).getpTime();
        String pLikes = postModelList.get(position).getpLikes();//contains total number likes
        final String title = postModelList.get(position).getpTitle();
        final String description = postModelList.get(position).getpDescripition();
        final String pImage = postModelList.get(position).getpImage();
        String pComments = postModelList.get(position).getpComments();


        // convert timestamp to dd/mm/yyyy  hh:mm am/pm
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        if(pTimeStamp!=null){
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dateString = formatter.format(new Date(Long.parseLong(pTimeStamp)));


            holder.pTimeTv.setText(""+dateString);

        }



        //set data


        holder.uNameTv.setText(""+uName);
        holder.postTitle.setText(""+title);
        holder.postDescription.setText(""+description);
        holder.pLikesTv.setText(pLikes + "Likes");//eg. 100likes
        holder.pCommentTv.setText(pComments + "comments");//eg. 100 comments
        //set likes for each post
        //set likes for each post
        setLikes(holder, pId);

        Glide.with(context).load(pImage).into(holder.postImage);
        Glide.with(context).load(uDp).into(holder.profileImage);


        //set user dp

        //handle button clicks
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              showMoreOptions(holder.moreBtn, uid, myUid, pId, pImage);
            }

            private void showMoreOptions(ImageButton moreBtn, String uid, String myUid, final String pId, final String image) {
                 //creating popup menu currently having option Delete
                PopupMenu popupMenu= new PopupMenu(context, moreBtn, Gravity.END);

                //show delete option nly in current signed in user's posts

                if (uid.equals(myUid)){

                    //add items in menu
                    popupMenu.getMenu().add(Menu.NONE, 0,0,"Delete");

                }
              //  popupMenu.getMenu().add(Menu.NONE, 2,0,"View Details");


                //item click listener
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();
                        if(id==0){
                            removeItem(position);

                            //delete is clicked
                            beginDelete(pId, pImage);

                        }
                        else if(id==1){

                            Intent intent = new Intent(context, PostDetailsActivity.class);
                            intent.putExtra("postId", pId);//will get details of post using this id,
                            context.startActivity(intent);
                        }

                        return false;
                    }
                });

                //show menu
                popupMenu.show();
            }
        });
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get total number of likes for the post
                //if currently signed in user has not liked it before
                // increase value by 1, otherwise decrease value by one
                final int pLikes =Integer.parseInt(postModelList.get(position).getpLikes());
                mProcessLike =true;

                //get id of the post clicked
                final String postIde =  postModelList.get(position).getpId();
                likeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (mProcessLike) {
                            if(snapshot.child(postIde).hasChild(myUid)) {
                                //already liked so remove like
                                postsRef.child(postIde).child("pLikes").setValue(""+(pLikes-1));
                                likeRef.child(postIde).child(myUid).removeValue();
                                mProcessLike = false;
                            }
                            else {
                                //not liked, like it
                                postsRef.child(postIde).child("pLikes").setValue(""+(pLikes+1));
                                likeRef.child(postIde).child(myUid).setValue("Liked");
                                mProcessLike = false;


                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });
        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //get image from imageView
                BitmapDrawable bitmapDrawable = (BitmapDrawable)holder.postImage.getDrawable();
                if(bitmapDrawable == null){
                    //post without image
                    shareTextOnly(title,description);



                }
                else{

                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    shareImageAndText(title,description, bitmap);
                }

            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //start postDetailsActivity
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("postId", pId);//will get details of post using this id,
                intent.putExtra("name",uName);
                intent.putExtra("dp",uDp);
                intent.putExtra("pimg",pImage);

                context.startActivity(intent);

            }
        });

      //  holder.profileImage.setOnClickListener(new View.OnClickListener() {
         //   @Override
          //  public void onClick(View v) {
                //click to go to thereprofileactivity with uid, this uid is of clicked user
                //which will be used to show user specific data /posts
              //  Intent intent = new Intent(context, ThereProfileActivity.class);
              //  intent.putExtra("uid" , uid);
              //  context.startActivity(intent);
        ///    }
      //  });

    }

    private void shareTextOnly(String title, String description) {
        String shareBody = title+"\n"+ description;
        //share intent
        Intent sIntent = new Intent(Intent.ACTION_SEND);
        sIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        sIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
        sIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sIntent,"Share Via"));
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
        context.startActivity(Intent.createChooser(sIntent,"Share Via"));

    }

    private Uri saveImageToShare(Bitmap bitmap) {
        File imageFolder = new File(context.getCacheDir(), "images");

        Uri uri = null;
        try {
            imageFolder.mkdirs();//create if not exists
            File file = new File(imageFolder, "shared_image.png");

            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(context, "com.example.yummfoodapp.fileprovider", file);

        }
        catch(Exception e){
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return uri;

    }

    private void removeItem(int position) {

        postModelList.remove(position);
        notifyItemRemoved(position);

    }

    private void beginDelete(String pId, String pImage) {
        deleteWithImage(pId, pImage);

    }

    private void deleteWithImage(final String pId, String pImage) {
        //progress bar

        final ProgressDialog pd = new ProgressDialog(context);
        pd.setMessage("Deleting");
        pd.show();
        //delete image using url, delete from database using id
        StorageReference picRef = FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //image deleted from database
                        Query fquery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
                        fquery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds:snapshot.getChildren()){
                                    ds.getRef().removeValue();// remove firebase where pid matches

                                }

                                pd.dismiss();
                                Toast.makeText(context,"Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //image can't delete
                        pd.dismiss();

                        Query fquery = FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
                        fquery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds:snapshot.getChildren()){
                                    ds.getRef().removeValue();// remove firebase where pid matches

                                }

                                pd.dismiss();
                                Toast.makeText(context,"Deleted Successfully", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }
                });




    }

    private void setLikes(final MyHolder holder, final String postKey) {
        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postKey).hasChild(myUid)){
                //user has liked this post, change text from like to liked, change icon color
                holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_24, 0,0,0);
                holder.likeBtn.setText("Like");
                }
                else{
                //user has not like post
                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_thumb_up_black, 0,0,0);
                    holder.likeBtn.setText("Liked");


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return postModelList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        ImageView postImage, uPictureIv;
        TextView postTitle , postDescription, uNameTv, pLikesTv, pTimeTv,pCommentTv;
        Button likeBtn, commentBtn, shareBtn;
        ImageButton moreBtn;
        CircleImageView profileImage;

        public MyHolder(@NonNull View itemView) {
            super(itemView);


            postImage = itemView.findViewById(R.id.postImage);
            postTitle = itemView.findViewById(R.id.postTitle);
            postDescription = itemView.findViewById(R.id.postDescription);
            likeBtn = itemView.findViewById(R.id.likeBtn);
            commentBtn = itemView.findViewById(R.id.commentBtn);
            shareBtn = itemView.findViewById(R.id.shareBtn);
            moreBtn = itemView.findViewById(R.id.moreBtn);
            pTimeTv = itemView.findViewById(R.id.pTime);
            uPictureIv = itemView.findViewById(R.id.profile_image);
            uNameTv = itemView.findViewById(R.id.name);
            pLikesTv = itemView.findViewById(R.id.pLikesTv);
            profileImage = itemView.findViewById(R.id.profile_image);
           pCommentTv = itemView.findViewById(R.id.pCommentTv);

        }
    }
}


