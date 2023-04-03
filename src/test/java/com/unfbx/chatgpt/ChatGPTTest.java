package com.unfbx.chatgpt;

/**
 * 描述： 测试类
 *
 * @author https:www.unfbx.com
 *  2023-02-11
 */
public class ChatGPTTest {
    /**
     * 已经弃用，请使用下面的实现方式：
     * @see OpenAiClient
     * @see OpenAiStreamClient
     *
     * @param args
     */
    public static void main(String[] args) {
        ChatGPTClient client = new ChatGPTClient("*********************");
        String body = client.askQuestion("简单描述下三体这本书");
        System.out.println(body);
    }
}
