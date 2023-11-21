package com.unfbx.chatgpt.entity.fineTune.job;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class FineTuneJob implements Serializable {

    /**
     * 参考
     *
     * @see FineTuneJob.Model
     */
    private String model;

    /**
     * 上传的文件ID
     */
    @JsonProperty("training_file")
    private String trainingFile;

    /**
     * 用于微调工作的参数
     */
    @JsonProperty("hyperparameters")
    private HyperParameters hyperParameters;

    private String suffix;

    @JsonProperty("validation_file")
    private String validationFile;

    @Getter
    @AllArgsConstructor
    public enum Model {
        GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106"),
        GPT_3_5_TURBO_0613("gpt-3.5-turbo-0613"),
        BABBAGE_002("babbage-002"),
        GPT_4_0613("gpt-4-0613"),
        DAVINCI_002("davinci-002"),
        ;
        private final String name;
    }
}
