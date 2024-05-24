package br.spacer.picpaychallenge.controller;

import br.spacer.picpaychallenge.exception.PicPayException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(PicPayException.class)
    public ProblemDetail handlePicPayException(PicPayException e) {
        return e.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleArgumentException(MethodArgumentNotValidException e) {
        var errors = e.getFieldErrors()
                .stream()
                .map(error -> new InvalidParam(error.getField(), error.getDefaultMessage()))
                .toList();

        var pb = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pb.setDetail("Invalid request");
        pb.setProperty("invalid-params", errors);
        return pb;
    }


    private record InvalidParam(String field, String message) {}
}
