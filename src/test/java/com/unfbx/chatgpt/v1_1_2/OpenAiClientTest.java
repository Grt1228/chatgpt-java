package com.unfbx.chatgpt.v1_1_2;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.*;

import com.unfbx.chatgpt.entity.Tts.TextToSpeech;
import com.unfbx.chatgpt.entity.Tts.TtsFormat;
import com.unfbx.chatgpt.entity.Tts.TtsVoice;
import com.unfbx.chatgpt.entity.chat.*;
import com.unfbx.chatgpt.entity.chat.tool.ToolCallFunction;
import com.unfbx.chatgpt.entity.chat.tool.ToolCalls;
import com.unfbx.chatgpt.entity.chat.tool.Tools;
import com.unfbx.chatgpt.entity.chat.tool.ToolsFunction;
import com.unfbx.chatgpt.entity.files.UploadFileResponse;
import com.unfbx.chatgpt.entity.fineTune.job.FineTuneJobEvent;
import com.unfbx.chatgpt.entity.fineTune.job.FineTuneJobListResponse;
import com.unfbx.chatgpt.entity.fineTune.job.FineTuneJobResponse;
import com.unfbx.chatgpt.entity.images.Image;
import com.unfbx.chatgpt.entity.images.ImageResponse;
import com.unfbx.chatgpt.entity.images.SizeEnum;
import com.unfbx.chatgpt.entity.models.Model;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.DynamicKeyOpenAiAuthInterceptor;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 描述： 测试类
 *
 * @author https:www.unfbx.com
 * 2023-11-10
 */
@Slf4j
public class OpenAiClientTest {

    private OpenAiClient client;
    private OpenAiStreamClient streamClient;


    @Before
    public void before() {
        //可以为null
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！！千万别再生产或者测试环境打开BODY级别日志！！！！
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
//                .proxy(proxy)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new OpenAiResponseInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        client = OpenAiClient.builder()
                //支持多key传入，请求时候随机选择
                .apiKey(Arrays.asList("*********************"))
                //自定义key的获取策略：默认KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new FirstKeyStrategy())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传,(关注公众号回复：openai ，获取免费的测试代理地址)
                .apiHost("https://*************/")
                .build();

        streamClient = OpenAiStreamClient.builder()
                //支持多key传入，请求时候随机选择
                .apiKey(Arrays.asList("*********************"))
                //自定义key的获取策略：默认KeyRandomStrategy
                .keyStrategy(new KeyRandomStrategy())
                .authInterceptor(new DynamicKeyOpenAiAuthInterceptor())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传,(关注公众号回复：openai ，获取免费的测试代理地址)
                .apiHost("https://*************/")
                .build();
    }

    /**
     * 聊天模型支持图片流式示例
     */
    @Test
    public void pictureChat() {
        Content textContent = Content.builder().text("What’s in this image?").type(Content.Type.TEXT.getName()).build();
        ImageUrl imageUrl = ImageUrl.builder().url("https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Gfp-wisconsin-madison-the-nature-boardwalk.jpg/2560px-Gfp-wisconsin-madison-the-nature-boardwalk.jpg").build();
        Content imageContent = Content.builder().imageUrl(imageUrl).type(Content.Type.IMAGE_URL.getName()).build();
        List<Content> contentList = new ArrayList<>();
        contentList.add(textContent);
        contentList.add(imageContent);
        MessagePicture message = MessagePicture.builder().role(Message.Role.USER).content(contentList).build();
        ChatCompletionWithPicture chatCompletion = ChatCompletionWithPicture
                .builder()
                .messages(Collections.singletonList(message))
                .model(ChatCompletion.Model.GPT_4_VISION_PREVIEW.getName())
                .build();
        ChatCompletionResponse chatCompletionResponse = client.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices().forEach(e -> System.out.println(e.getMessage()));
    }


    /**
     * 聊天模型支持图片流式示例
     */
    @Test
    public void pictureChatV2() {
        Content textContent = Content.builder().text("What’s in this image?").type(Content.Type.TEXT.getName()).build();
        ImageUrl imageUrl = ImageUrl.builder().url("https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Gfp-wisconsin-madison-the-nature-boardwalk.jpg/2560px-Gfp-wisconsin-madison-the-nature-boardwalk.jpg").build();
        Content imageContent = Content.builder().imageUrl(imageUrl).type(Content.Type.IMAGE_URL.getName()).build();
        List<Content> contentList = new ArrayList<>();
        contentList.add(textContent);
        contentList.add(imageContent);
        MessagePicture message = MessagePicture.builder().role(Message.Role.USER).content(contentList).build();
        ChatCompletionWithPicture chatCompletion = ChatCompletionWithPicture
                .builder()
                .messages(Collections.singletonList(message))
                .model(ChatCompletion.Model.GPT_4_VISION_PREVIEW.getName())
                .build();
        ChatCompletionResponse chatCompletionResponse = client.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices().forEach(e -> System.out.println(e.getMessage()));
    }


    /**
     * 聊天模型支持图片流式示例
     */
    @Test
    public void pictureChatStream() {
        Content textContent = Content.builder().text("What’s in this image?").type(Content.Type.TEXT.getName()).build();
        ImageUrl imageUrl = ImageUrl.builder().url("https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Gfp-wisconsin-madison-the-nature-boardwalk.jpg/2560px-Gfp-wisconsin-madison-the-nature-boardwalk.jpg").build();
        Content imageContent = Content.builder().imageUrl(imageUrl).type(Content.Type.IMAGE_URL.getName()).build();
        List<Content> contentList = new ArrayList<>();
        contentList.add(textContent);
        contentList.add(imageContent);
        MessagePicture message = MessagePicture.builder().role(Message.Role.USER).content(contentList).build();
        ChatCompletionWithPicture chatCompletion = ChatCompletionWithPicture
                .builder()
                .messages(Collections.singletonList(message))
                .model(ChatCompletion.Model.GPT_4_VISION_PREVIEW.getName())
                .build();
        streamClient.streamChatCompletion(chatCompletion, new ConsoleEventSourceListener());
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义返回数据格式
     */
    @Test
    public void diyReturnDataModelChat() {
        Message message = Message.builder().role(Message.Role.USER).content("随机输出10个单词，使用json输出").build();
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .messages(Collections.singletonList(message))
                .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.JSON_OBJECT.getName()).build())
                .model(ChatCompletion.Model.GPT_4_1106_PREVIEW.getName())
                .build();
        ChatCompletionResponse chatCompletionResponse = client.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices().forEach(e -> System.out.println(e.getMessage()));
    }

    /**
     * tools使用示例
     */
    @Test
    public void toolsChat() {
        Message message = Message.builder().role(Message.Role.USER).content("给我输出一个长度为2的中文词语，并解释下词语对应物品的用途").build();
        //属性一
        JSONObject wordLength = new JSONObject();
        wordLength.putOpt("type", "number");
        wordLength.putOpt("description", "词语的长度");
        //属性二
        JSONObject language = new JSONObject();
        language.putOpt("type", "string");
        language.putOpt("enum", Arrays.asList("zh", "en"));
        language.putOpt("description", "语言类型，例如：zh代表中文、en代表英语");
        //参数
        JSONObject properties = new JSONObject();
        properties.putOpt("wordLength", wordLength);
        properties.putOpt("language", language);
        Parameters parameters = Parameters.builder()
                .type("object")
                .properties(properties)
                .required(Collections.singletonList("wordLength")).build();
        Tools tools = Tools.builder()
                .type(Tools.Type.FUNCTION.getName())
                .function(ToolsFunction.builder().name("getOneWord").description("获取一个指定长度和语言类型的词语").parameters(parameters).build())
                .build();

        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .messages(Collections.singletonList(message))
                .tools(Collections.singletonList(tools))
                .model(ChatCompletion.Model.GPT_4_1106_PREVIEW.getName())
                .build();
        ChatCompletionResponse chatCompletionResponse = client.chatCompletion(chatCompletion);

        ChatChoice chatChoice = chatCompletionResponse.getChoices().get(0);
        log.info("构造的方法值：{}", chatChoice.getMessage().getToolCalls());

        ToolCalls openAiReturnToolCalls = chatChoice.getMessage().getToolCalls().get(0);
        WordParam wordParam = JSONUtil.toBean(openAiReturnToolCalls.getFunction().getArguments(), WordParam.class);
        String oneWord = getOneWord(wordParam);


        ToolCallFunction tcf = ToolCallFunction.builder().name("getOneWord").arguments(openAiReturnToolCalls.getFunction().getArguments()).build();
        ToolCalls tc = ToolCalls.builder().id(openAiReturnToolCalls.getId()).type(ToolCalls.Type.FUNCTION.getName()).function(tcf).build();
        //构造tool call
        Message message2 = Message.builder().role(Message.Role.ASSISTANT).content("方法参数").toolCalls(Collections.singletonList(tc)).build();
        String content
                = "{ " +
                "\"wordLength\": \"3\", " +
                "\"language\": \"zh\", " +
                "\"word\": \"" + oneWord + "\"," +
                "\"用途\": [\"直接吃\", \"做沙拉\", \"售卖\"]" +
                "}";
        Message message3 = Message.builder().toolCallId(openAiReturnToolCalls.getId()).role(Message.Role.TOOL).name("getOneWord").content(content).build();
        List<Message> messageList = Arrays.asList(message, message2, message3);
        ChatCompletion chatCompletionV2 = ChatCompletion
                .builder()
                .messages(messageList)
                .model(ChatCompletion.Model.GPT_4_1106_PREVIEW.getName())
                .build();
        ChatCompletionResponse chatCompletionResponseV2 = client.chatCompletion(chatCompletionV2);
        log.info("自定义的方法返回值：{}", chatCompletionResponseV2.getChoices().get(0).getMessage().getContent());

    }

    /**
     * tools流式输出使用示例
     */
    @Test
    public void streamToolsChat() {

        CountDownLatch countDownLatch = new CountDownLatch(1);
        ConsoleEventSourceListenerV3 eventSourceListener = new ConsoleEventSourceListenerV3(countDownLatch);

        Message message = Message.builder().role(Message.Role.USER).content("给我输出一个长度为2的中文词语，并解释下词语对应物品的用途").build();
        //属性一
        JSONObject wordLength = new JSONObject();
        wordLength.putOpt("type", "number");
        wordLength.putOpt("description", "词语的长度");
        //属性二
        JSONObject language = new JSONObject();
        language.putOpt("type", "string");
        language.putOpt("enum", Arrays.asList("zh", "en"));
        language.putOpt("description", "语言类型，例如：zh代表中文、en代表英语");
        //参数
        JSONObject properties = new JSONObject();
        properties.putOpt("wordLength", wordLength);
        properties.putOpt("language", language);
        Parameters parameters = Parameters.builder()
                .type("object")
                .properties(properties)
                .required(Collections.singletonList("wordLength")).build();
        Tools tools = Tools.builder()
                .type(Tools.Type.FUNCTION.getName())
                .function(ToolsFunction.builder().name("getOneWord").description("获取一个指定长度和语言类型的词语").parameters(parameters).build())
                .build();

        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .messages(Collections.singletonList(message))
                .tools(Collections.singletonList(tools))
                .model(ChatCompletion.Model.GPT_4_1106_PREVIEW.getName())
                .build();
        streamClient.streamChatCompletion(chatCompletion, eventSourceListener);

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ToolCalls openAiReturnToolCalls = eventSourceListener.getToolCalls();
        WordParam wordParam = JSONUtil.toBean(openAiReturnToolCalls.getFunction().getArguments(), WordParam.class);
        String oneWord = getOneWord(wordParam);


        ToolCallFunction tcf = ToolCallFunction.builder().name("getOneWord").arguments(openAiReturnToolCalls.getFunction().getArguments()).build();
        ToolCalls tc = ToolCalls.builder().id(openAiReturnToolCalls.getId()).type(ToolCalls.Type.FUNCTION.getName()).function(tcf).build();
        //构造tool call
        Message message2 = Message.builder().role(Message.Role.ASSISTANT).content("方法参数").toolCalls(Collections.singletonList(tc)).build();
        String content
                = "{ " +
                "\"wordLength\": \"3\", " +
                "\"language\": \"zh\", " +
                "\"word\": \"" + oneWord + "\"," +
                "\"用途\": [\"直接吃\", \"做沙拉\", \"售卖\"]" +
                "}";
        Message message3 = Message.builder().toolCallId(openAiReturnToolCalls.getId()).role(Message.Role.TOOL).name("getOneWord").content(content).build();
        List<Message> messageList = Arrays.asList(message, message2, message3);
        ChatCompletion chatCompletionV2 = ChatCompletion
                .builder()
                .messages(messageList)
                .model(ChatCompletion.Model.GPT_4_1106_PREVIEW.getName())
                .build();


        CountDownLatch countDownLatch1 = new CountDownLatch(1);
        streamClient.streamChatCompletion(chatCompletionV2, new ConsoleEventSourceListenerV3(countDownLatch));
        try {
            countDownLatch1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            countDownLatch1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 新版图片生成模型使用示例
     */
    @Test
    public void generateImageByDall_e_3() {
        Image image = Image.builder()
                .responseFormat(com.unfbx.chatgpt.entity.images.ResponseFormat.URL.getName())
                .model(Image.Model.DALL_E_3.getName())
                .prompt("一个咖啡杯，上面印刷Unfbx四个字母。")
                .n(1)
                .quality(Image.Quality.HD.getName())
                .size(SizeEnum.size_1024_1792.getName())
                .style(Image.Style.NATURAL.getName())
                .build();
        ImageResponse imageResponse = client.genImages(image);
//        ImageResponse imageResponse = client.genImages("一个咖啡杯，上面印刷Unfbx四个字母。");
        System.out.println(imageResponse.getData().get(0).getUrl());
    }

    /**
     * fineTuneJob使用示例
     */
    @Test
    public void uploadFile() {
        UploadFileResponse uploadFileResponse = client.uploadFile(new java.io.File("fine_tune_test_file.json1"));
        //file id = file-6KaBdtVlaassk9Y2P5ZjTqIC
        //ftjob-eBYBlcF1ZutjEZrT5oSKsmvO
        //file-KaNQn5V9YHlLqVQzo8CUMdIr
        System.out.println(uploadFileResponse.getId());
    }

    @Test
    public void fineTuneJob() {
        FineTuneJobResponse fineTuneJobResponse = client.fineTuneJob("file-KaNQn5V9YHlLqVQzo8CUMdIr");
        System.out.println(fineTuneJobResponse.toString());
        //job id = ftjob-5WQr0bZ7grvjnY3Or2sqiixl
    }

    @Test
    public void fineTuneJobs() {
//        FineTuneJobListResponse<FineTuneJobResponse> jobListResponse = client.fineTuneJobs("ftjob-cG7zIraBhAkq5Ybs7311lH7t", 5);
        FineTuneJobListResponse<FineTuneJobResponse> jobListResponse = client.fineTuneJobs(null, 20);
        System.out.println(jobListResponse);
    }


    @Test
    public void retrieveFineTuneJob() {
        FineTuneJobResponse fineTuneJobResponse = client.retrieveFineTuneJob("ftjob-5WQr0bZ7grvjnY3Or2sqiixl");
        System.out.println(fineTuneJobResponse);
    }

    //
    @Test
    public void cancelFineTuneJob() {
        FineTuneJobResponse fineTuneJobResponse = client.cancelFineTuneJob("ftjob-cG7zIraBhAkq5Ybs7311lH7t");
        System.out.println(fineTuneJobResponse);
    }

    @Test
    public void fineTuneJobEvents() {
        FineTuneJobListResponse<FineTuneJobEvent> listResponse = client.fineTuneJobEvents("ftjob-5WQr0bZ7grvjnY3Or2sqiixl", null, 20);
//        FineTuneJobListResponse<FineTuneJobEvent> listResponse = client.fineTuneJobEvents("ftjob-5WQr0bZ7grvjnY3Or2sqiixl", "ftevent-WwB8lpWxhjgUJX9DYdb47zJe", 20);
        listResponse.getData().forEach(e -> System.out.println(e.getMessage()));
        /**
         * The job has successfully completed
         * New fine-tuned model created: ft:gpt-3.5-turbo-1106:personal::8K5KwJTU
         * Step 91/100: training loss=0.45
         * Step 81/100: training loss=0.00
         * Step 71/100: training loss=0.00
         * Step 61/100: training loss=0.94
         * Step 51/100: training loss=0.19
         * Step 41/100: training loss=0.06
         * Step 31/100: training loss=0.95
         * Step 21/100: training loss=1.99
         * Step 11/100: training loss=2.50
         * Step 1/100: training loss=5.42
         * Fine-tuning job started
         * Files validated, moving job to queued state
         * Validating training file: file-KaNQn5V9YHlLqVQzo8CUMdIr
         * Created fine-tuning job: ftjob-5WQr0bZ7grvjnY3Or2sqiixl
         *
         * Process finished with exit code 0
         */
    }

    @Test
    public void fineTuneJobModelChat() {
        Message message1 = Message.builder().role(Message.Role.SYSTEM).content("OnBot是一个聊天机器人。").build();
        Message message2 = Message.builder().role(Message.Role.USER).content("OnBot请问：Chatgpt-java的作者是谁？").build();
        List<Message> messages = new ArrayList<>(2);
        messages.add(message1);
        messages.add(message2);
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .messages(messages)
                .model("ft:gpt-3.5-turbo-1106:personal::8K5KwJTU")
                .build();
        ChatCompletionResponse chatCompletionResponse = client.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices().forEach(e -> {
            System.out.println(e.getMessage());
            //返回值：Message(content=作者是Unfbx，个人网站：https://www.unfbx.com)
        });
    }

    @Test
    public void models() {
        List<Model> models = client.models();
        System.out.println(models);
    }

    /**
     * tts使用示例
     */
    @Test
    public void textToSpeed() {
        TextToSpeech textToSpeech = TextToSpeech.builder()
                .model(TextToSpeech.Model.TTS_1_HD.getName())
                .input("OpenAI官方Api的Java SDK，可以快速接入项目使用。目前支持OpenAI官方全部接口，同时支持Tokens计算。官方github地址：https://github.com/Grt1228/chatgpt-java。欢迎star。")
                .voice(TtsVoice.NOVA.getName())
                .responseFormat(TtsFormat.MP3.getName())
                .build();
        File file = new File("C:\\Users\\***\\Desktop\\test.mp3");
        client.textToSpeech(textToSpeech, new Callback<ResponseBody>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                InputStream inputStream = response.body().byteStream();
                //创建文件
                if (!file.exists()) {
                    if (!file.getParentFile().exists())
                        file.getParentFile().mkdir();
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                        log.error("createNewFile IOException");
                    }
                }

                OutputStream os = null;
                try {
                    os = new BufferedOutputStream(new FileOutputStream(file));
                    byte data[] = new byte[8192];
                    int len;
                    while ((len = inputStream.read(data, 0, 8192)) != -1) {
                        os.write(data, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (os != null) {
                            os.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取一个词语
     *
     * @param wordParam
     * @return
     */
    public String getOneWord(WordParam wordParam) {

        List<String> zh = Arrays.asList("大香蕉", "哈密瓜", "苹果");
        List<String> en = Arrays.asList("apple", "banana", "cantaloupe");
        if (wordParam.getLanguage().equals("zh")) {
            for (String e : zh) {
                if (e.length() == wordParam.getWordLength()) {
                    return e;
                }
            }
        }
        if (wordParam.getLanguage().equals("en")) {
            for (String e : en) {
                if (e.length() == wordParam.getWordLength()) {
                    return e;
                }
            }
        }
        return "西瓜";
    }

    @Test
    public void testInput() {
        System.out.println(getOneWord(WordParam.builder().wordLength(2).language("zh").build()));
    }

    @Data
    @Builder
    static class WordParam {
        private int wordLength;
        @Builder.Default
        private String language = "zh";
    }
}
