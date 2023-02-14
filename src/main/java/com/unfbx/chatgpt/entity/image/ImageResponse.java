package com.unfbx.chatgpt.entity.image;

import lombok.Data;

import java.util.List;

@Data
public class ImageResponse {
    private long created;
    private List<Item> data;
}
