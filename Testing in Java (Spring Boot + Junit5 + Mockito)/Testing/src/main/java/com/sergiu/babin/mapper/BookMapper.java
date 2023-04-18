package com.sergiu.babin.mapper;

import com.sergiu.babin.dto.BookDTO;
import com.sergiu.babin.model.Book;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookMapper {

    BookDTO toDTO(Book book);
    Book toEntity(BookDTO bookDTO);
    List<BookDTO> toDTO(List<Book> book);
}
