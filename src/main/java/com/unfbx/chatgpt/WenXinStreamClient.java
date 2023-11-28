package com.unfbx.chatgpt;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.constant.OpenAIConst;
import com.unfbx.chatgpt.constant.WenXinConst;
import com.unfbx.chatgpt.entity.chat.wenxin.ChatCompletion;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.function.KeyStrategyFunction;
import com.unfbx.chatgpt.interceptor.*;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

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
public class WenXinStreamClient {
    @Getter
    @NotNull
    private List<String> apiKey;
    /**
     * 自定义api host使用builder的方式构造client
     */
    @Getter
    private String apiHost;
    /**
     * 自定义的okHttpClient
     * 如果不自定义 ，就是用sdk默认的OkHttpClient实例
     */
    @Getter
    private OkHttpClient okHttpClient;

    /**
     * api key的获取策略
     */
    @Getter
    private KeyStrategyFunction<List<String>, String> keyStrategy;

    @Getter
    private WenXinApi wenXinApi;

    /**
     * 自定义鉴权处理拦截器<br/>
     * 可以不设置，默认实现：DefaultOpenAiAuthInterceptor <br/>
     * 如需自定义实现参考：DealKeyWithOpenAiAuthInterceptor
     *
     * @see DynamicKeyOpenAiAuthInterceptor
     * @see DefaultOpenAiAuthInterceptor
     */
    @Getter
    private WenXinAuthInterceptor authInterceptor;

    /**
     * 构造实例对象
     *
     * @param builder
     */
    private WenXinStreamClient(Builder builder) {
        if (CollectionUtil.isEmpty(builder.apiKey)) {
            throw new BaseException(CommonError.API_KEYS_NOT_NUL);
        }
        apiKey = builder.apiKey;

        if (StrUtil.isBlank(builder.apiHost)) {
            builder.apiHost = WenXinConst.WenXin_HOST;
        }
        apiHost = builder.apiHost;

        if (Objects.isNull(builder.keyStrategy)) {
            builder.keyStrategy = new KeyRandomStrategy();
        }
        keyStrategy = builder.keyStrategy;

        if (Objects.isNull(builder.authInterceptor)) {
            builder.authInterceptor = new DefaultWenXinAuthInterceptor();
        }
        authInterceptor = builder.authInterceptor;
        //设置apiKeys和key的获取策略
        authInterceptor.setApiKey(this.apiKey);
        authInterceptor.setKeyStrategy(this.keyStrategy);

        if (Objects.isNull(builder.okHttpClient)) {
            builder.okHttpClient = this.okHttpClient();
        } else {
            //自定义的okhttpClient  需要增加api keys
            builder.okHttpClient = builder.okHttpClient
                    .newBuilder()
                    .addInterceptor(authInterceptor)
                    .build();
        }
        okHttpClient = builder.okHttpClient;

        this.wenXinApi = new Retrofit.Builder()
                .baseUrl(apiHost)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(WenXinApi.class);
    }

    /**
     * 创建默认的OkHttpClient
     */
    private OkHttpClient okHttpClient() {
        if (Objects.isNull(this.authInterceptor)) {
            this.authInterceptor = new DefaultWenXinAuthInterceptor();
        }
        this.authInterceptor.setApiKey(this.apiKey);
        this.authInterceptor.setKeyStrategy(this.keyStrategy);
        return new OkHttpClient
                .Builder()
                .addInterceptor(this.authInterceptor)
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();
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
                    .url(this.apiHost + "rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions")
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody))
                    .build();
            //创建事件
            factory.newEventSource(request, eventSourceListener);
        } catch (JsonProcessingException e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
        } catch (Exception e) {
            log.error("请求参数解析异常：{}", e);
            e.printStackTrace();
        }
    }


    /**
     * 构造
     *
     * @return Builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private @NotNull List<String> apiKey;
        /**
         * api请求地址，结尾处有斜杠
         *
         * @see OpenAIConst
         */
        private String apiHost;

        /**
         * 自定义OkhttpClient
         */
        private OkHttpClient okHttpClient;


        /**
         * api key的获取策略
         */
        private KeyStrategyFunction keyStrategy;

        /**
         * 自定义鉴权拦截器
         */
        private WenXinAuthInterceptor authInterceptor;

        public Builder() {
        }

        public Builder apiKey(@NotNull List<String> val) {
            apiKey = val;
            return this;
        }

        /**
         * @param val api请求地址，结尾处有斜杠
         * @return Builder
         * @see OpenAIConst
         */
        public Builder apiHost(String val) {
            apiHost = val;
            return this;
        }

        public Builder keyStrategy(KeyStrategyFunction val) {
            keyStrategy = val;
            return this;
        }

        public Builder okHttpClient(OkHttpClient val) {
            okHttpClient = val;
            return this;
        }

        public Builder authInterceptor(WenXinAuthInterceptor val) {
            authInterceptor = val;
            return this;
        }

        public WenXinStreamClient build() {
            return new WenXinStreamClient(this);
        }
    }
}
