package com.unfbx.chatgpt.entity.assistant.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 描述：
 * https://platform.openai.com/docs/api-reference/messages/object
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
public class Annotations {

    private String type;

    private String text;

    /**
     * https://platform.openai.com/docs/api-reference/messages/object
     * <p>
     * A citation within the message that points to a specific quote from a specific File associated with the assistant or the message.
     * Generated when the assistant uses the "retrieval" tool to search files.
     */
    @JsonProperty("file_citation")
    private FileCitation fileCitation;

    /**
     * https://platform.openai.com/docs/api-reference/messages/object
     * A URL for the file that's generated when the assistant used the code_interpreter tool to generate a file.
     */
    @JsonProperty("file_path")
    private FilePath filePath;

    @JsonProperty("start_index")
    private Integer startIndex;

    @JsonProperty("end_index")
    private Integer endIndex;


    /**
     * 类型
     */
    @Getter
    @AllArgsConstructor
    public enum Type {
        FILE_CITATION("file_citation"),
        FILE_PATH("file_path"),
        ;
        private final String name;
    }
}
