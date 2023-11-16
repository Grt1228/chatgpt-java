package com.unfbx.chatgpt.entity.fineTune.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FineTuneJobEvent implements Serializable {

    private String id;
    @JsonProperty("created_at")
    private Long createdAt;
    private String level;
    private String message;
    private String object;

}
