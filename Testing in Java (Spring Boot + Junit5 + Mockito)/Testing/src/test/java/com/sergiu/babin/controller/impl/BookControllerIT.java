package com.sergiu.babin.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sergiu.babin.dto.BookDTO;
import com.sergiu.babin.model.Book;
import com.sergiu.babin.repository.BookRepository;
import com.sergiu.babin.service.BookService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIT {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookRepository bookRepository;

    @SpyBean
    BookService bookService;

    @Test
    void getBooks_WhenBooksPresentInDatabase_ThenReturnListOfBooks() throws Exception {
        List<Book> books = new ArrayList<>();
        books.add(new Book(1, "Clean Code", "Uncle Bob", 500));
        books.add(new Book(2, "Rich Dad Poor Dad", "Robert K.", 300));
        books.add(new Book(3, "HOW TO WIN FRIENDS and INFLUENCE PEOPLE", "Daniel Carnegie", 400));
        Mockito.when(bookRepository.findAll()).thenReturn(books);

        mockMvc.perform(MockMvcRequestBuilders.get("/book"))
                .andDo(result -> assertEquals(SC_OK, result.getResponse().getStatus()))
                .andExpect(result -> status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.content().json("""
                                                                            {
                                                                               "message":"SUCCESS",
                                                                               "status":200,
                                                                               "payload":[
                                                                                  {
                                                                                     "id":1,
                                                                                     "title":"Clean Code",
                                                                                     "author":"Uncle Bob",
                                                                                     "pages":500
                                                                                  },
                                                                                  {
                                                                                     "id":2,
                                                                                     "title":"Rich Dad Poor Dad",
                                                                                     "author":"Robert K.",
                                                                                     "pages":300
                                                                                  },
                                                                                  {
                                                                                     "id":3,
                                                                                     "title":"HOW TO WIN FRIENDS and INFLUENCE PEOPLE",
                                                                                     "author":"Daniel Carnegie",
                                                                                     "pages":400
                                                                                  }
                                                                               ]
                                                                            }"""));

        verify(bookService, times(1)).getBooks();
    }

    @Test
    void getBooks_WhenBooksDatabaseException_ThenReturnError() throws Exception {
        Mockito.when(bookRepository.findAll()).thenThrow(RuntimeException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/book"))
                .andDo(result -> assertEquals(SC_INTERNAL_SERVER_ERROR, result.getResponse().getStatus()))
                .andExpect(result -> status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().json("""
                                                                            {
                                                                               "message":"ERROR",
                                                                               "status":500,
                                                                               "payload": null
                                                                            }"""));
        verify(bookService, times(1)).getBooks();
    }

    @Test
    void postBook_WhenInvalidRequestBody_ThenReturnError() throws Exception {
        BookDTO bookDTO = new BookDTO(1, "Clean Code", "Uncle Bob", 500);
        final var requestBody = new ObjectMapper().writer().writeValueAsString(bookDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/book")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(requestBody))
                .andExpect(status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.content().json("""
                                                                            {
                                                                               "type":"about:blank",
                                                                               "title":"Bad Request",
                                                                               "status":400,
                                                                               "detail":"Invalid request content.",
                                                                               "instance":"/book"
                                                                            }"""));
        verify(bookService, times(1)).postBook(bookDTO);
    }

    @Test
    void deleteBook_WhenInvalidPathVariable_ThenReturnError() throws Exception {
        int bookId = -1;
        mockMvc.perform(MockMvcRequestBuilders.delete("/book/" + bookId))
                .andExpect(status().is5xxServerError())
                .andExpect(MockMvcResultMatchers.content().json("""
                                                                            {
                                                                                "message":"ERROR",
                                                                                "status":500,
                                                                                "payload":null
                                                                            }"""));
        verify(bookService, times(1)).deleteBook(bookId);
    }
}