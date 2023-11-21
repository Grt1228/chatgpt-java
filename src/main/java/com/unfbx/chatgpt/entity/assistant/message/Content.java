package com.unfbx.chatgpt.entity.assistant.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述：
 *
 * @author https://www.unfbx.com
 * @since 1.1.3
 * 2023-11-20
 */
@Data
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Content {
    /**
     * 输入类型：text、image_file
     *
     * @see Type
     */
    private String type;

    private Text text;

    @JsonProperty("image_file")
    private ImageFile imageFile;

    /**
     * 类型
     */
    @Getter
    @AllArgsConstructor
    public enum Type {
        TEXT("text"),
        IMAGE_FILE("image_file"),
        ;
        private final String name;
    }
}
