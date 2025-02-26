package com.example.d424personalbooklibrary.UI;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d424personalbooklibrary.R;
import com.example.d424personalbooklibrary.database.Repository;
import com.example.d424personalbooklibrary.entities.Book;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {
    private TextView reportHeaderTextView, txtTotalCount;
    private Spinner spinnerReportType;
    private EditText editTextFilter;
    private Button btnGenerateReport;
    private RecyclerView reportRecyclerView;
    private ReportAdapter reportAdapter;
    private Repository repository;
    private List<Book> allBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // set up toolbar
        Toolbar toolbar = findViewById(R.id.reportToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // get references to UI parts
        reportHeaderTextView = findViewById(R.id.reportHeader);
        txtTotalCount = findViewById(R.id.txtTotalCount);
        spinnerReportType = findViewById(R.id.spinnerReportType);
        editTextFilter = findViewById(R.id.editTextReportFilter);
        btnGenerateReport = findViewById(R.id.btnGenerateReport);
        reportRecyclerView = findViewById(R.id.recyclerViewReport);
        reportRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // load all books
        repository = new Repository(getApplication());
        repository.getmAllBooks().observe(this, books -> {
            allBooks = books;
        });

        // setup spinner
        String[] reportOptions = {"All Books", "By Author", "By Genre"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, reportOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReportType.setAdapter(spinnerAdapter);

        // enable or disable filter EditText based on selected option
        spinnerReportType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                if (selected.equals("All Books")) {
                    editTextFilter.setText("");
                    editTextFilter.setEnabled(false);
                } else editTextFilter.setEnabled(true);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        // setup button click listener for generate report
        btnGenerateReport.setOnClickListener(v -> generateReport());
    }

    private void generateReport() {
        if (allBooks == null || allBooks.isEmpty()) {
            Toast.makeText(this, "No books available to generate report", Toast.LENGTH_SHORT).show();
            return;
        }
        String selected = (String) spinnerReportType.getSelectedItem();
        List<Book> filteredBooks = new ArrayList<>();
        if (selected.equals("All Books")) {
            filteredBooks = allBooks;
        } else if (selected.equals("By Author")) {
            String filter = editTextFilter.getText().toString().toLowerCase().trim();
            for (Book book : allBooks) {
                if (book.getAuthor().toLowerCase().contains(filter)) {
                    filteredBooks.add(book);
                }
            }
        } else if (selected.equals("By Genre")) {
            String filter = editTextFilter.getText().toString().toLowerCase().trim();
            for (Book book : allBooks) {
                if (book.getGenre().toLowerCase().contains(filter)) {
                    filteredBooks.add(book);
                }
            }
        }

        // set report header with title and current date
        String reportTitle = "Library Report (" + selected + ")";
        String currentDateTime = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
        reportHeaderTextView.setText(reportTitle + "\nGenerated on: " + currentDateTime);

        // update total count
        txtTotalCount.setText("Total Books: " + filteredBooks.size());

        // set up adapter for report
        reportAdapter = new ReportAdapter(filteredBooks);
        reportRecyclerView.setAdapter(reportAdapter);
    }

    // enable back arrow
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
