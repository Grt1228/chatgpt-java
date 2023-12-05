package com.unfbx.chatgpt.interceptor;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import lombok.NoArgsConstructor;
import okhttp3.Request;

/**
 * @author skywalker
 * @since 2023/5/7 17:22
 */
@NoArgsConstructor
public class AzureOpenAiAuthInterceptor extends DefaultOpenAiAuthInterceptor {
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
