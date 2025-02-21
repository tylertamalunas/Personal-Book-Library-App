package com.example.d424personalbooklibrary.database;

import android.app.Application;

import com.example.d424personalbooklibrary.dao.BookDAO;
import com.example.d424personalbooklibrary.entities.Book;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private BookDAO mBookDAO;
    private List<Book> mAllBooks;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        BookDatabaseBuilder db = BookDatabaseBuilder.getDatabase(application);
        mBookDAO = db.bookDAO();
    }

    public List<Book> getmAllBooks() {
        databaseExecutor.execute(() -> {
            mAllBooks = mBookDAO.getAllBooks();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllBooks;
    }
    public void insert(Book book) {
        databaseExecutor.execute(() -> {
            mBookDAO.insert(book);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public void update(Book book) {
        databaseExecutor.execute(() -> {
            mBookDAO.update(book);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(Book book) {
        databaseExecutor.execute(() -> {
            mBookDAO.delete(book);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
