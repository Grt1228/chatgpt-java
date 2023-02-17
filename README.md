it’s an “unofficial" or "community-maintained” library.

这是一个非官方的社区维护的库。

---
#### 已经支持OpenAI官方的全部api，有bug欢迎朋友们指出，互相学习。

注意：由于这个接口：

https://platform.openai.com/docs/api-reference/files/retrieve-content

**免费用户无法使用，所以并未经过测试！！！**（哪位朋友有收费版keys也可以提供下）


# 工程简介

**ChatGPT的Java客户端**

OpenAI官方Api的Java SDK

目前支持api-keys的方式调用，获取api-keys可以百度或者csdn查一下。

**api-keys的方式调用目前不用梯子即可访问。**


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

# 快速开始

## 方式一
导入pom依赖
```
<dependency>
    <groupId>com.unfbx</groupId>
    <artifactId>chatgpt-java</artifactId>
    <version>1.0.0</version>
</dependency>
```

使用示例：
```
package com.unfbx.eventTest.test;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import java.util.Arrays;

public class TestB {
    public static void main(String[] args) {
        //配置api keys
        OpenAiClient openAiClient = new OpenAiClient("sk-—***************api keys ****************");
        CompletionResponse completions = openAiClient.completions("三体人是什么？");
        Arrays.stream(completions.getChoices()).forEach(System.out::println);
    }
}
```

输出：
```
Choice(text=

三体人是一种虚构的外星生物，出现在中国作家刘慈欣的科幻小说《三体》中。它们是一种三节身体的外星生物，每个节身体都有自己的大脑，它们可以通过超越光速的思维来沟通。, index=0, logprobs=null, finishReason=stop)
```

## 方式二（下载源码直接运行）

### **OpenAI全部接口支持调用**

创建客户端配置api-key
完整测试案例参考：com.unfbx.chatgpt.OpenAiClientTest
```
public class OpenAiClientTest {

    private OpenAiClient v2;

    @Before
    public void before() {
        v2 = new OpenAiClient("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");
    }

    @Test
    public void models() {
        List<Model> models = v2.models();
        System.out.println(models.toString());
    }

    @Test
    public void model() {
        Model model = v2.model("code-davinci-002");
        System.out.println(model.toString());
    }

    @Test
    public void completions() {
        CompletionResponse completions = v2.completions("Java Stream list to map");
        Arrays.stream(completions.getChoices()).forEach(System.out::println);
    }
    
    @Test
    public void completionsv2() {
        Completion q = Completion.builder()
                .prompt("三体人是什么？")
                .build();
        CompletionResponse completions = v2.completions(q);
        System.out.println(completions);
    }
}
```
### 问答接口第二种调用方式
**目前ChatGPTClient只支持Completions相关api**
创建客户端配置api-key
```
public class ChatGPTTest {
    public static void main(String[] args) {
        //输入官方申请的api-keys
        ChatGPTClient client = new ChatGPTClient("sk-****************");
        //输入问题描述
        String body = client.askQuestion("简单描述下三体这本书");
        System.out.println(body);
    }
}
```
输出：
```
《三体》是中国作家刘慈欣创作的科幻小说，书中描写了一个存在于三体星系的中心神秘文明——「三体文明」的兴衰历程，叙述了它与地球文明之间的碰撞历史。
```

Completions接口的model目前官方支持的三种模型，参考代码
```
enum Model {
    DAVINCI_003("text-davinci-003"),
    DAVINCI_002("text-davinci-002"),
    DAVINCI("davinci"),
    ;
    private String name;
}
```
