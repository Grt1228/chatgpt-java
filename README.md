# 已经支持OpenAI官方的绝大多数api

**切换分支到1.0.0**，目前还在测试阶段

参考：OpenAiClientTest

OpenAi官方文档地址：https://platform.openai.com/docs/api-reference
- [x] Models
- [x] Completions
- [x] Images
- [x] Embeddings
- [x] Files
- [ ] Fine-tune
- [x] Moderations
- [x] Engines

# 工程简介 
master分支

**ChatGPT的Java客户端**
目前支持api-keys的方式调用.
后续会持续集成国内类ChatGpt的厂商api。

# 快速开始
创建客户端配置api-key即可
api-key
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

目前官方支持的三种模型，参考代码
```
enum Model {
    DAVINCI_003("text-davinci-003"),
    DAVINCI_002("text-davinci-002"),
    DAVINCI("davinci"),
    ;
    private String name;
}
```