package com.ttrip.api.exception;


import com.ttrip.api.dto.DataResDto;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public DataResDto<?> handle(BadRequestException e){
        return DataResDto.builder().status(400).message(e.getMessage()).build();
    }
    @ExceptionHandler(UnauthorizationException.class)
    public DataResDto<?> handle(UnauthorizationException e){
        return DataResDto.builder().status(401).message(e.getMessage()).build();
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public DataResDto<?> handle(IllegalArgumentException e){
        return DataResDto.builder().status(400).message(e.getMessage()).build();
    }
    @ExceptionHandler(NotFoundException.class)
    public DataResDto<?> handle(NotFoundException e){
        return DataResDto.builder().status(404).message(e.getMessage()).build();
    }
    @ExceptionHandler(IOException.class)
    public DataResDto<?> handle(IOException e){
        return DataResDto.builder().status(500).message(e.getMessage()).build();
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public DataResDto<?> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        return DataResDto.builder().status(400).message(exception.getMessage()).build();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public DataResDto<?> handleMethodArgumentTypeNotMismatch(MethodArgumentTypeMismatchException exception) {
        return DataResDto.builder().status(400).message(exception.getMessage()).build();
    }

    @ExceptionHandler(UnsupportedAudioFileException.class)
    public DataResDto<?> handleUnSupportedAudioFileException(UnsupportedAudioFileException exception) {
        return DataResDto.builder().status(400).message(exception.getMessage()).build();
    }
}
