package com.unfbx.chatgpt.entity.assistant;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Getter
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class AssistantFile implements Serializable {
    /**
     * 文件id
     */
    @JsonProperty("file_id")
    private String fileId;
    /**
     * 助手id
     */
    @JsonProperty("assistant_id")
    private String assistantId;
}
