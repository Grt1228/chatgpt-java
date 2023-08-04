package com.unfbx.chatgpt.entity.moderations;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Categories implements Serializable {
    /**
     * 表达、煽动或宣扬基于种族、性别、民族、宗教、国籍、性取向、残疾状况或种姓的仇恨的内容。
     */
    private boolean hate;
    /**
     * 仇恨内容，还包括对目标群体的暴力或严重伤害。
     */
    @JsonProperty("hate/threatening")
    private boolean hateThreatening;
    /**
     * 宣扬、鼓励或描绘自残行为（例如自杀、割伤和饮食失调）的内容。
     */
    @JsonProperty("self-harm")
    private boolean selfHarm;
    /**
     * 旨在引起性兴奋的内容，例如对性活动的描述，或宣传性服务（不包括性教育和健康）的内容。
     */
    private boolean sexual;
    /**
     * 包含未满 18 周岁的个人的色情内容。
     */
    @JsonProperty("sexual/minors")
    private boolean sexualMinors;
    /**
     * 宣扬或美化暴力或歌颂他人遭受苦难或羞辱的内容。
     */
    private boolean violence;
    /**
     * 以极端血腥细节描绘死亡、暴力或严重身体伤害的暴力内容。
     */
    @JsonProperty("violence/graphic")
    private boolean violenceGraphic;
    /**
     * 对任何目标表达、煽动或宣扬骚扰性语言的内容。
     */
    @JsonProperty("harassment")
    private boolean harassment;
    /**
     * 骚扰内容还包括对任何目标的暴力或严重伤害。
     */
    @JsonProperty("harassment/threatening")
    private boolean harassmentThreatening;
    /**
     * 说话者表示他们正在或打算进行自残行为的内容，例如自杀、割伤和饮食失调。
     */
    @JsonProperty("self-harm/intent")
    private boolean selfHarmIntent;
    /**
     * 鼓励进行自残行为（例如自杀、割伤和饮食失调）的内容，或者提供有关如何实施此类行为的说明或建议的内容。
     */
    @JsonProperty("self-harm/instructions")
    private boolean selfHarmInstructions;
}
