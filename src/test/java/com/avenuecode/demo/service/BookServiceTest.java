package com.avenuecode.demo.service;

import com.avenuecode.demo.entities.Book;
import com.avenuecode.demo.repository.BookRepository;
import com.avenuecode.demo.service.BookService;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
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
}