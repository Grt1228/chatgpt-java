package com.unfbx.chatgpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.ModelType;
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
    List<Message> message;

    @Before
    public void prepareData() {
        text = "关注微信公众号：程序员的黑洞。进入chatgpt-java交流群获取最新版本更新通知。";
        message = new ArrayList<>(2);
        message.add(Message.builder().role(Message.Role.USER).content("关注微信公众号：程序员的黑洞。").build());
        message.add(Message.builder().role(Message.Role.USER).content("进入chatgpt-java交流群获取最新版本更新通知。").build());

    }

    @Test
    public void chatCompletionTokensTest() {
        ChatCompletion completion = ChatCompletion.builder().messages(message).build();
        long tokens = completion.tokens();
        log.info("Message集合文本：【{}】", message, tokens);
        log.info("总tokens数{}", tokens);
    }

    @Test
    public void completionTokensTest() {
        Completion completion = Completion.builder().prompt(text).build();
        long tokens = completion.tokens();
        log.info("单句文本：【{}】", text);
        log.info("总tokens数{}", tokens);
    }


    @Test
    public void byEncodingTest() {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncoding(EncodingType.P50K_BASE);
        List<Integer> encode = TikTokensUtil.encode(enc, text);
        log.info(encode.toString());
        long tokens = TikTokensUtil.tokens(enc, text);
        log.info("单句文本：【{}】", text);
        log.info("总tokens数{}", tokens);
        log.info("--------------------------------------------------------------");
        TikTokensUtil.tokens(enc, message);
        log.info("Message集合文本：【{}】", message, tokens);
        log.info("总tokens数{}", tokens);
    }

    @Test
    public void byEncodingTypeTest() {
        List<Integer> encode = TikTokensUtil.encode(EncodingType.CL100K_BASE, text);
        log.info(encode.toString());
        long tokens = TikTokensUtil.tokens(EncodingType.CL100K_BASE, text);
        log.info("单句文本：【{}】", text);
        log.info("总tokens数{}", tokens);
        log.info("--------------------------------------------------------------");
        List<Message> message = new ArrayList<>(2);
        TikTokensUtil.tokens(EncodingType.CL100K_BASE, message);
        log.info("Message集合文本：【{}】", message, tokens);
        log.info("总tokens数{}", tokens);
    }


    @Test
    public void byModelNameTest() {
        String modelName = ChatCompletion.Model.GPT_3_5_TURBO.getName();
//        String modelName = Completion.Model.DAVINCI_003.getName();
        List<Integer> encode = TikTokensUtil.encode(modelName, text);
        log.info(encode.toString());
        long tokens = TikTokensUtil.tokens(modelName, text);
        log.info("单句文本：【{}】", text);
        log.info("总tokens数{}", tokens);
        log.info("--------------------------------------------------------------");
        List<Message> message = new ArrayList<>(2);
        TikTokensUtil.tokens(modelName, message);
        log.info("Message集合文本：【{}】", message, tokens);
        log.info("总tokens数{}", tokens);
    }
}
