package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import com.unfbx.chatgpt.entity.image.Image;
import com.unfbx.chatgpt.entity.image.ImageResponse;
import com.unfbx.chatgpt.entity.models.Model;
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
public class ChatGPTTestV2 {

    private ChatGPTClientV2 v2;
    @Before
    public void before() {
        v2 = new ChatGPTClientV2("**************************");
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
}
