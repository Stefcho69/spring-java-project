package com.mainapp;

import com.mainapp.exceptions.NotEnoughMoneyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(NotEnoughMoneyException.class)
    public String handleNotEnoughMoneyError(){
        return "not-enough-money";
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public String handleNotAuthorizedError(){
        return "no-authorization";
    }
}
