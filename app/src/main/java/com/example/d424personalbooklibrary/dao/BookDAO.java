package com.example.d424personalbooklibrary.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d424personalbooklibrary.entities.Book;

import java.util.List;

@Dao
public interface BookDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Book book);

    @Update
    void update(Book book);

    @Delete
    void delete(Book book);

    // get all books
    @Query("SELECT * FROM books ORDER BY title ASC")
    List<Book> getAllBooks();

    //get books by author
    @Query("SELECT * FROM books WHERE author = :author ORDER BY title ASC")
    List<Book> getBooksByAuthor(String author);

    // get books by genre
    @Query("SELECT * FROM books WHERE genre = :genre ORDER BY title ASC")
    List<Book> getBooksByGenre(String genre);
}
