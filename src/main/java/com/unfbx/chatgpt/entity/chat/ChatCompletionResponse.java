package com.unfbx.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unfbx.chatgpt.entity.common.Usage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述： chat答案类
 *
 * @author https:www.unfbx.com
 * 2023-03-02
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatCompletionResponse implements Serializable {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatChoice> choices;
    private Usage usage;
    private String warning;
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;
}
