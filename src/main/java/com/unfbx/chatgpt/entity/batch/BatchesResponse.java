package com.unfbx.chatgpt.entity.batch;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;

@NoArgsConstructor
@Data
public class BatchesResponse {

    @JsonProperty("id")
    private String id;
    @JsonProperty("object")
    private String object;
    @JsonProperty("endpoint")
    private String endpoint;
    @JsonProperty("errors")
    private Object errors;
    @JsonProperty("input_file_id")
    private String inputFileId;
    @JsonProperty("completion_window")
    private String completionWindow;
    @JsonProperty("status")
    private String status;
    @JsonProperty("output_file_id")
    private Object outputFileId;
    @JsonProperty("error_file_id")
    private Object errorFileId;
    @JsonProperty("created_at")
    private Integer createdAt;
    @JsonProperty("in_progress_at")
    private Object inProgressAt;
    @JsonProperty("expires_at")
    private Object expiresAt;
    @JsonProperty("finalizing_at")
    private Object finalizingAt;
    @JsonProperty("completed_at")
    private Object completedAt;
    @JsonProperty("failed_at")
    private Object failedAt;
    @JsonProperty("expired_at")
    private Object expiredAt;
    @JsonProperty("cancelling_at")
    private Object cancellingAt;
    @JsonProperty("cancelled_at")
    private Object cancelledAt;
    @JsonProperty("request_counts")
    private RequestCountsDTO requestCounts;
    @JsonProperty("metadata")
    private Metadata metadata;

    @NoArgsConstructor
    @Data
    public static class RequestCountsDTO {
        @JsonProperty("total")
        private Integer total;
        @JsonProperty("completed")
        private Integer completed;
        @JsonProperty("failed")
        private Integer failed;
    }

}
