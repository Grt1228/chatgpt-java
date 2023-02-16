##### 已经支持OpenAI官方的绝大多数api，目前还在测试阶段，有问题欢迎朋友们指出，互相学习。

# 工程简介

**ChatGPT的Java客户端**
OpenAI官方Api的Java SDK
目前支持api-keys的方式调用，获取api-keys可以百度或者csdn查一下。
**api-keys的方式调用目前不用梯子即可访问。**
后续可能会持续集成国内类ChatGpt的厂商api。

OpenAi官方文档地址：https://platform.openai.com/docs/api-reference
**已完成接口列表：**
- [x] Models
- [x] Completions
- [x] Images
- [x] Embeddings
- [x] Files
- [ ] Fine-tune
- [x] Moderations
- [x] Engines

# 快速开始

有两种调用方式

## 方式一（推荐方式）

**这种调用方式除了 ++Fine-tune++相关的几个接口，其他接口全部支持调用**
调用方式和上面类似，创建客户端配置api-key
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
## 方式二
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
