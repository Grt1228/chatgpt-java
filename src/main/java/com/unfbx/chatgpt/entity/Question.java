package com.unfbx.chatgpt.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import lombok.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 描述： 问题类
 *
 * @author https:www.unfbx.com
 * @date 2023-02-11
 */
@Data
@Builder
public class Question implements Serializable {

    private String prompt;
    @Builder.Default
    private String model = Model.DAVINCI_003.getName();
    @JsonProperty("max_tokens")
    @Builder.Default
    private Integer max_tokens = 1024;
    @Builder.Default
    private double temperature = 0.9;
    @Builder.Default
    private List<String> stop = Arrays.asList("#");
    @JsonProperty("top_p")
    @Builder.Default
    private Integer top_p = 1;
    @Builder.Default
    private Integer n = 1;

    public void setPrompt(String prompt) {
        if (Objects.isNull(prompt) || "".equals(prompt)) {
            throw new BaseException(CommonError.PARAM_ERROR);
        }
        this.prompt = prompt;
    }
}

@Getter
@AllArgsConstructor
enum Model {
    DAVINCI_003("text-davinci-003"),
    DAVINCI_002("text-davinci-002"),
    DAVINCI("davinci"),
    ;
    private String name;
}
