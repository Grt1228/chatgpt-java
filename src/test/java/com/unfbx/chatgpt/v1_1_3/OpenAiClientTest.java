package com.unfbx.chatgpt.v1_1_3;


import com.unfbx.chatgpt.FirstKeyStrategy;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.assistant.Assistant;
import com.unfbx.chatgpt.entity.assistant.AssistantResponse;
import com.unfbx.chatgpt.entity.assistant.Tool;
import com.unfbx.chatgpt.entity.chat.*;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.DynamicKeyOpenAiAuthInterceptor;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 描述： 测试类
 *
 * @author https:www.unfbx.com
 * 2023-11-15
 */
@Slf4j
public class OpenAiClientTest {

    private OpenAiClient client;

    @Before
    public void before() {
        //可以为null
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！！千万别再生产或者测试环境打开BODY级别日志！！！！
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
//                .proxy(proxy)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new OpenAiResponseInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        client = OpenAiClient.builder()
                //支持多key传入，请求时候随机选择
                .apiKey(Arrays.asList("*********************"))
                //自定义key的获取策略：默认KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new FirstKeyStrategy())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传,(关注公众号回复：openai ，获取免费的测试代理地址)
                .apiHost("https://*********************/")
                .build();
    }

    /**
     * 聊天模型支持图片流式示例
     */
    @Test
    public void pictureChat() {
        Tool tool = Tool.builder().type(Tool.Type.CODE_INTERPRETER.getName()).build();
        Assistant assistant = Assistant.builder()
                .model(BaseChatCompletion.Model.GPT_3_5_TURBO_16K_0613.getName())
                .name("UnfbxBot")
                .description("UnfbxBot是一个自定义数学bot。")
                .instructions("你是一个数学导师。当我问你问题时，编写并运行Java代码来回答问题。")
                .tools(Collections.singletonList(tool))
//                .fileIds()
                .metadata(new HashMap())
                .build();
        AssistantResponse assistants = client.assistants(assistant);
        System.out.println(assistants.getId());
    }

}
