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

public class BookAccountHistory extends AppCompatActivity implements View.OnClickListener{

    Intent intent = getIntent();
    BookData bookData = new BookData();
    private TextView bookNameTV, authorNameTV, publicationTV, issuesTV, availableBooksTV, avgRatingTV, priceTV, pagesTV;
    private EditText reviewET;
    private ImageView bookIV;
    private RatingBar ratingBar;
    private Button btnRate, btnReview;
    private String bookDbName, bookUid, bookName, authorName, publication, review;
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
        setContentView(R.layout.activity_book_account_history);
        bookDbName = BookDbName.getBookDbName();
        bookUid = BookDbName.getBookUid();
        Log.d("bookDbName", bookDbName);
        Log.d("bookUid", bookUid);

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

        btnRate.setOnClickListener(this);
        btnReview.setOnClickListener(this);

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

    public void onRateClick(){
        rating = ratingBar.getRating();
        Toast.makeText(this, "Clicked Rating with "+rating, Toast.LENGTH_SHORT).show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //userUid = firebaseUser.getUid();
                Log.d("userUid", userUid);
                userId = dataSnapshot.child("userData").child("uidAndId").child(userUid).child("Id").getValue().toString();

                DataSnapshot ratingSnapShot = dataSnapshot.child("bookData").child(bookDbName).child("ratings");
                long childC = ratingSnapShot.getChildrenCount();
                Log.d("rating123", "Child COunt" +childC);

                bookDatabaseReference.child("ratings").child(userId).setValue(rating);
                userDatabaseReference.child(userId).child("returnedBooks").child(bookUid).child("rating").setValue(rating);

                getAllRatings();
                /*bookDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int ratingCount = (int) dataSnapshot.child(bookDbName).child("ratings").getChildrenCount();
                        for(int i=0; i<ratingCount; i++){
                            dataSnapshot.child(bookDbName).child("ratings").getValue(i);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //bookDatabaseReference.child("avgRating").setValue(bookData.getAvgRating());

    }

    public void getAllRatings(){
        ratingKey.clear();
        ratingValue.clear();
        totalRating = 0;
        bookDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dsp : dataSnapshot.child("ratings").getChildren()){
                    ratingKey.add(dsp.getKey());
                    //Log.d("ratingKey", ratingKey.toString());
                }
                for (int i=0; i<dataSnapshot.child("ratings").getChildrenCount(); i++){
                    Float temp = Float.valueOf(dataSnapshot.child("ratings").child(ratingKey.get(i)).getValue().toString());
                    ratingValue.add(temp);
                    Log.d("ratingValue", ratingValue.toString());
                }
                for (int j=0; j<ratingValue.size(); j++){
                    totalRating = totalRating + ratingValue.get(j);
                    Log.d("ratingValue", "total" +totalRating);
                }
                avgRating = totalRating/ratingValue.size();
                Log.d("ratingValue", "avgRating " +avgRating);

                bookData.setAvgRating(avgRating);
                Log.d("rating", "getAvgRating "+bookData.getAvgRating());
                bookDatabaseReference.child("avgRating").setValue(bookData.getAvgRating());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void onReviewClick() {
        Toast.makeText(this, "Review Submitted ", Toast.LENGTH_SHORT).show();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userUid = firebaseUser.getUid();
                Log.d("userUid", userUid);
                userId = dataSnapshot.child("userData").child("uidAndId").child(userUid).child("Id").getValue().toString();
                bookDatabaseReference.child("reviews").child(userId).setValue(reviewET.getText().toString());
                userDatabaseReference.child(userId).child("returnedBooks").child(bookUid).child("review").setValue(reviewET.getText().toString());
                reviewET.setText("");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //userDatabaseReference.child(userId).child("returnedBooks").child(bookUid).child("review").setValue(review);
    }

    @Override
    public void onClick(View view) {
        if(view == btnRate){
            onRateClick();
        }
        if(view == btnReview){
            review = reviewET.getText().toString();
            Log.d("review", review);
            if(review.isEmpty()){
                reviewET.setError("No Review Written");
            } else {
                onReviewClick();
            }
        }
    }
}
