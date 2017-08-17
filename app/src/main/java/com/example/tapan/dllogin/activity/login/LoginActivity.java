package com.example.tapan.dllogin.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapan.dllogin.R;
import com.example.tapan.dllogin.activity.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail, edtPassword;
    private TextInputLayout layoutEmail, layoutPassword;
    private Button btnSignIn;
    private TextView txtCreateUser,txtForgotPassword;
    private ProgressDialog progressDialog;
    private FirebaseApp app;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference userDatabaseReference;
    private String email=null, password=null, userUid=null;
    private boolean isUser;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        app = FirebaseApp.getInstance(getString(R.string.secondary));
        FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(app);
        userDatabaseReference = secondaryDatabase.getReference("userData");

        initView();

        if(firebaseAuth.getCurrentUser()!=null && firebaseAuth.getCurrentUser().isEmailVerified()){
            //Go to Main Activity(Already logged in)
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        btnSignIn.setOnClickListener(this);
        txtCreateUser.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
    }

    //For signing in with email and password
    private void signIn() {
        progressDialog.setMessage("Signing In");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.cancel();
                if (!task.isSuccessful()) {
                    //ERROR
                    Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                } else {
                    firebaseUser = firebaseAuth.getCurrentUser();
                    userUid = firebaseUser.getUid();
                    Log.d("uid", userUid);
                    isUser();
                    //if(isUser()){
                        //checkIfEmailVerified();
                    //}

                    /*Toast.makeText(LoginActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
                    intent = new Intent();
                    intent.setClass(LoginActivity.this,MainActivity.class);
                    startActivity(intent);*/
                }
            }
        });

    }

    private void checkIfEmailVerified() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user.isEmailVerified()) {
            // user is verified, so you can finish this activity or send user to activity which you want.
            intent = new Intent();
            intent.setClass(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
        } else {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            firebaseAuth.getInstance().signOut();
            Toast.makeText(LoginActivity.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
            //restart this activity
        }
    }

    private void isUser(){
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("inSnap", "In Data Snapshot");
                if (dataSnapshot.child("uidAndId").child(userUid).exists()) {
                    Log.d("emailExists", "Email Exists");
                    isUser=true;
                    checkIfEmailVerified();

                } else {
                    Log.d("emailNotFound", "Email Not Found!");
                    Toast.makeText(LoginActivity.this, "Email Not Found!!!", Toast.LENGTH_SHORT).show();
                    isUser=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("error", databaseError.getDetails());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnSignIn) {
            email = edtEmail.getText().toString().trim();
            password = edtPassword.getText().toString();
            Log.d("toString", "email and password converted to string");
            if (isEmpty()) {
                Log.d("notEmpty", "Not empty");
                signIn();

            }
        }
        if (view == txtCreateUser) {
            startActivity(new Intent(this, CreateUserActivity.class));
            //finish();
        }
        if (view == txtForgotPassword){
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        }
    }

    private boolean isEmpty(){
        if (email.isEmpty()) {
            edtEmail.setError("Enter Email");
            return false;
        }
        if (password.isEmpty()) {
            edtPassword.setError("Enter Password");
            return false;
        }
        if (password.length()<6){
            edtPassword.setError("Minimum 6 characters required");
            return false;
        }
        return true;
    }

    private void initView(){
        layoutEmail = (TextInputLayout) findViewById(R.id.layout_email);
        layoutPassword = (TextInputLayout) findViewById(R.id.layout_password);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        txtCreateUser = (TextView) findViewById(R.id.txt_create_user);
        txtForgotPassword = (TextView) findViewById(R.id.txt_forgot_password);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance(app);
    }
}
