it’s an “unofficial" or "community-maintained” library.

这是一个非官方的社区维护的库。

---
#### 已经支持OpenAI官方的全部api，有bug欢迎朋友们指出，互相学习。

注意：由于这个接口：

https://platform.openai.com/docs/api-reference/files/retrieve-content

**免费用户无法使用，所以并未经过测试！！！**（哪位朋友有收费版keys也可以提供下）
---
#### **Q：如何连续对话？**
#### **A：issues，https://github.com/Grt1228/chatgpt-java/issues/8**
---
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

# 更新日志
- [x] 1.0.0   支持所有的OpenAI官方接口
- [x] 1.0.1   支持自定义超时时间，自定义OkHttpClient拦截器，参考：OpenAiClient构造函数


# 快速开始

## 方式一
导入pom依赖
```
<dependency>
    <groupId>com.unfbx</groupId>
    <artifactId>chatgpt-java</artifactId>
    <version>1.0.1</version>
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
        OpenAiClient openAiClient = new OpenAiClient("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",60,60,60);
//        OpenAiClient openAiClient = new OpenAiClient("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",60,60,60,null);
//        OpenAiClient openAiClient = new OpenAiClient("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");
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

## 方式二（下载源码直接运行）

### **OpenAI全部接口支持调用**

创建客户端配置api-key
完整测试案例参考：com.unfbx.chatgpt.OpenAiClientTest
```
public class OpenAiClientTest {

    private OpenAiClient v2;

    @Before
    public void before() {
        // v2 = new OpenAiClient("sk-******************************************");
        // v2 = new OpenAiClient("sk-******************************************",60,60,60);
        v2 = new OpenAiClient("sk-******************************************",60,60,60,null);
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
### 问答接口第二种调用方式（推荐使用方式一）
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
