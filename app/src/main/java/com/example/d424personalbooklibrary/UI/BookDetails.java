package com.example.d424personalbooklibrary.UI;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d424personalbooklibrary.R;
import com.example.d424personalbooklibrary.database.Repository;
import com.example.d424personalbooklibrary.entities.Book;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookDetails extends AppCompatActivity {
    String title;
    String author;
    String genre;
    String dateAdded;
    String description;
    String ISBN;
    int bookID;
    Book currentBook;
    EditText editTitle;
    EditText editAuthor;
    EditText editGenre;
    EditText editISBN;
    EditText editDescription;
    TextView editDate;
    Repository repository;
    DatePickerDialog.OnDateSetListener bookDateAdded;
    final Calendar myCalendarDate = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit_book);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        repository = new Repository(getApplication());
        editTitle = findViewById(R.id.editBookTitle);
        editAuthor = findViewById(R.id.editBookAuthor);
        editGenre = findViewById(R.id.editBookGenre);
        editISBN = findViewById(R.id.editBookISBN);
        editDate = findViewById(R.id.purchaseDate);
        editDescription = findViewById(R.id.editBookDescription);

        bookID = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        author = getIntent().getStringExtra("author");
        genre = getIntent().getStringExtra("genre");
        ISBN = getIntent().getStringExtra("ISBN");
        dateAdded = getIntent().getStringExtra("datepurchased");
        description = getIntent().getStringExtra("description");


        editTitle.setText(title);
        editAuthor.setText(author);
        editGenre.setText(genre);
        editISBN.setText(ISBN);
        editDate.setText(dateAdded);
        editDescription.setText(description);


        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        bookDateAdded = (view, year, month, dayOfMonth) -> {
            myCalendarDate.set(Calendar.YEAR, year);
            myCalendarDate.set(Calendar.MONTH, month);
            myCalendarDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };
        editDate.setOnClickListener(v -> {
            String info = editDate.getText().toString();
            if (info.isEmpty()) info = sdf.format(new Date());
            try {
                myCalendarDate.setTime(sdf.parse(info));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            new DatePickerDialog(BookDetails.this, bookDateAdded, myCalendarDate.get(Calendar.YEAR), myCalendarDate.get(Calendar.MONTH), myCalendarDate.get(Calendar.DAY_OF_MONTH)).show();
        });

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editDate.setText(sdf.format(myCalendarDate.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_edit_book, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        // back button action?
        if (item.getItemId() == R.id.booksave) {
            Book book;
            if (bookID == -1) {
                book = new Book(0, editTitle.getText().toString(), editAuthor.getText().toString(), editGenre.getText().toString(), editISBN.getText().toString(), editDate.getText().toString(), editDescription.getText().toString());
                repository.insert(book);
            } else {
                book = new Book(bookID, editTitle.getText().toString(), editAuthor.getText().toString(), editGenre.getText().toString(), editISBN.getText().toString(), editDate.getText().toString(), editDescription.getText().toString());
                repository.update(book);
            }
            finish();
            return true;
        }
        if (item.getItemId() == R.id.bookdelete) {
            repository.getmAllBooks().observe(this, books -> {
                if (books != null) {
                    for (Book book : books) {
                        if (book.getBookID() == bookID) {
                            currentBook = book;
                            break;
                        }
                    }
                    if (currentBook != null) {
                        repository.delete(currentBook);
                        Toast.makeText(BookDetails.this, currentBook.getTitle() + " was deleted.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
        if (item.getItemId() == R.id.bookshare) {
            String bookTitle = editTitle.getText().toString();
            String bookDetails = "Here are the details for a book that I have!\n\n" + "Title: " + editTitle.getText().toString() + "\n" + "Author: " + editAuthor.getText().toString() + "\n" + "Genre: " + editGenre.getText().toString() + "\n" + "ISBN: " + editISBN.getText().toString() + "\n" + "Description: " + editDescription.getText().toString();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, bookDetails);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, bookTitle + " Book Details");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, "Share Book Details");
            startActivity(shareIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
