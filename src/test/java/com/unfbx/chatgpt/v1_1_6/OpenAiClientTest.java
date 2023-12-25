package com.unfbx.chatgpt.v1_1_6;


import com.unfbx.chatgpt.FirstKeyStrategy;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.OpenAiStreamClient;
import com.unfbx.chatgpt.entity.chat.*;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.DynamicKeyOpenAiAuthInterceptor;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
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
 * 描述： 测试类
 *
 * @author https:www.unfbx.com
 * 2023-12-25
 */
@Slf4j
public class OpenAiClientTest {

    private OpenAiClient client;
    private OpenAiStreamClient streamClient;


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
                .apiKey(Arrays.asList("sk-*************************************"))
                //自定义key的获取策略：默认KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new FirstKeyStrategy())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传,(关注公众号回复：openai ，获取免费的测试代理地址)
                .apiHost("https://*************/")
                .build();

        streamClient = OpenAiStreamClient.builder()
                //支持多key传入，请求时候随机选择
                .apiKey(Arrays.asList("sk-*************************************"))
                //自定义key的获取策略：默认KeyRandomStrategy
                .keyStrategy(new KeyRandomStrategy())
                .authInterceptor(new DynamicKeyOpenAiAuthInterceptor())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传,(关注公众号回复：openai ，获取免费的测试代理地址)
                .apiHost("https://*********/")
                .build();
    }

    /**
     * 聊天模型支持图片流式示例
     */
    @Test
    public void pictureChat() {
        Content textContent = Content.builder().text("What’s in this image?").type(Content.Type.TEXT.getName()).build();
        ImageUrl imageUrl = ImageUrl.builder().url("https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Gfp-wisconsin-madison-the-nature-boardwalk.jpg/2560px-Gfp-wisconsin-madison-the-nature-boardwalk.jpg").build();
        Content imageContent = Content.builder().imageUrl(imageUrl).type(Content.Type.IMAGE_URL.getName()).build();
        List<Content> contentList = new ArrayList<>();
        contentList.add(textContent);
        contentList.add(imageContent);
        MessagePicture message = MessagePicture.builder().role(Message.Role.USER).content(contentList).build();
        ChatCompletionWithPicture chatCompletion = ChatCompletionWithPicture
                .builder()
                .messages(Collections.singletonList(message))
                .model(ChatCompletion.Model.GPT_4_VISION_PREVIEW.getName())
                .build();
        ChatCompletionResponse chatCompletionResponse = client.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices().forEach(e -> System.out.println(e.getMessage()));
    }


    @Test
    public void chat() {
        //聊天模型：gpt-3.5
        Message message = Message.builder().role(Message.Role.USER).content("你好啊我的伙伴！").build();
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .messages(Collections.singletonList(message))
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .logprobs(true)
                .topLogprobs(2)
                .build();
        ChatCompletionResponse chatCompletionResponse = client.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices().forEach(e -> {
            System.out.println(e.getMessage());
        });
    }


    @Test
    public void chatCompletions() {
        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener();
        Message message = Message.builder().role(Message.Role.USER).content("random one word！").build();
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .model(ChatCompletion.Model.GPT_4_1106_PREVIEW.getName())
                .temperature(0.2)
                .maxTokens(2048)
                .messages(Collections.singletonList(message))
                .stream(true)
                .logprobs(true)
                .topLogprobs(2)
                .build();
        streamClient.streamChatCompletion(chatCompletion, eventSourceListener);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
