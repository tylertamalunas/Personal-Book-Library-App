package com.example.d424personalbooklibrary.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424personalbooklibrary.R;
import com.example.d424personalbooklibrary.entities.Book;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private List<Book> mBooks;
    private final Context context;
    private final LayoutInflater mInflator;
    public BookAdapter(Context context) {
        mInflator = LayoutInflater.from(context);
        this.context = context;
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
        private final TextView bookTitle;
        private final TextView bookAuthor;
        private final TextView bookGenre;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.bookTitleTextView);
            bookAuthor = itemView.findViewById(R.id.bookAuthorTextView);
            bookGenre = itemView.findViewById(R.id.bookGenreTextView);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                final Book current = mBooks.get(position);
                Intent intent = new Intent(context, BookDetails.class);
                intent.putExtra("id", current.getBookID());
                intent.putExtra("title", current.getTitle());
                intent.putExtra("author", current.getAuthor());
                intent.putExtra("genre", current.getGenre());
                intent.putExtra("ISBN", current.getISBN());
                intent.putExtra("datepurchased", current.getDateAdded());
                intent.putExtra("description", current.getDescription());
                context.startActivity(intent);
            });
        }
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflator.inflate(R.layout.activity_book_item_layout, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.BookViewHolder holder, int position) {
        if (mBooks != null) {
            Book current = mBooks.get(position);
            String title = current.getTitle();
            String author = current.getAuthor();
            String genre = current.getGenre();
            holder.bookTitle.setText(title);
            holder.bookAuthor.setText(author);
            holder.bookGenre.setText(genre);
        } else {
            holder.bookTitle.setText("No Name Yet");
        }
    }

    @Override
    public int getItemCount() {
        if (mBooks != null) {
            return mBooks.size();
        } else return 0;
    }

    public void setBooks(List<Book> books) {
        mBooks = books;
        notifyDataSetChanged();
    }
}
