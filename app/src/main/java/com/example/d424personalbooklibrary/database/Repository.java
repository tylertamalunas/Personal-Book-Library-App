package com.example.d424personalbooklibrary.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.d424personalbooklibrary.dao.BookDAO;
import com.example.d424personalbooklibrary.entities.Book;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private BookDAO mBookDAO;
    private LiveData<List<Book>> mAllBooks;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        BookDatabaseBuilder db = BookDatabaseBuilder.getDatabase(application);
        mBookDAO = db.bookDAO();
        mAllBooks = mBookDAO.getAllBooks();
    }

    public LiveData<List<Book>> getmAllBooks() {
        return mAllBooks;
    }

    public void insert(Book book) {
        databaseExecutor.execute(() -> mBookDAO.insert(book));
    }

    public void update(Book book) {
        databaseExecutor.execute(() -> mBookDAO.update(book));
    }

    public void delete(Book book) {
        databaseExecutor.execute(() -> mBookDAO.delete(book));
    }
}
