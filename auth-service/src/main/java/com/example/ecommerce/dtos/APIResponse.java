package com.example.ecommerce.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Schema(description = "Response structure of API")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class APIResponse<T> {

    @Schema(description = "Indicates whether the request is successful or not",example = "true")
    private boolean success;

    @Schema(description = "Response message", example = "SUCCESS")
    private String message;

    @Schema(description = "Response data", implementation = Object.class)
    @JsonProperty("data")
    private T data;

    @Schema(description = "Http status", example = "200")
    private HttpStatus status;

    @Schema(description = "Response error")
    private ErrorResponse error;

    @Schema(description = "Response time")
    private LocalDateTime timestamp;

    @Schema(description = "Error details of the response")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    public static class ErrorResponse {
        @Schema(description = "Error message", example = "Invalid request")
        private String message;

        @Schema(description = "Error code", example = "400")
        private String code;

        @Schema(description = "Error details", example = "Field 'email' is required")
        private String details;

        public ErrorResponse(String message){
            this.message = message;
        }

        public ErrorResponse(String message, String code) {
            this.message = message;
            this.code = code;
        }

        public ErrorResponse(String message, String code,String details) {
            this.message = message;
            this.code = code;
            this.details=details;
        }
    }

    private APIResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T>{
        private final APIResponse<T> response;

        private Builder() {
            response = new APIResponse<>();
        }

        public Builder<T> success(boolean success) {
            response.success = success;
            return this;
        }

        public Builder<T> message(String message) {
            response.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            response.data = data;
            return this;
        }

        public Builder<T> status(HttpStatus status) {
            response.status = status;
            return this;
        }

        public Builder<T> error(ErrorResponse error) {
            response.error = error;
            return this;
        }

        public APIResponse<T> build() {
            return response;
        }
    }

    public static <T> APIResponse<T> ok(T data, String message){
        return APIResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .status(HttpStatus.OK)
                .build();
    }

    public static <T> APIResponse<T> message(String message, HttpStatus status){
        return APIResponse.<T>builder()
                .success(true)
                .message(message)
                .status(status)
                .build();
    }

    public static <T> APIResponse<T> error(String code, String message, HttpStatus status){
        return APIResponse.<T>builder()
                .success(false)
                .status(status)
                .error(new ErrorResponse(message,code))
                .build();
    }
}
