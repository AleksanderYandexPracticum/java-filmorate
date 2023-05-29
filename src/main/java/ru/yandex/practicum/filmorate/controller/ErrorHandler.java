package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice("ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // если ошибка валидации: ValidationException
    public ErrorResponse handle(final ValidationException e) {
        return new ErrorResponse(e.getMessage());
    }
    //HTTP_INTERNAL_ERROR  500
    //HTTP_NOT_FOUND  404

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)   // для всех ситуаций, если искомый объект не найден
    public ErrorResponse handle(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  //если возникло исключение.
    public ErrorResponse handleThrowable(final Throwable e) {
        return new ErrorResponse(e.getMessage());
    }
}
