package com.unfbx.chatgpt.entity.chat.tool;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * ToolCall 的 Function参数
 * The function that the model called.
 *
 * @author https:www.unfbx.com
 * @since 1.1.2
 * 2023-11-09
 */
@Data
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ToolCallFunction implements Serializable {
    /**
     * 方法名
     */
    private String name;
    /**
     * 方法参数
     */
    private String arguments;
}
