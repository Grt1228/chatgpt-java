package com.unfbx.chatgpt.entity.completions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.*;

/**
 * 描述： 问题类
 *
 * @author https:www.unfbx.com
 * @date 2023-02-11
 */
@Data
@Builder
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Completion implements Serializable {

    @NonNull
    @Builder.Default
    private String model = Model.DAVINCI_003.getName();
    /**
     * 问题描述
     */
    @NonNull
    private String prompt;
    /**
     * 完成输出后的后缀，用于格式化输出结果
     */
    private String suffix;

    /**
     * 最大支持4096
     */
    @JsonProperty("max_tokens")
    @Builder.Default
    private Integer maxTokens = 2048;
    /**
     * 使用什么取样温度，0到2之间。较高的值(如0.8)将使输出更加随机，而较低的值(如0.2)将使输出更加集中和确定。
     * <p>
     * We generally recommend altering this or but not both.top_p
     */
    @Builder.Default
    private double temperature = 0;

    /**
     * 使用温度采样的替代方法称为核心采样，其中模型考虑具有top_p概率质量的令牌的结果。因此，0.1 意味着只考虑包含前 10% 概率质量的代币。
     * <p>
     * 我们通常建议更改此设置，但不要同时更改两者。temperature
     */
    @JsonProperty("top_p")
    @Builder.Default
    private Integer topP = 1;

    /**
     * 为每个提示生成的完成次数。
     */
    @Builder.Default
    private Integer n = 1;

    @Builder.Default
    private boolean stream = false;
    /**
     * 最大值：5
     */
    private Integer logprobs;

    @Builder.Default
    private boolean echo = false;

    @Builder.Default
    private List<String> stop = Arrays.asList("#");

    @JsonProperty("presence_penalty")
    @Builder.Default
    private double presencePenalty = 0;

    /**
     * -2.0 ~~ 2.0
     */
    @JsonProperty("frequency_penalty")
    @Builder.Default
    private double frequencyPenalty = 0;

    @JsonProperty("best_of")
    @Builder.Default
    private Integer bestOf = 1;

    @JsonProperty("logit_bias")
    private Map logitBias;
    /**
     * 用户唯一值，确保接口不被重复调用
     */
    private String user;

    public void setPrompt(String prompt) {
        if (Objects.isNull(prompt) || "".equals(prompt)) {
            log.error("prompt参数异常");
            throw new BaseException(CommonError.PARAM_ERROR);
        }
        this.prompt = prompt;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
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

    public void setTopP(Integer topP) {
        this.topP = topP;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public void setLogprobs(Integer logprobs) {
        if (logprobs > 5) {
            log.error("logprobs参数异常，logprobs最大值==5");
            this.logprobs = 5;
            return;
        }
        this.logprobs = logprobs;
    }

    public void setEcho(boolean echo) {
        this.echo = echo;
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

    public void setBestOf(Integer bestOf) {
        this.bestOf = bestOf;
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
        DAVINCI_003("text-davinci-003"),
        DAVINCI_002("text-davinci-002"),
        DAVINCI("davinci"),
        ;
        private String name;
    }
}


