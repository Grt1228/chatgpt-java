package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import com.unfbx.chatgpt.entity.embeddings.Embedding;
import com.unfbx.chatgpt.entity.embeddings.EmbeddingResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Azure OpenAI api接口
 * api版本：2023-03-15-preview
 *
 * apiHost：
 * https://${your-resource-name}.openai.azure.com/openai/deployments/${deployment-id}/
 *
 * 文档：
 * https://learn.microsoft.com/zh-cn/azure/cognitive-services/openai/reference
 * swagger：https://github.com/Azure/azure-rest-api-specs/blob/main/specification/cognitiveservices/data-plane/AzureOpenAI/inference/stable/2022-12-01/inference.json
 *
 * @author skywalker
 * @since 2023/5/7 17:22
 */
public interface AzureOpenAiApi extends OpenAiApi {

    /**
     * 与OpenAiApi接口保持一直，不额外增加参数传递api-version字段
     */
    String API_VERSION_QUERY_STRING = "?api-version=2023-03-15-preview";

    /**
     * 文本问答
     * Given a prompt, the model will return one or more predicted completions, and can also return the probabilities of alternative tokens at each position.
     *
     * 注意：
     * logprobs, best_of and echo parameters are not available on gpt-35-turbo model.
     * azure版本api，在gpt-35-turbo model下，不支持传递logprobs, best_of, echo这3个参数，需要置为null
     *
     * 示例：
     * Completion q = Completion.builder()
     *             .prompt("who are you?")
     *             .logprobs(null)
     *             .bestOf(null)
     *             .echo(null)
     *             .maxTokens(16)
     *             .build();
     *
     * @param completion 问答参数
     * @return Single CompletionResponse
     */
    @POST("completions" + API_VERSION_QUERY_STRING)
    Single<CompletionResponse> completions(@Body Completion completion);

    /**
     * 文本向量计算
     *
     * 注意：
     * Too many inputs for model None. The max number of inputs is 1.  We hope to increase the number of inputs per request soon.
     * Azure版本api只支持传递一个input
     *
     * @param embedding 向量参数
     * @return Single EmbeddingResponse
     */
    @POST("embeddings" + API_VERSION_QUERY_STRING)
    Single<EmbeddingResponse> embeddings(@Body Embedding embedding);

    /**
     * 最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param chatCompletion chat completion
     * @return 返回答案
     */
    @Override
    @POST("chat/completions" + API_VERSION_QUERY_STRING)
    Single<ChatCompletionResponse> chatCompletion(@Body ChatCompletion chatCompletion);


}
