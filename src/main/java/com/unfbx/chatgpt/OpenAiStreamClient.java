package com.unfbx.chatgpt;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;

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
    private final String apiKey;

    private OkHttpClient okHttpClient;

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
        this.okHttpClient(connectTimeout, writeTimeout, readTimeout);
    }

    /**
     * 创建OpenAiClient，使用默认的超时时间
     * 注意当超时时间过短，长的文本输出问答系统可能超时。
     *
     * @param apiKey key
     */
    public OpenAiStreamClient(String apiKey) {
        this.apiKey = apiKey;
        this.okHttpClient();
    }


    /**
     * 创建 OkHttpClient，默认超时时间30秒
     */
    private void okHttpClient() {
        this.okHttpClient(30, 30, 30);
    }


    /**
     * 创建 OkHttpClient，自定义超时时间和拦截器
     *
     * @param connectTimeout 默认30秒
     * @param writeTimeout   默认30秒
     * @param readTimeout    默认30秒
     */
    private void okHttpClient(long connectTimeout, long writeTimeout, long readTimeout) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        client.writeTimeout(writeTimeout, TimeUnit.SECONDS);
        client.readTimeout(readTimeout, TimeUnit.SECONDS);
        this.okHttpClient = client.build();
    }

    /**
     * 问答接口 stream 形式
     *
     * @param completion          open ai 参数
     * @param eventSourceListener sse监听器
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
     * @param question              请求参数
     */
    public void streamCompletions(String question, EventSourceListener eventSourceListener) {
        Completion q = Completion.builder()
                .prompt(question)
                .stream(true)
                .build();
        this.streamCompletions(q, eventSourceListener);
    }
}
