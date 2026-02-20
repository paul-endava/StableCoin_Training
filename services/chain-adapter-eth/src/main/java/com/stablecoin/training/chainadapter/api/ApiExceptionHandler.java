package com.stablecoin.training.chainadapter.api;

import com.stablecoin.training.chainadapter.eth.RpcException;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleBadPathArgument(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.badRequest().body(new ApiErrorResponse(
            "BAD_REQUEST",
            "Invalid path argument: " + ex.getName(),
            Instant.now()
        ));
    }

    @ExceptionHandler(RpcException.class)
    public ResponseEntity<ApiErrorResponse> handleRpcException(RpcException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ApiErrorResponse(
            "ETH_RPC_UNAVAILABLE",
            ex.getMessage(),
            Instant.now()
        ));
    }
}
