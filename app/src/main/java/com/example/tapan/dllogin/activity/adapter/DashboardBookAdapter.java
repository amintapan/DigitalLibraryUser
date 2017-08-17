package com.example.tapan.dllogin.activity.adapter;

import android.content.Context;
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
import com.example.tapan.dllogin.activity.model.BookDetails;
import com.example.tapan.dllogin.activity.model.BookDetailsDashboard;

import java.util.ArrayList;

/**
 * Created by Tapan on 3/16/2017.
 */

public class DashboardBookAdapter extends RecyclerView.Adapter<DashboardBookAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BookDetailsDashboard> bookList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bookNameTextView, authorTextView;
        ImageView bookImageView;
        View bookCard;
        public MyViewHolder(View view) {
            super(view);
            bookNameTextView = (TextView) view.findViewById(R.id.bookNameTextViewDB);
            authorTextView = (TextView) view.findViewById(R.id.bookAuthorNameTextViewDB);
            bookImageView = (ImageView) view.findViewById(R.id.bookImageViewDB);
            bookCard = view.findViewById(R.id.bookDashboardCard);

            bookCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "You Clicked "+getAdapterPosition()+" item",Toast.LENGTH_LONG).show();

                    // Intent putExtra
                }
            });
        }

    }

    public DashboardBookAdapter (Context context, ArrayList<BookDetailsDashboard> list) {
        this.context = context;
        this.bookList = list;
    }

    @Override
    public DashboardBookAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_dashboard_card, parent, false);

        return new DashboardBookAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DashboardBookAdapter.MyViewHolder holder, final int position) {
        BookDetailsDashboard bookDetailsDashboard = bookList.get(position);
        holder.bookNameTextView.setText(bookDetailsDashboard.getBookName());
        holder.authorTextView.setText(bookDetailsDashboard.getAuthorName());
        Log.d("bookDbName", "AUTHOR NAME from dash" +bookDetailsDashboard.getAuthorName());
        Glide.with(context).load("http://eurodroid.com/pics/beginning_android_book.jpg").into(holder.bookImageView);     // bookimage url from firebase

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
