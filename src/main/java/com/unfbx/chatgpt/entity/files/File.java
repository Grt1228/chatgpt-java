package com.unfbx.chatgpt.entity.files;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class File implements Serializable {

    private String id;
    private Long bytes;
    @JsonProperty("created_at")
    private Long createdAt;
    private String filename;
    private String object;
    private String purpose;
    @Deprecated
    private String status;
    @Deprecated
    @JsonProperty("status_details")
    private String statusDetails;
}
