package com.sergiu.babin.exception;

import com.sergiu.babin.dto.Response;
import com.sergiu.babin.utils.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static jakarta.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<Object> handleDatabaseException() {
        return ResponseEntity.internalServerError().build();
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response<Object>> handleException(Exception e) {
        log.error(e.getMessage());
        Response<Object> objectResponse = new Response<>(MessageResponse.ERROR.name(), SC_INTERNAL_SERVER_ERROR, null);
        return new ResponseEntity<>(objectResponse, INTERNAL_SERVER_ERROR);
    }
}
