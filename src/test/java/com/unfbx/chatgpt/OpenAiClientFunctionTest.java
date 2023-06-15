package com.unfbx.chatgpt;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.entity.chat.*;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.DynamicKeyOpenAiAuthInterceptor;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 描述： 测试类
 *
 * @author https:www.unfbx.com
 * 2023-06-14
 */
@Slf4j
public class OpenAiClientFunctionTest {

    private OpenAiClient openAiClient;
    private OpenAiStreamClient openAiStreamClient;

    @Before
    public void before() {
        //可以为null
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
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
        openAiClient = OpenAiClient.builder()
                //支持多key传入，请求时候随机选择
                .apiKey(Arrays.asList("sk-******************"))
                //自定义key的获取策略：默认KeyRandomStrategy
                .keyStrategy(new KeyRandomStrategy())
                .authInterceptor(new DynamicKeyOpenAiAuthInterceptor())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传,(关注公众号回复：openai ，获取免费的测试代理地址)
                .apiHost("https://dgr.life/")
                .build();

        openAiStreamClient = OpenAiStreamClient.builder()
                //支持多key传入，请求时候随机选择
                .apiKey(Arrays.asList("sk-*********************"))
                //自定义key的获取策略：默认KeyRandomStrategy
                .keyStrategy(new KeyRandomStrategy())
                .authInterceptor(new DynamicKeyOpenAiAuthInterceptor())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传,(关注公众号回复：openai ，获取免费的测试代理地址)
                .apiHost("https://dgr.life/")
                .build();
    }

    /**
     * 阻塞输出日志如下：
     *
     * [main] INFO com.unfbx.chatgpt.OpenAiClientFunctionTest - 自定义的方法返回值：词语：苹果
     *
     * 用途：苹果是一种水果，具有多种用途。以下是苹果的几种常见用途：
     *
     * 1. 直接吃：苹果可以直接食用，具有清爽的口感和丰富的营养成分，是一种健康的零食选择。
     *
     * 2. 做沙拉：苹果可以切成块状或丝状，加入其他蔬菜和调味料，制作成沙拉。苹果的甜脆口感可以为沙拉增添口感和风味。
     *
     * 3. 售卖：苹果是一种常见的水果，可以被商家售卖。人们可以购买苹果作为食物或礼物，满足自己或他人的需求。
     *
     * 总之，苹果是一种多功能的水果，可以直接食用，也可以用于制作沙拉，同时也是一种常见的商业商品。
     */
    @Test
    public void chatFunction() {

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
                .required(Arrays.asList("wordLength")).build();
        Functions functions = Functions.builder()
                .name("getOneWord")
                .description("获取一个指定长度和语言类型的词语")
                .parameters(parameters)
                .build();

        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .messages(Arrays.asList(message))
                .functions(Arrays.asList(functions))
                .functionCall("auto")
                .model(ChatCompletion.Model.GPT_3_5_TURBO_16K_0613.getName())
                .build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);

        ChatChoice chatChoice = chatCompletionResponse.getChoices().get(0);
        log.info("构造的方法值：{}", chatChoice.getMessage().getFunctionCall());
        log.info("构造的方法名称：{}", chatChoice.getMessage().getFunctionCall().getName());
        log.info("构造的方法参数：{}", chatChoice.getMessage().getFunctionCall().getArguments());
        WordParam wordParam = JSONUtil.toBean(chatChoice.getMessage().getFunctionCall().getArguments(), WordParam.class);
        String oneWord = getOneWord(wordParam);

        FunctionCall functionCall = FunctionCall.builder()
                .arguments(chatChoice.getMessage().getFunctionCall().getArguments())
                .name("getOneWord")
                .build();
        Message message2 = Message.builder().role(Message.Role.ASSISTANT).content("方法参数").functionCall(functionCall).build();
        String content
                = "{ " +
                "\"wordLength\": \"3\", " +
                "\"language\": \"zh\", " +
                "\"word\": \"" + oneWord + "\"," +
                "\"用途\": [\"直接吃\", \"做沙拉\", \"售卖\"]" +
                "}";
        Message message3 = Message.builder().role(Message.Role.FUNCTION).name("getOneWord").content(content).build();
        List<Message> messageList = Arrays.asList(message, message2, message3);
        ChatCompletion chatCompletionV2 = ChatCompletion
                .builder()
                .messages(messageList)
                .model(ChatCompletion.Model.GPT_3_5_TURBO_16K_0613.getName())
                .build();
        ChatCompletionResponse chatCompletionResponseV2 = openAiClient.chatCompletion(chatCompletionV2);
        log.info("自定义的方法返回值：{}",chatCompletionResponseV2.getChoices().get(0).getMessage().getContent());
    }


    /**
     * 流式输出最后输出日志如下
     * .........省略省略省略省略省略............
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：{"id":"chatcmpl-7RXBSbHZPGCbiV9uO9iPlgoZ56t9y","object":"chat.completion.chunk","created":1686796770,"model":"gpt-3.5-turbo-16k-0613","choices":[{"index":0,"delta":{"content":"、"},"finish_reason":null}]}
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：{"id":"chatcmpl-7RXBSbHZPGCbiV9uO9iPlgoZ56t9y","object":"chat.completion.chunk","created":1686796770,"model":"gpt-3.5-turbo-16k-0613","choices":[{"index":0,"delta":{"content":"水"},"finish_reason":null}]}
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：{"id":"chatcmpl-7RXBSbHZPGCbiV9uO9iPlgoZ56t9y","object":"chat.completion.chunk","created":1686796770,"model":"gpt-3.5-turbo-16k-0613","choices":[{"index":0,"delta":{"content":"果"},"finish_reason":null}]}
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：{"id":"chatcmpl-7RXBSbHZPGCbiV9uO9iPlgoZ56t9y","object":"chat.completion.chunk","created":1686796770,"model":"gpt-3.5-turbo-16k-0613","choices":[{"index":0,"delta":{"content":"摊"},"finish_reason":null}]}
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：{"id":"chatcmpl-7RXBSbHZPGCbiV9uO9iPlgoZ56t9y","object":"chat.completion.chunk","created":1686796770,"model":"gpt-3.5-turbo-16k-0613","choices":[{"index":0,"delta":{"content":"等"},"finish_reason":null}]}
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：{"id":"chatcmpl-7RXBSbHZPGCbiV9uO9iPlgoZ56t9y","object":"chat.completion.chunk","created":1686796770,"model":"gpt-3.5-turbo-16k-0613","choices":[{"index":0,"delta":{"content":"渠"},"finish_reason":null}]}
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：{"id":"chatcmpl-7RXBSbHZPGCbiV9uO9iPlgoZ56t9y","object":"chat.completion.chunk","created":1686796770,"model":"gpt-3.5-turbo-16k-0613","choices":[{"index":0,"delta":{"content":"道"},"finish_reason":null}]}
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：{"id":"chatcmpl-7RXBSbHZPGCbiV9uO9iPlgoZ56t9y","object":"chat.completion.chunk","created":1686796770,"model":"gpt-3.5-turbo-16k-0613","choices":[{"index":0,"delta":{"content":"进行"},"finish_reason":null}]}
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：{"id":"chatcmpl-7RXBSbHZPGCbiV9uO9iPlgoZ56t9y","object":"chat.completion.chunk","created":1686796770,"model":"gpt-3.5-turbo-16k-0613","choices":[{"index":0,"delta":{"content":"销"},"finish_reason":null}]}
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：{"id":"chatcmpl-7RXBSbHZPGCbiV9uO9iPlgoZ56t9y","object":"chat.completion.chunk","created":1686796770,"model":"gpt-3.5-turbo-16k-0613","choices":[{"index":0,"delta":{"content":"售"},"finish_reason":null}]}
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：{"id":"chatcmpl-7RXBSbHZPGCbiV9uO9iPlgoZ56t9y","object":"chat.completion.chunk","created":1686796770,"model":"gpt-3.5-turbo-16k-0613","choices":[{"index":0,"delta":{"content":"。"},"finish_reason":null}]}
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：{"id":"chatcmpl-7RXBSbHZPGCbiV9uO9iPlgoZ56t9y","object":"chat.completion.chunk","created":1686796770,"model":"gpt-3.5-turbo-16k-0613","choices":[{"index":0,"delta":{},"finish_reason":"stop"}]}
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据：[DONE]
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI返回数据结束了
     * [OkHttp https://dgr.life/...] INFO com.unfbx.chatgpt.ConsoleEventSourceListenerV2 - OpenAI关闭sse连接...
     */
    @Test
    public void streamChatFunction() {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        ConsoleEventSourceListenerV2 eventSourceListener = new ConsoleEventSourceListenerV2(countDownLatch);

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
                .required(Arrays.asList("wordLength")).build();
        Functions functions = Functions.builder()
                .name("getOneWord")
                .description("获取一个指定长度和语言类型的词语")
                .parameters(parameters)
                .build();

        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .messages(Arrays.asList(message))
                .functions(Arrays.asList(functions))
                .functionCall("auto")
                .model(ChatCompletion.Model.GPT_3_5_TURBO_16K_0613.getName())
                .build();
        openAiStreamClient.streamChatCompletion(chatCompletion, eventSourceListener);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String args = eventSourceListener.getArgs();
        log.info("构造的方法参数：{}", args);
        WordParam wordParam = JSONUtil.toBean(args, WordParam.class);
        String oneWord = getOneWord(wordParam);

        FunctionCall functionCall = FunctionCall.builder()
                .arguments(args)
                .name("getOneWord")
                .build();
        Message message2 = Message.builder().role(Message.Role.ASSISTANT).content("方法参数").functionCall(functionCall).build();
        String content
                = "{ " +
                "\"wordLength\": \"3\", " +
                "\"language\": \"zh\", " +
                "\"word\": \"" + oneWord + "\"," +
                "\"用途\": [\"直接吃\", \"做沙拉\", \"售卖\"]" +
                "}";
        Message message3 = Message.builder().role(Message.Role.FUNCTION).name("getOneWord").content(content).build();
        List<Message> messageList = Arrays.asList(message, message2, message3);
        ChatCompletion chatCompletionV2 = ChatCompletion
                .builder()
                .messages(messageList)
                .model(ChatCompletion.Model.GPT_3_5_TURBO_16K_0613.getName())
                .build();
        CountDownLatch countDownLatch1 = new CountDownLatch(1);
        openAiStreamClient.streamChatCompletion(chatCompletionV2, new ConsoleEventSourceListenerV2(countDownLatch));
        try {
            countDownLatch1.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
