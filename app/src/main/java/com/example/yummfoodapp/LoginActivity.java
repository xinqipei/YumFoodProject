package com.example.yummfoodapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN=1234;
    EditText emailId, passwordEt;
    Button btnSignIn;
    Button btnLoginWithGoogle;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    ProgressDialog pb;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init
        pb=new ProgressDialog(this);
        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        passwordEt = findViewById(R.id.editText2);
        btnSignIn = findViewById(R.id.button2);
        tvSignUp = findViewById(R.id.textView2);
        btnLoginWithGoogle=findViewById(R.id.button3);

        btnLoginWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {

                loginWithGoogle();
            }
        });
        setupGoogleLogin();

        // mAuthStateListener = new FirebaseAuth.AuthStateListener() {

        //    @Override
        //   public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {



        //     FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
        //    if( mFirebaseUser != null){
        //       Toast.makeText(LoginActivity.this,"You are logged in successfully",Toast.LENGTH_SHORT).show();
        //       Intent i = new Intent(LoginActivity.this, HomeActivity.class);
        //       startActivity(i);
        //   }
        //   else{
        Toast.makeText(LoginActivity.this,"please login",Toast.LENGTH_SHORT).show();
        //   }
        //  }
        //  };
        btnSignIn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                String email = emailId.getText().toString();
                String password = passwordEt.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("please enter your email");
                    emailId.setFocusable(true);

                }
                else if(!isEmailValid(email)){
                    emailId.setError("Invalid Email");
                    emailId.setFocusable(true);
                }
                else if(password.isEmpty()){
                    passwordEt.setError("please enter your password");
                    passwordEt.setFocusable(true);
                }

                else{

                    loginUser(email, password);
                }
                //  else if((email.isEmpty() && pwd.isEmpty())){
                //     Toast.makeText(LoginActivity.this,"Fields are empty!",Toast.LENGTH_SHORT).show();
                //  }
             /*   else if(!(email.isEmpty() && pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this," Login Error please try again ",Toast.LENGTH_SHORT).show();
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

                                Toast.makeText(LoginActivity.this," "+user.getEmail(),Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                            }
                        }
                    });
                }*/
                //   else{
                //     Toast.makeText(LoginActivity.this," Error Occurred",Toast.LENGTH_SHORT).show();

                //     }


            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intSignUp = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intSignUp);
            }
        });
    }

    private void loginUser(String email, String password)
    {

        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
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
                    hashMap.put ("name","");
                    hashMap.put ("image","");
                    hashMap.put ("cover","");//will be abel to edit in profile

                    ////firebase database instance

                    FirebaseDatabase database = FirebaseDatabase.getInstance();

                    // path to store user data named "user"
                    DatabaseReference reference = database.getReference("Users");
                    //put data with in hasgmap  in database

                    reference.child(uid).setValue(hashMap);

                    Toast.makeText(LoginActivity.this," "+user.getEmail(),Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this,ProfileActivity.class));

                }
                else{
                    Toast.makeText(LoginActivity.this," Login Error please try again ",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //error, get and show message
                Toast.makeText(LoginActivity.this," "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public  void  setupGoogleLogin()
    {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient =GoogleSignIn.getClient(this , gso);
    }

    private void loginWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Snackbar.make(findViewById(android.R.id.content),"Error Login", BaseTransientBottomBar.LENGTH_LONG).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        pb.setCancelable(false);
        pb.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {

                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else
                        {
                            pb.dismiss();
                            Snackbar.make(findViewById(android.R.id.content),"Error Login", BaseTransientBottomBar.LENGTH_LONG).show();

                        }
                    }
                });
    }

    private void updateUI (FirebaseUser user)
    {




        String email = user.getEmail();
        final String uid = user.getUid();

        // when user registered, user info is in realtime database too
        //using Hasmap
        final HashMap<Object, String> hashMap = new HashMap<>();
        //put into hashmap

        hashMap.put ("email",email);
        hashMap.put ("userId",uid);
        hashMap.put ("name","");
        hashMap.put ("image","");
        hashMap.put ("cover","");//will be abel to edit in profile

        ////firebase database instance

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        // path to store user data named "user"
        final DatabaseReference reference = database.getReference("Users");
        //put data with in hasgmap  in database

        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot) {
                pb.dismiss();
                if (!snapshot.exists())
                {
                    reference.child(uid).setValue(hashMap);
                }
                startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error) {
                pb.dismiss();
            }
        });


    }


    //@Override
    // protected void onStart() {

    //   super.onStart();
    //   mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    //  }
}