package com.unfbx.chatgpt.entity.moderations;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @date 2023-02-15
 */
@Data
public class CategoryScores {
    private BigDecimal hate;
    @JsonProperty("hate/threatening")
    private BigDecimal hateThreatening;
    @JsonProperty("self-harm")
    private BigDecimal selfHarm;
    private BigDecimal sexual;
    @JsonProperty("sexual/minors")
    private BigDecimal sexualMinors;
    private BigDecimal violence;
    @JsonProperty("violence/graphic")
    private BigDecimal violenceGraphic;

}
