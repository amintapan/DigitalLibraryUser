package com.example.tapan.dllogin.activity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tapan.dllogin.R;
import com.example.tapan.dllogin.activity.model.BookData;
import com.example.tapan.dllogin.activity.model.BookDetails;
import com.example.tapan.dllogin.activity.adapter.BooksAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private String authorName=null, availableBooks=null, bookId=null, bookName=null;
    private String  issues=null, pages=null, price=null, publication=null, quantity=null;
    private int numberOfBooks=0;
    private static BookData bookData = new BookData();
    private ArrayList<String> booksDbName;
    private RecyclerView recyclerView;
    private BooksAdapter adapter;
    private ArrayList<BookDetails> bookList;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.searchRecyclerView);

        bookList = new ArrayList<>();
        adapter = new BooksAdapter(getContext(), bookList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        Log.d("fragment", "Search Fragment");


        FirebaseApp app = FirebaseApp.getInstance(getString(R.string.secondary));
        FirebaseDatabase secondaryDatabase = FirebaseDatabase.getInstance(app);
        DatabaseReference bookDatabaseReference = secondaryDatabase.getReference("bookData");

        booksDbName = new ArrayList<>();

        bookDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("BOOKDATA", dataSnapshot.getValue().toString());
                Log.d("BOOKDATA", String.valueOf(dataSnapshot.getChildrenCount()));
                bookData.setNumberOfBooks((int) dataSnapshot.getChildrenCount());
                numberOfBooks = bookData.getNumberOfBooks();
//                Log.d("BOOKDATA", String.valueOf(numberOfBooks));

                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    booksDbName.add(dsp.getKey());
                }
                bookList.clear();

                for (int i=0; i<dataSnapshot.getChildrenCount(); i++){
                    booksDbName.add(dataSnapshot.getValue().toString());
                    booksDbName.get(i);
                    Log.d("BOOKDATA", String.valueOf(i));
                    Log.d("BOOKDATA", booksDbName.get(i));

//                    BookData.setAuthorName(dataSnapshot.child(booksDbName.get(i)).child("authorName").getValue().toString());
//                    BookData.setAvailableBooks(dataSnapshot.child(booksDbName.get(i)).child("availableBooks").getValue().toString());
//                    BookData.setBookId(dataSnapshot.child(booksDbName.get(i)).child("bookId").getValue().toString());
//                    BookData.setBookName(dataSnapshot.child(booksDbName.get(i)).child("bookName").getValue().toString());
//                    BookData.setIssues(dataSnapshot.child(booksDbName.get(i)).child("issues").getValue().toString());
//                    BookData.setPages(dataSnapshot.child(booksDbName.get(i)).child("pages").getValue().toString());
//                    BookData.setPrice(dataSnapshot.child(booksDbName.get(i)).child("price").getValue().toString());
//                    BookData.setPublication(dataSnapshot.child(booksDbName.get(i)).child("publication").getValue().toString());
//                    BookData.setQuantity(dataSnapshot.child(booksDbName.get(i)).child("quantity").getValue().toString());

                    BookDetails tmpBook = new BookDetails();
                    tmpBook.setBookDbName(booksDbName.get(i));
                    tmpBook.setAuthorName(dataSnapshot.child(booksDbName.get(i)).child("authorName").getValue().toString().trim());
                    tmpBook.setAvailableBooks(dataSnapshot.child(booksDbName.get(i)).child("availableBooks").getValue().toString().trim());
//                    BookData.setBookId(dataSnapshot.child(booksDbName.get(i)).child("bookId").getValue().toString().trim());
                    tmpBook.setBookName(dataSnapshot.child(booksDbName.get(i)).child("bookName").getValue().toString().trim());
                    tmpBook.setIssues(dataSnapshot.child(booksDbName.get(i)).child("issues").getValue().toString().trim());
//                    BookData.setPages(dataSnapshot.child(booksDbName.get(i)).child("pages").getValue().toString().trim());
//                    BookData.setPrice(dataSnapshot.child(booksDbName.get(i)).child("price").getValue().toString().trim());
//                    BookData.setPublication(dataSnapshot.child(booksDbName.get(i)).child("publication").getValue().toString().trim());
//                    BookData.setQuantity(dataSnapshot.child(booksDbName.get(i)).child("quantity").getValue().toString().trim());
//                    Log.d("SEARCH",tmpBook.getBookName().toString().trim());
                    bookList.add(tmpBook);
                    adapter.notifyDataSetChanged();
//                    Log.d("BOOKDATA", BookData.getBookName());
//                    Log.d("BOOKDATA", authorName +" " + availableBooks +" " + bookId +" " + bookName +" " + issues +" " + pages +" " + price +" " + publication +" " + quantity);

                }

//                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
