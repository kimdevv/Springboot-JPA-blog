package com.kimdev.Blog.handler;

import com.kimdev.Blog.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice // 모든 Exception이 발생하면 이 클래스로 들어옴.
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(value=Exception.class) // 이거 말고 다른 Exception 받고 싶으면 그 이름 적어주면 된다.
    public ResponseDto<String> handleArgumentException(Exception e) { // 여기에도!
        return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
}
