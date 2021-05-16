package com.example.yummfoodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText emailId, passwordEt;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        passwordEt = findViewById(R.id.editText2);
        tvSignIn = findViewById(R.id.textView2);
        btnSignUp = findViewById(R.id.button2);
        FirebaseUser user=mFirebaseAuth.getCurrentUser();
        if(user!=null){
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        }


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailId.getText().toString();
                String password = passwordEt.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("please enter your email");
                    emailId.setFocusable(true);

                }
                else if(!LoginActivity.isEmailValid(email)){
                    emailId.setError("Invalid Email");
                    emailId.setFocusable(true);
                }
                else if(password.isEmpty()){
                    passwordEt.setError("please enter your password");
                    passwordEt.setFocusable(true);
                }
                else{
    registerUser(email, password);

                }

             /* else if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd) .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Registered Failed, please enter proper email address && password",Toast.LENGTH_SHORT).show();

                            }
                            else{
                                FirebaseUser user = mFirebaseAuth.getCurrentUser();

                                //get user email and userId from auth
                                String email = user.getEmail();
                                String uid = user.getUid();

                                // when user registered, user info is in realtime database too
                                //using Hasmap
                                HashMap<Object, String> hashMap = new HashMap<>();
                                //put into hashmap

                                hashMap.put ("email",email);
                                hashMap.put ("userId",uid);

                                ////firebase database instance

                               FirebaseDatabase database = FirebaseDatabase.getInstance();

                                // path to store user data named "user"
                             DatabaseReference reference = database.getReference("Users");
                                //put data with in hasgmap  in database

                             reference.child(uid).setValue(hashMap);

                                Toast.makeText(MainActivity.this," registered"+user.getEmail(),Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                            }
                        }
                    });
                }*/
              //  else{
               //     Toast.makeText(MainActivity.this," Error Occurred",Toast.LENGTH_SHORT).show();

          //      }

            }
        });

      tvSignIn.setOnClickListener(new View.OnClickListener() {
           @Override
          public void onClick(View v) {
               Intent i =new Intent(MainActivity.this,LoginActivity.class);
               startActivity(i);
           }
      });


    }

    private void registerUser(String email,String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password) .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    //get user email and userId from auth
                    String email = user.getEmail();
                    String uid = user.getUid();

                    // when user registered, user info is in realtime database too
                    //using Hasmap
                    HashMap<Object, String> hashMap = new HashMap<>();
                    //put into hashmap

                      hashMap.put ("email",email);
                      hashMap.put ("userId",uid);
                      hashMap.put ("name","");//will be abel to edit in profile
                    hashMap.put ("image","");//will be abel to edit in profile
                    hashMap.put ("cover","");//will be abel to edit in profile


                    ////firebase database instance

                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    // path to store user data named "users"
                    DatabaseReference reference = database.getReference("Users");
                    //put data with in hasgmap  in database

                    reference.child(uid).setValue(hashMap);

                    Toast.makeText(MainActivity.this," registered"+user.getEmail(),Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(MainActivity.this,ProfileActivity.class));
                    finish();

                }
                else{
                    //if sign in fails, display a message to user
                    Toast.makeText(MainActivity .this," register failed",Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity .this," "+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }
}