package com.unfbx.chatgpt.entity.images;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Getter
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Image implements Serializable {

    @NonNull
    private String prompt;
    /**
     * 为每个提示生成的完成次数。
     */
    @Builder.Default
    private Integer n = 1;
    /**
     * 256x256
     * 512x512
     * 1024x1024
     */
    @Builder.Default
    private String size = SizeEnum.size_512.getName();

    @JsonProperty("response_format")
    @Builder.Default
    private String responseFormat = ResponseFormat.URL.getName();

    private String user;

    public void setN(Integer n) {
        if(n < 1){
            log.warn("n最小值1");
            this.n = 1;
            return;
        }
        if(n > 10){
            log.warn("n最大值10");
            this.n = 10;
            return;
        }
        this.n = n;
    }

    public void setPrompt(String prompt) {
        if(Objects.isNull(prompt) || "".equals(prompt)){
            log.error("参数异常");
            throw new BaseException(CommonError.PARAM_ERROR);
        }
        if(prompt.length() > 1000){
            log.error("长度超过1000");
            throw new BaseException(CommonError.PARAM_ERROR);
        }
        this.prompt = prompt;
    }

    public void setSize(SizeEnum size) {
        if(Objects.isNull(size)){
            size = SizeEnum.size_512;
        }
        this.size = size.getName();
    }

    public void setResponseFormat(ResponseFormat responseFormat) {
        if(Objects.isNull(responseFormat)){
            responseFormat = ResponseFormat.URL;
        }
        this.responseFormat = responseFormat.getName();
    }

    public void setUser(String user) {
        this.user = user;
    }

}
