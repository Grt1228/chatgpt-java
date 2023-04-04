package com.unfbx.chatgpt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.ModelType;
import com.unfbx.chatgpt.entity.chat.Message;
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
public class TokensTest {

    @Test
    public void byEncodingTest() throws JsonProcessingException {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncoding(EncodingType.P50K_BASE);
        String text = "关注微信公众号：程序员的黑洞。进入chatgpt-java交流群获取最新版本更新通知。";
        List<Integer> encode = TikTokensUtil.encode(enc, text);
        log.info(encode.toString());
        long tokens = TikTokensUtil.tokens(enc, text);
        log.info("单句文本：【{}】", text);
        log.info("总tokens数{}", tokens);
        log.info("--------------------------------------------------------------");
        List<Message> message = new ArrayList<>(2);
        message.add(Message.builder().role(Message.Role.USER).content("关注微信公众号：程序员的黑洞。").build());
        message.add(Message.builder().role(Message.Role.USER).content("进入chatgpt-java交流群获取最新版本更新通知。").build());
        TikTokensUtil.tokens(enc, message);
        log.info("Message集合文本：【{}】", message, tokens);
        log.info("总tokens数{}", tokens);
    }


    @Test
    public void testJson() throws JsonProcessingException {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncoding(EncodingType.P50K_BASE);
        ModelType modelType = ModelType.valueOf("text-davinci-003");
        List<Integer> encoded = enc.encode("我爱你中国。");
        System.out.println(encoded.toString());
        // encoded = [2028, 374, 264, 6205, 11914, 13]

        String decoded = enc.decode(encoded);
        System.out.println(decoded);

        // decoded = "This is a sample sentence."

        // Or get the tokenizer based on the model type
        Encoding secondEnc = registry.getEncodingForModel(ModelType.TEXT_DAVINCI_003);
        System.out.println(secondEnc);
        System.out.println(1);
        // enc == secondEnc
    }
}
