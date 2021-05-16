package com.example.yummfoodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;


import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;

public class AddPostActivity extends AppCompatActivity {

    EditText title, Description;
    Button upload;
    ImageView blog_image;
    Uri image_uri = null;
    //TextView Comments;

    private static final int GALLERY_IMAGE_CODE = 100;
    private static final int CAMERA_IMAGE_CODE = 200;
    ProgressDialog pd;
    FirebaseAuth auth;
    DatabaseReference userDbRef;

    //user info
    String name, email, uid, dp;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        auth = FirebaseAuth.getInstance();
        checkUserStatus();

        //get some info of current user to include in post
        userDbRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = userDbRef.orderByChild("email").equalTo(email);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                    name = "" +ds.child("name").getValue();
                    email = "" +ds.child("email").getValue();
                    dp = "" +ds.child("image").getValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //call the method here and ask permission in manifest
        permission();
      title = findViewById(R.id.title);
      Description= findViewById(R.id.description);
      blog_image= findViewById(R.id.image);
      upload= findViewById(R.id.uploadButton);
      pd =  new ProgressDialog(this);
      auth = FirebaseAuth.getInstance();
      //Comments= findViewById(R.id.pCommentTv);



    // whe click on image , it will ask choose oic from camera or gallery
        blog_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imagePickDialog();
            }
        });
        //when user click upload button, it will upload to the firebase
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titles =  title.getText().toString();
                String descr = Description.getText().toString();

                if(TextUtils.isEmpty(titles)){
                    title.setError("Title is required");
                }
                else if(TextUtils.isEmpty(descr)){

                    Description.setError("Description is required");
                }
                else{
                    uploadData(titles , descr);
                }


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUserStatus();
    }
    private void checkUserStatus(){
        //get current user
        FirebaseUser user = auth.getCurrentUser();

        if(user !=null) {
            email = user.getEmail();
            uid = user.getUid();
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void uploadData(final String titles, final String descr) {
        pd.setMessage("Publishing Post");
        pd.show();
        // upload data to firebase
        final String timestamp = String.valueOf(System.currentTimeMillis());

        //file path to image
        String filepath = "Posts/" + "Post_" + timestamp;
        if (blog_image.getDrawable() != null) {
            //get image from image view:
            Bitmap bitmap = ((BitmapDrawable) blog_image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100 , baos);
            byte[] data = baos.toByteArray();

        //create the redernce of storage in firebase as we have already added the linraries
            StorageReference reference = FirebaseStorage.getInstance().getReference().child(filepath);
            reference.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            String downloadUri = uriTask.getResult().toString();

                            if(uriTask.isSuccessful()){
                                //url is retrieved post is published to database
                                FirebaseUser user = auth.getCurrentUser();
                                HashMap<String , Object> hashMap = new HashMap<>();

                                hashMap.put("uid" , user.getUid() );
                                hashMap.put("uName" , name );
                                hashMap.put("uDp" , dp );
                                hashMap.put("uEmail" , email);
                                hashMap.put("pId" , timestamp);
                                hashMap.put("pTitle" , titles);
                                hashMap.put("pImage" , downloadUri);
                                hashMap.put("pDescripition" , descr);
                                hashMap.put("pTime" , timestamp);
                                hashMap.put("pLikes" , "0");

                               // hashMap.put("pComments" , Comments);


                                //post data to firebase
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                                ref.child(timestamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                            pd.dismiss();
                                                Toast.makeText(AddPostActivity.this, "Post Published", Toast.LENGTH_SHORT).show();
                                                title.setText("");
                                                Description.setText("");
                                                blog_image.setImageURI(null);
                                                image_uri = null;

                                                startActivity(new Intent(AddPostActivity.this ,HomeActivity.class));
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });


                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddPostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }
    }

    private void imagePickDialog() {
        String[] options = {"Camera" , "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose image from");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){
                    cameraPick();


                }
                if (which == 1)
                    galleryPick();
                            }
        });
        builder.create().show();
    }

    private void cameraPick() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE , " Temp Pick");
        contentValues.put(MediaStore.Images.Media.TITLE , " Temp desc");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT , image_uri);
        startActivityForResult(intent , CAMERA_IMAGE_CODE);

    }

    private void galleryPick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent , GALLERY_IMAGE_CODE);
    }

    private void permission(){


        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){

            if(requestCode == GALLERY_IMAGE_CODE){
                image_uri = data.getData();
                blog_image.setImageURI(image_uri);
            }
            if(requestCode == CAMERA_IMAGE_CODE){

                blog_image.setImageURI(image_uri);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}



