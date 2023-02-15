package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.common.OpenAiResponse;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import com.unfbx.chatgpt.entity.edits.Edit;
import com.unfbx.chatgpt.entity.edits.EditResponse;
import com.unfbx.chatgpt.entity.embeddings.Embedding;
import com.unfbx.chatgpt.entity.embeddings.EmbeddingResponse;
import com.unfbx.chatgpt.entity.engines.Engine;
import com.unfbx.chatgpt.entity.files.File;
import com.unfbx.chatgpt.entity.files.FileDeleteResponse;
import com.unfbx.chatgpt.entity.files.UploadFileResponse;
import com.unfbx.chatgpt.entity.images.Image;
import com.unfbx.chatgpt.entity.images.ImageResponse;
import com.unfbx.chatgpt.entity.models.Model;
import com.unfbx.chatgpt.entity.models.ModelResponse;
import com.unfbx.chatgpt.entity.moderations.Moderation;
import com.unfbx.chatgpt.entity.moderations.ModerationResponse;
import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.*;

/**
 * 描述： open ai官方api接口
 *
 * @author https:www.unfbx.com
 * @date 2023-02-15
 */
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
     *
     * @param completion
     * @return
     */
    @POST("v1/completions")
    Single<CompletionResponse> completions(@Body Completion completion);

    /**
     * Creates a new edit for the provided input, instruction, and parameters.
     * 文本修复
     *
     * @param edit
     * @return
     */
    @POST("v1/edits")
    Single<EditResponse> edits(@Body Edit edit);

    /**
     * Creates an image given a prompt.
     * 根据描述生成图片
     *
     * @param image
     * @return
     */
    @POST("v1/images/generations")
    Single<ImageResponse> genImages(@Body Image image);

    /**
     * Creates an edited or extended image given an original image and a prompt.
     * 根据描述修改图片
     * @param image
     * @param mask
     * @param prompt
     * @param n         生成几张照片：1~10
     * @param size
     * @param responseFormat
     * @param user
     * @return
     */
    @Multipart
    @POST("v1/images/edits")
    Single<ImageResponse> editImages(@Part ("image") RequestBody image,
                                     @Part ("mask") RequestBody mask,
                                     @Part ("prompt") String prompt,
                                     @Part ("n") int n,
                                     @Part ("size") String size,
                                     @Part ("response_format")String responseFormat,
                                     @Part ("user")String user);

    /**
     * Creates a variation of a given image.
     * @param image
     * @param n
     * @param size
     * @param responseFormat
     * @param user
     * @return
     */
    @Multipart
    @POST("v1/images/variations")
    Single<ImageResponse> variationsImages(@Part ("image") RequestBody image,
                                           @Part ("n") int n,
                                           @Part ("size") String size,
                                           @Part ("response_format")String responseFormat,
                                           @Part ("user")String user);

    /**
     * Creates an embedding vector representing the input text.
     *
     * @param embedding
     * @return
     */
    @POST("v1/embeddings")
    Single<EmbeddingResponse> embeddings(@Body Embedding embedding);


    /**
     * Returns a list of files that belong to the user's organization.
     *
     * @return
     */
    @GET("/v1/files")
    Single<OpenAiResponse<File>> files();

    /**
     * 删除文件
     *
     * @param fileId
     * @return
     */
    @DELETE("v1/files")
    Single<FileDeleteResponse> deleteFile(@Path("file_id") String fileId);

    /**
     * 上传文件
     *
     * @param purpose
     * @param file
     * @return
     */
    @Multipart
    @POST("v1/files/{file_id}")
    Single<UploadFileResponse> uploadFile(@Path("purpose") String purpose, @Part("file") RequestBody file);


    /**
     * 检索文件
     *
     * @param fileId
     * @return
     */
    @GET("v1/files/{file_id}")
    Single<File> retrieveFile(@Path("file_id") String fileId);

    /**
     * 检索文件内容
     * ###不对免费用户开放###
     * ###不对免费用户开放###
     * ###不对免费用户开放###
     *
     * @param fileId
     * @return
     */
    @Streaming
    @GET("v1/files/{file_id}/content")
    Single<ResponseBody> retrieveFileContent(@Path("file_id") String fileId);


    /**
     * 文本审核
     *
     * @param moderation
     * @return
     */
    @POST("v1/moderations")
    Single<ModerationResponse> moderations(@Body Moderation moderation);

    /**
     * 引擎列表
     * 官方已废弃此接口
     *
     * @return
     */
    @Deprecated
    @GET("v1/engines")
    Single<OpenAiResponse<Engine>> engines();

    /**
     * 检索引擎
     * 官方已废弃此接口
     *
     * @return
     */
    @Deprecated
    @GET("v1/engines/{engine_id}")
    Single<Engine> engine(@Path("engine_id") String engineId);
}
