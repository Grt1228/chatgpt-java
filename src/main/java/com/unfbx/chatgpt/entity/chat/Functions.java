package com.unfbx.chatgpt.entity.chat;

import lombok.Builder;
import lombok.Data;

/**
 * 方法参数实体类
 * 实例数据如下：
 *
 * <code>
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
 * <code/>
 */

@Data
@Builder
public class Functions {

    private String name;
    private String description;
    private Parameters parameters;
}
