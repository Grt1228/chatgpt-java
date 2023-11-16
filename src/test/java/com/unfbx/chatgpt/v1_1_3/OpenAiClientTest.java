package com.unfbx.chatgpt.v1_1_3;


import com.unfbx.chatgpt.FirstKeyStrategy;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.assistant.*;
import com.unfbx.chatgpt.entity.chat.BaseChatCompletion;
import com.unfbx.chatgpt.entity.common.DeleteResponse;
import com.unfbx.chatgpt.entity.common.PageRequest;
import com.unfbx.chatgpt.entity.files.File;
import com.unfbx.chatgpt.entity.files.UploadFileResponse;
import com.unfbx.chatgpt.entity.assistant.thread.ModifyThread;
import com.unfbx.chatgpt.entity.assistant.thread.Thread;
import com.unfbx.chatgpt.entity.assistant.thread.ThreadMessage;
import com.unfbx.chatgpt.entity.assistant.thread.ThreadResponse;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
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
 * 2023-11-15
 */
@Slf4j
public class OpenAiClientTest {

    private OpenAiClient client;

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
                .proxy(proxy)
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(new OpenAiResponseInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        client = OpenAiClient.builder()
                //支持多key传入，请求时候随机选择
                .apiKey(Arrays.asList("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"))
                //自定义key的获取策略：默认KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new FirstKeyStrategy())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传,(关注公众号回复：openai ，获取免费的测试代理地址)
                .apiHost("https://api.openai.com/")
                .build();
    }

    /**
     * 创建助手
     */
    @Test
    public void assistant() {
        Tool tool = Tool.builder().type(Tool.Type.CODE_INTERPRETER.getName()).build();
        Assistant assistant = Assistant.builder()
                .model(BaseChatCompletion.Model.GPT_3_5_TURBO_16K_0613.getName())
                .name("UnfbxBotV3")
                .description("UnfbxBotV3是一个自定义数学bot。")
                .instructions("你是一个数学导师。当我问你问题时，编写并运行Java代码来回答问题。")
                .tools(Collections.singletonList(tool))
//                .fileIds()
                .metadata(new HashMap())
                .build();
        AssistantResponse assistants = client.assistant(assistant);
        System.out.println(assistants.getId());
    }

    /**
     * 获取助手信息
     */
    @Test
    public void retrieveAssistant() {
        AssistantResponse assistants = client.retrieveAssistant("asst_V7ITX0eKgpm1tMU7L0bTlwaR");
        System.out.println(assistants.getId());
    }

    /**
     * 修改助手信息
     */
    @Test
    public void modifyAssistant() {
        Tool tool = Tool.builder().type(Tool.Type.CODE_INTERPRETER.getName()).build();
        Assistant assistant = Assistant.builder()
                .model(BaseChatCompletion.Model.GPT_3_5_TURBO_16K_0613.getName())
                .name("UnfbxBotPlus")
                .description("UnfbxBotPlus是一个自定义数学bot。")
                .instructions("你是一个数学导师。当我问你问题时，编写并运行Java代码来回答问题。")
                .tools(Collections.singletonList(tool))
//                .fileIds()
                .metadata(new HashMap())
                .build();
        AssistantResponse assistants = client.modifyAssistant("asst_V7ITX0eKgpm1tMU7L0bTlwaR", assistant);
        System.out.println(assistants.getId());
    }

    /**
     * 删除助手信息
     */
    @Test
    public void deleteAssistant() {
        DeleteResponse response = client.deleteAssistant("asst_V7ITX0eKgpm1tMU7L0bTlwaR");
        System.out.println(response.isDeleted());
    }


    /**
     * 获取助手信息
     */
    @Test
    public void assistants() {
//        PageRequest pageRequest = PageRequest.builder().build();
        PageRequest pageRequest = PageRequest
                .builder()
                .order(PageRequest.Order.ASC.getName())
                .limit(3)
                .after("asst_F6OCbL3dVG2V58mWJudohOVq")
                .before("asst_l12cLNjXqqI3Dm7DOARKa1s7")
                .build();
        AssistantListResponse<AssistantResponse> assistants = client.assistants(pageRequest);
        assistants.getData().forEach(e -> {
            System.out.println(e.getName());
            System.out.println(e.getId());
            System.out.println("--------------");
        });
    }


    @Test
    public void uploadFile() {
        UploadFileResponse uploadFileResponse = client.uploadFile("assistants", new java.io.File("C:\\Users\\\\FLJS188\\Desktop\\手册.pdf"));
        System.out.println(uploadFileResponse.getId());//file-ehPUcWnsB4XnMAhnVH0nSJjD
        System.out.println(uploadFileResponse.getFilename());
    }

    @Test
    public void files() {
        List<File> files = client.files();
        files.forEach(e -> {
            System.out.println(e.getId() + " | " + e.getFilename() + " | " + e.getPurpose() + " | " + e.getStatus() + " | " + e.getStatusDetails());
        });
//        file-RFWxhYSsE4pNS4KcAEs7mzuW | 手册.pdf | assistants | processed | null
//        file-VnfAMgLr4XSd7SHZTkQh5rkJ | 手册.pdf | assistants | processed | null
//        file-ehPUcWnsB4XnMAhnVH0nSJjD | 手册.pdf | assistants | processed | null
    }

    /**
     * 创建助手文件
     */
    @Test
    public void assistantFile() {
        AssistantFile assistantFile = AssistantFile.builder().fileId("file-RFWxhYSsE4pNS4KcAEs7mzuW").build();
        AssistantFileResponse assistantFileResponse = client.assistantFile("asst_F6OCbL3dVG2V58mWJudohOVq", assistantFile);
        System.out.println(assistantFileResponse.getId());
    }


    /**
     * 检索助手文件
     */
    @Test
    public void retrieveAssistantFile() {
        AssistantFileResponse assistantFileResponse = client.retrieveAssistantFile("asst_F6OCbL3dVG2V58mWJudohOVq", "file-ehPUcWnsB4XnMAhnVH0nSJjD");
        System.out.println(assistantFileResponse.getId());
    }

    /**
     * 删除助手文件
     */
    @Test
    public void deleteAssistantFile() {
        DeleteResponse deleteResponse = client.deleteAssistantFile("asst_F6OCbL3dVG2V58mWJudohOVq", "file-VnfAMgLr4XSd7SHZTkQh5rkJ");
        System.out.println(deleteResponse.isDeleted());
    }

    /**
     * 助手文件列表
     */
    @Test
    public void assistantFiles() {
        PageRequest pageRequest = PageRequest.builder().build();
//        PageRequest pageRequest = PageRequest
//                .builder()
//                .order(PageRequest.Order.DESC.getName())
//                .limit(1)
//                .after("file-RFWxhYSsE4pNS4KcAEs7mzuW")
//                .before("file-ehPUcWnsB4XnMAhnVH0nSJjD")
//                .build();
        AssistantListResponse<AssistantFileResponse> response = client.assistantFiles("asst_F6OCbL3dVG2V58mWJudohOVq", pageRequest);
        response.getData().forEach(e -> {
            System.out.println(e.getId() + " | " + e.getAssistantId());
        });
//        file-RFWxhYSsE4pNS4KcAEs7mzuW | asst_F6OCbL3dVG2V58mWJudohOVq
//        file-VnfAMgLr4XSd7SHZTkQh5rkJ | asst_F6OCbL3dVG2V58mWJudohOVq
//        file-ehPUcWnsB4XnMAhnVH0nSJjD | asst_F6OCbL3dVG2V58mWJudohOVq
    }


    /**
     * 创建线程
     */
    @Test
    public void thread() {
        ThreadMessage threadMessage = ThreadMessage
                .builder()
                .content("hello.")
                .role(ThreadMessage.Role.USER.getName()).build();
        Thread thread = Thread.builder()
                .metadata(new HashMap())
                .messages(Collections.singletonList(threadMessage))
                .build();
        ThreadResponse threadResponse = client.thread(thread);
        System.out.println(threadResponse.getId());
        //thread_lYig5XqdtEn5UDzsZh53qfq1
    }

    /**
     * 获取助手信息
     */
    @Test
    public void retrieveThread() {
        ThreadResponse threadResponse = client.retrieveThread("thread_lYig5XqdtEn5UDzsZh53qfq1");
        System.out.println(threadResponse.getId());
    }

    /**
     * 修改县城信息
     */
    @Test
    public void modifyThread() {
        Map<String,String> map = new HashMap<>();
        map.put("hello", "unfbx");
        ModifyThread mod = ModifyThread.builder().metadata(map).build();
        ThreadResponse threadResponse = client.modifyThread("thread_lYig5XqdtEn5UDzsZh53qfq1", mod);
        System.out.println(threadResponse.getId());
    }

    /**
     * 删除线程信息
     */
    @Test
    public void deleteThread() {
        DeleteResponse response = client.deleteThread("thread_yrzSAYVLhaDTdKsx1InD6o9R");
        System.out.println(response.isDeleted());
    }

}
