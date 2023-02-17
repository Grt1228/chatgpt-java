package com.unfbx.chatgpt;

import com.unfbx.chatgpt.entity.completions.Completion;
import com.unfbx.chatgpt.entity.completions.CompletionResponse;
import com.unfbx.chatgpt.entity.edits.Edit;
import com.unfbx.chatgpt.entity.edits.EditResponse;
import com.unfbx.chatgpt.entity.embeddings.Embedding;
import com.unfbx.chatgpt.entity.embeddings.EmbeddingResponse;
import com.unfbx.chatgpt.entity.engines.Engine;
import com.unfbx.chatgpt.entity.files.File;
import com.unfbx.chatgpt.entity.common.DeleteResponse;
import com.unfbx.chatgpt.entity.files.UploadFileResponse;
import com.unfbx.chatgpt.entity.fineTune.Event;
import com.unfbx.chatgpt.entity.fineTune.FineTune;
import com.unfbx.chatgpt.entity.fineTune.FineTuneResponse;
import com.unfbx.chatgpt.entity.images.*;
import com.unfbx.chatgpt.entity.models.Model;
import com.unfbx.chatgpt.entity.moderations.Moderation;
import com.unfbx.chatgpt.entity.moderations.ModerationResponse;
import okhttp3.ResponseBody;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 描述： 测试类
 *
 * @author https:www.unfbx.com
 *  2023-02-11
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
        models.forEach(e -> {
            System.out.print(e.getOwnedBy() + " ");
            System.out.print(e.getID() + " ");
            System.out.println(e.getObject() + " ");
        });
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
    public void completionsV2() {
        Completion q = Completion.builder()
                .prompt("三体人是什么？")
                .model("ada:ft-org-DL6GzliwY20i7Lxr5pUAoKUH:grttttttttt-2023-02-16-05-42-02")
                .build();
        CompletionResponse completions = v2.completions(q);
        System.out.println(completions);
    }

    @Test
    public void editText() {
        //文本修改
//        Edit edit = Edit.builder().input("我爱你麻麻").instruction("帮我修改错别字").model(Edit.Model.TEXT_DAVINCI_EDIT_001.getName()).build();
        //代码修改 NB....
        Edit edit = Edit.builder().input("System.out.pri(\"AAAAA\");").instruction("帮我修改这个java代码").model(Edit.Model.CODE_DAVINCI_EDIT_001.getName()).build();
        EditResponse editResponse = v2.edit(edit);
        System.out.println(editResponse);
    }


    @Test
    public void genImages() {
        Image image = Image.builder().prompt("电脑画面").build();
        ImageResponse imageResponse = v2.genImages(image);
        System.out.println(imageResponse);
    }

    @Test
    public void genImagesV2() {
        ImageResponse imageResponse = v2.genImages("睡着的小朋友");
        System.out.println(imageResponse);
    }

    /**
     * Invalid input image - format must be in ['RGBA', 'LA', 'L'], got RGB.
     */

    @Test
    public void editImageV2() {
        ImageEdit imageEdit = ImageEdit.builder().prompt("去除图片中的文字").build();
        List<Item> images = v2.editImages(new java.io.File("C:\\Users\\***\\Desktop\\1.png"),
                imageEdit);
        System.out.println(images);
    }

    @Test
    public void editImageV3() {
        List<Item> images = v2.editImages(new java.io.File("C:\\Users\\***\\Desktop\\1.png"),
                "去除图片中的文字");
        System.out.println(images);
    }

    @Test
    public void editImage() {
        List<Item> images = v2.editImages(new java.io.File("C:\\Users\\***\\Desktop\\1.png"),
                "去除图片中的文字");
        System.out.println(images);
    }


    @Test
    public void variationsImagesV2() {
        ImageVariations imageVariations = ImageVariations.builder().build();
        ImageResponse imageResponse = v2.variationsImages(new java.io.File("C:\\Users\\***\\Desktop\\12.png"), imageVariations);
        System.out.println(imageResponse);
    }

    @Test
    public void variationsImages() {
        ImageResponse imageResponse = v2.variationsImages(new java.io.File("C:\\Users\\***\\Desktop\\12.png"));
        System.out.println(imageResponse);
    }


    @Test
    public void embeddingsV2() {
        Embedding embedding = Embedding.builder().input("我爱你亲爱的姑娘").build();
        EmbeddingResponse embeddings = v2.embeddings(embedding);
        System.out.println(embeddings);
    }


    @Test
    public void embeddings() {
        EmbeddingResponse embeddings = v2.embeddings("The food was delicious and the waiter...");
        System.out.println(embeddings);
    }


    @Test
    public void files() {
        List<File> files = v2.files();
        System.out.println(files);
    }

    @Test
    public void retrieveFile() {
        File files = v2.retrieveFile("file-EHB0Wp3wcZu6tpbwkB6xeiEd");
        System.out.println(files);
    }

    /**
     * 不支持免费用户： To help mitigate abuse, downloading of fine-tune training files is disabled for free accounts.
     * 暂时没有测试
     */
    @Test
    public void retrieveFileContent() {
//        ResponseBody responseBody = v2.retrieveFileContent("file-EHB0Wp3wcZu6tpbwkB6xeiEd");
//        System.out.println(responseBody);
    }

    @Test
    public void uploadFileV1() {
        UploadFileResponse uploadFileResponse = v2.uploadFile(new java.io.File("C:\\Users\\***\\Desktop\\2.txt"));
        System.out.println(uploadFileResponse);
    }

    @Test
    public void uploadFileV2() {
        UploadFileResponse uploadFileResponse = v2.uploadFile("fine-tune", new java.io.File("C:\\Users\\***\\Desktop\\2.txt"));
        System.out.println(uploadFileResponse);
    }

    @Test
    public void deleteFile() {
        DeleteResponse deleteResponse = v2.deleteFile("file-GreIoKq6lWHvq8PDwDZIGJjm");
        System.out.println(deleteResponse);
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

    @Test
    public void fineTune() {
        FineTuneResponse fineTuneResponse = v2.fineTune("file-EHB0Wp3wcZu6tpbwkB6xeiEd");
        System.out.println(fineTuneResponse);
    }
    @Test
    public void fineTuneV2() {
        FineTune fineTune = FineTune.builder()
                .trainingFile("file-OcQb9zg35cxa4WLBZJ9K2523")
                .suffix("grttttttttt")
                .model(FineTune.Model.ADA.getName())
                .build();
        FineTuneResponse fineTuneResponse = v2.fineTune(fineTune);
        System.out.println(fineTuneResponse);
    }

    @Test
    public void fineTunes() {
        List<FineTuneResponse> fineTuneResponses = v2.fineTunes();
        System.out.println(fineTuneResponses);
    }

    @Test
    public void retrieveFineTune() {
        FineTuneResponse fineTuneResponses = v2.retrieveFineTune("ft-bU0xJzVfrgOjqoy1e9lC2oDP");
        System.out.println(fineTuneResponses);
    }

    @Test
    public void cancelFineTune() {
        //status发生变化 pending -> cancelled
        FineTuneResponse fineTuneResponses = v2.cancelFineTune("ft-KohbEOCbPyNTyQmt5UV1F1cb");
        System.out.println(fineTuneResponses);
    }

    @Test
    public void fineTuneEvents() {
        List<Event> events = v2.fineTuneEvents("ft-KohbEOCbPyNTyQmt5UV1F1cb");
        System.out.println(events);
    }

    @Test
    public void deleteFineTuneModel() {
        DeleteResponse deleteResponse = v2.deleteFineTuneModel("ft-KohbEOCbPyNTyQmt5UV1F1cb");
        System.out.println(deleteResponse);
    }
}
