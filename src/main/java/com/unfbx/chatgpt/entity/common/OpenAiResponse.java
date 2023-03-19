package com.unfbx.chatgpt.entity.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Data
public class OpenAiResponse<T> implements Serializable {
    private String object;
    private List<T> data;
    private Error error;


    @Data
    public class Error {
        private String message;
        private String type;
        private String param;
        private String code;
    }
}
