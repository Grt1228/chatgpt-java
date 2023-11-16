package com.unfbx.chatgpt.entity.assistant.thread;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

@Getter
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ThreadMessage {

    private String role;
    private String content;
    @JsonProperty("file_ids")
    private List<String> fileIds;
    private Map metadata;

    @Getter
    @AllArgsConstructor
    public enum Role {

        USER("user"),
        ;
        private final String name;
    }
}
