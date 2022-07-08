package de.home.mayumi.practice.common;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

    @Jacksonized
    @Data
    @Builder
    public class ResponseMessage<T> {
        private String message;
        private T data;
    }