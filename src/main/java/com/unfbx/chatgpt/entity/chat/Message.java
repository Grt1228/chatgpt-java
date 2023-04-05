package com.unfbx.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @since 2023-03-02
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message implements Serializable {
    @NotNull
    /**
     * 目前支持三中角色参考官网，进行情景输入：https://platform.openai.com/docs/guides/chat/introduction
     */
    private String role;

    private String content;

    private String name;

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 构造函数
     *
     * @param role
     * @param content 描述主题信息
     */
    public Message(String role, String content, String name) {
        this.role = role;
        this.content = content;
        this.name = name;
    }

    public Message() {
    }

    private Message(Builder builder) {
        setRole(builder.role);
        setContent(builder.content);
        setName(builder.name);
    }


    @Getter
    @AllArgsConstructor
    public enum Role {

        SYSTEM("system"),
        USER("user"),
        ASSISTANT("assistant"),
        ;
        private String name;
    }

    public static final class Builder {
        private @NotNull String role;
        private String content;
        private String name;

        public Builder() {
        }

        public Builder role(@NotNull Role role) {
            this.role = role.getName();
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
