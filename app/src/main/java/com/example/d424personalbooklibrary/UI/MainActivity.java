package com.example.d424personalbooklibrary.UI;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424personalbooklibrary.R;
import com.example.d424personalbooklibrary.database.Repository;
import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity {
    private Repository repository;
    private BookAdapter bookAdapter;
    private RecyclerView recyclerView;

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

        // set up recyclerview and adapter
        recyclerView = findViewById(R.id.booksRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bookAdapter = new BookAdapter(this);
        recyclerView.setAdapter(bookAdapter);

        // buttons setup
        MaterialButton btnViewReport = findViewById(R.id.btnViewReport);
        MaterialButton btnAddBook = findViewById(R.id.btnAddBook);

        btnViewReport.setOnClickListener(v -> {
            Intent reportIntent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(reportIntent);
        });
        btnAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, BookDetails.class);
            startActivity(intent);
        });

        // set up searchview to filter adapter
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                bookAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bookAdapter.getFilter().filter(newText);
                return false;
            }
        });

        // initialize repo and view changes
        repository = new Repository(getApplication());
        repository.getmAllBooks().observe(this, books -> {
            bookAdapter.setBooks(books);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}