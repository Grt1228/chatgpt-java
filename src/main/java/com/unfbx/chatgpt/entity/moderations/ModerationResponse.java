package com.unfbx.chatgpt.entity.moderations;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Data
public class ModerationResponse implements Serializable {
    private String id;
    private String model;
    private List<Result> results;
}
