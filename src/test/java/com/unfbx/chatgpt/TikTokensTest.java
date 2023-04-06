package com.unfbx.chatgpt;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.utils.TikTokensUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @sine 2023-04-05
 */
@Slf4j
public class TikTokensTest {
    String text;
    List<Message> messages;

    @Before
    public void prepareData() {
        text = "关注微信公众号：程序员的黑洞。进入chatgpt-java交流群获取最新版本更新通知。";
        messages = new ArrayList<>(2);
        messages.add(Message.builder().role(Message.Role.USER).content("关注微信公众号：程序员的黑洞。").build());
        messages.add(Message.builder().role(Message.Role.USER).content("进入chatgpt-java交流群获取最新版本更新通知。").build());
    }

    /**
     * gpt-3.5和gpt4.0聊天模型接口计算推荐这种方法
     */
    @Test
    public void chatCompletionTokensTest() {
        ChatCompletion completion = ChatCompletion.builder().messages(messages).build();
        long tokens = completion.tokens();
        log.info("Message集合文本：【{}】", messages, tokens);
        log.info("总tokens数{}", tokens);
    }
    /**
     * Completion 接口计算推荐使用这种方法
     */
    @Test
    public void completionTokensTest() {
        Completion completion = Completion.builder().prompt(text).build();
        long tokens = completion.tokens();
        log.info("单句文本：【{}】", text);
        log.info("总tokens数{}", tokens);
    }
    /**
     * 通过模型模型名称计算
     */
    @Test
    public void byModelNameTest() {
        String modelName = ChatCompletion.Model.GPT_4.getName();
//        String modelName = ChatCompletion.Model.GPT_3_5_TURBO.getName();
        List<Integer> encode = TikTokensUtil.encode(modelName, text);
        log.info(encode.toString());
        long tokens = TikTokensUtil.tokens(modelName, text);
        log.info("单句文本：【{}】", text);
        log.info("总tokens数{}", tokens);
        log.info("--------------------------------------------------------------");
        tokens = TikTokensUtil.tokens(modelName, messages);
        log.info("Message集合文本：【{}】", messages, tokens);
        log.info("总tokens数{}", tokens);
    }


    /**
     * 通过Encoding计算
     */
    @Test
    public void byEncodingTest() {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncoding(EncodingType.P50K_BASE);
        List<Integer> encode = TikTokensUtil.encode(enc, text);
        log.info(encode.toString());
        long tokens = TikTokensUtil.tokens(enc, text);
        log.info("单句文本：【{}】", text);
        log.info("总tokens数{}", tokens);
    }

    /**
     * 通过EncodingType计算
     */
    @Test
    public void byEncodingTypeTest() {
        List<Integer> encode = TikTokensUtil.encode(EncodingType.CL100K_BASE, text);
        log.info(encode.toString());
        long tokens = TikTokensUtil.tokens(EncodingType.CL100K_BASE, text);
        log.info("单句文本：【{}】", text);
        log.info("总tokens数{}", tokens);
    }

}
