package com.unfbx.chatgpt.entity.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
public class Model implements Serializable {

    private String id;
    private String object;
    private long created;
    private String ownedBy;
    private List<Permission> permission;
    private String root;
    private Object parent;

    @JsonProperty("id")
    public String getID() { return id; }
    @JsonProperty("id")
    public void setID(String value) { this.id = value; }

    @JsonProperty("object")
    public String getObject() { return object; }
    @JsonProperty("object")
    public void setObject(String value) { this.object = value; }

    @JsonProperty("created")
    public long getCreated() { return created; }
    @JsonProperty("created")
    public void setCreated(long value) { this.created = value; }

    @JsonProperty("owned_by")
    public String getOwnedBy() { return ownedBy; }
    @JsonProperty("owned_by")
    public void setOwnedBy(String value) { this.ownedBy = value; }

    @JsonProperty("permission")
    public List<Permission> getPermission() { return permission; }
    @JsonProperty("permission")
    public void setPermission(List<Permission> value) { this.permission = value; }

    @JsonProperty("root")
    public String getRoot() { return root; }
    @JsonProperty("root")
    public void setRoot(String value) { this.root = value; }

    @JsonProperty("parent")
    public Object getParent() { return parent; }
    @JsonProperty("parent")
    public void setParent(Object value) { this.parent = value; }
}
