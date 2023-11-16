package com.unfbx.chatgpt.entity.assistant.run;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unfbx.chatgpt.entity.assistant.Tool;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 *
 * @author https://www.unfbx.com
 * @since 1.1.3
 * 2023-11-17
 */
@Getter
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RunResponse implements Serializable {

    private String id;
    private String object;
    /**
     * 创建时间戳
     */
    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("thread_id")
    private String threadId;

    @JsonProperty("assistant_id")
    private String assistantId;

    /**
     * @see Status
     */
    private String status;

    @JsonProperty("required_action")
    private Action requiredAction;

    /**
     * 最有一个错误信息，没有错误为null
     */
    @JsonProperty("last_error")
    private RunError lastError;

    @JsonProperty("expires_at")
    private Long expiresAt;

    @JsonProperty("started_at")
    private Long startedAt;

    @JsonProperty("cancelled_at")
    private Long cancelledAt;

    @JsonProperty("failed_at")
    private Long failedAt;

    @JsonProperty("completed_at")
    private Long completedAt;
    /**
     * All models except gpt-3.5-turbo-0301 supported. retrieval tool requires gpt-4-1106-preview or gpt-3.5-turbo-1106.
     *
     * @see com.unfbx.chatgpt.entity.chat.ChatCompletion.Model
     */
    private String model;
    /**
     * 助手使用的系统指令。最大长度为 32768 个字符。
     */
    private String instructions;
    /**
     * 助手上启用的工具列表。每个助手最多可以有 128 个工具。工具可以是 code_interpreter、retrieval或function。
     */
    private List<Tool> tools;
    /**
     * 附加到该助手的文件 ID 列表。助手最多可以附加 20 个文件。文件按其创建日期升序排列。
     */
    @JsonProperty("file_ids")
    private List<String> fileIds;
    /**
     * Set of 16 key-value pairs that can be attached to an object.
     * This can be useful for storing additional information about the object in a structured format.
     * Keys can be a maximum of 64 characters long and values can be a maxium of 512 characters long.
     */
    private Map metadata;

    @Getter
    @AllArgsConstructor
    public enum Status {
        QUEUED("queued", "排队"),
        IN_PROGRESS("in_progress", "进行中"),
        REQUIRE_ACTION("require_action", "需要操作"),
        CANCELLING("cancelling", "取消"),
        CANCELLED("cancelled", "已取消"),
        FAILED("failed", "失败"),
        COMPLETED("completed", "已完成"),
        EXPIRED("expired", "已过期"),
        ;
        private final String name;
        private final String desc;
    }
}
