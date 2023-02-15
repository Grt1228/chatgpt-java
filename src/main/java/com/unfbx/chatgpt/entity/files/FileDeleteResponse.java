package com.unfbx.chatgpt.entity.files;

import lombok.Data;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @date 2023-02-15
 */
@Data
public class FileDeleteResponse {
    private String id;
    private String object;
    private boolean deleted;
}
