package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 描述： 测试类
 *
 * @author Nixer
 *  2023-02-11
 */
@Slf4j
public class OpenAzureAiClientTest {

    private OpenAiAzureStreamClient v3;

    @Before
    public void before() {
        //可以为null
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！！千万别再生产或者测试环境打开BODY级别日志！！！！
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        List<String> apikeys  =Arrays.asList("ef*********************************");
        List<String> apiHost = Arrays.asList("https://gptservice01.openai.azure");
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
//                .proxy(proxy)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new OpenAiResponseInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        v3 = OpenAiAzureStreamClient.builder()
                .apiKey(apikeys)
                .okHttpClient(okHttpClient)
                .apiHost(apiHost)
                .build();
    }

    @Test
    public void chatCompletions() {
        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener();
        Message message = Message.builder().role(Message.Role.USER).content("random one word！").build();
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .temperature(0.2)
                .maxTokens(2048)
                .messages(Collections.singletonList(message))
                .stream(true)
                .build();
        for (int i = 0; i < 2; i++) {
            v3.streamAzureChatCompletion(chatCompletion, eventSourceListener,null);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



/*    @Test
    public void completions() {
        CompletionResponse completions = v3.completions("什么?");
        (completions.getChoices()).forEach(System.out::println);
    }*/
}
