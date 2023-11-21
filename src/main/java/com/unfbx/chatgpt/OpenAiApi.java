package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.Tts.TextToSpeech;
import com.unfbx.chatgpt.entity.assistant.*;
import com.unfbx.chatgpt.entity.assistant.message.MessageFileResponse;
import com.unfbx.chatgpt.entity.assistant.message.MessageResponse;
import com.unfbx.chatgpt.entity.assistant.message.ModifyMessage;
import com.unfbx.chatgpt.entity.assistant.run.*;
import com.unfbx.chatgpt.entity.assistant.thread.ThreadMessage;
import com.unfbx.chatgpt.entity.billing.BillingUsage;
import com.unfbx.chatgpt.entity.billing.CreditGrantsResponse;
import com.unfbx.chatgpt.entity.billing.Subscription;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.ChatCompletionWithPicture;
import com.unfbx.chatgpt.entity.common.OpenAiResponse;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import com.unfbx.chatgpt.entity.edits.Edit;
import com.unfbx.chatgpt.entity.edits.EditResponse;
import com.unfbx.chatgpt.entity.embeddings.Embedding;
import com.unfbx.chatgpt.entity.embeddings.EmbeddingResponse;
import com.unfbx.chatgpt.entity.engines.Engine;
import com.unfbx.chatgpt.entity.files.File;
import com.unfbx.chatgpt.entity.common.DeleteResponse;
import com.unfbx.chatgpt.entity.files.UploadFileResponse;
import com.unfbx.chatgpt.entity.fineTune.Event;
import com.unfbx.chatgpt.entity.fineTune.FineTune;
import com.unfbx.chatgpt.entity.fineTune.FineTuneDeleteResponse;
import com.unfbx.chatgpt.entity.fineTune.FineTuneResponse;
import com.unfbx.chatgpt.entity.fineTune.job.FineTuneJob;
import com.unfbx.chatgpt.entity.fineTune.job.FineTuneJobEvent;
import com.unfbx.chatgpt.entity.fineTune.job.FineTuneJobListResponse;
import com.unfbx.chatgpt.entity.fineTune.job.FineTuneJobResponse;
import com.unfbx.chatgpt.entity.images.Image;
import com.unfbx.chatgpt.entity.images.ImageResponse;
import com.unfbx.chatgpt.entity.models.Model;
import com.unfbx.chatgpt.entity.models.ModelResponse;
import com.unfbx.chatgpt.entity.moderations.Moderation;
import com.unfbx.chatgpt.entity.moderations.ModerationResponse;
import com.unfbx.chatgpt.entity.assistant.thread.ModifyThread;
import com.unfbx.chatgpt.entity.assistant.thread.Thread;
import com.unfbx.chatgpt.entity.assistant.thread.ThreadResponse;
import com.unfbx.chatgpt.entity.whisper.WhisperResponse;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.time.LocalDate;
import java.util.Map;

/**
 * 描述： open ai官方api接口
 *
 * @author https:www.unfbx.com
 * 2023-02-15
 */
public interface OpenAiApi {

    /**
     * 模型列表
     *
     * @return Single ModelResponse
     */
    @GET("v1/models")
    Single<ModelResponse> models();

    /**
     * models 返回的数据id
     *
     * @param id 模型主键
     * @return Single Model
     */
    @GET("v1/models/{id}")
    Single<Model> model(@Path("id") String id);

    /**
     * 文本问答
     * Given a prompt, the model will return one or more predicted completions, and can also return the probabilities of alternative tokens at each position.
     *
     * @param completion 问答参数
     * @return Single CompletionResponse
     */
    @POST("v1/completions")
    Single<CompletionResponse> completions(@Body Completion completion);

    /**
     * Creates a new edit for the provided input, instruction, and parameters.
     * 文本修复
     *
     * @param edit 编辑参数
     * @return Single EditResponse
     */
    @Deprecated
    @POST("v1/edits")
    Single<EditResponse> edits(@Body Edit edit);

    /**
     * Creates an image given a prompt.
     * 根据描述生成图片
     *
     * @param image 图片对象
     * @return Single ImageResponse
     */
    @POST("v1/images/generations")
    Single<ImageResponse> genImages(@Body Image image);

    /**
     * Creates an edited or extended image given an original image and a prompt.
     * 根据描述修改图片
     *
     * @param image          图片对象
     * @param mask           图片对象
     * @param requestBodyMap 请求参数
     * @return Single ImageResponse
     */
    @Multipart
    @POST("v1/images/edits")
    Single<ImageResponse> editImages(@Part() MultipartBody.Part image,
                                     @Part() MultipartBody.Part mask,
                                     @PartMap() Map<String, RequestBody> requestBodyMap
    );

    /**
     * Creates a variation of a given image.
     *
     * @param image          图片对象
     * @param requestBodyMap 请求参数
     * @return Single ImageResponse
     */
    @Multipart
    @POST("v1/images/variations")
    Single<ImageResponse> variationsImages(@Part() MultipartBody.Part image,
                                           @PartMap() Map<String, RequestBody> requestBodyMap
    );

    /**
     * 文本向量计算
     *
     * @param embedding 向量参数
     * @return Single EmbeddingResponse
     */
    @POST("v1/embeddings")
    Single<EmbeddingResponse> embeddings(@Body Embedding embedding);


    /**
     * Returns a list of files that belong to the user's organization.
     *
     * @return Single OpenAiResponse File
     */
    @GET("/v1/files")
    Single<OpenAiResponse<File>> files();

    /**
     * 删除文件
     *
     * @param fileId 文件id
     * @return Single DeleteResponse
     */
    @DELETE("v1/files/{file_id}")
    Single<DeleteResponse> deleteFile(@Path("file_id") String fileId);

    /**
     * 上传文件
     *
     * @param purpose purpose
     * @param file    文件对象
     * @return Single UploadFileResponse
     */
    @Multipart
    @POST("v1/files")
    Single<UploadFileResponse> uploadFile(@Part MultipartBody.Part file,
                                          @Part("purpose") RequestBody purpose);


    /**
     * 检索文件
     *
     * @param fileId 文件id
     * @return Single File
     */
    @GET("v1/files/{file_id}")
    Single<File> retrieveFile(@Path("file_id") String fileId);

    /**
     * 检索文件内容
     * ###不对免费用户开放###
     * ###不对免费用户开放###
     * ###不对免费用户开放###
     *
     * @param fileId 文件id
     * @return Single ResponseBody
     */
    @Streaming
    @GET("v1/files/{file_id}/content")
    Single<ResponseBody> retrieveFileContent(@Path("file_id") String fileId);


    /**
     * 文本审核
     *
     * @param moderation 文本审核参数
     * @return Single ModerationResponse
     */
    @POST("v1/moderations")
    Single<ModerationResponse> moderations(@Body Moderation moderation);


    /**
     * 创建微调作业
     *
     * @param fineTune 微调
     * @return Single FineTuneResponse
     * @see #fineTuneJob(FineTuneJob fineTuneJob)
     */
    @Deprecated
    @POST("v1/fine-tunes")
    Single<FineTuneResponse> fineTune(@Body FineTune fineTune);

    /**
     * 微调作业集合
     *
     * @return Single OpenAiResponse FineTuneResponse
     * @see #fineTuneJobs(String, Integer)
     */
    @Deprecated
    @GET("v1/fine-tunes")
    Single<OpenAiResponse<FineTuneResponse>> fineTunes();


    /**
     * 检索微调作业
     *
     * @return Single FineTuneResponse
     * @see #retrieveFineTuneJob(String fineTuneJobId)
     */
    @Deprecated
    @GET("v1/fine-tunes/{fine_tune_id}")
    Single<FineTuneResponse> retrieveFineTune(@Path("fine_tune_id") String fineTuneId);

    /**
     * 取消微调作业
     *
     * @return Single FineTuneResponse
     * @see #cancelFineTuneJob(String fineTuneJobId)
     */
    @Deprecated
    @POST("v1/fine-tunes/{fine_tune_id}/cancel")
    Single<FineTuneResponse> cancelFineTune(@Path("fine_tune_id") String fineTuneId);

    /**
     * 微调作业事件列表
     *
     * @return Single OpenAiResponse Event
     * @see #fineTuneJobEvents(String, String, Integer)
     */
    @Deprecated
    @GET("v1/fine-tunes/{fine_tune_id}/events")
    Single<OpenAiResponse<Event>> fineTuneEvents(@Path("fine_tune_id") String fineTuneId);

    /**
     * 删除微调作业模型
     * Delete a fine-tuned model. You must have the Owner role in your organization.
     *
     * @return Single DeleteResponse
     */
    @DELETE("v1/models/{model}")
    Single<FineTuneDeleteResponse> deleteFineTuneModel(@Path("model") String model);


    /**
     * 引擎列表
     * 官方已废弃此接口
     *
     * @return Single OpenAiResponse Engine
     */
    @Deprecated
    @GET("v1/engines")
    Single<OpenAiResponse<Engine>> engines();

    /**
     * 检索引擎
     * 官方已废弃此接口
     *
     * @param engineId 引擎id
     * @return Engine
     */
    @Deprecated
    @GET("v1/engines/{engine_id}")
    Single<Engine> engine(@Path("engine_id") String engineId);


    /**
     * 最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param chatCompletion chat completion
     * @return 返回答案
     */
    @POST("v1/chat/completions")
    Single<ChatCompletionResponse> chatCompletion(@Body ChatCompletion chatCompletion);

    /**
     * 最新版的GPT-4 chat completion 支持图片输入
     *
     * @param chatCompletion chat completion
     * @return 返回答案
     */
    @POST("v1/chat/completions")
    Single<ChatCompletionResponse> chatCompletionWithPicture(@Body ChatCompletionWithPicture chatCompletion);

    /**
     * 语音转文字
     *
     * @param file           语音文件
     * @param requestBodyMap 参数
     * @return 文本
     */
    @Multipart
    @POST("v1/audio/transcriptions")
    Single<WhisperResponse> speechToTextTranscriptions(@Part MultipartBody.Part file,
                                                       @PartMap() Map<String, RequestBody> requestBodyMap);

    /**
     * 语音翻译：目前仅支持翻译为英文
     *
     * @param file           语音文件
     * @param requestBodyMap 参数
     * @return 文本
     */
    @Multipart
    @POST("v1/audio/translations")
    Single<WhisperResponse> speechToTextTranslations(@Part MultipartBody.Part file,
                                                     @PartMap() Map<String, RequestBody> requestBodyMap);

    /**
     * 余额查询
     * 官方禁止访问此接口
     *
     * @return 余额结果
     */
    @GET("dashboard/billing/credit_grants")
    @Deprecated
    Single<CreditGrantsResponse> creditGrants();

    /**
     * 账户信息查询：里面包含总金额（美元）等信息
     *
     * @return 账户信息
     */
    @GET("v1/dashboard/billing/subscription")
    Single<Subscription> subscription();

    /**
     * 账户调用接口消耗金额信息查询
     * totalUsage = 账户总使用金额（美分）
     *
     * @param starDate 开始时间
     * @param endDate  结束时间
     * @return 消耗金额信息
     */
    @GET("v1/dashboard/billing/usage")
    Single<BillingUsage> billingUsage(@Query("start_date") LocalDate starDate, @Query("end_date") LocalDate endDate);

    /**
     * 文本转语音
     *
     * @param textToSpeech 参数
     * @return ResponseBody body
     * @since 1.1.2
     */
    @POST("v1/audio/speech")
    @Streaming
    Call<ResponseBody> textToSpeech(@Body TextToSpeech textToSpeech);


    /**
     * 创建微调job
     *
     * @param fineTuneJob 微调
     * @return Single FineTuneJobResponse
     * @since 1.1.2
     */
    @POST("v1/fine_tuning/jobs")
    Single<FineTuneJobResponse> fineTuneJob(@Body FineTuneJob fineTuneJob);

    /**
     * 微调job集合
     *
     * @param after 上一个分页请求中最后一个job id
     * @param limit 每次查询数量
     * @return FineTuneJobListResponse #FineTuneResponse
     * @since 1.1.2
     */
    @GET("v1/fine_tuning/jobs")
    Single<FineTuneJobListResponse<FineTuneJobResponse>> fineTuneJobs(@Query("after") String after, @Query("limit") Integer limit);


    /**
     * 检索微调job
     *
     * @param fineTuneJobId JobId
     * @return FineTuneJobResponse
     * @since 1.1.2
     */
    @GET("v1/fine_tuning/jobs/{fine_tuning_job_id}")
    Single<FineTuneJobResponse> retrieveFineTuneJob(@Path("fine_tuning_job_id") String fineTuneJobId);

    /**
     * 取消微调job
     *
     * @param fineTuneJobId JobId
     * @return FineTuneJobResponse
     * @since 1.1.2
     */
    @POST("v1/fine_tuning/jobs/{fine_tuning_job_id}/cancel")
    Single<FineTuneJobResponse> cancelFineTuneJob(@Path("fine_tuning_job_id") String fineTuneJobId);

    /**
     * 微调job事件列表
     *
     * @param fineTuneJobId JobId
     * @param after         上一个分页请求中最后一个id，默认值：null
     * @param limit         每次查询数量 默认值：20
     * @return FineTuneJobListResponse #FineTuneResponse
     * @since 1.1.2
     */
    @GET("v1/fine_tuning/jobs/{fine_tuning_job_id}/events")
    Single<FineTuneJobListResponse<FineTuneJobEvent>> fineTuneJobEvents(@Path("fine_tuning_job_id") String fineTuneJobId, @Query("after") String after, @Query("limit") Integer limit);


    /**
     * 创建助手
     *
     * @param assistant 创建助手参数
     * @return 助手信息
     * @since 1.1.3
     */
    @POST("v1/assistants")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<AssistantResponse> assistant(@Body Assistant assistant);


    /**
     * 获取助手详细信息
     *
     * @param assistantId 助手id
     * @return 助手信息
     * @since 1.1.3
     */
    @GET("v1/assistants/{assistant_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<AssistantResponse> retrieveAssistant(@Path("assistant_id") String assistantId);

    /**
     * 修改助手信息
     *
     * @param assistantId 助手id
     * @param assistant   修改助手参数
     * @return 助手信息
     * @since 1.1.3
     */
    @POST("v1/assistants/{assistant_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<AssistantResponse> modifyAssistant(@Path("assistant_id") String assistantId, @Body Assistant assistant);

    /**
     * 删除助手
     *
     * @param assistantId 助手id
     * @return 删除状态
     * @since 1.1.3
     */
    @DELETE("v1/assistants/{assistant_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<DeleteResponse> deleteAssistant(@Path("assistant_id") String assistantId);


    /**
     * 助手列表
     *
     * @param limit  每次查询数量 默认值：20
     * @param order  排序类型
     * @param before 分页参数，之前的id，默认值：null
     * @param after  分页参数，之后的id，默认值：null
     * @return AssistantListResponse #AssistantResponse
     * @since 1.1.3
     */
    @GET("v1/assistants")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<AssistantListResponse<AssistantResponse>> assistants(@Query("limit") Integer limit, @Query("order") String order, @Query("before") String before, @Query("after") String after);

    /**
     * 创建助手文件
     *
     * @param assistantId   助手id
     * @param assistantFile 文件信息
     * @return 返回信息AssistantResponse
     */
    @POST("v1/assistants/{assistant_id}/files")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<AssistantFileResponse> assistantFile(@Path("assistant_id") String assistantId, @Body AssistantFile assistantFile);

    /**
     * 检索助手文件
     *
     * @param assistantId 助手id
     * @param fileId      文件id
     * @return 助手文件信息
     */
    @GET("v1/assistants/{assistant_id}/files/{file_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<AssistantFileResponse> retrieveAssistantFile(@Path("assistant_id") String assistantId, @Path("file_id") String fileId);

    /**
     * 删除助手文件
     *
     * @param assistantId 助手id
     * @param fileId      文件id
     * @return 删除状态
     */
    @DELETE("v1/assistants/{assistant_id}/files/{file_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<DeleteResponse> deleteAssistantFile(@Path("assistant_id") String assistantId, @Path("file_id") String fileId);

    /**
     * 助手文件列表
     *
     * @param assistantId 助手id
     * @param limit       一页数据大小
     * @param order       排序类型
     * @param before      分页参数，之前的id，默认值：null
     * @param after       分页参数，之后的id，默认值：null
     * @return 助手文件列表
     */
    @GET("v1/assistants/{assistant_id}/files")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<AssistantListResponse<AssistantFileResponse>> assistantFiles(@Path("assistant_id") String assistantId,
                                                                        @Query("limit") Integer limit, @Query("order") String order, @Query("before") String before, @Query("after") String after);


    /**
     * 创建线程
     *
     * @param thread 创建线程参数
     * @return 线程信息
     * @since 1.1.3
     */
    @POST("v1/threads")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<ThreadResponse> thread(@Body Thread thread);


    /**
     * 获取线程详细信息
     *
     * @param threadId 线程id
     * @return 线程信息
     * @since 1.1.3
     */
    @GET("v1/threads/{thread_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<ThreadResponse> retrieveThread(@Path("thread_id") String threadId);

    /**
     * 修改线程信息
     *
     * @param threadId 线程id
     * @param thread   线程信息
     * @return 线程信息
     * @since 1.1.3
     */
    @POST("v1/threads/{thread_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<ThreadResponse> modifyThread(@Path("thread_id") String threadId, @Body ModifyThread thread);

    /**
     * 删除线程
     *
     * @param threadId 线程id
     * @return 删除状态
     * @since 1.1.3
     */
    @DELETE("v1/threads/{thread_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<DeleteResponse> deleteThread(@Path("thread_id") String threadId);

    /**
     * 为线程创建消息
     *
     * @param threadId 线程id
     * @param message  message参数
     * @return
     * @since 1.1.3
     */
    @POST("v1/threads/{thread_id}/messages")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<MessageResponse> message(@Path("thread_id") String threadId, @Body ThreadMessage message);

    /**
     * 检索某一个线程对应的消息详细信息
     *
     * @param threadId  线程id
     * @param messageId 消息id
     * @return
     * @since 1.1.3
     */
    @GET("v1/threads/{thread_id}/messages/{message_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<MessageResponse> retrieveMessage(@Path("thread_id") String threadId, @Path("message_id") String messageId);

    /**
     * 修改某一个线程对应的消息
     *
     * @param threadId  线程id
     * @param messageId 消息id
     * @param message   消息体
     * @return
     * @since 1.1.3
     */
    @POST("v1/threads/{thread_id}/messages/{message_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<MessageResponse> modifyMessage(@Path("thread_id") String threadId, @Path("message_id") String messageId, @Body ModifyMessage message);

    /**
     * 获取某一个线程的消息集合
     *
     * @param threadId 线程id
     * @param limit    一页数据大小
     * @param order    排序类型
     * @param before   分页参数，之前的id，默认值：null
     * @param after    分页参数，之后的id，默认值：null
     * @return
     * @since 1.1.3
     */
    @GET("v1/threads/{thread_id}/messages")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<AssistantListResponse<MessageResponse>> messages(@Path("thread_id") String threadId,
                                                            @Query("limit") Integer limit, @Query("order") String order, @Query("before") String before, @Query("after") String after);

    /**
     * 检索某一个线程对应某一个消息的一个文件信息
     *
     * @param threadId  线程id
     * @param messageId 消息id
     * @param fileId    文件id
     * @return
     * @since 1.1.3
     */
    @GET("v1/threads/{thread_id}/messages/{message_id}/files/{file_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<MessageFileResponse> retrieveMessageFile(@Path("thread_id") String threadId, @Path("message_id") String messageId, @Path("file_id") String fileId);

    /**
     * messageFiles集合
     *
     * @param threadId  线程id
     * @param messageId 消息id
     * @param limit     一页数据大小
     * @param order     排序类型
     * @param before    分页参数，之前的id，默认值：null
     * @param after     分页参数，之后的id，默认值：null
     * @return
     * @since 1.1.3
     */
    @GET("v1/threads/{thread_id}/messages/{message_id}/files")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<AssistantListResponse<MessageFileResponse>> messageFiles(@Path("thread_id") String threadId, @Path("message_id") String messageId,
                                                                @Query("limit") Integer limit, @Query("order") String order, @Query("before") String before, @Query("after") String after);


    /**
     * 创建Run
     *
     * @param threadId 线程id
     * @param run      run
     * @return
     * @since 1.1.3
     */
    @POST("v1/threads/{thread_id}/runs")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<RunResponse> run(@Path("thread_id") String threadId, @Body Run run);


    /**
     * 检索run详细信息
     *
     * @param threadId 线程id
     * @param runId    run_id
     * @return
     * @since 1.1.3
     */
    @GET("v1/threads/{thread_id}/runs/{run_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<RunResponse> retrieveRun(@Path("thread_id") String threadId, @Path("run_id") String runId);

    /**
     * 修改某一个run
     *
     * @param threadId 线程id
     * @param runId    run_id
     * @param run      消息体
     * @return
     * @since 1.1.3
     */
    @POST("v1/threads/{thread_id}/runs/{run_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<RunResponse> modifyRun(@Path("thread_id") String threadId, @Path("run_id") String runId, @Body ModifyRun run);


    /**
     * 获取某一个线程的run集合
     *
     * @param threadId 线程id
     * @param limit    一页数据大小
     * @param order    排序类型
     * @param before   分页参数，之前的id，默认值：null
     * @param after    分页参数，之后的id，默认值：null
     * @return
     * @since 1.1.3
     */
    @GET("v1/threads/{thread_id}/runs")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<AssistantListResponse<RunResponse>> runs(@Path("thread_id") String threadId,
                                                    @Query("limit") Integer limit, @Query("order") String order, @Query("before") String before, @Query("after") String after);


    /**
     * 获取某一个线程的run集合
     *
     * @param threadId    线程id
     * @param runId       run id
     * @param toolOutputs 为其提交输出的工具列表。
     * @return
     * @since 1.1.3
     */
    @POST("v1/threads/{thread_id}/runs/{run_id}/submit_tool_outputs")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<RunResponse> submitToolOutputs(@Path("thread_id") String threadId, @Path("run_id") String runId, @Body ToolOutputBody toolOutputs);


    /**
     * 取消正在进行中的run
     *
     * @param threadId 线程id
     * @param runId    run id
     * @return
     * @since 1.1.3
     */
    @POST("v1/threads/{thread_id}/runs/{run_id}/cancel")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<RunResponse> cancelRun(@Path("thread_id") String threadId, @Path("run_id") String runId);


    /**
     * 创建一个线程并在一个请求中运行它
     *
     * @param threadRun 对象
     * @return
     * @since 1.1.3
     */
    @POST("v1/threads/runs")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<RunResponse> threadRun(@Body ThreadRun threadRun);

    /**
     * 检索run step详细信息
     *
     * @param threadId 线程id
     * @param runId    run_id
     * @param stepId   step_id
     * @return
     * @since 1.1.3
     */
    @GET("v1/threads/{thread_id}/runs/{run_id}/steps/{step_id}")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<RunStepResponse> retrieveRunStep(@Path("thread_id") String threadId, @Path("run_id") String runId, @Path("step_id") String stepId);


    /**
     * 获取某一个线程的run step集合
     *
     * @param threadId 线程id
     * @param runId    run_id
     * @param limit    一页数据大小
     * @param order    排序类型
     * @param before   分页参数，之前的id，默认值：null
     * @param after    分页参数，之后的id，默认值：null
     * @return
     * @since 1.1.3
     */
    @GET("v1/threads/{thread_id}/runs/{run_id}/steps")
    @Headers("OpenAI-Beta: assistants=v1")
    Single<AssistantListResponse<RunStepResponse>> runSteps(@Path("thread_id") String threadId, @Path("run_id") String runId,
                                                            @Query("limit") Integer limit, @Query("order") String order, @Query("before") String before, @Query("after") String after);

}
