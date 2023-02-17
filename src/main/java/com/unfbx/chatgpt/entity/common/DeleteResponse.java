package com.unfbx.chatgpt.entity.common;

import lombok.Data;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Data
public class DeleteResponse {
    private String id;
    private String object;
    private boolean deleted;
}
