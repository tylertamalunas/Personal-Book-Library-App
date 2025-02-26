package com.example.d424personalbooklibrary.UI;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424personalbooklibrary.R;
import com.example.d424personalbooklibrary.entities.Book;


import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {
    private List<Book> bookList;

    public ReportAdapter(List<Book> bookList) {
        this.bookList = bookList;
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView rowNumberTextView, tvTitle, tvAuthor, tvGenre, tvDateAdded;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            rowNumberTextView = itemView.findViewById(R.id.reportRowNumber);
            tvTitle = itemView.findViewById(R.id.reportTitle);
            tvAuthor = itemView.findViewById(R.id.reportAuthor);
            tvGenre = itemView.findViewById(R.id.reportGenre);
            tvDateAdded = itemView.findViewById(R.id.reportDateAdded);
        }
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.report_item_layout, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.rowNumberTextView.setText(String.valueOf(position + 1));
        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvGenre.setText(book.getGenre());
        holder.tvDateAdded.setText(book.getDateAdded());
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
