package com.example.tapan.dllogin.activity.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapan.dllogin.R;
import com.example.tapan.dllogin.activity.adapter.AccountHistoryAdapter;
import com.example.tapan.dllogin.activity.model.BookDetailsAccountHistory;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountHistoryFragment extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference, userDatabaseReference, bookDatabaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private String userUid=null, userId=null;
    private ArrayList<BookDetailsAccountHistory> accountHistoryArrayList;
    private ArrayList<String> bookDbName;
    private ArrayList<String> bookDbKey;
    private ArrayList<String> bookUid;
    private AccountHistoryAdapter accountHistoryAdapter;
    private RecyclerView recyclerView;
    private BookDetailsAccountHistory obj_book_details_history = new BookDetailsAccountHistory();
    private TextView historyOfBooks;

    int i=0;

    public AccountHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("fragment", "History Fragment");

        View view = inflater.inflate(R.layout.fragment_account_history, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.accountHistoryRecyclerView);

        bookDbName = new ArrayList<>();
        bookDbKey = new ArrayList<>();
        bookUid = new ArrayList<>();
        accountHistoryArrayList = new ArrayList<>();
        accountHistoryAdapter = new AccountHistoryAdapter(getContext(), accountHistoryArrayList);

        historyOfBooks = (TextView) view.findViewById(R.id.historyOfBooks);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(accountHistoryAdapter);

        FirebaseApp app = FirebaseApp.getInstance(getString(R.string.secondary));
        FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(app);
        databaseReference = secondaryDatabase.getReference();
        userDatabaseReference = secondaryDatabase.getReference("userData");
        bookDatabaseReference = secondaryDatabase.getReference("bookData");
        firebaseAuth = FirebaseAuth.getInstance(app);
        firebaseUser = firebaseAuth.getCurrentUser();
        userUid = firebaseUser.getUid();

        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("uidAndId").child(userUid).child("Id").exists()){
                    userId = dataSnapshot.child("uidAndId").child(userUid).child("Id").getValue().toString();
                    if (dataSnapshot.child(userId).exists() && dataSnapshot.child(userId).child("returnedBooks").exists()
                            && dataSnapshot.child(userId).child("returnedBooks").getChildrenCount()>0) {
                        Log.d("return", "userUid" + userUid);
                        Log.d("return", "userId" + userId);
                        Log.d("return", dataSnapshot.child(userId).child("returnedBooks").getValue().toString());

                        obj_book_details_history.setUserId(userId);
                        obj_book_details_history.setNumberOfBooks((int) dataSnapshot.child(userId)
                                .child("returnedBooks").getChildrenCount());

                        bookDbName.clear();
                        bookDbKey.clear();
                        bookUid.clear();

                        for (DataSnapshot dsp : dataSnapshot.child(obj_book_details_history.getUserId())
                                .child("returnedBooks").getChildren()) {
                            bookDbKey.add(dsp.getKey());
                        }

                        accountHistoryArrayList.clear();
                        String bookUId, bookId, bookName;
                        for (i = 0; i < obj_book_details_history.getNumberOfBooks(); i++) {
                            bookUId = bookDbKey.get(i);
                            bookId = bookUId.substring(0, 3);
                            bookName = dataSnapshot.child(userId).child("returnedBooks").child(bookUId).child("bookName").getValue().toString();
                            bookDbName.add(i,bookName + bookId);
                            obj_book_details_history.setBookDbName(bookName+bookId);
                            obj_book_details_history.setBookUid(bookUId);
                            bookUid.add(i, bookUId);
                            Log.d("bookUid", bookUid.get(i));
                        }

                        databaseReference.addValueEventListener(new ValueEventListener() {
                            //BookDetailsAccountHistory obj_book_details_history = new BookDetailsAccountHistory();
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (i = 0; i < bookDbName.size(); i++) {
                                    Log.d("bookDbName", bookDbName.get(i));
                                    Log.d("bookDbName",dataSnapshot.child("bookData").child(bookDbName.get(i))
                                            .child("authorName").getValue().toString());
                                    obj_book_details_history.setAuthorName(dataSnapshot.child("bookData").child(bookDbName.get(i))
                                            .child("authorName").getValue().toString());
                                    obj_book_details_history.setBookName(dataSnapshot.child("bookData").child(bookDbName.get(i))
                                            .child("bookName").getValue().toString());
                                    obj_book_details_history.setAvailableBooks(String.valueOf(dataSnapshot.child("bookData").child(bookDbName.get(i))
                                            .child("availableBooks").getChildrenCount()));
                                    Log.d("data", "bookUid"+bookUid.get(i));

                                    dataSnapshot = dataSnapshot.child("userData").child(obj_book_details_history.getUserId());
//                                    Log.d("datasnapshot", dataSnapshot.child("returnedBooks").child(bookUid.get(i))
//                                            .child("issuedBy").getValue().toString());
                                    obj_book_details_history.setIssuedBy(dataSnapshot.child("returnedBooks")
                                            .child(bookUid.get(i)).child("issuedBy").getValue().toString());
                                    obj_book_details_history.setIssuedOn(dataSnapshot.child("returnedBooks")
                                            .child(bookUid.get(i)).child("issuedOn").getValue().toString());
                                    obj_book_details_history.setReturnedBy(dataSnapshot.child("returnedBooks")
                                            .child(bookUid.get(i)).child("returnedBy").getValue().toString());
                                    obj_book_details_history.setReturnedOn(dataSnapshot.child("returnedBooks")
                                            .child(bookUid.get(i)).child("returnedOn").getValue().toString());
/*
                                    userDatabaseReference = userDatabaseReference.child(obj_book_details_history.getUserId());
                                    userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Log.d("datasnapshot", dataSnapshot.child("returnedBooks")
                                                    .child("ipl0").child("issuedBy").getValue().toString());
                                            obj_book_details_history.setIssuedBy(dataSnapshot.child("returnedBooks")
                                                    .child(bookUid.get(i)).child("issuedBy").getValue().toString());
                                            obj_book_details_history.setIssuedOn(dataSnapshot.child("returnedBooks")
                                                    .child(bookUid.get(i)).child("issuedOn").getValue().toString());
                                            obj_book_details_history.setReturnedBy(dataSnapshot.child("returnedBooks")
                                                    .child(bookUid.get(i)).child("returnedBy").getValue().toString());
                                            obj_book_details_history.setReturnedBy(dataSnapshot.child("returnedBooks")
                                                    .child(bookUid.get(i)).child("returnedOn").getValue().toString());
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
*/

                                    //dataSnapshot.child("userData").child(bookDbName.get(i)).getValue().toString();
                                    accountHistoryArrayList.add(obj_book_details_history);
                                }
                                accountHistoryAdapter.notifyDataSetChanged();
                                bookDbName.clear();
                                bookDbKey.clear();
                                bookUid.clear();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else{
                        historyOfBooks.setText("No Returned Books");
                        Log.d("getData", "No Returned Books");
                    }
                } else{
                    historyOfBooks.setText("No Returned Books");
                    Log.d("getData", "No ID FOUND");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }

}
