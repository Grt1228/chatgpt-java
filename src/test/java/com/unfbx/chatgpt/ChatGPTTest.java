package com.unfbx.chatgpt;

/**
 * 描述： 测试类
 *
 * @author https:www.unfbx.com
 * @date 2023-02-11
 */
public class ChatGPTTest {
    public static void main(String[] args) {
        ChatGPTClient client = new ChatGPTClient("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");
        String body = client.askQuestion("简单描述下三体这本书");
        System.out.println(body);
    }
}
