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
 * @since 1.1.4
 * 2023-11-20
 */
@Data
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image implements Serializable {

    @JsonProperty("file_id")
    private String fileId;
}
