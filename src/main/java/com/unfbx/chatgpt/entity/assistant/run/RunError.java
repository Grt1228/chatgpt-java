package com.unfbx.chatgpt.entity.assistant.run;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author https://www.unfbx.com
 * @since 1.1.3
 * 2023-11-17
 */
@Data
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RunError implements Serializable {
    /**
     * One of server_error or rate_limit_exceeded.
     * @see Code
     */
    private String code;
    private String message;


    @Getter
    @AllArgsConstructor
    public enum Code {
        SERVER_ERROR("server_error"),
        RATE_LIMIT_EXCEEDED("rate_limit_exceeded"),
        ;
        private final String name;
    }
}
