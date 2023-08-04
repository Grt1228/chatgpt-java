package com.unfbx.chatgpt.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 描述：方法参数实体类，实例数据如下
 * <pre>
 *     {
 *          "name": "get_current_weather",
 *          "description": "Get the current weather in a given location",
 *          "parameters": {
 *              "type": "object",
 *              "properties": {
 *                  "location": {
 *                      "type": "string",
 *                      "description": "The city and state, e.g. San Francisco, CA"
 *                  },
 *                  "unit": {"type": "string", "enum": ["celsius", "fahrenheit"]}
 *              },
 *              "required": ["location"]
 *          },
 *     }
 * </pre>
 * @author https:www.unfbx.com
 * @since  2023-06-14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Functions implements Serializable {
    /**
     * 方法名称
     */
    private String name;
    /**
     * 方法描述
     */
    private String description;
    /**
     * 方法参数
     * 扩展参数可以继承Parameters自己实现，json格式的数据
     */
    private Parameters parameters;
}
