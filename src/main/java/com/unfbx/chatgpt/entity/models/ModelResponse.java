package com.unfbx.chatgpt.entity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
public class ModelResponse {
    private String object;
    private List<Model> data;

    @JsonProperty("object")
    public String getObject() {
        return object;
    }

    @JsonProperty("object")
    public void setObject(String value) {
        this.object = value;
    }

    @JsonProperty("data")
    public List<Model> getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<Model> value) {
        this.data = value;
    }
}
