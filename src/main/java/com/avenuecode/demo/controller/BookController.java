package com.avenuecode.demo.controller;

import com.avenuecode.demo.entity.Book;
import com.avenuecode.demo.service.BookService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {

    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getBooks() {
        log.info("Books list endpoint was called");
        return ResponseEntity.ok(bookService.list());
    }

    @PostMapping("/create")
    public ResponseEntity<Book> create(@RequestBody Book book) {
        log.info("Books create endpoint was called");
        return ResponseEntity.ok(bookService.create(book));
    }

    @PostMapping("/createAll")
    public ResponseEntity<List<Book>> createAll(@RequestBody List<Book> books) {
        log.info("Books createAll endpoint was called");
        return ResponseEntity.ok(bookService.createAll(books));
    }

}
