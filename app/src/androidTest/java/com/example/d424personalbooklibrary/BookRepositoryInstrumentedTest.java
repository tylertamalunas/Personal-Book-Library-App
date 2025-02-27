package com.example.d424personalbooklibrary;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.d424personalbooklibrary.dao.BookDAO;
import com.example.d424personalbooklibrary.database.BookDatabaseBuilder;
import com.example.d424personalbooklibrary.entities.Book;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;


import static org.junit.Assert.*;

/**
 * Unit test for BookDAO.
 */
@RunWith(AndroidJUnit4.class)
public class BookRepositoryInstrumentedTest {

    private BookDAO bookDAO;
    private BookDatabaseBuilder db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        // Using an in-memory database because the information stored here disappears when the process is killed.
        db = Room.inMemoryDatabaseBuilder(context, BookDatabaseBuilder.class)
                // Allowing main thread queries
                .allowMainThreadQueries()
                .build();
        bookDAO = db.bookDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void insertAndGetBook() throws Exception {
        String uniqueTitle = "Test Book Title " + System.currentTimeMillis();
        Book book = new Book(0, uniqueTitle, "Test Author", "Fiction", "1234567890", "07/26/2024", "Test description");
        bookDAO.insert(book);

        List<Book> allBooks = LiveDataTestUtil.getValue(bookDAO.getAllBooks()); // Helper to get LiveData value synchronously
        assertNotNull("Books list is null", allBooks);
        assertFalse("Books list is empty", allBooks.isEmpty());

        Book insertedBook = allBooks.get(0);
        assertEquals("Title does not match", uniqueTitle, insertedBook.getTitle());
        assertEquals("Author does not match", "Test Author", insertedBook.getAuthor());
        assertEquals("Genre does not match", "Fiction", insertedBook.getGenre());
        assertEquals("ISBN does not match", "1234567890", insertedBook.getISBN());
        assertEquals("DateAdded does not match", "07/26/2024", insertedBook.getDateAdded());
        assertEquals("Description does not match", "Test description", insertedBook.getDescription());
    }

    @Test
    public void updateBook() throws Exception {
        // Insert a book first
        Book book = new Book(0, "Original Title", "Original Author", "Fiction", "1234567890", "07/26/2024", "Original description");
        bookDAO.insert(book);

        // Retrieve inserted book
        List<Book> allBooks = LiveDataTestUtil.getValue(bookDAO.getAllBooks());
        assertFalse("Books list is empty", allBooks.isEmpty());

        Book bookToUpdate = allBooks.get(0);
        int bookId = bookToUpdate.getBookID();

        // Modify book details
        bookToUpdate.setTitle("Updated Title");
        bookToUpdate.setAuthor("Updated Author");
        bookDAO.update(bookToUpdate);

        // Retrieve updated book and verify changes
        List<Book> updatedBooks = LiveDataTestUtil.getValue(bookDAO.getAllBooks());
        Book updatedBook = updatedBooks.stream().filter(b -> b.getBookID() == bookId).findFirst().orElse(null);

        assertNotNull("Updated book not found", updatedBook);
        assertEquals("Title did not update", "Updated Title", updatedBook.getTitle());
        assertEquals("Author did not update", "Updated Author", updatedBook.getAuthor());
    }

    @Test
    public void deleteBook() throws Exception {
        // Insert a book first
        Book book = new Book(0, "Delete Me", "Test Author", "Fiction", "1234567890", "07/26/2024", "Test description");
        bookDAO.insert(book);

        // Retrieve inserted book
        List<Book> allBooks = LiveDataTestUtil.getValue(bookDAO.getAllBooks());
        assertFalse("Books list is empty", allBooks.isEmpty());

        Book bookToDelete = allBooks.get(0);
        int bookId = bookToDelete.getBookID();

        // Delete book
        bookDAO.delete(bookToDelete);

        // Verify that book was deleted
        List<Book> booksAfterDeletion = LiveDataTestUtil.getValue(bookDAO.getAllBooks());
        assertFalse("Deleted book still exists", booksAfterDeletion.stream().anyMatch(b -> b.getBookID() == bookId));
    }

    @Test
    public void searchBookByTitle() throws Exception {
        // Insert multiple books
        bookDAO.insert(new Book(0, "Harry Potter", "J.K. Rowling", "Fantasy", "1111111111", "07/26/2024", "Magical book"));
        bookDAO.insert(new Book(0, "The Hobbit", "J.R.R. Tolkien", "Fantasy", "2222222222", "07/26/2024", "Adventure book"));

        // Retrieve search results
        List<Book> searchResults = LiveDataTestUtil.getValue(bookDAO.searchBooks("%Harry Potter%"));

        // Verify search returned expected results
        assertNotNull("Search result is null", searchResults);
        assertFalse("Search returned no results", searchResults.isEmpty());
        assertEquals("Search result does not match", "Harry Potter", searchResults.get(0).getTitle());
    }

    @Test
    public void generateReport() throws Exception {
        // Insert test books
        bookDAO.insert(new Book(0, "Book One", "Author A", "Sci-Fi", "3333333333", "07/26/2024", "Test book 1"));
        bookDAO.insert(new Book(0, "Book Two", "Author B", "Drama", "4444444444", "07/26/2024", "Test book 2"));

        // Get all books (simulating report retrieval)
        List<Book> reportBooks = LiveDataTestUtil.getValue(bookDAO.getAllBooks());

        // Verify that at least two books exist in the report
        assertNotNull("Report generation failed", reportBooks);
        assertTrue("Report has no data", reportBooks.size() >= 2);
    }


    public static class LiveDataTestUtil {
        /**
         * Get the value from a LiveData object. We're waiting for LiveData to emit, for at most 2 seconds.
         * Once we get a notification via onChanged, we stop observing.
         */
        public static <T> T getValue(final LiveData<T> liveData) throws InterruptedException, TimeoutException {
            final Object[] data = new Object[1];
            final CountDownLatch latch = new CountDownLatch(1);
            Observer<T> observer = new Observer<T>() {
                @Override
                public void onChanged(@Nullable T o) {
                    data[0] = o;
                    latch.countDown();
                    liveData.removeObserver(this);
                }
            };

            // Run observeForever on the main thread using InstrumentationRegistry
            InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
                @Override
                public void run() {
                    liveData.observeForever(observer);
                }
            });

            // Don't wait indefinitely if the LiveData is not set.
            if (!latch.await(2, TimeUnit.SECONDS)) {
                // We got a timeout before a value was emitted.
                liveData.removeObserver(observer);
                throw new TimeoutException("LiveData value was never set.");
            }
            //noinspection unchecked
            return (T) data[0];
        }
    }
}