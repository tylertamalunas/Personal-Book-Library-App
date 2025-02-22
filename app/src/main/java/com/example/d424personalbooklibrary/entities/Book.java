package com.example.d424personalbooklibrary.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int bookID;
    private String title;
    private String author;
    private String genre;
    private String ISBN; // optional
    private String dateAdded;
    private String description; // optional

    public Book(int bookID, String title, String author, String genre, String ISBN, String dateAdded, String description) {
        this.bookID = bookID;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.ISBN = ISBN;
        this.dateAdded = dateAdded;
        this.description = description;
    }

    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
