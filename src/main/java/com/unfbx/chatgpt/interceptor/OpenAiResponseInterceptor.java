package com.unfbx.chatgpt.interceptor;

import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.entity.common.OpenAiResponse;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Objects;

/**
 * 描述：openai 返回值处理Interceptor
 *
 * @author https:www.unfbx.com
 * @since  2023-03-23
 */
@Slf4j
public class OpenAiResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();
        Response response = chain.proceed(original);
        if (!response.isSuccessful() && response.body() != null) {
            if (response.code() == CommonError.OPENAI_AUTHENTICATION_ERROR.code()
                    || response.code() == CommonError.OPENAI_LIMIT_ERROR.code()
                    || response.code() == CommonError.OPENAI_SERVER_ERROR.code()) {
                OpenAiResponse openAiResponse = JSONUtil.toBean(response.body().string(), OpenAiResponse.class);
                log.error(openAiResponse.getError().getMessage());
                throw new BaseException(openAiResponse.getError().getMessage());
            }
            String errorMsg = response.body().string();
            log.error("--------> 请求异常：{}", errorMsg);
            OpenAiResponse openAiResponse = JSONUtil.toBean(errorMsg, OpenAiResponse.class);
            if (Objects.nonNull(openAiResponse.getError())) {
                log.error(openAiResponse.getError().getMessage());
                throw new BaseException(openAiResponse.getError().getMessage());
            }
            throw new BaseException(CommonError.RETRY_ERROR);
        }
        return response;
    }
}
