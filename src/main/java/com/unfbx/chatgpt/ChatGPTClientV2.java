package com.unfbx.chatgpt;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpStatus;
import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.common.OpenAiResponse;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import com.unfbx.chatgpt.entity.edits.Edit;
import com.unfbx.chatgpt.entity.edits.EditResponse;
import com.unfbx.chatgpt.entity.image.Image;
import com.unfbx.chatgpt.entity.image.ImageResponse;
import com.unfbx.chatgpt.entity.models.Model;
import com.unfbx.chatgpt.entity.models.ModelResponse;
import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import io.reactivex.Single;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * 描述： 客户端
 *
 * @author https:www.unfbx.com
 * @date 2023-02-11
 */
@Getter
@Slf4j
public class ChatGPTClientV2 {

    private String apiKey;

    private OpenAiApi openAiApi;

    public ChatGPTClientV2(String apiKey) {
        this.apiKey = apiKey;
        this.openAiApi = new Retrofit.Builder()
                .baseUrl("https://api.openai.com/")
                .client(okHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(OpenAiApi.class);
    }

    private OkHttpClient okHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(chain -> {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header(Header.AUTHORIZATION.getValue(), "Bearer " + apiKey)
                    .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        }).addInterceptor(chain -> {
            Request original = chain.request();
            Response response = chain.proceed(original);
            if (!response.isSuccessful()) {
                if (response.code() == HttpStatus.HTTP_UNAUTHORIZED) {
                    OpenAiResponse body = JSONUtil.toBean(response.body().string(), OpenAiResponse.class);
                    throw new BaseException(body.getError().getMessage());
                }
                String errorMsg = response.body().string();
                log.error("请求异常：{}", errorMsg);
                OpenAiResponse openAiResponse = JSONUtil.toBean(errorMsg, OpenAiResponse.class);
                if (Objects.nonNull(openAiResponse.getError())) {
                    throw new BaseException(openAiResponse.getError().getMessage());
                }
                throw new BaseException(CommonError.RETRY_ERROR);
            }
            return response;
        });
        client.connectTimeout(30, TimeUnit.SECONDS);
        client.writeTimeout(30, TimeUnit.SECONDS);
        client.readTimeout(30, TimeUnit.SECONDS);
        OkHttpClient httpClient = client.build();
        return httpClient;
    }

    /**
     * openAi模型列表
     *
     * @return
     */
    public List<Model> models() {
        Single<ModelResponse> models = this.openAiApi.models();
        List<Model> modelList = models.blockingGet().getData();
        return modelList;
    }

    /**
     * openAi模型详细信息
     *
     * @param id
     * @return
     */
    public Model model(String id) {
        if(Objects.isNull(id) || "".equals(id)){
            throw new BaseException(CommonError.PARAM_ERROR);
        }
        Single<Model> model = this.openAiApi.model(id);
        return model.blockingGet();
    }


    /**
     * 问答接口
     *
     * @param completion
     * @return
     */
    public CompletionResponse completions(Completion completion) {
        if(Objects.isNull(completion)){
            throw new BaseException(CommonError.PARAM_ERROR);
        }
        Single<CompletionResponse> completions = this.openAiApi.completions(completion);
        return completions.blockingGet();
    }

    /**
     * 问答接口-简易版
     *
     * @param question
     * @return
     */
    public CompletionResponse completions(String question) {
        Completion q = Completion.builder()
                .prompt(question)
                .build();
        Single<CompletionResponse> completions = this.openAiApi.completions(q);
        return completions.blockingGet();
    }

    /**
     * 文本修改
     * @param edit
     * @return
     */
    public EditResponse edit(Edit edit){
        Single<EditResponse> edits = this.openAiApi.edits(edit);
        return edits.blockingGet();
    }

    /**
     * 根据描述生成图片
     * @param image
     * @return
     */
    public ImageResponse genImages(Image image){
        Single<ImageResponse> edits = this.openAiApi.genImages(image);
        return edits.blockingGet();
    }
}
