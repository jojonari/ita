package com.cafe24.apps.ita.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;


@Log4j2
@ControllerAdvice
public class ControllerAdviser {

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String bindException(final BindException bindException, final Model model) {
        log.error("BindException application", bindException);

        List<String> errorMsg = bindException
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        model.addAttribute("error_msg", errorMsg);

        return "error";
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String throwable(final Throwable throwable, final Model model) {
        log.error("throwable application", throwable);

        String errorMessage = (throwable != null ? throwable.getMessage() : "Unknown error(throwable)");

        model.addAttribute("error_msg", errorMessage);

        return "error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String exception(final Exception exception, final Model model) {
        log.error("Exception application", exception);

        String errorMessage = (exception != null ? exception.getMessage() : "Unknown error(exception)");

        model.addAttribute("error_msg", errorMessage);

        return "error";
    }
}