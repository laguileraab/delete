package com.list.delete.exceptions.handler;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

// import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.list.delete.exceptions.MessageResponseError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import javax.naming.CommunicationException;

@RestControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<MessageResponseError> noSuchElementExceptionsHandler(Exception ex, WebRequest request) {
        MessageResponseError errorDetails = new MessageResponseError
                (HttpStatus.NOT_FOUND, ex.getMessage(), "Error_in_" + request.getDescription(false),ex.getStackTrace().toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
        MethodArgumentTypeMismatchException.class,
        DataIntegrityViolationException.class,
        SQLIntegrityConstraintViolationException.class,
        IllegalArgumentException.class,
        JsonProcessingException.class,
        InvalidFormatException.class
    })
    public ResponseEntity<MessageResponseError> fieldsNotValidsExceptionsHandler(Exception ex, WebRequest request) {
        MessageResponseError errorDetails = new MessageResponseError
                (HttpStatus.BAD_REQUEST,
                        "Los datos de entrada no son válidos",
                        "Error_in_" + request.getDescription(false),ex.getStackTrace().toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }


	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                  HttpHeaders headers, HttpStatus status, WebRequest request) {
           String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        //    List<String> validationList = ex.getBindingResult().getFieldErrors().stream().map(fieldError->fieldError.getDefaultMessage()).collect(Collectors.toList());
           //ApiErrorVO apiErrorVO = new ApiErrorVO(errorMessage);
           //apiErrorVO.setErrorList(validationList);
           MessageResponseError errorDetails = new MessageResponseError
                (HttpStatus.BAD_REQUEST,
                errorMessage,
                        "Error_in_" + request.getDescription(false),ex.getStackTrace().toString());
           return new ResponseEntity<>(errorDetails, status);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                  HttpHeaders headers, HttpStatus status, WebRequest request) {
           String errorMessage = "Los datos de entrada no son válidos";
        //    List<String> validationList = ex.getBindingResult().getFieldErrors().stream().map(fieldError->fieldError.getDefaultMessage()).collect(Collectors.toList());
           //ApiErrorVO apiErrorVO = new ApiErrorVO(errorMessage);
           //apiErrorVO.setErrorList(validationList);
           MessageResponseError errorDetails = new MessageResponseError
                (HttpStatus.BAD_REQUEST,
                errorMessage,
                        "Error_in_" + request.getDescription(false),ex.getStackTrace().toString());
           return new ResponseEntity<>(errorDetails, status);
    }

    @ExceptionHandler({MalformedURLException.class,RemoteException.class})
    public ResponseEntity<MessageResponseError> malformedURLExceptionsHandler(Exception ex, WebRequest request) {
        MessageResponseError errorDetails = new MessageResponseError
                (HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(),
                        "Error_in_" + request.getDescription(false),ex.getStackTrace().toString());
        // ErrorHandler.log.error(HttpStatus.BAD_REQUEST + " " + ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponseError> globalExceptionsHandler(Exception ex, WebRequest request) {
        MessageResponseError errorDetails = new MessageResponseError
                (HttpStatus.INTERNAL_SERVER_ERROR,
                        "Ha ocurrido un error",
                        "Error_in_" + request.getDescription(false),ex.getStackTrace().toString());
        // ErrorHandler.log.error(HttpStatus.INTERNAL_SERVER_ERROR + " " + ex.getMessage()
        //         + "Stacktrace" + ExceptionUtils.getStackTrace(ex));
        System.out.println(ex.getStackTrace());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}