package com.unfbx.chatgpt.v1_1_2;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.FirstKeyStrategy;
import com.unfbx.chatgpt.OpenAiClient;

import com.unfbx.chatgpt.OpenAiClientFunctionTest;
import com.unfbx.chatgpt.entity.chat.*;
import com.unfbx.chatgpt.entity.chat.tool.ToolCallFunction;
import com.unfbx.chatgpt.entity.chat.tool.ToolCalls;
import com.unfbx.chatgpt.entity.chat.tool.Tools;
import com.unfbx.chatgpt.entity.chat.tool.ToolsFunction;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
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
//                .apiKey(Arrays.asList("*********************"))
                //自定义key的获取策略：默认KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new FirstKeyStrategy())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传,(关注公众号回复：openai ，获取免费的测试代理地址)
                .apiHost("https://dgr.life/")
                .build();
    }


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
                .responseFormat(ResponseFormat.builder().type(ResponseFormat.Type.JSON_OBJECT.getName()).build())
                .model(ChatCompletion.Model.GPT_4_VISION_PREVIEW.getName())
                .build();
        ChatCompletionResponse chatCompletionResponse = client.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices().forEach(e -> System.out.println(e.getMessage()));
    }

    @Test
    public void diyReturnModelChat() {
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


    @Test
    public void toolsChat() {

        //模型：GPT_3_5_TURBO_16K_0613
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
        log.info("自定义的方法返回值：{}",chatCompletionResponseV2.getChoices().get(0).getMessage().getContent());

    }






    /**
     * 获取一个词语
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
