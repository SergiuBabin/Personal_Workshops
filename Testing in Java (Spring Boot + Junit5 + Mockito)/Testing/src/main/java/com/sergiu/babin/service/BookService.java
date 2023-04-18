package com.sergiu.babin.service;

import com.sergiu.babin.dto.BookDTO;
import com.sergiu.babin.dto.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BookService {
    Response<List<BookDTO>> getBooks();
    Response<Void> postBook(@RequestBody BookDTO bookDTO);
    Response<Void> deleteBook(@PathVariable Integer id);
}
