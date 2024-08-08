package com.unfbx.chatgpt.entity.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@Data
public class BatchesRequest {

    @JsonProperty("input_file_id")
    private String inputFileId;
    @JsonProperty("endpoint")
    private String endpoint;
    @JsonProperty("completion_window")
    private String completionWindow;
    @JsonProperty("metadata")
    private Map<String, String> metadata;

}
