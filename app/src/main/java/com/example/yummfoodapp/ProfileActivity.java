package com.example.yummfoodapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.renderscript.Sampler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yummfoodapp.Adapter.MyPostAdapter;
import com.example.yummfoodapp.Adapter.PostAdapter;
import com.example.yummfoodapp.Model.PostModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class ProfileActivity extends AppCompatActivity {

    //firebase auth

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    StorageReference storageReference;


    //path where images of user profile will be stored
    String storagePath = "Users_Profile_Cover_images/";

    CircleImageView avatarIv, coverIv ;
    TextView backtoHome,emailTv,nameTv;
    FloatingActionButton fab;
    ProgressDialog pd;
    RecyclerView PostsRecycleView;

    //permissions

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 100;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    //arrays of permission to be requested
    String cameraPermissions[];
    String storagePermissions[];

    List<PostModel> postModelList=new ArrayList<>();
    MyPostAdapter postAdapter;
    String uid;

    Uri downloadUri;

    //uri of pick image

    Uri image_uri;

    //checking profile pic
    String profilePic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        backtoHome = findViewById(R.id.back);

        //init firebase
        firebaseAuth = firebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference = getInstance().getReference();
        //init array of permissions
        cameraPermissions = new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions= new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //init views
       // coverIv = findViewById(R.id.coverIv);
        avatarIv = findViewById(R.id.profile_image);
        emailTv = findViewById(R.id.emailTv);
        nameTv= findViewById(R.id.nameTv);
        fab = findViewById(R.id.fab);
        PostsRecycleView = findViewById(R.id.recyclerView_posts);
        pd = new ProgressDialog(ProfileActivity.this);
/*
we have to get info of currently signed in user, we can get it using user's email or uid, but
 here is using email, By using orderByChild query we will show the detail from a node whose key named email has value
  equal to currently signed in email. it will search all nodes, where the key matches it will get its details


*/

        if(user!=null){
            Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //check until required data get
                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        //get data
                        String name = ""+ ds.child("name").getValue();
                        String email = ""+ ds.child("email").getValue();
                        String image = ""+ ds.child("image").getValue();

                        //set data
                        nameTv.setText(name);
                        emailTv.setText(email);
                        try{
                            //if image is recieved then set
                            Picasso.get().load(image).into(avatarIv);
                        }
                        catch (Exception e){
                            //if there is any exception while getting image then set default
                            Picasso.get().load(R.drawable.logoo).into(avatarIv);

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else {
            Toast.makeText(this, "Sign Up !", Toast.LENGTH_SHORT).show();
        }


//fab button

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });
      checkUserStatus();
      loadMyPosts();

    backtoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a =new Intent(ProfileActivity.this,HomeActivity.class);
                startActivity(a);
            }
        });

    askPermission();
    }

    private void loadMyPosts() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(ProfileActivity.this);

        //show newest post first
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        //set this layout to recyclerView
        PostsRecycleView.setLayoutManager(layoutManager);

        //init posts list
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
        //query to load posts
        Query query = ref.orderByChild("uid").equalTo(uid);
        //get all data from this ref
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 postModelList.clear();
                 for(DataSnapshot ds: snapshot.getChildren()){
                     PostModel myPosts = ds.getValue(PostModel.class);
                    postModelList.add(myPosts);
                     //add to list
                     postAdapter =  new MyPostAdapter(ProfileActivity.this, postModelList);

                     //set this adapter to recyclerView
                     PostsRecycleView.setAdapter(postAdapter);
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void askPermission() {
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {askPermission();}
        }).check();
    }

  /*  private boolean checkStoragePermission(){
//check id storage permission is enabled or nit
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestStoragePermission(){
        //request runtime storage permission
        ActivityCompat.requestPermissions(ProfileActivity.this, storagePermissions, STORAGE_REQUEST_CODE);



    }
    private boolean checkCameraPermission(){
//check id storage permission is enabled or nit
        //return true if enabled
        //return false if not enabled
        boolean result = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(ProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    private void requestCameraPermission(){
        //request runtime storage permission
        ActivityCompat.requestPermissions(ProfileActivity.this, cameraPermissions, CAMERA_REQUEST_CODE);



    }*/

    private void showEditProfileDialog() {
        //show dialog will abel to edit profile pic , name
        String options[] = {"Edit Profile Picture", "Edit Name"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder( ProfileActivity.this);
       //set title
        builder.setTitle("Choose Action");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            //handle dialog items clicks
                if(which == 0){
                    //edit profile clicked
                    pd.setMessage("updating profile pic");
                    profilePic = "image";
                    showImagePicDialog();

                }
                else if(which == 1){
                    pd.setMessage("updating name");
                    showNameDialog("name");


                }
            }
        });
        //create  and show dialog
        builder.create().show();

    }

    private void showNameDialog(final String key) {
          AlertDialog.Builder reNameBuilder = new AlertDialog.Builder(ProfileActivity.this);
        reNameBuilder.setTitle("update" + key);
        LinearLayout linearLayout = new LinearLayout(ProfileActivity.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
      final EditText editText = new EditText(ProfileActivity.this);
       editText.setHint("enter" + key);
        linearLayout.addView(editText);
        reNameBuilder.setView(linearLayout);
        // add button to update
        reNameBuilder.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text from edit text
                final String value = editText.getText().toString().trim();
                if(!TextUtils.isEmpty(value)){
                    pd.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(ProfileActivity.this,"updated...", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(ProfileActivity.this, "not updated successful", Toast.LENGTH_SHORT).show();
                                }
                            });
                    //if user edit name , it also will be changed from his post
                    if(key.equals("name")){
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                        Query query = ref.orderByChild("uid").equalTo(uid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds: snapshot.getChildren()){
                                    String child = ds.getKey();
                                    snapshot.getRef().child(child).child("uName").setValue(value);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        //update name in current users comment on posts
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot ds: snapshot.getChildren()){
                                    String child =ds.getKey();
                                    if(snapshot.child(child).hasChild("Comments")){
                                        String child1 = ""+snapshot.child(child).getKey();
                                        Query child2 = FirebaseDatabase.getInstance().getReference("Posts").child(child1).child("Comments").orderByChild("uid");
                                        child2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot ds:snapshot.getChildren()){
                                                    String child = ds.getKey();
                                                    snapshot.getRef().child(child).child("uName").setValue(value);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }

                }

            }

        });

        reNameBuilder.show();

    }

    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};

        //alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder( ProfileActivity.this);
        //set title
        builder.setTitle("Pick an Image");
        //set items to dialog
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //handle dialog items clicks
                if(which == 0){
                    //Camera clicked

                        pickFromCamera();
                        requestCodeVal=0;


                }
                else if(which == 1){
                    //Gallery clicked

                        pickFromGallery();
                        requestCodeVal=1;

                }
            }
        });
        //create  and show dialog
        builder.create().show();
    }
    int requestCodeVal=-1;

 @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method called when user press allow or deny from permission request dialog

        switch(requestCode){
            case 1010:
                {
                if(grantResults.length >0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if(cameraAccepted && writeStorageAccepted){
                        pickFromCamera();
                    }
                     else{
                        //permisssion denied
                        Toast.makeText(ProfileActivity.this,"please enable camera permission", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            break;
            case STORAGE_REQUEST_CODE: {
                if(grantResults.length >0){

                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                         if( writeStorageAccepted){
                        pickFromGallery();
                    }
                         else{
                        //permisssion denied
                        Toast.makeText(ProfileActivity.this,"please enable storage permission", Toast.LENGTH_SHORT);
                    }

                }

            }
            break;
            default:
                throw new IllegalStateException("Unexpected value: " + requestCode);
        }


       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         //called after picking image from cam or gallery
        if(resultCode == RESULT_OK){

            if(requestCodeVal== 1){
                image_uri = data.getData();
                avatarIv.setImageURI(image_uri);
                uploadProfilecoverPicture(image_uri);


            }
            if(requestCodeVal == 0){
                uploadProfilecoverPicture(image_uri);


            }
        }

    }

    String imgPath="";
    private void uploadProfilecoverPicture(Uri uri) {

        pd.show();
        String filepathandname = storagePath + "" +profilePic+ "" +user.getUid();
        StorageReference storageReference2nd  = storageReference.child(filepathandname);
        storageReference2nd.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask =  taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                         downloadUri = uriTask.getResult();
                            imgPath= String.valueOf(taskSnapshot.getStorage().getDownloadUrl());
                        //check if image is uploaded or not
                        if(uriTask.isSuccessful()){
                            //image uploaded
                            //add / update url in user's database
                            HashMap<String, Object> results = new HashMap<>();
                            results.put(profilePic, downloadUri.toString());

                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            pd.dismiss();
                                            Toast.makeText(ProfileActivity.this,"image uploaded",Toast.LENGTH_SHORT).show();
                                            setImage();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pd.dismiss();
                                            Toast.makeText(ProfileActivity.this,"error occurred updating image",Toast.LENGTH_SHORT).show();
                                        }
                                    });


                        }
                        else {
                            //error
                            pd.dismiss();

                            Toast.makeText(ProfileActivity.this,"error occurred",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(ProfileActivity.this,"Error"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                Toast.makeText(ProfileActivity.this, "Processsing", Toast.LENGTH_SHORT).show();
            }
        });

            //update user image in current users comments on posts
           /* ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds: snapshot.getChildren()){
                        String child =ds.getKey();
                        if(snapshot.child(child).hasChild("Comments")){
                            String child1 = ""+snapshot.child(child).getKey();
                            Query child2 = FirebaseDatabase.getInstance().getReference("Posts").child(child1).child("Comments").orderByChild("uid");
                            child2.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot ds:snapshot.getChildren()){
                                        String child = ds.getKey();
                                        snapshot.getRef().child(child).child("uDp").setValue(downloadUri.toString());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });*/


    }

    private void setImage() {
        if (downloadUri!=null) {

            if (profilePic.equals("image")) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Posts");
                Query query = ref.orderByChild("uid").equalTo(uid);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String child = ds.getKey();
                            snapshot.getRef().child(child).child("uDp").setValue(downloadUri.toString());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            } else {
                Toast.makeText(this, "Didn't find Image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);

    }

    private void pickFromCamera() {
        //intent of picking image from device camera
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");


        //put image uri
        image_uri = ProfileActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);



    }


    public void checkUserStatus(){
     FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user !=null){
     //   mProfileTv.setText(user.getEmail());
            uid = user.getUid();
     }
     else{
         startActivity(new Intent(ProfileActivity.this,MainActivity.class));
    finish();
       }
   }




   // @Override
   // protected void onStart() {
        //check on start of app
     //   checkUserStatus();
    //    super.onStart();

  //  }

}