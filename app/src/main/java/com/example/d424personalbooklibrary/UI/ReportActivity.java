package com.example.d424personalbooklibrary.UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424personalbooklibrary.R;
import com.example.d424personalbooklibrary.database.Repository;
import com.example.d424personalbooklibrary.entities.Book;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {
    private TextView reportHeaderTextView;
    private RecyclerView reportRecyclerView;
    private ReportAdapter reportAdapter;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        reportHeaderTextView = findViewById(R.id.reportHeader);
        reportRecyclerView = findViewById(R.id.recyclerViewReport);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        repository = new Repository(getApplication());
        List<Book> allBooks = repository.getmAllBooks();

        // set report header with title and current date
        String reportTitle = "Library Report";
        String currentDateTime = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        reportHeaderTextView.setText(reportTitle + "\nGenerated on: " + currentDateTime);

        // set up adapter for report
        reportAdapter = new ReportAdapter(allBooks);
        reportRecyclerView.setAdapter(reportAdapter);
    }
}
