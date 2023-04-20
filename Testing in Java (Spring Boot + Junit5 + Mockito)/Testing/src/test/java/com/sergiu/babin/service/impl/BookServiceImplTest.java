package com.sergiu.babin.service.impl;

import com.sergiu.babin.dto.BookDTO;
import com.sergiu.babin.dto.Response;
import com.sergiu.babin.mapper.BookMapperImpl;
import com.sergiu.babin.model.Book;
import com.sergiu.babin.repository.BookRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.sergiu.babin.utils.MessageResponse.SUCCESS;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    private BookRepository bookRepository;
    private BookServiceImpl bookService;

    // Injection with annotations and without
    @BeforeEach
    void init() {
        BookMapperImpl bookMapper = Mockito.spy(BookMapperImpl.class);
        bookRepository = Mockito.mock(BookRepository.class);
        bookService = new BookServiceImpl(bookRepository, bookMapper);
        ReflectionTestUtils.setField(bookService, "myProperty", "my-property");
    }

    @Test
    @DisplayName("Get all books when there are no books in the database")
    void getBooks_WhenBooksNotPresent_ThenReturnEmptyPayloadSuccess() {
        Mockito.when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        final var response = bookService.getBooks();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(SC_OK, response.status());
        Assertions.assertEquals(SUCCESS.name(), response.message());
        Assertions.assertEquals(Collections.emptyList(), response.payload());
    }

    // Specify Assertions and Mockito Packages
    @RepeatedTest(3)
    @DisplayName("Get all books when there are one book in the database")
    void getBooks_WhenBooksPresent_ThenReturnBooksSuccess() {
        Book book = new Book(1, "Clean Code" ,"Uncle Bob" , 500);

        Mockito.when(bookRepository.findAll())
                .thenReturn(Collections.singletonList(book));

        List<BookDTO> expected = Collections.singletonList(new BookDTO(1, "Clean Code", "Uncle Bob", 500));
        final var response = bookService.getBooks();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(SC_OK, response.status());
        Assertions.assertEquals(SUCCESS.name(), response.message());
        Assertions.assertEquals(expected, response.payload());
    }

    @Test
    void postBook_WhenBookIsPresent_ThenThrowRuntimeException() {
        BookDTO bookDTO = new BookDTO(1, "Clean Code", "Uncle Bob", 500);
        Book book = new Book(1, "Clean Code", "Uncle Bob", 500);

        Mockito.when(bookRepository.findById(bookDTO.id())).thenReturn(Optional.of(book));

        Assertions.assertThrows(RuntimeException.class, () -> bookService.postBook(bookDTO));
    }

    @Test
//    @Timeout(1)
    void postBook_WhenBookIsNotPresent_ThenReturnSuccess() {
        BookDTO bookDTO = new BookDTO(null, "Clean Code", "Uncle Bob", 500);
        Mockito.when(bookRepository.findById(any())).thenReturn(Optional.empty());

//        Thread.sleep(20000);

        final var response = bookService.postBook(bookDTO);

        Mockito.verify(bookRepository, times(1)).save(any(Book.class));
        Assertions.assertNotNull(response);
        Assertions.assertEquals(SC_OK, response.status());
        Assertions.assertEquals(SUCCESS.name(), response.message());
        Assertions.assertNull(response.payload());
    }

//    @Test
//    @Disabled
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void deleteBook_WhenBookIsPresent_ThenReturnSuccess(Integer bookId) {
        Book book = new Book(bookId, "Clean Code", "Uncle Bob", 500);

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        Response<Void> response = bookService.deleteBook(bookId);
        
        Mockito.verify(bookRepository, Mockito.times(1)).delete(any(Book.class));

        Assertions.assertNotNull(response);
        Assertions.assertEquals(SC_OK, response.status());
        Assertions.assertEquals(SUCCESS.name(), response.message());
        Assertions.assertNull(response.payload());
    }

    @Test
    @Tag("delete")
    void deleteBook_WhenBookIsNotPresent_ThenThrowRuntimeException() {
        Integer bookId = 1;

        Mockito.when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        Assertions.assertThrows(RuntimeException.class, () -> bookService.deleteBook(bookId));
    }

    static Stream<Arguments> getArguments() {

        return Stream.of(
                Arguments.of(new Book(1, "Clean Code", "Uncle Bob", 500)),
                Arguments.of(new Book(2, "Clean Code", "Uncle Bob", 500)),
                Arguments.of(new Book(3, "Clean Code", "Uncle Bob", 500))
        );
    }
}