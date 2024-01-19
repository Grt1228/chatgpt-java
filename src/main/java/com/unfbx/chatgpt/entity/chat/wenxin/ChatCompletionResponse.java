package com.unfbx.chatgpt.entity.chat.wenxin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
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
    private long sentence_id;
    private Boolean is_end;
    private Boolean is_truncated;
    private String result;
    private Boolean need_clear_history;
    private int ban_round;
}
