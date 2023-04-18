package com.sergiu.babin.controller.impl;

import com.sergiu.babin.controller.BookController;
import com.sergiu.babin.dto.BookDTO;
import com.sergiu.babin.dto.Response;
import com.sergiu.babin.service.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class BookControllerImpl implements BookController {

    private final BookService bookService;

    @Override
    public ResponseEntity<Response<List<BookDTO>>> getBooks() {
        return ResponseEntity.ok(bookService.getBooks());
    }

    @Override
    public ResponseEntity<Response<Void>> postBook(@Valid BookDTO bookDTO) {
        bookService.postBook(bookDTO);
        return ResponseEntity.status(CREATED).build();
    }

    @Override
    public ResponseEntity<Response<Void>> deleteBook(@PositiveOrZero Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok().build();
    }
}
