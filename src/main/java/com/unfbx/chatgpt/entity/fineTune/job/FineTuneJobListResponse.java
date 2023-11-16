package com.unfbx.chatgpt.entity.fineTune.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FineTuneJobListResponse<T> implements Serializable {
    private String object;
    private List<T> data;
    @JsonProperty("has_more")
    private Boolean hasMore;
}
