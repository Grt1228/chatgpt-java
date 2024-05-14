package com.unfbx.chatgpt.interceptor;

import lombok.Getter;
import lombok.Setter;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author skywalker
 * @since 2023/5/7 17:22
 */
public class AzureOpenAiApiVersionInterceptor implements Interceptor {


    @Getter
    @Setter
    private String apiVersion;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        HttpUrl originalHttpUrl = original.url();
        HttpUrl url = originalHttpUrl.newBuilder()
                .addQueryParameter("api-version", apiVersion)
                .build();
        Request.Builder requestBuilder = original.newBuilder()
                .url(url);
        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
