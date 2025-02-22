package com.example.d424personalbooklibrary.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424personalbooklibrary.R;
import com.example.d424personalbooklibrary.database.Repository;
import com.example.d424personalbooklibrary.entities.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fab = findViewById(R.id.fabAddBook);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, BookDetails.class);
            startActivity(intent);
        });
        RecyclerView recyclerView = findViewById(R.id.booksRecyclerView);
        repository = new Repository(getApplication());
        List<Book> allBooks = repository.getmAllBooks();
        final BookAdapter bookAdapter = new BookAdapter(this);
        recyclerView.setAdapter(bookAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter.setBooks(allBooks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Book> allBooks = repository.getmAllBooks();
        RecyclerView recyclerView = findViewById(R.id.booksRecyclerView);
        final BookAdapter bookAdapter = new BookAdapter(this);
        recyclerView.setAdapter(bookAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter.setBooks(allBooks);
    }
}