package com.example.d424personalbooklibrary.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.d424personalbooklibrary.dao.BookDAO;
import com.example.d424personalbooklibrary.entities.Book;

@Database(entities = {Book.class}, version = 2, exportSchema = false)
public abstract class BookDatabaseBuilder extends RoomDatabase {
    public abstract BookDAO bookDAO();

    private static volatile BookDatabaseBuilder INSTANCE;

    static BookDatabaseBuilder getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BookDatabaseBuilder.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BookDatabaseBuilder.class, "MyPersonalBookLibrary.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
