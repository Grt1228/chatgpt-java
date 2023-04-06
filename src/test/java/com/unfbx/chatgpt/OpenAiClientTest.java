package com.unfbx.chatgpt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.unfbx.chatgpt.entity.billing.CreditGrantsResponse;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
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
import com.unfbx.chatgpt.entity.images.*;
import com.unfbx.chatgpt.entity.models.Model;
import com.unfbx.chatgpt.entity.moderations.Moderation;
import com.unfbx.chatgpt.entity.moderations.ModerationResponse;
import com.unfbx.chatgpt.entity.whisper.Whisper;
import com.unfbx.chatgpt.entity.whisper.WhisperResponse;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import com.unfbx.chatgpt.utils.TikTokensUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 描述： 测试类
 *
 * @author https:www.unfbx.com
 *  2023-02-11
 */
@Slf4j
public class OpenAiClientTest {

    private OpenAiClient v2;

    @Before
    public void before() {
        //可以为null
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .proxy(proxy)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new OpenAiResponseInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        v2 = OpenAiClient.builder()
                //支持多key传入，请求时候随机选择
                .apiKey(Arrays.asList("sk-***********","sk-*********"))
                //自定义key的获取策略：默认KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new FirstKeyStrategy())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传
//                .apiHost("https://自己代理的服务器地址/")
                .build();
    }

    @Test
    public void chatTokensTest() {
        //聊天模型：gpt-3.5
        List<Message> messages = new ArrayList<>(2);
        messages.add(Message.builder().role(Message.Role.USER).content("关注微信公众号：程序员的黑洞。").build());
        messages.add(Message.builder().role(Message.Role.USER).content("进入chatgpt-java交流群获取最新版本更新通知。").build());
        ChatCompletion chatCompletion = ChatCompletion
                .builder()
                .messages(messages)
                .maxTokens((4096 - TikTokensUtil.tokens(ChatCompletion.Model.GPT_3_5_TURBO.getName(),messages)))
                .build();
        ChatCompletionResponse chatCompletionResponse = v2.chatCompletion(chatCompletion);
        //获取请求的tokens数量
        long tokens = chatCompletion.tokens();
        //这种方式也可以
//        long tokens = TikTokensUtil.tokens(chatCompletion.getModel(),messages);
        log.info("Message集合文本：【{}】", messages, tokens);
        log.info("本地计算的请求的tokens数{}", tokens);
        log.info("本地计算的返回的tokens数{}", TikTokensUtil.tokens(chatCompletion.getModel(),chatCompletionResponse.getChoices().get(0).getMessage().getContent()));
        log.info("---------------------------------------------------");
        log.info("Open AI 官方计算的总的tokens数{}", chatCompletionResponse.getUsage().getTotalTokens());
        log.info("Open AI 官方计算的请求的tokens数{}", chatCompletionResponse.getUsage().getPromptTokens());
        log.info("Open AI 官方计算的返回的tokens数{}", chatCompletionResponse.getUsage().getCompletionTokens());
    }

    @Test
    public void testJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setTimeZone(TimeZone.getTimeZone("GMT+8"))
                .setLocale(Locale.CHINA);
        Completion completion = Completion.builder().prompt("你好啊").build();
        String jsonStr = objectMapper.writeValueAsString(completion);
        Completion completion1 = objectMapper.readValue(jsonStr, Completion.class);
    }

    @Test
    public void creditGrants() {
        CreditGrantsResponse creditGrantsResponse = v2.creditGrants();
        log.info("账户总余额（美元）：{}", creditGrantsResponse.getTotalGranted());
        log.info("账户总使用金额（美元）：{}", creditGrantsResponse.getTotalUsed());
        log.info("账户总剩余金额（美元）：{}", creditGrantsResponse.getTotalAvailable());
    }
    @Test
    public void speechToTextTranscriptions() {
        //语音转文字
        WhisperResponse whisperResponse =
                v2.speechToTextTranscriptions(new java.io.File("C:\\***********\\1.m4a")
                , Whisper.Model.WHISPER_1);
        System.out.println(whisperResponse.getText());
    }

    @Test
    public void speechToTextTranslations() {
        //语音转文字
        WhisperResponse whisperResponse =
                v2.speechToTextTranslations(new java.io.File("C:\\***********\\1.m4a")
                        , Whisper.Model.WHISPER_1);
        System.out.println(whisperResponse.getText());
    }

    @Test
    public void chat() {
        //聊天模型：gpt-3.5
        Message message = Message.builder().role(Message.Role.USER).content("你好啊我的伙伴！").build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
        ChatCompletionResponse chatCompletionResponse = v2.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices().forEach(e -> {
            System.out.println(e.getMessage());
        });
    }

    @Test
    public void models() {
        List<Model> models = v2.models();
        models.forEach(e -> {
            System.out.print(e.getOwnedBy() + " ");
            System.out.print(e.getId() + " ");
            System.out.println(e.getObject() + " ");
        });
    }

    @Test
    public void model() {
        Model model = v2.model("code-davinci-002");
        System.out.println(model.toString());
    }

    @Test
    public void completions() {
//        CompletionResponse completions = v2.completions("Java Stream list to map");
//        Arrays.stream(completions.getChoices()).forEach(System.out::println);

        CompletionResponse completions = v2.completions("我想申请转专业，从计算机专业转到会计学专业，帮我完成一份两百字左右的申请书");
        Arrays.stream(completions.getChoices()).forEach(System.out::println);
    }

    //对话测试
    @Test
    public void completionsV3() {
        String question = "Human: 帮我把下面的文本翻译成英文；我爱你中国\n";
        Completion q = Completion.builder()
                .prompt(question)
                .stop(Arrays.asList(" Human:", " Bot:"))

                .echo(true)
                .build();
        CompletionResponse completions = v2.completions(q);
        String text = completions.getChoices()[0].getText();

        q.setPrompt(text + "\n" + "再翻译成韩文\n");
        completions = v2.completions(q);
        text = completions.getChoices()[0].getText();

        q.setPrompt(text + "\n" + "再翻译成日文\n");
        completions = v2.completions(q);
        text = completions.getChoices()[0].getText();
        System.out.println(text);
    }

    @Test
    public void completionsV2() {
        Completion q = Completion.builder()
                .prompt("三体人是什么？")
                .model("ada:ft-org-DL6GzliwY20i7Lxr5pUAoKUH:2023-02-16-05-42-02")
                .build();
        CompletionResponse completions = v2.completions(q);
        System.out.println(completions);
    }

    @Test
    public void editText() {
        //文本修改
//        Edit edit = Edit.builder().input("我爱你麻麻").instruction("帮我修改错别字").model(Edit.Model.TEXT_DAVINCI_EDIT_001.getName()).build();
        //代码修改 NB....
        Edit edit = Edit.builder().input("System.out.pri(\"AAAAA\");").instruction("帮我修改这个java代码").model(Edit.Model.CODE_DAVINCI_EDIT_001.getName()).build();
        EditResponse editResponse = v2.edit(edit);
        System.out.println(editResponse);
    }


    @Test
    public void genImages() {
        Image image = Image.builder().prompt("电脑画面").build();
        ImageResponse imageResponse = v2.genImages(image);
        System.out.println(imageResponse);
    }

    @Test
    public void genImagesV2() {
        ImageResponse imageResponse = v2.genImages("睡着的小朋友");
        System.out.println(imageResponse);
    }

    /**
     * Invalid input image - format must be in ['RGBA', 'LA', 'L'], got RGB.
     */

    @Test
    public void editImageV2() {
        ImageEdit imageEdit = ImageEdit.builder().prompt("去除图片中的文字").build();
        List<Item> images = v2.editImages(new java.io.File("C:\\Users\\FLJS188\\Desktop\\o.png"),
                imageEdit);
        System.out.println(images);
    }

    @Test
    public void editImageV3() {
        List<Item> images = v2.editImages(new java.io.File("C:\\Users\\***\\Desktop\\1.png"),
                "去除图片中的文字");
        System.out.println(images);
    }

    @Test
    public void editImage() {
        List<Item> images = v2.editImages(new java.io.File("C:\\Users\\***\\Desktop\\1.png"),
                "去除图片中的文字");
        System.out.println(images);
    }


    @Test
    public void variationsImagesV2() {
        ImageVariations imageVariations = ImageVariations.builder().build();
        ImageResponse imageResponse = v2.variationsImages(new java.io.File("C:\\Users\\***\\Desktop\\12.png"), imageVariations);
        System.out.println(imageResponse);
    }

    @Test
    public void variationsImages() {
        ImageResponse imageResponse = v2.variationsImages(new java.io.File("C:\\Users\\***\\Desktop\\12.png"));
        System.out.println(imageResponse);
    }


    @Test
    public void embeddingsV2() {
        Embedding embedding = Embedding.builder().input("我爱你亲爱的姑娘").build();
        EmbeddingResponse embeddings = v2.embeddings(embedding);
        System.out.println(embeddings);
    }


    @Test
    public void embeddings() {
        EmbeddingResponse embeddings = v2.embeddings("The food was delicious and the waiter...");
        System.out.println(embeddings);
    }


    @Test
    public void files() {
        List<File> files = v2.files();
        System.out.println(files);
    }

    @Test
    public void retrieveFile() {
        File files = v2.retrieveFile("file-EHB0Wp3wcZu6tpbwkB6xeiEd");
        System.out.println(files);
    }

    /**
     * 不支持免费用户： To help mitigate abuse, downloading of fine-tune training files is disabled for free accounts.
     * 暂时没有测试
     */
    @Test
    public void retrieveFileContent() {
//        ResponseBody responseBody = v2.retrieveFileContent("file-EHB0Wp3wcZu6tpbwkB6xeiEd");
//        System.out.println(responseBody);
    }

    @Test
    public void uploadFileV1() {
        UploadFileResponse uploadFileResponse = v2.uploadFile(new java.io.File("C:\\Users\\***\\Desktop\\2.txt"));
        System.out.println(uploadFileResponse);
    }

    @Test
    public void uploadFileV2() {
        UploadFileResponse uploadFileResponse = v2.uploadFile("fine-tune", new java.io.File("C:\\Users\\***\\Desktop\\2.txt"));
        System.out.println(uploadFileResponse);
    }

    @Test
    public void deleteFile() {
        DeleteResponse deleteResponse = v2.deleteFile("file-GreIoKq6lWHvq8PDwDZIGJjm");
        System.out.println(deleteResponse);
    }

    @Test
    public void moderations() {
        ModerationResponse moderations = v2.moderations("I want to kill them.");
        System.out.println(moderations);
    }

    @Test
    public void moderationsv3() {
        List<String> list = Arrays.asList("I want to kill them.");
        ModerationResponse moderations = v2.moderations(list);
        System.out.println(moderations);
    }

    @Test
    public void moderationsV2() {
        Moderation moderation = Moderation.builder().input(Arrays.asList("I want to kill them.")).build();
        ModerationResponse moderations = v2.moderations(moderation);
        System.out.println(moderations);
    }


    @Test
    public void engines() {
        List<Engine> engines = v2.engines();
        System.out.println(engines);
    }

    @Test
    public void engine() {
        Engine engines = v2.engine("code-davinci-002");
        System.out.println(engines);
    }

    @Test
    public void fineTune() {
        FineTuneResponse fineTuneResponse = v2.fineTune("file-EHB0Wp3wcZu6tpbwkB6xeiEd");
        System.out.println(fineTuneResponse);
    }
    @Test
    public void fineTuneV2() {
        FineTune fineTune = FineTune.builder()
                .trainingFile("file-OcQb9zg35cxa4WLBZJ9K2523")
                .suffix("grttttttttt")
                .model(FineTune.Model.ADA.getName())
                .build();
        FineTuneResponse fineTuneResponse = v2.fineTune(fineTune);
        System.out.println(fineTuneResponse);
    }

    @Test
    public void fineTunes() {
        List<FineTuneResponse> fineTuneResponses = v2.fineTunes();
        System.out.println(fineTuneResponses);
    }

    @Test
    public void retrieveFineTune() {
        FineTuneResponse fineTuneResponses = v2.retrieveFineTune("ft-bU0xJzVfrgOjqoy1e9lC2oDP");
        System.out.println(fineTuneResponses);
    }

    @Test
    public void cancelFineTune() {
        //status发生变化 pending -> cancelled
        FineTuneResponse fineTuneResponses = v2.cancelFineTune("ft-KohbEOCbPyNTyQmt5UV1F1cb");
        System.out.println(fineTuneResponses);
    }

    @Test
    public void fineTuneEvents() {
        List<Event> events = v2.fineTuneEvents("ft-KohbEOCbPyNTyQmt5UV1F1cb");
        System.out.println(events);
    }

    @Test
    public void deleteFineTuneModel() {
        FineTuneDeleteResponse deleteResponse = v2.deleteFineTuneModel("curie:ft-winter-2023-02-16-10-24-10");
        System.out.println(deleteResponse);
    }
}
