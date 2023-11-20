package com.unfbx.chatgpt.v1_1_3;


import com.unfbx.chatgpt.FirstKeyStrategy;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.assistant.*;
import com.unfbx.chatgpt.entity.assistant.message.MessageFileResponse;
import com.unfbx.chatgpt.entity.assistant.message.MessageResponse;
import com.unfbx.chatgpt.entity.assistant.message.ModifyMessage;
import com.unfbx.chatgpt.entity.assistant.run.*;
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
                .apiKey(Arrays.asList("sk-888888888888888888888888888888888888"))
                //自定义key的获取策略：默认KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new FirstKeyStrategy())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传,(关注公众号回复：openai ，获取免费的测试代理地址)
                .apiHost("https://dgr.life/")
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
        DeleteResponse response = client.deleteAssistant("asst_ksxfcLpWxm4YI479l6qRQ9xX");
        System.out.println(response.isDeleted());
    }


    /**
     * 获取助手信息
     * UnfbxBotV3
     * asst_Ay5j9BvBKtkgPdXMwnhz3Cwv
     * --------------
     * UnfbxBotV2
     * asst_zAWMSZwtoLK7W9k5FhlYNfqh
     * --------------
     * UnfbxBotV1
     * asst_V4Grgp2BQWAmH03Bs56E1H7c
     * --------------
     */
    @Test
    public void assistants() {
        PageRequest pageRequest = PageRequest.builder().build();
//        PageRequest pageRequest = PageRequest
//                .builder()
//                .order(PageRequest.Order.ASC.getName())
//                .limit(3)
//                .after("asst_F6OCbL3dVG2V58mWJudohOVq")
//                .before("asst_l12cLNjXqqI3Dm7DOARKa1s7")
//                .build();
        AssistantListResponse<AssistantResponse> assistants = client.assistants(pageRequest);
        assistants.getData().forEach(e -> {
            System.out.println(e.getName());
            System.out.println(e.getId());
            System.out.println("--------------");
        });
    }

    /**
     * file-tJoDYPF2MMlIOlvwfGbIV94D
     */
    @Test
    public void uploadFile() {
        UploadFileResponse uploadFileResponse = client.uploadFile("assistants", new java.io.File("C:\\Users\\\\grt\\Desktop\\文件.pdf"));
        System.out.println(uploadFileResponse.getId());//
        System.out.println(uploadFileResponse.getFilename());
    }

    @Test
    public void files() {
        List<File> files = client.files();
        files.forEach(e -> System.out.println(e.getId() + " | " + e.getFilename() + " | " + e.getPurpose() + " | " + e.getStatus() + " | " + e.getStatusDetails()));
//        file-RFWxhYSsE4pNS4KcAEs7mzuW | 手册.pdf | assistants | processed | null
//        file-VnfAMgLr4XSd7SHZTkQh5rkJ | 手册.pdf | assistants | processed | null
//        file-ehPUcWnsB4XnMAhnVH0nSJjD | 手册.pdf | assistants | processed | null
    }

    /**
     * 创建助手文件
     */
    @Test
    public void assistantFile() {
        AssistantFile assistantFile = AssistantFile.builder().fileId("file-tJoDYPF2MMlIOlvwfGbIV94D").build();
        AssistantFileResponse assistantFileResponse = client.assistantFile("asst_V4Grgp2BQWAmH03Bs56E1H7c", assistantFile);
        System.out.println(assistantFileResponse.getId());
    }


    /**
     * 检索助手文件
     */
    @Test
    public void retrieveAssistantFile() {
        AssistantFileResponse assistantFileResponse = client.retrieveAssistantFile("asst_V4Grgp2BQWAmH03Bs56E1H7c", "file-tJoDYPF2MMlIOlvwfGbIV94D");
        System.out.println(assistantFileResponse.getId());
    }

    /**
     * 删除助手文件
     */
    @Test
    public void deleteAssistantFile() {
        DeleteResponse deleteResponse = client.deleteAssistantFile("asst_V4Grgp2BQWAmH03Bs56E1H7c", "file-VnfAMgLr4XSd7SHZTkQh5rkJ");
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
        AssistantListResponse<AssistantFileResponse> response = client.assistantFiles("asst_V4Grgp2BQWAmH03Bs56E1H7c", pageRequest);
        response.getData().forEach(e -> System.out.println(e.getId() + " | " + e.getAssistantId()));
//        file-RFWxhYSsE4pNS4KcAEs7mzuW | asst_F6OCbL3dVG2V58mWJudohOVq
//        file-VnfAMgLr4XSd7SHZTkQh5rkJ | asst_F6OCbL3dVG2V58mWJudohOVq
//        file-ehPUcWnsB4XnMAhnVH0nSJjD | asst_F6OCbL3dVG2V58mWJudohOVq
//        file-tJoDYPF2MMlIOlvwfGbIV94D | asst_V4Grgp2BQWAmH03Bs56E1H7c

    }


    /**
     * 创建线程
     * thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj
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
        //thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj
    }

    /**
     * 获取助手信息
     */
    @Test
    public void retrieveThread() {
        ThreadResponse threadResponse = client.retrieveThread("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj");
        System.out.println(threadResponse.getId());
    }

    /**
     * 修改线程信息
     */
    @Test
    public void modifyThread() {
        Map<String, String> map = new HashMap<>();
        map.put("hello", "unfbx");
        ModifyThread mod = ModifyThread.builder().metadata(map).build();
        ThreadResponse threadResponse = client.modifyThread("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj", mod);
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

    /**
     * 为线程创建消息
     */
    @Test
    public void message() {
        ThreadMessage threadMessage = ThreadMessage.builder()
                .content("thread message 2")
                .role(ThreadMessage.Role.USER.getName())
                .metadata(new HashMap())
                .fileIds(Collections.singletonList("file-tJoDYPF2MMlIOlvwfGbIV94D"))
                .build();

        MessageResponse response = client.message("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj", threadMessage);
        System.out.println(response.getId());
        // 附加file msg_poRST2FdtK4qbPX7ofbxOKKX  file-tJoDYPF2MMlIOlvwfGbIV94D
    }

    /**
     * 检索线程消息
     */
    @Test
    public void retrieveMessage() {
        MessageResponse response = client.retrieveMessage("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj", "msg_poRST2FdtK4qbPX7ofbxOKKX");
        System.out.println(response);
    }

    /**
     * 修改线程消息
     */
    @Test
    public void modifyMessage() {
        HashMap<String, String> map = new HashMap<>();
        map.put("A", "a");
        ModifyMessage modifyMessage = ModifyMessage.builder().metadata(map).build();
        MessageResponse response = client.modifyMessage("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj", "msg_poRST2FdtK4qbPX7ofbxOKKX", modifyMessage);
        System.out.println(response);
    }


    /**
     * 线程消息列表
     */
    @Test
    public void messages() {
//        PageRequest pageRequest = PageRequest.builder().build();
        PageRequest pageRequest = PageRequest
                .builder()
                .order(PageRequest.Order.ASC.getName())
                .limit(1)
                .before("msg_H4be0EWtG45QJaAxPpKoLhzK")
                .after("msg_LvjLgmupKneuvBK5sWZHgmLJ")
                .build();
        AssistantListResponse<MessageResponse> response = client.messages("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj", pageRequest);
        response.getData().forEach(e -> System.out.println(e.getId() + " | "));
//        msg_YD77vfFfTcw51q64R5YkN5y9 |
//        msg_H4be0EWtG45QJaAxPpKoLhzK |
//        msg_4zLoVjGJAccgc4vqJcFC0mRY |
//        msg_LvjLgmupKneuvBK5sWZHgmLJ |
//        msg_bIdACDFABc0OuSHktS42ucjZ |
//        msg_kkvLQ1vvTYhxmQZuWeQFg06i |
//        msg_8ilJmfrwWP3Ori8tMcnEmnRc |
//        msg_bL0TU8DTVABl4oy5qia8fJwI |
    }

    /**
     * 检索线程消息
     */
    @Test
    public void retrieveMessageFile() {
        MessageFileResponse response = client.retrieveMessageFile("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj",
                "msg_poRST2FdtK4qbPX7ofbxOKKX",
                "file-tJoDYPF2MMlIOlvwfGbIV94D");
        System.out.println(response.getId() + " | " + response.getMessageId());
        //file-tJoDYPF2MMlIOlvwfGbIV94D | msg_poRST2FdtK4qbPX7ofbxOKKX
    }

    /**
     * 检索线程消息
     */
    @Test
    public void messageFiles() {
        PageRequest pageRequest = PageRequest.builder().build();
//        PageRequest pageRequest = PageRequest
//                .builder()
//                .order(PageRequest.Order.ASC.getName())
//                .limit(1)
//                .before("msg_H4be0EWtG45QJaAxPpKoLhzK")
//                .after("msg_LvjLgmupKneuvBK5sWZHgmLJ")
//                .build();
        AssistantListResponse<MessageFileResponse> response = client.messageFiles("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj",
                "msg_poRST2FdtK4qbPX7ofbxOKKX",
                pageRequest);
        response.getData().forEach(e -> System.out.println(e.getId() + " | " + e.getMessageId()));
//        file-tJoDYPF2MMlIOlvwfGbIV94D | msg_poRST2FdtK4qbPX7ofbxOKKX
    }

    /**
     * 创建run
     */
    @Test
    public void run() {
        HashMap<String, String> map = new HashMap<>();
        map.put("A", "a");
        Tool tool = Tool.builder().type(Tool.Type.CODE_INTERPRETER.getName()).build();
        Run run = Run
                .builder()
                .assistantId("asst_V4Grgp2BQWAmH03Bs56E1H7c")
                .model(BaseChatCompletion.Model.GPT_3_5_TURBO_1106.getName())
                .instructions("你是一个数学导师。当我问你问题时，编写并运行Java代码来回答问题。")
                .tools(Collections.singletonList(tool))
                .metadata(map).build();
        RunResponse response = client.run("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj", run);
        System.out.println(response.getId() + " | " + response.getStatus());
    }

    /**
     * 检索run详细信息
     * run_7vFpIcFgINTLj6JIEvFGn5i7
     */
    @Test
    public void retrieveRun() {
        RunResponse response = client.retrieveRun("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj", "run_7vFpIcFgINTLj6JIEvFGn5i7");
        System.out.println(response.toString());
    }

    /**
     * 检索run详细信息
     * run_7vFpIcFgINTLj6JIEvFGn5i7
     */
    @Test
    public void modifyRun() {
        HashMap<String, String> map = new HashMap<>();
        map.put("B", "b");
        map.put("user_id", "user_abc123");
        ModifyRun modifyRun = ModifyRun.builder().metadata(map).build();
        RunResponse response = client.modifyRun("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj", "run_7vFpIcFgINTLj6JIEvFGn5i7", modifyRun);
        System.out.println(response.toString());
    }

    /**
     * 检索线程消息
     */
    @Test
    public void runs() {
        PageRequest pageRequest = PageRequest.builder().build();
//        PageRequest pageRequest = PageRequest
//                .builder()
//                .order(PageRequest.Order.ASC.getName())
//                .limit(1)
//                .before("msg_H4be0EWtG45QJaAxPpKoLhzK")
//                .after("msg_LvjLgmupKneuvBK5sWZHgmLJ")
//                .build();
        AssistantListResponse<RunResponse> response = client.runs("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj",
                pageRequest);
        response.getData().forEach(e -> System.out.println(e.toString()));
    }

    /**
     * 提交run
     */
    @Test
    public void submitToolOutputs() {
        ToolOutputBody toolOutputBody = ToolOutputBody.builder().toolOutputs(new ArrayList<>()).build();
        RunResponse response = client.submitToolOutputs("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj",
                "run_7vFpIcFgINTLj6JIEvFGn5i7",
                toolOutputBody);
        System.out.println(response.toString());
    }

    /**
     * 取消run
     */
    @Test
    public void cancelRun() {
        RunResponse response = client.cancelRun("thread_Qm0KgV0RgeDUNj0SSQ5Bf8tj",
                "run_7vFpIcFgINTLj6JIEvFGn5i7");
        System.out.println(response.toString());
    }

    /**
     * 开始一个thread run
     */
    @Test
    public void threadRun() {
        ThreadRun threadRun = ThreadRun.builder().assistantId("asst_V4Grgp2BQWAmH03Bs56E1H7c").build();
        RunResponse response = client.threadRun(threadRun);
        System.out.println(response.toString());
        //RunResponse(id=run_dmR2AtFe9nyGal6uSSxG6GI5, object=thread.run, createdAt=1700492750, threadId=thread_E1mqqdApmpNOCBfoLnqH7KMp, assistantId=asst_V4Grgp2BQWAmH03Bs56E1H7c, status=queued, requiredAction=null, lastError=null, expiresAt=1700493350, startedAt=null, cancelledAt=null, failedAt=null, completedAt=null, model=gpt-3.5-turbo-16k-0613, instructions=你是一个数学导师。当我问你问题时，编写并运行Java代码来回答问题。, tools=[Tool(type=code_interpreter, function=null)], fileIds=[file-tJoDYPF2MMlIOlvwfGbIV94D], metadata={})
    }

    @Test
    public void retrieveRunStep() {
        RunStepResponse response = client.retrieveRunStep("thread_E1mqqdApmpNOCBfoLnqH7KMp",
                "run_dmR2AtFe9nyGal6uSSxG6GI5",
                "");
        System.out.println(response.toString());
        //RunResponse(id=run_dmR2AtFe9nyGal6uSSxG6GI5, object=thread.run, createdAt=1700492750, threadId=thread_E1mqqdApmpNOCBfoLnqH7KMp, assistantId=asst_V4Grgp2BQWAmH03Bs56E1H7c, status=queued, requiredAction=null, lastError=null, expiresAt=1700493350, startedAt=null, cancelledAt=null, failedAt=null, completedAt=null, model=gpt-3.5-turbo-16k-0613, instructions=你是一个数学导师。当我问你问题时，编写并运行Java代码来回答问题。, tools=[Tool(type=code_interpreter, function=null)], fileIds=[file-tJoDYPF2MMlIOlvwfGbIV94D], metadata={})
    }

    /**
     * runSteps
     */
    @Test
    public void runSteps() {
//        PageRequest pageRequest = PageRequest.builder().build();
        AssistantListResponse<RunStepResponse> response = client.runSteps("thread_E1mqqdApmpNOCBfoLnqH7KMp", "run_dmR2AtFe9nyGal6uSSxG6GI5", null);
        response.getData().forEach(e -> System.out.println(e.toString()));
    }


}
