package com.unfbx.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @since 2023-12-25
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogprobContent implements Serializable {

    private List<Logprob> content;
}
