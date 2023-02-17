package com.unfbx.chatgpt.entity.images;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@AllArgsConstructor
@Getter
public enum ResponseFormat{
    URL("url"),
    B64_JSON("urlb64_json"),
    ;

    private String name;
}
