package com.unfbx.chatgpt.entity.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class BatchesListResponse {

    @JsonProperty("object")
    private String object;
    @JsonProperty("data")
    private List<BatchesResponse> data;
    @JsonProperty("first_id")
    private String firstId;
    @JsonProperty("last_id")
    private String lastId;
    @JsonProperty("has_more")
    private Boolean hasMore;

}
