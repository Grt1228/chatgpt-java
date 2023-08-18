package com.unfbx.chatgpt;

import com.unfbx.chatgpt.plugin.PluginAbstract;


public class WeatherPlugin extends PluginAbstract<WeatherReq, WeatherResp> {

    public WeatherPlugin(Class<?> r) {
        super(r);
    }

    @Override
    public WeatherResp func(WeatherReq args) {
        WeatherResp weatherResp = new WeatherResp();
        weatherResp.setTemp("25到28摄氏度");
        weatherResp.setLevel(3);
        return weatherResp;
    }

    @Override
    public String content(WeatherResp weatherResp) {
        return "当前天气温度：" + weatherResp.getTemp() + "，风力等级：" + weatherResp.getLevel();
    }
}
