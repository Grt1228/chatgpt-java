package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.chat.wenxin.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.wenxin.ChatCompletionResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WenXinApi {
    /**
     * 最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param chatCompletion chat completion
     * @return 返回答案
     */
    @POST("rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions")
    Single<ChatCompletionResponse> chatCompletion(@Body ChatCompletion chatCompletion);
}