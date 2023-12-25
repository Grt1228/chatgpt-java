package com.unfbx.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @since 2023-12-25
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Logprob implements Serializable {
    private String token;

    private BigDecimal logprob;

    private List<Integer> bytes;

    @JsonProperty("top_logprobs")
    private List<Logprob> topLogprobs;
}
