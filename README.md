it’s an “unofficial" or "community-maintained” library.

这是一个非官方的社区维护的库。

**国内访问可以看下这个解决方案**：https://github.com/noobnooc/noobnooc/discussions/9
## 更新日志
- [x] 1.0.0   支持所有的OpenAI官方接口
- [x] 1.0.1   支持自定义超时时间，自定义OkHttpClient拦截器，参考：OpenAiClient构造函数
- [x] 1.0.2   支持Stream流式输出，参考：OpenAiStreamClient
- [x] 1.0.3   支持最新的GPT-3.5-Turbo模型和Whisper-1模型，支持语音功能转文字，语音翻译。OpenAiClient和OpenAiStreamClient支持Builder构造，支持代理。
- [x] 1.0.4   官方最新的ChatGPT Stream模式下的Api返回值改动。
- [x] 1.0.5   支持自定义Api Host，使用Builder构建。参考下面的快速开始部分代码。
---
#### 已经支持OpenAI官方的全部api，有bug欢迎朋友们指出，互相学习。

注意：由于这个接口：

https://platform.openai.com/docs/api-reference/files/retrieve-content

**免费用户无法使用，所以并未经过测试！！！**（哪位朋友有收费版keys也可以提供下）

**完整测试案例参考源码中的：com.unfbx.chatgpt.OpenAiClientTest**和
**com.unfbx.chatgpt.OpenAiStreamClientTest**

---

Q | A
---|---
如何实现连续对话？ | issues：https://github.com/Grt1228/chatgpt-java/issues/8
如何实现流式输出？ | 升级1.0.2版本，参考源码：[OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiStreamClientTest.java/)
如何整合SpringBoot实现流式输出的Api接口？ | 参考另外一个项目：[chatgpt-steam-output](https://github.com/Grt1228/chatgpt-steam-output)
最新版GPT-3.5-TURBO是否支持？ | 升级1.0.3 已经支持ChatCompletion, 参考测试案例：[OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiStreamClientTest.java/) 和[OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiClientTest.java/)
最新版语言转文字和语言翻译是否支持？ | 升级1.0.3 已经支持whisper参考测试案例：[OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiStreamClientTest.java/) 和[OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiClientTest.java/)
---
# 工程简介

**ChatGPT的Java客户端**

OpenAI官方Api的Java SDK

目前支持api-keys的方式调用，获取api-keys可以百度或者csdn查一下。

**api-keys的方式调用目前需要用梯子才可访问。**


OpenAi官方文档地址：https://platform.openai.com/docs/api-reference
**已完成接口列表：**
- [x] Models
- [x] Completions
- [x] Images
- [x] Embeddings
- [x] Files
- [x] Fine-tune
- [x] Moderations
- [x] Engines
- [x] Chat
- [x] Speech To Text

# 快速开始

## 方式一
导入pom依赖
```
<dependency>
    <groupId>com.unfbx</groupId>
    <artifactId>chatgpt-java</artifactId>
    <version>1.0.5</version>
</dependency>
```

常规客户端使用示例：
```
package com.unfbx.eventTest.test;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import java.util.Arrays;

public class TestB {
    public static void main(String[] args) {
        //配置api keys
        //代理可以为null
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.1.111", 7890));
        //日志输出可以不添加
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        OpenAiClient openAiClient = new OpenAiClient("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",60,60,60);
//        OpenAiClient openAiClient = new OpenAiClient("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",60,60,60,null);
//        OpenAiClient openAiClient = new OpenAiClient("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");
        OpenAiClient openAiClient = OpenAiClient.builder()
            .apiKey("sk-***************************")
            .connectTimeout(50)
            .writeTimeout(50)
            .readTimeout(50)
            .interceptor(Arrays.asList(httpLoggingInterceptor))
            .proxy(proxy)
            .apiHost("https://api.openai.com/")
            .build();
        CompletionResponse completions = openAiClient.completions("我想申请转专业，从计算机专业转到会计学专业，帮我完成一份两百字左右的申请书");
        Arrays.stream(completions.getChoices()).forEach(System.out::println);
    }
}
```

输出：
```
Choice(text=

尊敬的领导：

您好！

我是XX，目前就读于XX大学计算机专业，现在我想申请转专业，从计算机专业转到会计学专业。

我有着良好的学习习惯，在计算机专业的学习中，我取得了良好的成绩，并且拥有了一定的计算机基础知识。在这一年的学习中，我发现自己对计算机的兴趣不太浓厚，而对会计学的兴趣却很浓厚，我觉得会计学是一个很有前景的专业，而且也是我的兴趣所在，我想把自己的未来打造成一个会计学专业的专家。

因此，我希望能够申请转专业，从计算机专业转到会计学专业，我会努力学习，努力完成学业，让自己成为一个优秀的会计学专业的专家。

最后，我再次表达我申请转专业的请求，希望能够得到您的认可和批准。

谨上

XX, index=0, logprobs=null, finishReason=stop)

Process finished with exit code 0

```
流式输出代码：

```
package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.sse.ConsoleEventSourceListener;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * 描述： 测试类
 *
 * @author https:www.unfbx.com
 * 2023-02-28
 */
public class OpenAiStreamClientTest {

    private OpenAiStreamClient client;

    @Before
    public void before() {
        //代理可以不设置
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.1.111", 7890));
//        client = new OpenAiStreamClient("sk-**********************",
//                60,
//                60,
//                60,
//                proxy);
        //推荐这种构造方式
        client = OpenAiStreamClient.builder()
                .connectTimeout(50)
                .readTimeout(50)
                .writeTimeout(50)
                .apiKey("sk-******************************")
                .proxy(proxy)
                .apiHost("https://api.openai.com/")
                .build();
    }
    
    @Test
    public void chatCompletions() {
        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener();
        Message message = Message.builder().role(Message.Role.USER).content("你好啊我的伙伴！").build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
        client.streamChatCompletion(chatCompletion, eventSourceListener);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void completions() {
        ConsoleEventSourceListener eventSourceListener = new ConsoleEventSourceListener();
        Completion q = Completion.builder()
                .prompt("一句话描述下开心的心情")
                .stream(true)
                .build();
        client.streamCompletions(q, eventSourceListener);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

输出日志（text是持续输出的）：
```
23:03:59.158 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI建立sse连接...
23:03:59.160 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\n", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.172 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\n", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.251 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u5fc3", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.313 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u60c5", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.380 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u8212", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.439 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u7545", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.532 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\uff0c", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.579 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u5fc3", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.641 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u65f7", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.673 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u795e", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.751 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u6021", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.782 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u3002", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.815 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：[DONE]
23:03:59.815 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据结束了
23:03:59.815 [OkHttp https://api.openai.com/...] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI关闭sse连接...

Process finished with exit code -1

```


## 方式二（下载源码直接运行）

### **OpenAI全部接口支持调用**

创建客户端配置api-key
完整测试案例参考：com.unfbx.chatgpt.OpenAiClientTest 和 com.unfbx.chatgpt.OpenAiStreamClientTest

# Star History

[![Star History Chart](https://api.star-history.com/svg?repos=Grt1228/chatgpt-java&type=Date)](https://star-history.com/#Grt1228/chatgpt-java&Date)
