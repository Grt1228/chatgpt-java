package com.unfbx.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 描述： chat
 *
 * @author https:www.unfbx.com
 * 2023-03-02
 */
@Data
@Builder
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletion implements Serializable {

    @NonNull
    @Builder.Default
    private String model = Model.GPT_3_5_TURBO.getName();
    /**
     * 问题描述
     */
    @NonNull
    private List<Message> messages;
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
    private Double topP = 1d;


    /**
     * 为每个提示生成的完成次数。
     */
    @Builder.Default
    private Integer n = 1;


    /**
     * 是否流式输出.
     * default:false
     *
     * @see com.unfbx.chatgpt.OpenAiStreamClient
     */
    @Builder.Default
    private boolean stream = false;
    /**
     * 停止输出标识
     */
    private List<String> stop;
    /**
     * 最大支持4096
     */
    @JsonProperty("max_tokens")
    @Builder.Default
    private Integer maxTokens = 2048;


    @JsonProperty("presence_penalty")
    @Builder.Default
    private double presencePenalty = 0;

    /**
     * -2.0 ~~ 2.0
     */
    @JsonProperty("frequency_penalty")
    @Builder.Default
    private double frequencyPenalty = 0;

    @JsonProperty("logit_bias")
    private Map logitBias;
    /**
     * 用户唯一值，确保接口不被重复调用
     */
    private String user;


    public void setModel(Model model) {
        this.model = model.getName();
    }

    public void setMaxTokens(Integer maxTokens) {
        if (maxTokens > 4096) {
            log.error("maxTokens参数异常，不能超过4096");
            this.maxTokens = 4096;
            return;
        }
        if (maxTokens <= 0) {
            log.error("maxTokens参数异常，不能小于0");
            this.maxTokens = 64;
            return;
        }
        this.maxTokens = maxTokens;
    }

    public void setTemperature(double temperature) {
        if (temperature > 2 || temperature < 0) {
            log.error("temperature参数异常，temperature属于[0,2]");
            this.temperature = 2;
            return;
        }
        if (temperature < 0) {
            log.error("temperature参数异常，temperature属于[0,2]");
            this.temperature = 0;
            return;
        }
        this.temperature = temperature;
    }

    public void setStop(List<String> stop) {
        this.stop = stop;
    }

    public void setTopP(Double topP) {
        this.topP = topP;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public void setPresencePenalty(double presencePenalty) {
        if (presencePenalty < -2.0) {
            this.presencePenalty = -2.0;
            return;
        }
        if (presencePenalty > 2.0) {
            this.presencePenalty = 2.0;
            return;
        }
        this.presencePenalty = presencePenalty;
    }

    public void setFrequencyPenalty(double frequencyPenalty) {
        if (frequencyPenalty < -2.0) {
            this.frequencyPenalty = -2.0;
            return;
        }
        if (frequencyPenalty > 2.0) {
            this.frequencyPenalty = 2.0;
            return;
        }
        this.frequencyPenalty = frequencyPenalty;
    }


    public void setLogitBias(Map logitBias) {
        this.logitBias = logitBias;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Getter
    @AllArgsConstructor
    public enum Model {
        /**
         * gpt-3.5-turbo
         */
        GPT_3_5_TURBO("gpt-3.5-turbo"),
        /**
         * 临时模型，不建议使用
         */
        GPT_3_5_TURBO_0301("gpt-3.5-turbo-0301"),
         /**
         * GPT4.0
         */
        GPT_4("gpt-4"),
        /**
         * 临时模型，不建议使用
         */
        GPT_4_0314("gpt-4-0314"),
        /**
         * GPT4.0 超长上下文
         */
        GPT_4_32K("gpt-4-32k"),
        /**
         * 临时模型，不建议使用
         */ 
        GPT_4_32K_0314("gpt-4-32k-0314"),
        ;
        private String name;
    }

}


