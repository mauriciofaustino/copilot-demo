package com.avenuecode.demo.service;

import com.avenuecode.demo.entity.Book;
import com.avenuecode.demo.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {

    private BookRepository bookRepository;

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> list() {
        return bookRepository.findAll();
    }

    public Book findOrCreateBook(String title, String editora, String isbn, Date dataPublicacao) {
        Optional<Book> optionalBook = bookRepository.findByTitulo(title);

        if (optionalBook.isPresent()) {
            return optionalBook.get();
        }

        Book newBook = Book.builder()
                .titulo(title)
                .editora(editora)
                .isbn(isbn)
                .dataPublicacao(dataPublicacao)
                .build();

        return bookRepository.save(newBook);
    }

    public Book updateBookDetails(String title, String newEditora, String newIsbn, Date newDataPublicacao) {
        Optional<Book> optionalBook = bookRepository.findByTitulo(title);

        if (!optionalBook.isPresent()) {
            throw new IllegalArgumentException("Book does not exist");
        }

        Book existingBook = optionalBook.get();
        existingBook.setEditora(newEditora);
        existingBook.setIsbn(newIsbn);
        existingBook.setDataPublicacao(newDataPublicacao);

        return bookRepository.save(existingBook);
    }

    public List<Book> processBooks(List<Book> books) {
        List<Book> processedBooks = new ArrayList<>();

        for (Book book : books) {
            Optional<Book> optionalBook = bookRepository.findByTitulo(book.getTitulo());

            if (optionalBook.isPresent()) {
                Book existingBook = optionalBook.get();
                existingBook.setEditora(book.getEditora());
                existingBook.setIsbn(book.getIsbn());
                existingBook.setDataPublicacao(book.getDataPublicacao());

                Book updatedBook = bookRepository.save(existingBook);
                processedBooks.add(updatedBook);
            } else {
                Book newBook = Book.builder()
                        .titulo(book.getTitulo())
                        .editora(book.getEditora())
                        .isbn(book.getIsbn())
                        .dataPublicacao(book.getDataPublicacao())
                        .build();

                Book savedBook = bookRepository.save(newBook);
                processedBooks.add(savedBook);
            }
        }

        return processedBooks;
    }
}