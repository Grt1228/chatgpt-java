package com.unfbx.chatgpt.entity.assistant.run;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unfbx.chatgpt.entity.chat.tool.ToolCalls;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

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
public class SubmitToolOutputs implements Serializable {
    @JsonProperty("tool_calls")
    private List<ToolCalls> toolCalls;
}
