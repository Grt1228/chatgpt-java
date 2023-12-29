package com.unfbx.chatgpt.interceptor;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import lombok.Getter;
import lombok.Setter;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author skywalker
 * @since 2023/5/7 17:22
 */
public class  AzureOpenAiAuthInterceptor  implements Interceptor {


    @Getter
    @Setter
    private List<String> apiKeys;

    @Getter
    @Setter
    private Map<String,Integer> cacheMap;


    public Request auth(String key, Request original) {

        Request request = original.newBuilder()
                .header("api-key",key)
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .method(original.method(), original.body())
                .build();
        return request;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        return chain.proceed(auth(this.getKey(), original));
    }

    private String getKey() {
        Integer count = this.cacheMap.get("cacheIndex");
        return this.apiKeys.get(count.intValue());
    }


}
