it’s an “unofficial" or "community-maintained” library.
This is an unofficial community-maintained library. Bug reports are welcome and let's learn from each other.
> Original publication must be credited if reprinted!

Open source license: [LICENSE](https://github.com/Grt1228/chatgpt-java/blob/main/LICENSE)
### 💬 Contact me, all ChatGPT-related learning, communication and consultation are free.

Let's discuss issues related to chatgpt-java, SDK problem consultation, project and product development communication | If the group is invalid, please follow the official account to restore: chatgpt-java |
---|---
<img src="https://user-images.githubusercontent.com/27008803/225246389-7b452214-f3fe-4a70-bd3e-832a0ed34288.jpg" width="210" height="300" alt="QR code" />  | <img src="https://g-photo.oss-cn-shanghai.aliyuncs.com/hd15.jpg" width="210" height="210" alt="QR code" /> | 
<!--<img src="https://user-images.githubusercontent.com/27008803/225246581-15e90f78-5438-4637-8e7d-14c68ca13b59.jpg" width="210" height="300" alt="QR code" />-->

- [📖 Project Introduction](#-project-introduction)
- [🚩 Features](#-features)
- [📑 Update Log](#-update-log)
- [🚀 Quick Start](#-quick-start)
  - [Option 1](#option-1)
    - [1. Import pom dependency](#1-import-pom-dependency)
    - [2. Example of using the streaming client:](#2-example-of-using-the-streaming-client)
      - [Default OkHttpClient](#default-okhttpclient)
      - [Example of using a custom OkHttpClient client:](#example-of-using-a-custom-okhttpclient-client)
    - [3. Example of using the default client (supports all APIs):](#3-example-of-using-the-default-client-supports-all-apis)
      - [Default OkHttpClient](#default-okhttpclient-1)
      - [Example of using a custom OkHttpClient client:](#example-of-using-a-custom-okhttpclient-client-1)
  - [Option 2 (download source code and run directly)](#option-2-download-source-code-and-run-directly)
- [❔ FAQ](#-faq)
- [📌 Solutions for accessing the website in China](#-solutions-for-accessing-the-website-in-china)
- [📋 Collection of development cases](#-collection-of-development-cases)
- [🌟 Star History](#-star-history)
- [🙏 Acknowledgments](#-acknowledgments)
- [☕ Buy Me a Coffee](#-buy-me-a-coffee)

# 📖 Project Introduction

**ChatGPT Java Client**

The Java SDK for OpenAI's official API allows for quick integration into projects. It supports all of OpenAI's official interfaces, **as well as token calculations. Reference documentation: [Tokens_README.md](https://github.com/Grt1228/chatgpt-java/blob/main/Tokens_README.md)**.

| TikToken | Chat | Completions | Images | Speech To Text | Balance Inquiry |
| --- | --- | --- | --- | --- | --- |
| Token Calculation | GPT-3.5, 4.0 Dialogue Model | GPT-3.0 Dialogue | Image Model | Speech-to-Text, Speech Translation | Balance Inquiry |

| Embeddings | Files | Moderations | Fine-tune | Models |
| --- | --- | --- | --- | --- |
| Embeddings | Custom Training Models | Text Review, Sensitive Word Identification | Fine-tune | Model Retrieval |


Supports streaming output:
Streaming output implementation | Mini Program | Android | iOS | H5 
---|---|---|---|---
SSE Reference：[OpenAISSEEventSourceListener](https://github.com/Grt1228/chatgpt-steam-output/blob/main/src/main/java/com/unfbx/chatgptsteamoutput/listener/OpenAISSEEventSourceListener.java) | Not Supported | Supported| Supported | Supported
WebSocket Reference：[OpenAIWebSocketEventSourceListener](https://github.com/Grt1228/chatgpt-steam-output/blob/main/src/main/java/com/unfbx/chatgptsteamoutput/listener/OpenAIWebSocketEventSourceListener.java) | Supported| Supported | Supported | Supported

# 🚩 Features

- Supports dynamic handling of keys in case of exceptions (invalid, expired, blocked). See the reference implementation [DynamicKeyOpenAiAuthInterceptor](https://github.com/Grt1228/chatgpt-java/blob/main/src/main/java/com/unfbx/chatgpt/interceptor/DynamicKeyOpenAiAuthInterceptor.java).
- Supports alerting in case of key exceptions (custom development needed for platforms like DingTalk, Feishu, email, WeChat Work). See the reference implementation [DynamicKeyOpenAiAuthInterceptor](https://github.com/Grt1228/chatgpt-java/blob/main/src/main/java/com/unfbx/chatgpt/interceptor/DynamicKeyOpenAiAuthInterceptor.java).
- Supports multiple ways of token calculation.
- Supports customizing the OkHttpClient.
- Supports customizing multiple Apikeys.
- Supports customizing the key acquisition strategy.
- Supports balance inquiry.
- Supports personal account information inquiry.
- Supports GPT3, GPT3.5, GPT4.0.
- Supports all OpenAI APIs.

# 📑 Update Log
- [x] 1.0.13  Added support for custom handling of key exceptions (invalid, expired, blocked) with the implementation reference [DynamicKeyOpenAiAuthInterceptor](https://github.com/Grt1228/chatgpt-java/blob/main/src/main/java/com/unfbx/chatgpt/interceptor/DynamicKeyOpenAiAuthInterceptor.java). Also added support for alerting on key exceptions (DingTalk, Feishu, email, WeChat Enterprise, etc.), which requires custom development.
- [x] 1.0.12  Optimized token calculation, modified delete model interface, and updated speech interface to support the latest official parameters.
- [x] 1.0.11  Added new balance query interface with reference to [OpenAiClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiClientTest.java) and [OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiStreamClientTest.java). Fixed slow token calculation issue.
- [x] 1.0.10  Added support for token calculation with reference to [TikTokensTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/TikTokensTest.java). For more detailed information, please refer to the document: [Tokens_README.md](https://github.com/Grt1228/chatgpt-java/blob/main/Tokens_README.md)
- [x] 1.0.9   Added support for custom key usage strategies with reference to [OpenAiClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiClientTest.java) and [OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiStreamClientTest.java). Deprecated ChatGPTClient and optimized Moderation interface.
- [x] 1.0.8   Modified the custom implementation of OpenAiClient and OpenAiStreamClient, and changed the timeout setting, proxy setting, and custom interceptor setting to be implemented through custom OkHttpClient. This makes it more reasonable to hand over the OkHttpClient to the user for custom control, and more parameters can be customized. Also added support for multiple API key configurations.
- [x] 1.0.7   Fixed deserialization error bug: https://github.com/Grt1228/chatgpt-java/issues/79, and image SDK enumeration value bug: https://github.com/Grt1228/chatgpt-java/issues/76. Thanks to [@CCc3120](https://github.com/CCc3120) and [@seven-cm](https://github.com/seven-cm) for pointing them out.
- [x] 1.0.6   Added support for balance inquiry with reference to [OpenAiClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiClientTest.java) and [OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiStreamClientTest.java) creditGrants method. Also added support for the latest GPT-4 model, reference: [ChatCompletion.Model](https://github.com/Grt1228/chatgpt-java/blob/main/src/main/java/com/unfbx/chatgpt/OpenAiStreamClientTest.java)The `creditGrants` method supports the latest GPT-4 model. Refer to the [ChatCompletion.Model](https://github.com/Grt1228/chatgpt-java/blob/main/src/main/java/com/unfbx/chatgpt/entity/chat/ChatCompletion.java/) to build the message body and pass it into the model. Thanks to the group members for providing the balance interface address and [@PlexPt](https://github.com/PlexPt) for providing the model parameters.
- [x] 1.0.5   Supports custom Api Host and can be built using Builder. Refer to the code in the Quick Start section below.
- [x] 1.0.4   Changes to the Api return value in the latest ChatGPT Stream mode.
- [x] 1.0.3   Supports the latest GPT-3.5-Turbo and Whisper-1 models, supports speech-to-text and voice translation. OpenAiClient and OpenAiStreamClient support Builder construction and proxy support.
- [x] 1.0.2   Supports Stream output, refer to: OpenAiStreamClient
- [x] 1.0.1   Supports custom timeout and custom OkHttpClient interceptor, refer to: OpenAiClient constructor
- [x] 1.0.0   Supports all official OpenAI interfaces.

# 🚀 Quick Start

This project supports both **default output** and **streaming output**. For a complete SDK test case, see:

SDK Test Cases | Token Test Cases |
---|---|
[OpenAiClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiClientTest.java) and [OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiStreamClientTest.java) | Token calculation reference: [TikTokensTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/TikTokensTest.java)|

## Method 1

### 1. Import pom dependency
```
<dependency>
    <groupId>com.unfbx</groupId>
    <artifactId>chatgpt-java</artifactId>
    <version>1.0.13</version>
</dependency>
```
### 2. Streaming client usage example:
More SDK examples can be found at: [OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiStreamClientTest.java)

#### Default OkHttpClient
```
public class Test {
    public static void main(String[] args) {
        OpenAiStreamClient client = OpenAiStreamClient.builder()
                .apiKey(Arrays.asList("sk-********","sk-********"))
                // Custom key acquisition strategy: default KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new FirstKeyStrategy())
                // If you have a proxy, pass the proxy address, otherwise you can skip it
//                .apiHost("https://自己代理的服务器地址/")
                .build();
        // Chat model: gpt-3.5
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
}
```
#### Customize the usage of OkHttpClient client example:
```
public class Test {
    public static void main(String[] args) {
        // If accessing the service from China, a proxy needs to be configured. No proxy is needed for accessing the service from overseas servers.
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        // !!! Do not enable BODY level logging in production or test environment !!!
        // !!! It is recommended to set the logging level to one of these three levels in production or test environment: NONE, BASIC, HEADERS !!!
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .proxy(proxy) // custom proxy
                .addInterceptor(httpLoggingInterceptor) // custom logger
                .connectTimeout(30, TimeUnit.SECONDS) // custom connection timeout
                .writeTimeout(30, TimeUnit.SECONDS) // custom write timeout
                .readTimeout(30, TimeUnit.SECONDS) // custom read timeout
                .build();
        OpenAiStreamClient client = OpenAiStreamClient.builder()
                .apiKey(Arrays.asList("sk-********","sk-********"))
                // custom key acquisition strategy: default is KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new FirstKeyStrategy())
                .okHttpClient(okHttpClient) // custom OkHttpClient
                // if a proxy is used, pass the proxy address, if not, do not pass
//                .apiHost("https://your.proxy.server.address/")
                .build();
    }
}
```
Output log (text is continuously output):
```
23:03:59.158 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI建立sse连接...
23:03:59.160 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\n", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.172 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\n", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.251 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u5fc3", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.313 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u60c5", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.380 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u8212", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.439 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u7545", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.532 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\uff0c", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.579 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u5fc3", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.641 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u65f7", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.673 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u795e", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.751 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u6021", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.782 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：{"id": "cmpl-6pIHnOOJiiUEVMesXwxzzcSQFoZHj", "object": "text_completion", "created": 1677683039, "choices": [{"text": "\u3002", "index": 0, "logprobs": null, "finish_reason": null}], "model": "text-davinci-003"}
23:03:59.815 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据：[DONE]
23:03:59.815 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI返回数据结束了
23:03:59.815 [省略无效信息] INFO com.unfbx.chatgpt.sse.ConsoleEventSourceListener - OpenAI关闭sse连接...
```
### 3、Default client usage example (supports all APIs):
For more SDK examples, please refer to: [OpenAiClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiClientTest.java) 

#### Default OkHttpClient
```
public class Test {
    public static void main(String[] args) {
        OpenAiClient openAiClient = OpenAiClient.builder()
                .apiKey(Arrays.asList("sk-********","sk-********"))
                // customize key acquisition strategy: default is KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new FirstKeyStrategy())
                // if you have a proxy, you can set it here; otherwise, leave it empty
//                .apiHost("https://your-proxy-server/")
                .build();
        // chat model: gpt-3.5
        Message message = Message.builder().role(Message.Role.USER).content("你好啊我的伙伴！").build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices().forEach(e -> {
            System.out.println(e.getMessage());
        });
    }
}

```
#### Custom OkHttpClient Client Usage Example:
```
public class Test {
    public static void main(String[] args) {
        // Proxy is needed for accessing in China, not needed for foreign servers
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        // !!! Don't enable BODY level logging in production or testing environment !!!
        // !!! It's recommended to set logging level to NONE, BASIC, or HEADERS in production or testing environment. !!!
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .proxy(proxy) // custom proxy
                .addInterceptor(httpLoggingInterceptor) // custom log output
                .addInterceptor(new OpenAiResponseInterceptor()) // custom return value interceptor
                .connectTimeout(10, TimeUnit.SECONDS) // custom timeout
                .writeTimeout(30, TimeUnit.SECONDS) // custom timeout
                .readTimeout(30, TimeUnit.SECONDS) // custom timeout
                .build();
        // build client
        OpenAiClient openAiClient = OpenAiClient.builder()
                .apiKey(Arrays.asList("sk-********","sk-********"))
                // custom key acquisition strategy: default KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new FirstKeyStrategy())
                .okHttpClient(okHttpClient)
                // If you have set up your own proxy, pass in the proxy address. If not, you can skip this.
//                .apiHost("https://your_own_proxy_server_address/")
                .build();
                // chat model: gpt-3.5
        Message message = Message.builder().role(Message.Role.USER).content("你好啊我的伙伴！").build();
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
        ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
        chatCompletionResponse.getChoices().forEach(e -> {
            System.out.println(e.getMessage());
        });
    }
}

```
## Option 2 (Run directly after downloading source code)

Download the source code and package it. 

### ❔ QA

| Q | A |
| --- | --- |
| How to implement continuous dialogue? | Issues: https://github.com/Grt1228/chatgpt-java/issues/8 |
| How to implement streaming output? | Upgrade to version 1.0.2, refer to the source code: [OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiStreamClientTest.java/) |
| How to integrate SpringBoot to implement streaming output API interface? | Refer to another project: [chatgpt-steam-output](https://github.com/Grt1228/chatgpt-steam-output) |
| Does the latest version of GPT-3.5-TURBO support it? | Upgrade to version 1.0.3, ChatCompletion is already supported, refer to test case: [OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiStreamClientTest.java/) and [OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiClientTest.java/) |
| Does the latest version support language-to-text and language translation? | Upgrade to version 1.0.3, whisper is already supported, refer to test case: [OpenAiStreamClientTest](https://github.com/Grt1228/chatgpt-java/blob/main/src/test/java/com/unfbx/chatgpt/OpenAiStreamClientTest.java/) and [OpenAiStreamClientTest](https://

## 📌Solution for accessing in China
You can check out this solution for accessing in China: **https://github.com/noobnooc/noobnooc/discussions/9**

## 📋Development Case Collection
**Development case collection based on this SDK**: [chatgpt-java SDK case collection](https://github.com/Grt1228/chatgpt-java/issues/87)

## 🌟Star History

[![Star History Chart](https://api.star-history.com/svg?repos=Grt1228/chatgpt-java&type=Date)](https://star-history.com/#Grt1228/chatgpt-java&Date)

# 🙏 Acknowledgments
Standing on the shoulders of giants:
- OpenAi: https://openai.com/
- [knuddelsgmbh](https://github.com/knuddelsgmbh)'s [jtokkit](https://github.com/knuddelsgmbh/jtokkit) open source computational algorithm.

# ☕ Buy me a coffee
If this project is helpful to you, you can buy me a cup of milk tea.

<img width="180" alt="微信截图_20230405222411" src="https://user-images.githubusercontent.com/27008803/230111508-3179cf30-e128-4




