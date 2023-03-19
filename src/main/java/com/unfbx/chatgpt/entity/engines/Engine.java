package com.unfbx.chatgpt.entity.engines;

import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Data
public class Engine implements Serializable {

    private String id;
    private String object;
    private String owner;
    private boolean ready;
    private Object permissions;
    private long created;

}
