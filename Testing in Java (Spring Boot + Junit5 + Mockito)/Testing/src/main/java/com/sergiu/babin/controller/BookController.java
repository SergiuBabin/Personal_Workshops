package com.sergiu.babin.controller;

import com.sergiu.babin.dto.BookDTO;
import com.sergiu.babin.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/book")
public interface BookController {

    @GetMapping()
    ResponseEntity<Response<List<BookDTO>>> getBooks();

    @PostMapping()
    ResponseEntity<Response<Void>> postBook(@RequestBody BookDTO bookDTO);

    @DeleteMapping("/{id}")
    ResponseEntity<Response<Void>> deleteBook(@PathVariable Integer id);
}
