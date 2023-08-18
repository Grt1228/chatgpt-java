package com.unfbx.chatgpt;

import lombok.Data;

@Data
public class WeatherResp {
    /**
     * 温度
     */
    private String temp;
    /**
     * 风力等级
     */
    private Integer level;
}
