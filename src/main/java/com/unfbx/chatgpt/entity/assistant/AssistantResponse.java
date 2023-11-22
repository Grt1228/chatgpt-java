package com.unfbx.chatgpt.entity.assistant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssistantResponse implements Serializable {

    private String id;
    private String object;
    /**
     * All models except gpt-3.5-turbo-0301 supported. retrieval tool requires gpt-4-1106-preview or gpt-3.5-turbo-1106.
     *
     * @see com.unfbx.chatgpt.entity.chat.ChatCompletion.Model
     */
    private String model;
    /**
     * 助理的名字。最大长度为 256 个字符。
     */
    private String name;
    /**
     * 助理的描述。最大长度为 512 个字符。
     */
    private String description;
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
    /**
     * 创建时间戳
     */
    @JsonProperty("created_at")
    private Long createdAt;


}
