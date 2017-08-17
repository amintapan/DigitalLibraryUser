package com.example.tapan.dllogin.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tapan.dllogin.R;
import com.example.tapan.dllogin.activity.activity.BookInfoActivity;
import com.example.tapan.dllogin.activity.model.BookDbName;
import com.example.tapan.dllogin.activity.model.BookDetails;

import java.util.ArrayList;

/**
 * Created by Tapan on 3/15/2017.
 */

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BookDetails> bookList;
    private Intent intent;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bookNameTextView, authorTextView, availableTextView, issuedTextView;
        ImageView bookImageView;
        String bookDbName=null;
        View bookCard;
        public MyViewHolder(View view) {
            super(view);
            bookNameTextView = (TextView) view.findViewById(R.id.bookNameTextView);
            authorTextView = (TextView) view.findViewById(R.id.bookAuthorNameTextView);
            bookImageView = (ImageView) view.findViewById(R.id.bookImageView);
            availableTextView = (TextView) view.findViewById(R.id.availableBooks);
            issuedTextView = (TextView) view.findViewById(R.id.issuedBooks);
            bookCard = view.findViewById(R.id.bookCard);

            bookCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    abc();
                    int temp = getAdapterPosition();
                    BookDetails objBookDetail = bookList.get(temp);
                    String bookDbName;
                    intent = new Intent(context, BookInfoActivity.class);
                    bookDbName = objBookDetail.getBookDbName().trim();
                    Toast.makeText(context, "You Clicked "+bookDbName+" item",Toast.LENGTH_LONG).show();
                    Log.d("bookDbName1", bookDbName);
                    BookDbName.setBookDbName(bookDbName);
//                    intent.putExtra("bookDbName", bookDbName);
                    v.getContext().startActivity(intent);
                }
            });
        }

        public void abc(){
//            int temp = getAdapterPosition();
//            BookDetails objBookDetail = bookList.get(temp);
//            String bookDbName;
//            intent = new Intent(context, BookInfoActivity.class);
//            bookDbName = objBookDetail.getBookDbName().trim();
//            Toast.makeText(context, "You Clicked "+bookDbName+" item",Toast.LENGTH_LONG).show();
//            Log.d("bookDbName1", bookDbName);
//            intent.putExtra("bookDbName", bookDbName);
//            context.startActivity(intent);
        }

    }

    public BooksAdapter(Context context, ArrayList<BookDetails> list) {
        this.context = context;
        this.bookList = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        BookDetails objBookDetail = bookList.get(position);
        holder.bookNameTextView.setText(objBookDetail.getBookName());
        holder.authorTextView.setText("by "+objBookDetail.getAuthorName());
        holder.availableTextView.setText("Available Books: " +objBookDetail.getAvailableBooks());
        holder.issuedTextView.setText("Issued Books: " +objBookDetail.getIssues());

        Glide.with(context).load("http://eurodroid.com/pics/beginning_android_book.jpg").into(holder.bookImageView);     // bookimage url from firebase

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
