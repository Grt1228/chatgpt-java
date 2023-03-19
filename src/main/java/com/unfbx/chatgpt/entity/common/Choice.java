package com.unfbx.chatgpt.entity.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Data
public class Choice implements Serializable {
    private String text;
    private long index;
    private Object logprobs;
    @JsonProperty("finish_reason")
    private String finishReason;
}