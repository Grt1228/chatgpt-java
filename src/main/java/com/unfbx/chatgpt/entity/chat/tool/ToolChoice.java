package com.unfbx.chatgpt.entity.chat.tool;

import lombok.*;

import java.io.Serializable;

/**
 * choice和object同时存在是以object为准
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToolChoice implements Serializable {

    @Getter
    @AllArgsConstructor
    public enum Choice {
        NONE("none"),
        AUTO("auto"),
        ;
        private final String name;
    }
}
