package com.example.yummfoodapp.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.yummfoodapp.Model.CommentModel;
import com.example.yummfoodapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyHolder> {
    Context context;
    List<CommentModel> commentList;
    String myUid, postId;

    public CommentsAdapter(Context context, List<CommentModel> commentList, String myUid, String postId) {
        this.context = context;
        this.commentList = commentList;
        this.myUid = myUid;
        this.postId = postId;
    }



    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {

        //bind the comments_demo.xml layout

        View view = LayoutInflater.from(context).inflate(R.layout.comments_demo, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {

        //get the data

        final String uid = commentList.get(position).getUid();
        String name = commentList.get(position).getuName();
        String email = commentList.get(position).getuEmail();
        String udp = commentList.get(position).getuDp();
        final String cid = commentList.get(position).getcId();
        String comment = commentList.get(position).getComment();
        String timestamp = commentList.get(position).getTimestamp();



        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        if(timestamp!=null){
          SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
           String dateString = formatter.format(new Date(Long.parseLong(timestamp)));


           holder.timeTv.setText(""+dateString);

        }

        //set data
        holder.nameTv.setText(""+name);
        holder.commentTv.setText(comment);



        //set user dp


       try {
            Picasso.get().load(udp).placeholder(R.drawable.ic_baseline_account_circle_24).into(holder.avatarIv);

        }
        catch (Exception e){

        }

        //Glide.with(context).load(image).into(holder.avatarIv);

        //comment click listener

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check if this comment is by current signed in user or not
                if(myUid.equals(uid)){
                    //my comment
                    //show delete dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
                    builder.setTitle("Delete");
                    builder.setMessage("are you sure to delete this comment?");
                    builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            commentList.remove(position);
                            notifyItemRemoved(position);
                            deleteComment(cid);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //dismiss dialog
                            dialog.dismiss();


                        }
                    });
                    //show dialog
                    builder.create().show();
                }
                else{
                    //not my comment
                  //  Toast.makeText(context, "Can't delete other's comment", Toast.LENGTH_SHORT).show();
                    
                }
            }
        });



    }

    private void deleteComment(String cid) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.child("comments").child(cid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });//it will delete the comment


    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{
        //declare views from comments_demo.xml
        CircleImageView avatarIv;
        TextView nameTv, commentTv, timeTv;


        public MyHolder(@NonNull View itemView) {
            super(itemView);


            avatarIv=itemView.findViewById(R.id.cAvatarIv);
            nameTv=itemView.findViewById(R.id.nameTv);
            commentTv=itemView.findViewById(R.id.commentTv);
            timeTv=itemView.findViewById(R.id.timeTv);

        }
    }
}
