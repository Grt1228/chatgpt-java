package com.unfbx.chatgpt.entity.fineTune;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TrainingFile {

    private String id;
    private String object;
    private long bytes;
    @JsonProperty("created_at")
    private long createdAt;
    private String filename;
    private String purpose;
    private String status;
    @JsonProperty("status_details")
    private String statusDetails;
}
