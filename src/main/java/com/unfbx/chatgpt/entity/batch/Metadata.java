package com.unfbx.chatgpt.entity.batch;

import lombok.ToString;

import java.util.HashMap;

@ToString(callSuper = true)
public class Metadata extends HashMap<String, String> {

    public Metadata() {
        super();
    }

    public Metadata(int initialCapacity) {
        super(initialCapacity);
    }

    public Metadata(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public Metadata(HashMap<? extends String, ? extends String> m) {
        super(m);
    }

}