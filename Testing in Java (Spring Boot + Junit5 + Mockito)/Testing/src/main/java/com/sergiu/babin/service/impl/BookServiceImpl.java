package com.sergiu.babin.service.impl;

import com.sergiu.babin.dto.BookDTO;
import com.sergiu.babin.dto.Response;
import com.sergiu.babin.exception.DatabaseException;
import com.sergiu.babin.exception.NotFoundException;
import com.sergiu.babin.mapper.BookMapper;
import com.sergiu.babin.model.Book;
import com.sergiu.babin.repository.BookRepository;
import com.sergiu.babin.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sergiu.babin.utils.MessageResponse.ERROR;
import static com.sergiu.babin.utils.MessageResponse.SUCCESS;
import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    @Value("${my.property}")
    private String myProperty;
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public Response<List<BookDTO>> getBooks() {
        List<Book> books = bookRepository.findAll();
        List<BookDTO> bookDTOList = bookMapper.toDTO(books);
        return new Response<>(SUCCESS.name(), SC_OK, bookDTOList);
    }

    @Override
    public Response<Void> postBook(BookDTO bookDTO) {
        bookRepository.findById(bookDTO.id())
                .ifPresentOrElse(
                        book -> {
                            throw new DatabaseException("Book is already present");
                        },
                        () -> {
                            Book book = bookMapper.toEntity(bookDTO);
                            bookRepository.save(book);
                        });

        return new Response<>(SUCCESS.name(), SC_OK, null);
    }

    @Override
    public Response<Void> deleteBook(Integer id) {
        if (myProperty == null) {
            return new Response<>(ERROR.name(), SC_INTERNAL_SERVER_ERROR, null);
        }
        bookRepository.findById(id)
                .ifPresentOrElse(
                        bookRepository::delete,
                        () -> {
                            throw new NotFoundException("Book not found ");
                        });

        return new Response<>(SUCCESS.name(), SC_OK, null);
    }
}
