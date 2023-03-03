package com.unfbx.chatgpt;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * 描述： open ai 客户端
 *
 * @author https:www.unfbx.com
 * 2023-02-28
 */

@Slf4j
public class OpenAiStreamClient {
    @Getter
    @NotNull
    private String apiKey;
    @Getter
    private OkHttpClient okHttpClient;
    /**
     * 连接超时
     */
    @Getter
    private long connectTimeout;
    /**
     * 写超时
     */
    @Getter
    private long writeTimeout;
    /**
     * 读超时
     */
    @Getter
    private long readTimeout;
    /**
     * okhttp 代理
     */
    @Getter
    private Proxy proxy;

    /**
     * 创建OpenAiClient，自定义OkHttpClient
     *
     * @param apiKey         key
     * @param connectTimeout 连接超时时间 单位秒
     * @param writeTimeout   写超时 单位秒
     * @param readTimeout    超时 单位秒
     * @param proxy          代理
     */
    public OpenAiStreamClient(String apiKey, long connectTimeout, long writeTimeout, long readTimeout, Proxy proxy) {
        this.apiKey = apiKey;
        this.connectTimeout = connectTimeout;
        this.writeTimeout = writeTimeout;
        this.readTimeout = readTimeout;
        this.proxy = proxy;
        this.okHttpClient(connectTimeout, writeTimeout, readTimeout, proxy);
    }

    /**
     * 创建OpenAiClient，自定义OkHttpClient
     *
     * @param apiKey         key
     * @param connectTimeout 连接超时时间 单位秒
     * @param writeTimeout   写超时 单位秒
     * @param readTimeout    超时 单位秒
     */
    public OpenAiStreamClient(String apiKey, long connectTimeout, long writeTimeout, long readTimeout) {
        this.apiKey = apiKey;
        this.connectTimeout = connectTimeout;
        this.writeTimeout = writeTimeout;
        this.readTimeout = readTimeout;
        this.okHttpClient(connectTimeout, writeTimeout, readTimeout, null);
    }

    /**
     * 创建OpenAiClient，使用默认的超时时间
     * 注意当超时时间过短，长的文本输出问答系统可能超时。
     *
     * @param apiKey key
     */
    public OpenAiStreamClient(String apiKey) {
        this.apiKey = apiKey;
        this.okHttpClient(30, 30, 30, null);
    }

    /**
     * 创建OpenAiClient，使用默认的超时时间
     * 注意当超时时间过短，长的文本输出问答系统可能超时。
     *
     * @param apiKey key
     * @param proxy  网络代理
     */
    public OpenAiStreamClient(String apiKey, Proxy proxy) {
        this.apiKey = apiKey;
        this.proxy = proxy;
        this.okHttpClient(30, 30, 30, proxy);
    }

    private OpenAiStreamClient(Builder builder) {
        if (StrUtil.isBlank(builder.apiKey)) {
            throw new BaseException(CommonError.API_KEYS_NOT_NUL);
        }
        apiKey = builder.apiKey;

        if (Objects.isNull(builder.connectTimeout)) {
            builder.connectTimeout(30);
        }
        connectTimeout = builder.connectTimeout;

        if (Objects.isNull(builder.writeTimeout)) {
            builder.writeTimeout(30);
        }
        writeTimeout = builder.writeTimeout;

        if (Objects.isNull(builder.readTimeout)) {
            builder.readTimeout(30);
        }
        readTimeout = builder.readTimeout;
        proxy = builder.proxy;
        this.okHttpClient(connectTimeout, writeTimeout, readTimeout, proxy);

    }

    /**
     * 创建 OkHttpClient，自定义超时时间和拦截器
     *
     * @param connectTimeout 默认30秒
     * @param writeTimeout   默认30秒
     * @param readTimeout    默认30秒
     */
    private void okHttpClient(long connectTimeout, long writeTimeout, long readTimeout, Proxy proxy) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        client.writeTimeout(writeTimeout, TimeUnit.SECONDS);
        client.readTimeout(readTimeout, TimeUnit.SECONDS);
        if (Objects.nonNull(proxy)) {
            client.proxy(proxy);
        }
        this.okHttpClient = client.build();
    }

    /**
     * 问答接口 stream 形式
     *
     * @param completion          open ai 参数
     * @param eventSourceListener sse监听器
     * @see ConsoleEventSourceListener
     */
    public void streamCompletions(Completion completion, EventSourceListener eventSourceListener) {
        if (Objects.isNull(eventSourceListener)) {
            log.error("参数异常：EventSourceListener不能为空，可以参考：com.unfbx.chatgpt.sse.ConsoleEventSourceListener");
            throw new BaseException(CommonError.PARAM_ERROR);
        }
        if (StrUtil.isBlank(completion.getPrompt())) {
            log.error("参数异常：Prompt不能为空");
            throw new BaseException(CommonError.PARAM_ERROR);
        }
        if (!completion.isStream()) {
            completion.setStream(true);
        }
        try {
            EventSource.Factory factory = EventSources.createFactory(this.okHttpClient);
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(completion);
            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/completions")
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody))
                    .header("Authorization", "Bearer " + this.apiKey)
                    .build();
            //创建事件
            EventSource eventSource = factory.newEventSource(request, eventSourceListener);
        } catch (JsonProcessingException e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
        } catch (Exception e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
        }
    }

    /**
     * 问答接口-简易版
     *
     * @param question            请求参数
     * @param eventSourceListener sse监听器
     * @see ConsoleEventSourceListener
     */
    public void streamCompletions(String question, EventSourceListener eventSourceListener) {
        Completion q = Completion.builder()
                .prompt(question)
                .stream(true)
                .build();
        this.streamCompletions(q, eventSourceListener);
    }

    /**
     * 流式输出，最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param chatCompletion      问答参数
     * @param eventSourceListener sse监听器
     * @see ConsoleEventSourceListener
     */
    public void streamChatCompletion(ChatCompletion chatCompletion, EventSourceListener eventSourceListener) {
        if (Objects.isNull(eventSourceListener)) {
            log.error("参数异常：EventSourceListener不能为空，可以参考：com.unfbx.chatgpt.sse.ConsoleEventSourceListener");
            throw new BaseException(CommonError.PARAM_ERROR);
        }
        if (!chatCompletion.isStream()) {
            chatCompletion.setStream(true);
        }
        try {
            EventSource.Factory factory = EventSources.createFactory(this.okHttpClient);
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(chatCompletion);
            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody))
                    .header("Authorization", "Bearer " + this.apiKey)
                    .build();
            //创建事件
            EventSource eventSource = factory.newEventSource(request, eventSourceListener);
        } catch (JsonProcessingException e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
        } catch (Exception e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
        }
    }

    /**
     * 流式输出，最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param messages            问答列表
     * @param eventSourceListener sse监听器
     * @see ConsoleEventSourceListener
     */
    public void streamChatCompletion(List<Message> messages, EventSourceListener eventSourceListener) {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(messages)
                .stream(true)
                .build();
        this.streamChatCompletion(chatCompletion, eventSourceListener);
    }

    /**
     * 构造
     *
     * @return
     */
    public static OpenAiStreamClient.Builder builder() {
        return new OpenAiStreamClient.Builder();
    }

    public static final class Builder {
        private @NotNull String apiKey;
        private long connectTimeout;
        private long writeTimeout;
        private long readTimeout;
        private Proxy proxy;

        public Builder() {
        }

        public Builder apiKey(@NotNull String val) {
            apiKey = val;
            return this;
        }
        public Builder connectTimeout(long val) {
            connectTimeout = val;
            return this;
        }

        public Builder writeTimeout(long val) {
            writeTimeout = val;
            return this;
        }

        public Builder readTimeout(long val) {
            readTimeout = val;
            return this;
        }

        public Builder proxy(Proxy val) {
            proxy = val;
            return this;
        }

        public OpenAiStreamClient build() {
            return new OpenAiStreamClient(this);
        }
    }
}
