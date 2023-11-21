package com.unfbx.chatgpt.entity.assistant.run;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author https://www.unfbx.com
 * @since 1.1.3
 * 2023-11-17
 */
@Data
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Action implements Serializable {
    /**
     * 1.1.3版本，始终是submit_tool_outputs。
     *
     * @see Type
     */
    private String type;

    @JsonProperty("submit_tool_outputs")
    private SubmitToolOutputs submitToolOutputs;

    @Getter
    @AllArgsConstructor
    public enum Type {
        SUBMIT_TOOL_OUTPUTS("submit_tool_outputs"),
        ;
        private final String name;
    }
}
