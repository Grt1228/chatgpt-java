package com.unfbx.chatgpt.entity.fineTune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Event {
    private String object;
    @JsonProperty("created_at")
    private long createdAt;
    private String level;
    private String message;
}
