package com.unfbx.chatgpt.entity.images;

import lombok.Data;

import java.util.List;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @date 2023-02-15
 */
@Data
public class ImageResponse {
    private long created;
    private List<Item> data;
}
