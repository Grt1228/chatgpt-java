package com.unfbx.chatgpt.entity.fineTune.job;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unfbx.chatgpt.entity.common.OpenAiResponse;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FineTuneJobResponse implements Serializable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("created_at")
    private Long createdAt;

    @JsonProperty("error")
    private OpenAiResponse.Error error;

    @JsonProperty("fine_tuned_model")
    private String fineTunedModel;

    @JsonProperty("finished_at")
    private Long finishedAt;

    @JsonProperty("hyperparameters")
    private HyperParameters hyperparameters;

    @JsonProperty("model")
    private String model;

    @JsonProperty("object")
    private String object;

    @JsonProperty("organization_id")
    private String organizationId;

    @JsonProperty("result_files")
    private List<String> resultFiles;

    @JsonProperty("status")
    private Long status;

    @JsonProperty("trained_tokens")
    private Integer trainedTokens;

    @JsonProperty("training_file")
    private String trainingFile;

    @JsonProperty("validation_file")
    private String validationFile;
}
