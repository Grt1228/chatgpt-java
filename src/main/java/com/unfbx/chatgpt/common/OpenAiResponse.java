package com.unfbx.chatgpt.common;

import lombok.Data;

@Data
public class OpenAiResponse<T> {
    private String object;
    private String data;
    private Error error;


    @Data
    public class Error {
        private String message;
        private String type;
        private String param;
        private String code;
    }
}
