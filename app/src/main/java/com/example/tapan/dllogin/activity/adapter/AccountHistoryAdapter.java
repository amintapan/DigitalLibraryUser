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
import com.example.tapan.dllogin.activity.activity.BookAccountHistory;
import com.example.tapan.dllogin.activity.activity.BookInfoActivity;
import com.example.tapan.dllogin.activity.model.BookDbName;
import com.example.tapan.dllogin.activity.model.BookDetails;
import com.example.tapan.dllogin.activity.model.BookDetailsAccountHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Tapan on 3/19/2017.
 */

public class AccountHistoryAdapter extends RecyclerView.Adapter<AccountHistoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BookDetailsAccountHistory> bookList ;
    private Intent intent;
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView bookNameTextView, authorTextView, issuedByTextView, issuedOnTextView, returnedByTextView, returnedOnTextView, availableTextView;
        ImageView bookImageView;
        View bookCard;
        public MyViewHolder(View view) {
            super(view);

            bookNameTextView = (TextView) view.findViewById(R.id.bookNameTextViewAH);
            authorTextView = (TextView) view.findViewById(R.id.bookAuthorNameTextViewAH);
            bookImageView = (ImageView) view.findViewById(R.id.bookImageViewAH);
            bookCard = view.findViewById(R.id.bookAccountHistoryCard);
            issuedByTextView = (TextView) view.findViewById(R.id.issuedByTextViewAH);
            issuedOnTextView = (TextView) view.findViewById(R.id.issuedOnTextViewAH);
            returnedByTextView = (TextView) view.findViewById(R.id.returnedByTextViewAH);
            returnedOnTextView = (TextView) view.findViewById(R.id.returnedOnTextViewAH);
            availableTextView = (TextView) view.findViewById(R.id.availableBooksTextViewAH);

            bookCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(context, "You clicked " +getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    int temp = getAdapterPosition();
                    BookDetailsAccountHistory objBookDetail = bookList.get(temp);
                    String bookDbName, bookUId;
                    intent = new Intent(context, BookAccountHistory.class);
                    bookDbName = objBookDetail.getBookDbName().trim();
                    bookUId = objBookDetail.getBookUid().trim();
                    Toast.makeText(context, "You Clicked "+bookDbName+" item",Toast.LENGTH_LONG).show();
                    Log.d("bookDbName1", bookDbName);
                    BookDbName.setBookDbName(bookDbName);
                    BookDbName.setBookUid(bookUId);
//                    intent.putExtra("bookDbName", bookDbName);
                    v.getContext().startActivity(intent);
                    //Intent putExtra
                }
            });
        }
    }

    public AccountHistoryAdapter(Context context, ArrayList<BookDetailsAccountHistory>  list) {
        this.context = context;
        this.bookList = list;
    }

    @Override
    public AccountHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_account_history_card, parent, false);
        return new AccountHistoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AccountHistoryAdapter.MyViewHolder holder, int position) {
        BookDetailsAccountHistory bookDetailsAccountHistory = bookList.get(position);

        /*SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        String dateAsString = sdf.format(bookDetailsAccountHistory.getIssuedOn());

        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(bookDetailsAccountHistory.getIssuedOn()));
        Log.d("calendar", String.valueOf(calendar.getTimeInMillis()));*/

        holder.bookNameTextView.setText(bookDetailsAccountHistory.getBookName());
        holder.authorTextView.setText(bookDetailsAccountHistory.getAuthorName());
        Log.d("bookDbName", "AUTHOR NAME" +bookDetailsAccountHistory.getAuthorName());
        holder.availableTextView.setText(bookDetailsAccountHistory.getAvailableBooks());
        holder.issuedOnTextView.setText("Issued On: "+bookDetailsAccountHistory.getIssuedOn());
        holder.issuedByTextView.setText("Issued By: "+bookDetailsAccountHistory.getIssuedBy());
        holder.returnedOnTextView.setText("Returned On: "+bookDetailsAccountHistory.getReturnedOn());
        holder.returnedByTextView.setText("Returned To: "+bookDetailsAccountHistory.getReturnedBy());
        Glide.with(context).load("http://eurodroid.com/pics/beginning_android_book.jpg").into(holder.bookImageView);
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
