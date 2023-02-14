package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import com.unfbx.chatgpt.entity.edits.Edit;
import com.unfbx.chatgpt.entity.edits.EditResponse;
import com.unfbx.chatgpt.entity.image.Image;
import com.unfbx.chatgpt.entity.image.ImageResponse;
import com.unfbx.chatgpt.entity.models.Model;
import com.unfbx.chatgpt.entity.models.ModelResponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OpenAiApi {

    /**
     * 模型列表
     *
     * @return
     */
    @GET("v1/models")
    Single<ModelResponse> models();

    /**
     * models 返回的数据id
     *
     * @return
     */
    @GET("v1/models/{id}")
    Single<Model> model(@Path("id") String id);

    /**
     * 文本问答
     * Given a prompt, the model will return one or more predicted completions, and can also return the probabilities of alternative tokens at each position.
     * @param completion
     * @return
     */
    @POST("v1/completions")
    Single<CompletionResponse> completions(@Body Completion completion);

    /**
     * Creates a new edit for the provided input, instruction, and parameters.
     * 文本修复
     * @param edit
     * @return
     */
    @POST("v1/edits")
    Single<EditResponse> edits(@Body Edit edit);

    /**
     * Creates an image given a prompt.
     * 根据描述生成图片
     * @param image
     * @return
     */
    @POST("v1/images/generations")
    Single<ImageResponse> genImages(@Body Image image);
}
