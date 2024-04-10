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
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class BookService {

    private BookRepository bookRepository;

    public List<Book> createAll(List<Book> books) {
        return bookRepository.saveAll(books);
    }

    public Book create(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> list() {
        return bookRepository.findAll();
    }

    public Book findOrCreateBook(String title, String editora, String isbn, Date dataPublicacao) {
        return bookRepository.findByTitulo(title)
                .orElseGet(() -> {
                    Book newBook = Book.builder()
                            .titulo(title)
                            .editora(editora)
                            .isbn(isbn)
                            .dataPublicacao(dataPublicacao)
                            .build();
                    return bookRepository.save(newBook);
                });
    }

    public Book updateBookDetails(String title, String newEditora, String newIsbn, Date newDataPublicacao) {
        return bookRepository.findByTitulo(title)
                .map(existingBook -> {
                    existingBook.setEditora(newEditora);
                    existingBook.setIsbn(newIsbn);
                    existingBook.setDataPublicacao(newDataPublicacao);
                    return bookRepository.save(existingBook);
                })
                .orElseThrow(() -> new IllegalArgumentException("Book does not exist"));
    }

    public List<Book> processBooks(List<Book> books) {
        return books.stream()
                .map(book -> bookRepository.findByTitulo(book.getTitulo())
                        .map(existingBook -> {
                            existingBook.setEditora(book.getEditora());
                            existingBook.setIsbn(book.getIsbn());
                            existingBook.setDataPublicacao(book.getDataPublicacao());
                            return bookRepository.save(existingBook);
                        })
                        .orElseGet(() -> {
                            Book newBook = Book.builder()
                                    .titulo(book.getTitulo())
                                    .editora(book.getEditora())
                                    .isbn(book.getIsbn())
                                    .dataPublicacao(book.getDataPublicacao())
                                    .build();
                            return bookRepository.save(newBook);
                        }))
                .collect(Collectors.toList());
    }
}