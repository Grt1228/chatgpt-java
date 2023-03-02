package com.unfbx.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @since  2023-03-02
 */
@Data
public class ChatChoice {
    private long index;
    private Message message;
    @JsonProperty("finish_reason")
    private String finishReason;
}
