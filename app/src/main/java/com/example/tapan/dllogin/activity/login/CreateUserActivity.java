package com.example.tapan.dllogin.activity.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tapan.dllogin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateUserActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText edtEmail, edtPassword, edtRePassword, edtUserId, edtUsername;
    private TextInputLayout layoutEmail, layoutPassword, layoutRePassword;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private FirebaseApp app;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference userDatabaseReference;
    private FirebaseDatabase secondaryDatabase;
    private FirebaseAuth.AuthStateListener authStateListener;
    private boolean authFlag = true, isFirstTimeUser;
    private String email=null, password=null, rePassword=null, username=null, userId=null, userUid=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        app = FirebaseApp.getInstance(getString(R.string.secondary));
        secondaryDatabase = FirebaseDatabase.getInstance(app);
        userDatabaseReference = secondaryDatabase.getReference("userData");

        initView();

        btnRegister.setOnClickListener(this);
    }

    private void btnRegisterUser(){
//        authStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                Log.d("auth", "AUTH CHANGED!!!!!");
//                FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
//                if(user!=null){
//                    sendVerificationEmail(user);
//                }else {
//
//                }
//            }
//        };
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d("auth", "AUTH CHANGED!!!!!   ");
                firebaseUser = firebaseAuth.getCurrentUser();
                userUid = firebaseUser.getUid();
                saveUserInfo();
                if(!firebaseUser.isEmailVerified()){
                    sendVerificationEmail(firebaseUser);
                }else {
                    Log.d("auth", "user is verified");
                }
            }
        };

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //user is registered and logged in
                            // start home activity
                            Toast.makeText(CreateUserActivity.this,"Registered Successfully",Toast.LENGTH_LONG).show();
                            progressDialog.cancel();
                            firebaseAuth.addAuthStateListener(authStateListener);
                            FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
                            //sendVerificationEmail(user);
                        }
                        else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(CreateUserActivity.this, "User with this email already exists.", Toast.LENGTH_SHORT).show();
                            progressDialog.cancel();
                        }
                        else {
                            Log.d("createUserException",task.getException().getMessage());
                            Toast.makeText(CreateUserActivity.this,"Error, plz try " +
                                    "again later",Toast.LENGTH_LONG).show();
                            progressDialog.cancel();
                        }
                    }
                });


//        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                Log.d("auth", "AUTH CHANGED!!!!!   ");
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if(!user.isEmailVerified() && authFlag){
//                    authFlag = false;
//                    sendVerificationEmail(user);
//                }else {
//                    Log.d("auth", "user is verified");
//                }
//            }
//        });
    }

    @Override
    public void onClick(View view) {
        if (view == btnRegister){
            progressDialog.setMessage("Registering User");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            email = edtEmail.getText().toString();
            password = edtPassword.getText().toString();
            rePassword = edtRePassword.getText().toString();
            username = edtUsername.getText().toString();
            userId = edtUserId.getText().toString();

            if(isEmpty()){
                userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.child(userId).exists()){
                            btnRegisterUser();
                        } else{
                            progressDialog.cancel();
                            Toast.makeText(CreateUserActivity.this, "There is already an account created from this User ID.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("error", databaseError.getDetails());
                        progressDialog.cancel();
                    }
                });

                //isFirstTImeUser();
                //btnRegisterUser();
            } else{
                progressDialog.cancel();
            }
        }
    }

    private boolean isEmpty(){
        if(TextUtils.isEmpty(email)||!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edtEmail.setError("Invalid Email");
            return false;
        }
        if(TextUtils.isEmpty(password)){
            edtPassword.setError("Enter Password");
            return false;
        }
        if(!password.equals(rePassword)){
            edtRePassword.setError("Password Does Not Match");
            return false;
        }
        if(password.length()<6){
            edtPassword.setError("Minimum 6 characters required");
            return false;
        }
        if(TextUtils.isEmpty(username)){
            edtUsername.setError("Enter Your Name");
            return false;
        }
        if(TextUtils.isEmpty(userId)){
            edtUserId.setError("Enter Your User ID");
            return false;
        }
        return true;
    }

    private void saveUserInfo(){
        secondaryDatabase.getReference(userId);
        //
        // userDatabaseReference.setValue(userId);
        userDatabaseReference.child(userId).child("userId").setValue(userId);
        userDatabaseReference.child(userId).child("Name").setValue(username);
        userDatabaseReference.child("uidAndId").child(userUid).child("Id").setValue(userId);
    }

    private void sendVerificationEmail(FirebaseUser user) {

        //FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.cancel();
                    Toast.makeText(CreateUserActivity.this,"Verification email sent",Toast.LENGTH_LONG).show();
                    firebaseAuth.removeAuthStateListener(authStateListener);
                }
                else {
                    progressDialog.cancel();
                    Log.d("task",task.getException().getMessage());
                    Toast.makeText(CreateUserActivity.this,"Error!!! Please Try Again Later",Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);

                //startActivity(new Intent(CreateUserActivity.this, LoginActivity.class));
                finish();

            }
        });
    }

    private void initView(){
        layoutEmail = (TextInputLayout) findViewById(R.id.layout_email);
        layoutPassword = (TextInputLayout) findViewById(R.id.layout_password);
        layoutRePassword = (TextInputLayout) findViewById(R.id.layout_re_password);
        edtEmail = (EditText) findViewById(R.id.edt_email);
        edtPassword = (EditText) findViewById(R.id.edt_password);
        edtRePassword = (EditText) findViewById(R.id.edt_re_password);
        edtUserId = (EditText) findViewById(R.id.edt_user_id);
        edtUsername = (EditText) findViewById(R.id.edt_username);
        btnRegister = (Button) findViewById(R.id.btn_Register);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance(app);
    }
}
