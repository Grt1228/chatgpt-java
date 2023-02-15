package com.unfbx.chatgpt.entity.files;

import lombok.Data;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @date 2023-02-15
 */
@Data
public class File {

    private String id;
    private String object;
    private long bytes;
    private long created_at;
    private String filename;
    private String purpose;
}
