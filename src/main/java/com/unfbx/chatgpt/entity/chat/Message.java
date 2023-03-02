package com.unfbx.chatgpt.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @since 2023-03-02
 */
@Data
public class Message {
    @NotNull
    private String role;
    @NotNull
    private String content;

    public static Builder builder(){
        return new Builder();
    }

    /**
     * 构造函数
     *
     * @param role    目前支持三中角色参考官网，进行情景输入：https://platform.openai.com/docs/guides/chat/introduction
     * @param content 描述主题信息
     */
    public Message(Role role, String content) {
        this.role = role.getName();
        this.content = content;
    }

    private Message(Builder builder) {
        setRole(builder.role);
        setContent(builder.content);
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
        private @NotNull String content;

        public Builder() {
        }

        public Builder role(@NotNull Role role) {
            this.role = role.getName();
            return this;
        }

        public Builder content(@NotNull String content) {
            this.content = content;
            return this;
        }

        public Message build() {
            return new Message(this);
        }
    }
}
