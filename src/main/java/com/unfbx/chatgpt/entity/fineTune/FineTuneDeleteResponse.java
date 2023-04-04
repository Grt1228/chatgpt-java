package com.unfbx.chatgpt.entity.fineTune;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unfbx.chatgpt.entity.models.Permission;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class FineTuneDeleteResponse implements Serializable {

    private String id;

    private String object;

    private long created;

    @JsonProperty("owned_by")
    private String ownedBy;

    @JsonProperty("permission")
    private List<Permission> permission;

    private String root;

    private String parent;

}
