package com.unfbx.chatgpt;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unfbx.chatgpt.constant.OpenAIConst;
import com.unfbx.chatgpt.entity.chat.BaseChatCompletion;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgpt.interceptor.AzureOpenAiApiVersionInterceptor;
import com.unfbx.chatgpt.interceptor.AzureOpenAiAuthInterceptor;
import com.unfbx.chatgpt.interceptor.DefaultOpenAiAuthInterceptor;
import com.unfbx.chatgpt.interceptor.DynamicKeyOpenAiAuthInterceptor;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import io.reactivex.Single;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * 描述： open ai 客户端
 *
 * @author https:www.unfbx.com
 * 2023-02-28
 */

@Slf4j
public class OpenAiAzureStreamClient {
    @Getter
    @NotNull
    private List<String> apiKey;
    /**
     * 自定义api host使用builder的方式构造client
     */
    @Getter
    private List<String> apiHost;

    @Getter
    private Integer apiHostIndex;

    @Getter
    private Map<String,Integer> cacheMap ;

    @Getter
    private String apiVerison;


//    @Getter
//    private Map<Integer,Long> auditCacheMap ;
//
//    @Getter
//    private Map<Integer,Long> timeCacheMap ;
//
//
//    @Getter
//    private Long exceededTokenlimit;
//
//    @Getter
//    private Long auditToken;

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
    private List<AzureOpenAiApi> azureOpenAiApis;

    /**
     * 自定义鉴权处理拦截器<br/>
     * 可以不设置，默认实现：DefaultOpenAiAuthInterceptor <br/>
     * 如需自定义实现参考：DealKeyWithOpenAiAuthInterceptor
     *
     * @see DynamicKeyOpenAiAuthInterceptor
     * @see DefaultOpenAiAuthInterceptor
     */
    @Getter
    private AzureOpenAiAuthInterceptor azureOpenAiAuthInterceptor;

    @Getter
    private AzureOpenAiApiVersionInterceptor apiVersionInterceptor;

    /**
     * 构造实例对象
     *
     * @param builder
     */
    private OpenAiAzureStreamClient(Builder builder) {
        if (CollectionUtil.isEmpty(builder.apiKey)) {
            throw new BaseException(CommonError.API_KEYS_NOT_NUL);
        }
        apiKey = builder.apiKey;

        if (CollectionUtil.isEmpty(builder.apiHost)) {
            builder.apiHost = Arrays.asList(OpenAIConst.OPENAI_HOST);
        }
        apiHost = builder.apiHost;
        apiHostIndex = 0;
        cacheMap = new HashMap();
//        auditCacheMap = new HashMap<>();
//        timeCacheMap   =   new HashMap<>();
        cacheMap.put("cacheIndex", apiHostIndex);


        if(StrUtil.isEmpty(builder.apiVerison)){
            builder.apiVerison =  "2023-07-01-preview";
        }
        apiVerison = builder.apiVerison;

        builder.azureOpenAiAuthInterceptor = new AzureOpenAiAuthInterceptor();
        azureOpenAiAuthInterceptor = builder.azureOpenAiAuthInterceptor;
        //设置apiKeys和key的获取策略
        azureOpenAiAuthInterceptor.setApiKeys(this.apiKey);
        azureOpenAiAuthInterceptor.setCacheMap(this.cacheMap);

        apiVersionInterceptor = new AzureOpenAiApiVersionInterceptor();
        apiVersionInterceptor.setApiVersion(this.apiVerison);


//        if(ObjectUtil.isEmpty(builder.exceededTokenlimit)){
//            builder.exceededTokenlimit =  0l;
//        }
//        exceededTokenlimit = builder.exceededTokenlimit;
//
//        if(ObjectUtil.isEmpty(builder.auditToken)){
//            builder.auditToken =  8000l;
//        }
//        auditToken = builder.auditToken;

        if (Objects.isNull(builder.okHttpClient)) {
            builder.okHttpClient = this.okHttpClient();
        } else {
            builder.okHttpClient = builder.okHttpClient
                    .newBuilder()
                    .addInterceptor(apiVersionInterceptor)
                    .addInterceptor(azureOpenAiAuthInterceptor)
                    .build();
        }

        okHttpClient = builder.okHttpClient;


        this.azureOpenAiApis = new ArrayList<>();
        for (int i = 0;i< apiHost.size();i++) {
            Retrofit.Builder builderx = new Retrofit.Builder()
                    .baseUrl(apiHost.get(i))
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(JacksonConverterFactory.create());
            azureOpenAiApis.add(builderx.build().create(AzureOpenAiApi.class));
        }
    }

    /**
     * 创建默认的OkHttpClient
     */
    private OkHttpClient okHttpClient() {
        if (Objects.isNull(this.azureOpenAiAuthInterceptor)) {
            this.azureOpenAiAuthInterceptor = new AzureOpenAiAuthInterceptor();
        }
        this.azureOpenAiAuthInterceptor.setApiKeys(this.apiKey);
        this.azureOpenAiAuthInterceptor.setCacheMap(this.cacheMap);
        this.apiVersionInterceptor.setApiVersion(this.apiVerison);

        return new OkHttpClient
                .Builder()
                .addInterceptor(this.apiVersionInterceptor)
                .addInterceptor(this.azureOpenAiAuthInterceptor)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();
    }
    //这个是给rifet的
    public CompletionResponse completions(String question) {
        Completion q = Completion.builder()
                .prompt(question)
                .build();
        Single<CompletionResponse> completions = this.azureOpenAiApis.get(apiHostIndex).completions(q);
        return completions.blockingGet();
    }

    /**
     * 流式输出，最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param chatCompletion      问答参数
     * @param eventSourceListener sse监听器
     * @see ConsoleEventSourceListener
     */
    public <T extends BaseChatCompletion> void streamAzureChatCompletion(T chatCompletion, EventSourceListener eventSourceListener,String tagid) {
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
                    .tag(tagid)
                    .url(getApiHostUrl() + "chat/completions")
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()), requestBody))
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

    private String getApiHostUrl() {
        SequentialKey();
        return this.apiHost.get(this.apiHostIndex);
    }
    private Integer SequentialKey() {
      //当前index值+1使用如果当前值已经超过最大值就从头开始
        if(this.apiHostIndex<this.apiHost.size()-1){
            this.apiHostIndex++;
        }else{
            this.apiHostIndex = 0;
        }
        this.cacheMap.put("cacheIndex", this.apiHostIndex);
        return this.apiHostIndex;
    }
    /**
     * 构造
     *
     * @return Builder
     */
    public static OpenAiAzureStreamClient.Builder builder() {
        return new OpenAiAzureStreamClient.Builder();
    }

    public static final class Builder {
        private @NotNull List<String> apiKey;
        /**
         * api请求地址，结尾处有斜杠
         *
         * @see OpenAIConst
         */
        private List<String> apiHost;

//        private Long exceededTokenlimit;
//
//        private Long auditToken;

        private String apiVerison;


        private OkHttpClient okHttpClient;

        /**
         * 自定义鉴权拦截器
         */
        private AzureOpenAiAuthInterceptor azureOpenAiAuthInterceptor;

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
        public Builder apiHost(@NotNull List<String> val) {
            apiHost = val;
            return this;
        }


        public Builder apiVerison(@NotNull String val) {
            apiVerison = val;
            return this;
        }

//        public Builder exceededTokenlimit(Long val) {
//            exceededTokenlimit = val;
//            return this;
//        }
//        public Builder auditToken(Long val) {
//            auditToken = val;
//            return this;
//        }
        public Builder okHttpClient(OkHttpClient val) {
            okHttpClient = val;
            return this;
        }

        public Builder authInterceptor(AzureOpenAiAuthInterceptor val) {
            azureOpenAiAuthInterceptor = val;
            return this;
        }

        public OpenAiAzureStreamClient build() {
            return new OpenAiAzureStreamClient(this);
        }


    }
}
