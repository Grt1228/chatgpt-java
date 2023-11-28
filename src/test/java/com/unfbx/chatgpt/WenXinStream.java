package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.chat.wenxin.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.wenxin.Message;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class WenXinStream {
    public static void main(String[] args) {
        //国内访问需要做代理，国外服务器不需要
        //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                //.proxy(proxy)//自定义代理
                .addInterceptor(httpLoggingInterceptor)//自定义日志输出
                .addInterceptor(new OpenAiResponseInterceptor())//自定义返回值拦截
                .connectTimeout(10, TimeUnit.SECONDS)//自定义超时时间
                .writeTimeout(30, TimeUnit.SECONDS)//自定义超时时间
                .readTimeout(30, TimeUnit.SECONDS)//自定义超时时间
                .build();

        WenXinStreamClient client = WenXinStreamClient.builder()
                .apiKey(Arrays.asList("24.ffc3e99e9fd85a7d275a43b6eb71651e.2592000.1702114689.xxx"))
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传
//                .apiHost("https://自己代理的服务器地址/")
                .build();

        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener();
        Message message = Message.builder().role(Message.Role.USER).content("你好").build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
        client.streamChatCompletion(chatCompletion,eventSourceListener);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
