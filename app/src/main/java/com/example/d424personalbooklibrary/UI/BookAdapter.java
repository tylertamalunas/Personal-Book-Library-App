package com.example.d424personalbooklibrary.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424personalbooklibrary.R;
import com.example.d424personalbooklibrary.entities.Book;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> implements Filterable {
    private List<Book> mBooks; // all books in library
    private List<Book> mBooksFilter; // just the list of filtered books
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

    @Override
    public Filter getFilter() {
        return bookFilter;
    }

    private Filter bookFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mBooksFilter);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Book book : mBooksFilter) {
                    if (book.getTitle().toLowerCase().contains(filterPattern) ||
                            book.getAuthor().toLowerCase().contains(filterPattern) ||
                            book.getGenre().toLowerCase().contains(filterPattern) ||
                            book.getISBN().toLowerCase().contains(filterPattern)) {
                        filteredList.add(book);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mBooks.clear();
            mBooks.addAll((List<Book>) results.values);
            notifyDataSetChanged();
        }
    };

    public void setBooks(List<Book> books) {
        mBooks = books;
        mBooksFilter = new ArrayList<>(books);
        notifyDataSetChanged();
    }
}
