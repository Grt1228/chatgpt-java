package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.chat.*;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import com.unfbx.chatgpt.plugin.PluginAbstract;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


/**
 * 描述： 插件测试类
 *
 * @author https:www.unfbx.com
 * 2023-08-18
 */
@Slf4j
public class PluginTest {

    private OpenAiClient openAiClient;
    private OpenAiStreamClient openAiStreamClient;

    @Before
    public void before() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！！千万别再生产或者测试环境打开BODY级别日志！！！！
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new OpenAiResponseInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        openAiClient = OpenAiClient.builder()
                .okHttpClient(okHttpClient)
                .apiKey(Arrays.asList("sk-********************************"))
                .apiHost("https://dgr.life/")
                .build();
        openAiStreamClient = OpenAiStreamClient.builder()
                //支持多key传入，请求时候随机选择
                .apiKey(Arrays.asList("sk-********************************"))
                .apiHost("https://dgr.life/")
                .build();
    }


    @Test
    public void plugin() {
        WeatherPlugin plugin = new WeatherPlugin(WeatherReq.class);
        plugin.setName("知心天气");
        plugin.setFunction("getLocationWeather");
        plugin.setDescription("提供一个地址，方法将会获取该地址的天气的实时温度信息。");
        PluginAbstract.Arg arg = new PluginAbstract.Arg();
        arg.setName("location");
        arg.setDescription("地名");
        arg.setType("string");
        arg.setRequired(true);
        plugin.setArgs(Collections.singletonList(arg));

//        Message message1 = Message.builder().role(Message.Role.USER).content("秦始皇统一了哪六国。").build();
        Message message2 = Message.builder().role(Message.Role.USER).content("获取上海市的天气现在多少度，然后再给出3个推荐的户外运动。").build();
        List<Message> messages = new ArrayList<>();
//        messages.add(message1);
        messages.add(message2);
        //默认模型：GPT_3_5_TURBO_16K_0613
        //有四个重载方法，都可以使用
        ChatCompletionResponse response = openAiClient.chatCompletionWithPlugin(messages, plugin);
        log.info("自定义的方法返回值：{}", response.getChoices().get(0).getMessage().getContent());
    }


    @Test
    public void streamPlugin() {
        WeatherPlugin plugin = new WeatherPlugin(WeatherReq.class);
        plugin.setName("知心天气");
        plugin.setFunction("getLocationWeather");
        plugin.setDescription("提供一个地址，方法将会获取该地址的天气的实时温度信息。");
        PluginAbstract.Arg arg = new PluginAbstract.Arg();
        arg.setName("location");
        arg.setDescription("地名");
        arg.setType("string");
        arg.setRequired(true);
        plugin.setArgs(Collections.singletonList(arg));

//        Message message1 = Message.builder().role(Message.Role.USER).content("秦始皇统一了哪六国。").build();
        Message message2 = Message.builder().role(Message.Role.USER).content("获取上海市的天气现在多少度，然后再给出3个推荐的户外运动。").build();
        List<Message> messages = new ArrayList<>();
//        messages.add(message1);
        messages.add(message2);
        //默认模型：GPT_3_5_TURBO_16K_0613
        //有四个重载方法，都可以使用
        openAiStreamClient.streamChatCompletionWithPlugin(messages, new ConsoleEventSourceListener(), plugin);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
