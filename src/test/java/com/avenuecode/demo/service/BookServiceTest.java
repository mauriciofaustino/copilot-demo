package com.avenuecode.demo.service;

import com.avenuecode.demo.entity.Book;
import com.avenuecode.demo.repository.BookRepository;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class BookServiceTest {

    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;
    private AutoCloseable closeable;

    @BeforeEach
    public void setup() {
        this.closeable = openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        this.closeable.close();
    }

    @Test
    void createBookSuccessfully() {
        Book book = Book.builder().titulo("Test Book").build();
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.create(book);

        assertEquals("Test Book", result.getTitulo());
    }

    @Test
    void createBookWithNullTitle() {
        Book book = Book.builder().build();
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.create(book);

        assertNull(result.getTitulo());
    }

    @Test
    void createBookWithEmptyTitle() {
        Book book = Book.builder().titulo(Strings.EMPTY).build();
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.create(book);

        assertEquals(Strings.EMPTY, result.getTitulo());
    }

    @Test
    void listBooksWhenNoneExist() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<Book> result = bookService.list();

        assertTrue(result.isEmpty());
    }

    @Test
    void listBooksWhenSomeExist() {
        List<Book> expectedBooks = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(expectedBooks);

        List<Book> result = bookService.list();

        assertEquals(expectedBooks, result);
    }

    @Test
    void findOrCreateBookReturnsExistingBookWhenTitleExists() {
        String title = "Existing Book";
        Book existingBook = new Book();
        when(bookRepository.findByTitulo(title)).thenReturn(Optional.of(existingBook));

        Book result = bookService.findOrCreateBook(title, "Editora", "ISBN", new Date());

        assertEquals(existingBook, result);
    }

    @Test
    void findOrCreateBookCreatesNewBookWhenTitleDoesNotExist() {
        String title = "New Book";
        Book book = Book.builder().titulo(title).editora("Editora").isbn("ISBN").dataPublicacao(new Date()).build();
        when(bookRepository.findByTitulo(title)).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        Book result = bookService.findOrCreateBook(title, "Editora", "ISBN", new Date());

        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateBookDetailsUpdatesExistingBookSuccessfully() {
        String title = "Existing Book";
        Book existingBook = new Book();
        when(bookRepository.findByTitulo(title)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any())).thenReturn(existingBook);

        Book result = bookService.updateBookDetails(title, "New Editora", "New ISBN", new Date());

        assertEquals("New Editora", result.getEditora());
        assertEquals("New ISBN", result.getIsbn());
    }

    @Test
    void updateBookDetailsThrowsExceptionWhenBookDoesNotExist() {
        String title = "Nonexistent Book";
        when(bookRepository.findByTitulo(title)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            bookService.updateBookDetails(title, "New Editora", "New ISBN", new Date());
        });
    }

    @Test
    void processBooksUpdatesExistingBooksAndCreatesNewBooks() {
        Book existingBook = new Book();
        Book newBook = new Book();
        List<Book> books = Arrays.asList(existingBook, newBook);

        when(bookRepository.findByTitulo(existingBook.getTitulo())).thenReturn(Optional.of(existingBook));
        when(bookRepository.findByTitulo(newBook.getTitulo())).thenReturn(Optional.empty());
        when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

        List<Book> result = bookService.processBooks(books);

        assertEquals(2, result.size());
        verify(bookRepository, times(2)).save(any(Book.class));
    }

    @Test
    void processBooksReturnsEmptyListWhenInputIsEmpty() {
        List<Book> books = Collections.emptyList();

        List<Book> result = bookService.processBooks(books);

        assertTrue(result.isEmpty());
        verify(bookRepository, never()).save(any(Book.class));
    }
}