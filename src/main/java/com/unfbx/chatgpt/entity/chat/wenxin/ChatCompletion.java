package com.unfbx.chatgpt.entity.chat.wenxin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unfbx.chatgpt.entity.chat.Functions;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 文心一言模型参数
 */
@Data
@Builder
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class ChatCompletion implements Serializable {
    /**
     * 使用什么取样温度，0到2之间。较高的值(如0.8)将使输出更加随机，而较低的值(如0.2)将使输出更加集中和确定。
     * <p>
     * We generally recommend altering this or but not both.top_p
     */
    @Builder.Default
    private double temperature = 0.2;

    /**
     * 使用温度采样的替代方法称为核心采样，其中模型考虑具有top_p概率质量的令牌的结果。因此，0.1 意味着只考虑包含前 10% 概率质量的代币。
     * <p>
     * 我们通常建议更改此设置，但不要同时更改两者。temperature
     */
    @JsonProperty("top_p")
    @Builder.Default
    private Double topP = 0.8d;

    /**
     * 通过对已生成的token增加惩罚，减少重复生成的现象。说明：
     * （1）值越大表示惩罚越大
     * （2）默认1.0，取值范围：[1.0, 2.0]
     */
    @JsonProperty("penalty_score")
    @Builder.Default
    private Double penalty_score = 1d;

    /**
     * 是否流式输出.
     * default:false
     *
     * @see com.unfbx.chatgpt.OpenAiStreamClient
     */
    @Builder.Default
    private boolean stream = false;

    /**
     * 用户唯一值，确保接口不被重复调用
     */
    private String user_id;

    /**
     * 问题描述
     */
    @NonNull
    private List<Message> messages;
}
