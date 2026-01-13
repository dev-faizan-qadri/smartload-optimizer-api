package com.smartload_optimizer.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> badRequest(Exception exception){
    return ResponseEntity.badRequest()
            .body(new ApiError("INVALID_REQUEST", exception.getMessage()));
  }
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> serverError(Exception exception){
    return ResponseEntity.internalServerError()
            .body(new ApiError("INTERNAL_ERROR", exception.getMessage()));
  }
}
