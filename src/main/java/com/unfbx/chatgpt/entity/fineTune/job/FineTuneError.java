package com.unfbx.chatgpt.entity.fineTune.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 描述：
 *
 * @author https://www.unfbx.com
 * @since 1.1.2
 * 2023-11-12
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FineTuneError {
    private String message;
    private String param;
    private String code;
}
