package com.unfbx.chatgpt.entity.files;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @date 2023-02-15
 */
@Data
public class UploadFileResponse extends File {
    private String status;
    @JsonProperty("status_details")
    private String statusDetails;
}
