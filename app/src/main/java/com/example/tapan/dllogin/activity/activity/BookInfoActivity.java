package com.example.tapan.dllogin.activity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tapan.dllogin.R;
import com.example.tapan.dllogin.activity.model.BookData;
import com.example.tapan.dllogin.activity.model.BookDbName;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookInfoActivity extends AppCompatActivity {

    Intent intent = getIntent();
    BookData bookData = new BookData();
    private TextView bookNameTV, authorNameTV, publicationTV, issuesTV, availableBooksTV, avgRatingTV, priceTV, pagesTV;
    private EditText reviewET;
    private ImageView bookIV;
    private RatingBar ratingBar;
    private Button btnRate, btnReview;
    private String bookDbName, bookName, authorName, publication, review;
    private int issues, availableBooks;
    private float avgRating; private float price; private float totalRating=0; private float temp=0;
    private Float rating;
    private ArrayList<String> ratingKey;
    private ArrayList<Float> ratingValue;

    private String userUid, userId;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    DatabaseReference databaseReference, bookDatabaseReference, userDatabaseReference;

    FirebaseApp app = FirebaseApp.getInstance("secondary");
    FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(app);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_info);

        bookDbName = BookDbName.getBookDbName();
        Log.d("bookDbName", bookDbName);

        databaseReference = secondaryDatabase.getReference();
        bookDatabaseReference = secondaryDatabase.getReference("bookData").child(bookDbName);
        userDatabaseReference = secondaryDatabase.getReference("userData");

        firebaseAuth = FirebaseAuth.getInstance(app);
        firebaseUser = firebaseAuth.getCurrentUser();
        userUid = firebaseUser.getUid();

        Log.d("userUid", userUid);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnRate = (Button) findViewById(R.id.ratingButton);
        btnReview = (Button) findViewById(R.id.reviewButton);

        bookIV = (ImageView) findViewById(R.id.bookPhotoImageView);

        ratingKey = new ArrayList<>();
        ratingValue = new ArrayList<>();

        reviewET = (EditText) findViewById(R.id.reviewEditText);

        bookNameTV = (TextView) findViewById(R.id.bookNameTextView);
        authorNameTV = (TextView) findViewById(R.id.authorTextView);
        publicationTV = (TextView) findViewById(R.id.publicationTextView);
        issuesTV = (TextView) findViewById(R.id.issueTextView);
        availableBooksTV = (TextView) findViewById(R.id.availableTextView);
        avgRatingTV = (TextView) findViewById(R.id.avgRatingTextView);
        priceTV = (TextView) findViewById(R.id.priceTextView);
        pagesTV = (TextView) findViewById(R.id.pagesTextView);

        reviewET = (EditText) findViewById(R.id.reviewEditText);

        Glide.with(this).load("http://eurodroid.com/pics/beginning_android_book.jpg").into(bookIV);

        bookDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookNameTV.setText(dataSnapshot.child("bookName").getValue().toString());
                authorNameTV.setText(dataSnapshot.child("authorName").getValue().toString());
                publicationTV.setText(dataSnapshot.child("publication").getValue().toString());
                issuesTV.setText(dataSnapshot.child("issues").getValue().toString());
                availableBooksTV.setText(dataSnapshot.child("availableBooks").getValue().toString());
                if (dataSnapshot.child("avgRating").exists()){
                    avgRatingTV.setText(dataSnapshot.child("avgRating").getValue().toString());
                }
                priceTV.setText(dataSnapshot.child("price").getValue().toString());
                pagesTV.setText(dataSnapshot.child("pages").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
