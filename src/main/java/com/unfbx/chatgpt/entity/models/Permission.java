package com.unfbx.chatgpt.entity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @date 2023-02-15
 */
public class Permission {
    private String id;
    private String object;
    private long created;
    private boolean allowCreateEngine;
    private boolean allowSampling;
    private boolean allowLogprobs;
    private boolean allowSearchIndices;
    private boolean allowView;
    private boolean allowFineTuning;
    private String organization;
    /**
     * 不知道是什么类型的数据
     */
    private Object group;
    private boolean isBlocking;

    @JsonProperty("id")
    public String getID() {
        return id;
    }

    @JsonProperty("id")
    public void setID(String value) {
        this.id = value;
    }

    @JsonProperty("object")
    public String getObject() {
        return object;
    }

    @JsonProperty("object")
    public void setObject(String value) {
        this.object = value;
    }

    @JsonProperty("created")
    public long getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(long value) {
        this.created = value;
    }

    @JsonProperty("allow_create_engine")
    public boolean getAllowCreateEngine() {
        return allowCreateEngine;
    }

    @JsonProperty("allow_create_engine")
    public void setAllowCreateEngine(boolean value) {
        this.allowCreateEngine = value;
    }

    @JsonProperty("allow_sampling")
    public boolean getAllowSampling() {
        return allowSampling;
    }

    @JsonProperty("allow_sampling")
    public void setAllowSampling(boolean value) {
        this.allowSampling = value;
    }

    @JsonProperty("allow_logprobs")
    public boolean getAllowLogprobs() {
        return allowLogprobs;
    }

    @JsonProperty("allow_logprobs")
    public void setAllowLogprobs(boolean value) {
        this.allowLogprobs = value;
    }

    @JsonProperty("allow_search_indices")
    public boolean getAllowSearchIndices() {
        return allowSearchIndices;
    }

    @JsonProperty("allow_search_indices")
    public void setAllowSearchIndices(boolean value) {
        this.allowSearchIndices = value;
    }

    @JsonProperty("allow_view")
    public boolean getAllowView() {
        return allowView;
    }

    @JsonProperty("allow_view")
    public void setAllowView(boolean value) {
        this.allowView = value;
    }

    @JsonProperty("allow_fine_tuning")
    public boolean getAllowFineTuning() {
        return allowFineTuning;
    }

    @JsonProperty("allow_fine_tuning")
    public void setAllowFineTuning(boolean value) {
        this.allowFineTuning = value;
    }

    @JsonProperty("organization")
    public String getOrganization() {
        return organization;
    }

    @JsonProperty("organization")
    public void setOrganization(String value) {
        this.organization = value;
    }

    @JsonProperty("group")
    public Object getGroup() {
        return group;
    }

    @JsonProperty("group")
    public void setGroup(Object value) {
        this.group = value;
    }

    @JsonProperty("is_blocking")
    public boolean getIsBlocking() {
        return isBlocking;
    }

    @JsonProperty("is_blocking")
    public void setIsBlocking(boolean value) {
        this.isBlocking = value;
    }
}
