package com.unfbx.chatgpt.entity.assistant.run;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;

/**
 * 描述：
 *
 * @author https://www.unfbx.com
 * @since 1.1.3
 * 2023-11-20
 */
@Getter
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class RunStepResponse implements Serializable {

    private String id;
    private String object;
    /**
     * 创建时间戳
     */
    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("assistant_id")
    private String assistantId;

    @JsonProperty("thread_id")
    private String threadId;

    @JsonProperty("run_id")
    private String runId;

    /**
     * @see Type
     */
    private String type;

    /**
     * @see Status
     */
    private String status;

    @JsonProperty("step_details")
    private StepDetail stepDetails;

    /**
     * 最有一个错误信息，没有错误为null
     */
    @JsonProperty("last_error")
    private RunError lastError;

    @JsonProperty("expires_at")
    private Long expiresAt;

    @JsonProperty("cancelled_at")
    private Long cancelledAt;

    @JsonProperty("failed_at")
    private Long failedAt;

    @JsonProperty("completed_at")
    private Long completedAt;

    /**
     * Set of 16 key-value pairs that can be attached to an object.
     * This can be useful for storing additional information about the object in a structured format.
     * Keys can be a maximum of 64 characters long and values can be a maxium of 512 characters long.
     */
    private Map metadata;

    @Getter
    @AllArgsConstructor
    public enum Status {
        IN_PROGRESS("in_progress", "进行中"),
        CANCELLED("cancelled", "已取消"),
        FAILED("failed", "失败"),
        COMPLETED("completed", "已完成"),
        EXPIRED("expired", "已过期"),
        ;
        private final String name;
        private final String desc;
    }

    @Getter
    @AllArgsConstructor
    public enum Type {
        MESSAGE_CREATION("message_creation"),
        TOOL_CALLS("tool_calls"),
        ;
        private final String name;
    }
}
