package com.example.tapan.dllogin.activity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tapan.dllogin.R;
import com.example.tapan.dllogin.activity.activity.MainActivity;
import com.example.tapan.dllogin.activity.adapter.DashboardBookAdapter;
import com.example.tapan.dllogin.activity.model.BookDetailsDashboard;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

public class DashBoardFragment extends Fragment {

    private static final String TAG = "States";
    private TextView issuedBooksTV, txtBookName, txtAuthorName, txtAvgRating, txtPrice, txtPublication;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference, bookDatabaseReference, userDatabaseReference;
    private FirebaseDatabase firebaseDatabase;
    private DashboardBookAdapter dashboardBookAdapter;
    private RecyclerView recyclerView;
    private ArrayList<BookDetailsDashboard> bookDetailsDashboard;
    private ArrayList<String> booksDbKey;
    private ArrayList<String> booksDbName;
    private int issuedBooks = 0;
    private String uid = null, userId = null;
    private String temp;
    private BookDetailsDashboard obj_bookDetailsDashboard = new BookDetailsDashboard();



    public DashBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.dashboardRecyclerView);

        issuedBooksTV = (TextView) view.findViewById(R.id.issuedBooks);

        Log.d("fragment", "Dashboard");

        booksDbName = new ArrayList<>();
        booksDbKey = new ArrayList<>();
        bookDetailsDashboard = new ArrayList<>();
        dashboardBookAdapter = new DashboardBookAdapter(getContext(), bookDetailsDashboard);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(dashboardBookAdapter);

        FirebaseApp app = FirebaseApp.getInstance(getString(R.string.secondary));
        FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(app);
        firebaseAuth = FirebaseAuth.getInstance(app);
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();
        bookDatabaseReference = secondaryDatabase.getReference("bookData");
        userDatabaseReference = secondaryDatabase.getReference("userData");

        userDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userId = dataSnapshot.child("uidAndId").child(uid).child("Id").getValue().toString();
                obj_bookDetailsDashboard.setUserId(userId);
                Log.d("DASH", "userid " + userId);
                //Log.d("DASH",dataSnapshot.getValue().toString());
                if (dataSnapshot.child(userId).child("issuedBooks").exists()
                        && dataSnapshot.child(userId).child("issuedBooks").getChildrenCount()>0) {
                    userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            obj_bookDetailsDashboard.setNumberOfBooks((int) dataSnapshot.child(obj_bookDetailsDashboard.getUserId())
                                    .child("issuedBooks").getChildrenCount());

                            booksDbName.clear();
                            booksDbKey.clear();
                            issuedBooks = obj_bookDetailsDashboard.getNumberOfBooks();
                            Log.d("BOOKDATATAPAN", String.valueOf(issuedBooks)+"no of issued books.........");
                            for (DataSnapshot dsp : dataSnapshot.child(obj_bookDetailsDashboard.getUserId())
                                    .child("issuedBooks").getChildren()) {
//                          Log.d("BOOKDATATAPAN",dataSnapshot.child(obj_bookDetailsDashboard.getUserId())
//                            .child("issuedBooks").getValue().toString());
                                booksDbKey.add(dsp.getKey());
//                          Log.d("BOOKDATATAPAN", String.valueOf(booksDbName.size()));
                            }
                            bookDetailsDashboard.clear();
                            for (int i = 0; i < dataSnapshot.child(obj_bookDetailsDashboard.getUserId())
                                    .child("issuedBooks").getChildrenCount(); i++) {
                                Log.d("BOOKDATATAPAN", String.valueOf(booksDbKey.size()));
                                booksDbKey.get(i);
                                Log.d("BOOKDATATAPAN", booksDbKey.get(i));
                                String bookId = booksDbKey.get(i);
                                bookId = bookId.substring(0, 3);

                                obj_bookDetailsDashboard.setBookName(dataSnapshot.child(obj_bookDetailsDashboard.getUserId())
                                        .child("issuedBooks").child(booksDbKey.get(i)).child("bookName").getValue().toString());

                                obj_bookDetailsDashboard.setBookId(bookId);

                                temp = obj_bookDetailsDashboard.getBookName() + obj_bookDetailsDashboard.getBookId();

                                booksDbName.add(temp);

                                Log.d("BOOKDATATAPAN", temp + " value of temp");

                                obj_bookDetailsDashboard.setBookDbName(temp);
                            }

                            bookDatabaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (int i = 0; i < booksDbName.size(); i++) {
                                        Log.d("BOOKDATATAPAN", dataSnapshot.child(booksDbName.get(i))
                                                .getValue().toString());

                                        BookDetailsDashboard obj_bookDetailsDashboard = new BookDetailsDashboard();


                                        obj_bookDetailsDashboard.setAuthorName(dataSnapshot.child(booksDbName.get(i))
                                                .child("authorName").getValue().toString());
                                        Log.d("DashFrag",dataSnapshot.child(booksDbName.get(i))
                                                .child("authorName").getValue().toString());
                                        obj_bookDetailsDashboard.setBookName(dataSnapshot.child(booksDbName.get(i))
                                                .child("bookName").getValue().toString());

                                        Log.d("BOOKDATATAPAN","bookName123"+ obj_bookDetailsDashboard.getBookName());
                                        Log.d("BOOKDATATAPAN", String.valueOf(booksDbName.size()));

//                                        setRecyclerView();

                                        bookDetailsDashboard.add(obj_bookDetailsDashboard);
                                    }
                                    dashboardBookAdapter.notifyDataSetChanged();

                                    booksDbName.clear();

//                                    dashboardBookAdapter.notifyDataSetChanged();

                                }



                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                    /*dataSnapshot.child(userId).child("issuedBooks").getKey();
                    bookDetailsDashboard.add(obj_bookDetailsDashboard);*/
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    bookDetailsDashboard.clear();
                    Log.d("DASH", dataSnapshot.child(userId).child("issuedBooks").getValue().toString());
                } else {
                    issuedBooksTV.setText("No Issued ");
                    Log.d("DASH", "No Issued Books");
                    //No issued Books
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DASH", databaseError.getMessage().toString());

            }
        });



        /*bookDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String bookName, authorName, avgRating, price, publication;

                Log.d("data", dataSnapshot.getValue().toString());
                for (DataSnapshot bookNameDataSnapshot : dataSnapshot.getChildren()) {
                    booksName.add(String.valueOf(bookNameDataSnapshot.getKey()));
//                    Log.d("bookData", String.valueOf(booksName.get(0)));

                }
                for (book = 0; book < booksName.size(); book++){
                    bookDatabaseReference.child(booksName.get(book)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String bookName, authorName, avgRating, price, publication;

                            bookName = dataSnapshot.getKey().toString();
                            authorName = dataSnapshot.child("author").getValue().toString();
                            avgRating = dataSnapshot.child("avg_rating").getValue().toString();
                            price = dataSnapshot.child("price").getValue().toString();
                            publication = dataSnapshot.child("publication").getValue().toString();
                            Log.e("bookName",bookName);
                            Log.e("authorName",authorName);
                            Log.e("avgRating",avgRating);
                            Log.e("price",price);
                            Log.e("publication",publication);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
//                Log.d("SizeArray",String.valueOf(booksName.size()));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        /*String temp;
        Log.d("SizeArray",String.valueOf(booksName.size()));
        for (book = 0; book < 10; book++) {
            temp = booksName.get(book);
            Log.d("fadaiGai", temp);
            bookDatabaseReference = firebaseDatabase.getReference(temp);

            bookDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String bookName, authorName, avgRating, price, publication;

                    authorName = dataSnapshot.child("author").getValue().toString();
                    avgRating = dataSnapshot.child("avg_rating").getValue().toString();
                    price = dataSnapshot.child("price").getValue().toString();
                    publication = dataSnapshot.child("publication").getValue().toString();

                    Log.d("author", authorName);
                    txtAuthorName.setText(authorName);
                    txtAvgRating.setText(avgRating);
                    txtPrice.setText(price);
                    txtPublication.setText(publication);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/
        return view;
    }

    void setRecyclerView(){
        Log.d("BOOKDATATAPAN","in setrecycler");
        bookDetailsDashboard.add(obj_bookDetailsDashboard);

    }

    /*private void getIssuedBooks() {
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                obj_bookDetailsDashboard.setNumberOfBooks((int) dataSnapshot.child(obj_bookDetailsDashboard.getUserId())
                        .child("issuedBooks").getChildrenCount());
                issuedBooks = obj_bookDetailsDashboard.getNumberOfBooks();
//                Log.d("BOOKDATATAPAN", String.valueOf(issuedBooks));
                for (DataSnapshot dsp : dataSnapshot.child(obj_bookDetailsDashboard.getUserId())
                        .child("issuedBooks").getChildren()) {
//                    Log.d("BOOKDATATAPAN",dataSnapshot.child(obj_bookDetailsDashboard.getUserId())
//                            .child("issuedBooks").getValue().toString());
                    booksDbKey.add(dsp.getKey());
//                    Log.d("BOOKDATATAPAN", String.valueOf(booksDbName.size()));
                }

                for (int i = 0; i < issuedBooks; i++) {
                    booksDbKey.get(i);
                    Log.d("BOOKDATATAPAN", booksDbKey.get(i));
                    String bookId = booksDbKey.get(i);
                    bookId = bookId.substring(0, 3);

                    obj_bookDetailsDashboard.setBookName(dataSnapshot.child(obj_bookDetailsDashboard.getUserId())
                            .child("issuedBooks").child(booksDbKey.get(i)).child("bookName").getValue().toString());



                    obj_bookDetailsDashboard.setBookId(bookId);

                    temp = obj_bookDetailsDashboard.getBookName() + obj_bookDetailsDashboard.getBookId();

                    booksDbName.add(temp);

                    Log.d("BOOKDATATAPAN", temp + " value of temp");

                    obj_bookDetailsDashboard.setBookDbName(temp);



                }
                    bookDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (int i = 0; i < booksDbName.size(); i++) {
                                Log.d("BOOKDATATAPAN", dataSnapshot.child(booksDbName.get(i))
                                        .getValue().toString());
                                obj_bookDetailsDashboard.setAuthorName(dataSnapshot.child(booksDbName.get(i))
                                        .child("authorName").getValue().toString());
                                obj_bookDetailsDashboard.setBookName(dataSnapshot.child(booksDbName.get(i))
                                        .child("bookName").getValue().toString());

                                bookDetailsDashboard.add(obj_bookDetailsDashboard);
                            }
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    *//*dataSnapshot.child(userId).child("issuedBooks").getKey();
                    bookDetailsDashboard.add(obj_bookDetailsDashboard);*//*
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }*/

    @Override
    public void onResume() {
        super.onResume();
        Log.d("PAUSE","oneresume");
        dashboardBookAdapter.notifyDataSetChanged();
//        bookDetailsDashboard.add(obj_bookDetailsDashboard);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("PAUSE","onepuse");
    }
}
