package com.unfbx.chatgpt.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
/**
 * 描述： 答案类
 *
 * @author https:www.unfbx.com
 * @date 2023-02-11
 */
@Data
public class Answer {
    private String id;
    private String object;
    private long created;
    private String model;
    private Choice[] choices;
    private Usage usage;
    private Error error;

    @Data
    public class Choice {
        private String text;
        private long index;
        private Object logprobs;
        @JsonProperty("finish_reason")
        private String finishReason;
    }

    @Data
    public class Usage {
        @JsonProperty("prompt_tokens")
        private long promptTokens;
        @JsonProperty("completion_tokens")
        private long completionTokens;
        @JsonProperty("total_tokens")
        private long totalTokens;
    }

    @Data
    public class Error {
        private String message;
        private String type;
        private String param;
        private String code;
    }
}
