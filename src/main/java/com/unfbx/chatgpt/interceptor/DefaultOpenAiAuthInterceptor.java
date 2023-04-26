package com.unfbx.chatgpt.interceptor;

import com.unfbx.chatgpt.function.KeyStrategyFunction;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 描述：请求增加header apikey
 *
 * @author https:www.unfbx.com
 * @since 2023-03-23
 */
@Slf4j
public class DefaultOpenAiAuthInterceptor extends OpenAiAuthInterceptor {
    /**
     * 请求头处理
     *
     * @param apiKey      api keys列表
     * @param keyStrategy 自定义key的使用策略
     */
    public DefaultOpenAiAuthInterceptor(List<String> apiKey, KeyStrategyFunction<List<String>, String> keyStrategy) {
        this.setKeyStrategy(keyStrategy);
        super.setApiKey(apiKey);
        super.setWarringConfig(null);
    }

    /**
     * 构造方法
     *
     * @param apiKey        apiKeys集合
     * @param keyStrategy   请求时key的获取策略
     * @param warringConfig 所有的key都失效后的告警参数配置
     */
    public DefaultOpenAiAuthInterceptor(List<String> apiKey, KeyStrategyFunction<List<String>, String> keyStrategy, Map warringConfig) {
        this.setKeyStrategy(keyStrategy);
        super.setApiKey(apiKey);
        super.setWarringConfig(warringConfig);
    }

    /**
     * 拦截器鉴权
     *
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        return chain.proceed(auth(super.getKey(), original));
    }

    /**
     * key失效或者禁用后的处理逻辑
     * 默认不处理
     *
     * @param apiKey 返回新的api keys集合
     * @return
     */
    @Override
    protected List<String> onErrorDealApiKeys(String apiKey) {
        return super.getApiKey();
    }

    @Override
    protected void noHaveActiveKeyWarring() {
        log.error("[告警]——————>没有可用的key！！！");
        return;
    }
}
