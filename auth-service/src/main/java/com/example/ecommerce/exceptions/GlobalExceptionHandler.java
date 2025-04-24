package com.example.ecommerce.exceptions;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.ecommerce.dtos.APIResponse;
import com.example.ecommerce.dtos.ErrorResponse;
import com.example.ecommerce.dtos.MessageResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.access.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidException(MethodArgumentNotValidException e){
        Map<String,String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(err -> {
            String fieldName = ((FieldError) err).getField();
            String message = err.getDefaultMessage();

            errors.put(fieldName,message);
        });

        ErrorResponse errorResponse = new ErrorResponse("There is some problem while validating data", errors);

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorResponse> handleJWTVerificationException(JWTVerificationException e){
        Map<String,String> errors = new HashMap<>();
        errors.put("message",e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse("There is some problem while validating data", errors);

        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e){
        Map<String ,String> errors = new HashMap<>();
        errors.put("message",e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse("There is some problem while validating data", errors);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<APIResponse<?>> handleResourceNotFoundException(ResourceNotFoundException e){
        APIResponse<?> response = APIResponse.error("NOT_FOUND",e.getMessage(),HttpStatus.NOT_FOUND);

        return new ResponseEntity<APIResponse<?>>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceExistedException.class)
    public ResponseEntity<ErrorResponse> handleResourceExistedException(ResourceExistedException e){
        Map<String ,String> errors = new HashMap<>();
        errors.put("message",e.getMessage());

        ErrorResponse errorResponse = new ErrorResponse("There is some problem while validating data", errors);
        return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<APIResponse<?>> handleAccessDeniedException(AccessDeniedException e) {
        APIResponse<?> response = APIResponse.error("FORBIDDEN", "You do not have access to this resource", HttpStatus.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse<?>> handleRuntimeException(RuntimeException e){
        APIResponse<?> response = APIResponse.error("INTERNAL_SERVER_ERROR",e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<APIResponse<?>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<?> handleMissingHeader(MissingRequestHeaderException ex) {
        return ResponseEntity
                .badRequest()
                .body(new MessageResponse("Authorization header is required"));
    }
}
