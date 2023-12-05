package com.unfbx.chatgpt.interceptor;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import lombok.NoArgsConstructor;
import okhttp3.Request;

/**
 * azure另一种鉴权方式，通过api-key
 * 文档：
 * https://learn.microsoft.com/zh-cn/azure/cognitive-services/openai/reference
 *
 * @author skywalker
 * @since 2023/5/7 17:22
 */
@NoArgsConstructor
public class AzureDynamicKeyOpenAiAuthInterceptor extends DynamicKeyOpenAiAuthInterceptor {

    /**
     * 默认的鉴权处理方法
     *
     * @param key      api key
     * @param original 源请求体
     * @return 请求体
     */
    @Override
    public Request auth(String key, Request original) {
        Request request = original.newBuilder()
            .header("api-key", key)
            .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
            .method(original.method(), original.body())
            .build();
        return request;
    }
}
