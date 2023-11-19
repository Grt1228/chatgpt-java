package com.unfbx.chatgpt.entity.assistant.run;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unfbx.chatgpt.entity.assistant.Tool;
import com.unfbx.chatgpt.entity.assistant.thread.Thread;
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
 * 2023-11-20
 */
@Getter
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ThreadRun implements Serializable {
    @JsonProperty("assistant_id")
    private String assistantId;

    private Thread thread;

    private String model;
    /**
     * 覆盖助手的默认系统消息。这对于修改每次运行的行为很有用。
     */
    private String instructions;

    /**
     * 覆盖助手可用于此运行的工具。这对于修改每次运行的行为很有用。
     */
    private List<Tool> tools;

    /**
     * Set of 16 key-value pairs that can be attached to an object.
     * This can be useful for storing additional information about the object in a structured format.
     * Keys can be a maximum of 64 characters long and values can be a maxium of 512 characters long.
     */
    private Map metadata;

}
