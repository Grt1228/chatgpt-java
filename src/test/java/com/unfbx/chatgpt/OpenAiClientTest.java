package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import com.unfbx.chatgpt.entity.engines.Engine;
import com.unfbx.chatgpt.entity.images.Image;
import com.unfbx.chatgpt.entity.images.ImageResponse;
import com.unfbx.chatgpt.entity.models.Model;
import com.unfbx.chatgpt.entity.moderations.Moderation;
import com.unfbx.chatgpt.entity.moderations.ModerationResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 描述： 测试类
 *
 * @author https:www.unfbx.com
 * @date 2023-02-11
 */
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
    public void genImages() {
        Image image = Image.builder().prompt("编程界面").build();
        ImageResponse imageResponse = v2.genImages(image);
        System.out.println(imageResponse);
    }

    @Test
    public void moderations() {
        ModerationResponse moderations = v2.moderations("I want to kill them.");
        System.out.println(moderations);
    }

    @Test
    public void moderationsV2() {
        Moderation moderation = Moderation.builder().input("I want to kill them.").build();
        ModerationResponse moderations = v2.moderations(moderation);
        System.out.println(moderations);
    }

    @Test
    public void engines() {
        List<Engine> engines = v2.engines();
        System.out.println(engines);
    }

    @Test
    public void engine() {
        Engine engines = v2.engine("code-davinci-002");
        System.out.println(engines);
    }
}
