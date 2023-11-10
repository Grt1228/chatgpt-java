package com.unfbx.chatgpt.entity.chat;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.unfbx.chatgpt.utils.TikTokensUtil;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;

/**
 * 描述： chat模型参数
 *
 * @author https:www.unfbx.com
 * 2023-03-02
 */
@Data
@SuperBuilder
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ChatCompletion extends BaseChatCompletion implements Serializable {

    /**
     * 问题描述
     */
    @NonNull
    private List<Message> messages;

    /**
     * 获取当前参数的tokens数
     */
    public long tokens() {
        if (CollectionUtil.isEmpty(this.messages) || StrUtil.isBlank(this.getModel())) {
            log.warn("参数异常model：{}，prompt：{}", this.getModel(), this.messages);
            return 0;
        }
        return TikTokensUtil.tokens(this.getModel(), this.messages);
    }
}
