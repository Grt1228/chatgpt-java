package com.unfbx.chatgpt.interceptor;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import com.unfbx.chatgpt.function.KeyStrategyFunction;
import lombok.Getter;
import lombok.Setter;
import okhttp3.Interceptor;
import okhttp3.Request;

import java.util.List;
import java.util.Map;

public abstract class OpenAiAuthInterceptor implements Interceptor {


    /**
     * key 集合
     */
    @Getter
    @Setter
    private List<String> apiKey;
    /**
     * 自定义的key的使用策略
     */
    @Getter
    @Setter
    private KeyStrategyFunction<List<String>, String> keyStrategy;

    @Getter
    @Setter
    private Map warringConfig;

    /**
     * 自定义apiKeys的处理逻辑
     *
     * @param errorKey
     * @return 返回值是新的apiKeys
     */
    protected abstract List<String> onErrorDealApiKeys(String errorKey);

    /**
     * 所有的key都失效后，自定义预警配置
     *
     * @return
     */
    protected abstract void noHaveActiveKeyWarring();


    /**
     * 获取请求key
     *
     * @return
     */
    public final String getKey() {
        if (CollectionUtil.isEmpty(apiKey)) {
            this.noHaveActiveKeyWarring();
            throw new BaseException(CommonError.NO_ACTIVE_API_KEYS);
        }
        return keyStrategy.apply(apiKey);
    }

    /**
     * 默认的鉴权处理方法
     *
     * @param key
     * @param original
     * @return
     */
    public Request auth(String key, Request original) {
        Request request = original.newBuilder()
                .header(Header.AUTHORIZATION.getValue(), "Bearer " + key)
                .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                .method(original.method(), original.body())
                .build();
        return request;
    }
}
