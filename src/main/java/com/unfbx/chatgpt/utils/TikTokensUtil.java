package com.unfbx.chatgpt.utils;

import cn.hutool.core.util.StrUtil;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.ModelType;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * 描述：token计算工具类
 *
 * @author https:www.unfbx.com
 * @since 2023-04-04
 */
@Slf4j
public class TikTokensUtil {
    /**
     * 模型名称对应Encoding
     */
    private static final Map<String, Encoding> modelMap = new HashMap<>();
    /**
     * registry实例
     */
    private static final EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();

    static {
        for (ModelType modelType : ModelType.values()) {
            modelMap.put(modelType.getName(), registry.getEncodingForModel(modelType));
        }
        modelMap.put(ChatCompletion.Model.GPT_3_5_TURBO_0301.getName(), registry.getEncodingForModel(ModelType.GPT_3_5_TURBO));
        modelMap.put(ChatCompletion.Model.GPT_4_32K.getName(), registry.getEncodingForModel(ModelType.GPT_4));
        modelMap.put(ChatCompletion.Model.GPT_4_32K_0314.getName(), registry.getEncodingForModel(ModelType.GPT_4));
        modelMap.put(ChatCompletion.Model.GPT_4_0314.getName(), registry.getEncodingForModel(ModelType.GPT_4));
    }

    /**
     * 通过Encoding和text获取编码数组
     *
     * @param enc
     * @param text
     * @return
     */
    public static List<Integer> encode(@NotNull Encoding enc, String text) {
        return StrUtil.isBlank(text) ? new ArrayList<>() : enc.encode(text);
    }

    /**
     * 通过Encoding计算text信息的tokens
     *
     * @param enc
     * @param text
     * @return
     */
    public static int tokens(@NotNull Encoding enc, String text) {
        return encode(enc, text).size();
    }


    /**
     * 通过Encoding和encoded数组反推text信息
     *
     * @param enc
     * @param encoded
     * @return
     */
    public static String decode(@NotNull Encoding enc, @NotNull List<Integer> encoded) {
        return enc.decode(encoded);
    }

    /**
     * 获取一个Encoding对象，通过Encoding类型
     *
     * @param encodingType
     * @return
     */
    public static Encoding getEncoding(@NotNull EncodingType encodingType) {
        Encoding enc = registry.getEncoding(encodingType);
        return enc;
    }

    /**
     * 获取encode的编码数组
     *
     * @param text
     * @return
     */
    public static List<Integer> encode(@NotNull EncodingType encodingType, String text) {
        if (StrUtil.isBlank(text)) {
            return new ArrayList<>();
        }
        Encoding enc = getEncoding(encodingType);
        List<Integer> encoded = enc.encode(text);
        return encoded;
    }

    /**
     * 计算指定字符串的tokens，通过EncodingType
     *
     * @param encodingType
     * @param text
     * @return
     */
    public static int tokens(@NotNull EncodingType encodingType, String text) {
        return encode(encodingType, text).size();
    }


    /**
     * 通过EncodingType和encoded编码数组，反推字符串文本
     *
     * @param encodingType
     * @param encoded
     * @return
     */
    public static String decode(@NotNull EncodingType encodingType, @NotNull List<Integer> encoded) {
        Encoding enc = getEncoding(encodingType);
        return enc.decode(encoded);
    }


    /**
     * 获取一个Encoding对象，通过模型名称
     *
     * @param modelName
     * @return
     */
    public static Encoding getEncoding(@NotNull String modelName) {
        return modelMap.get(modelName);
    }

    /**
     * 获取encode的编码数组，通过模型名称
     *
     * @param text
     * @return
     */
    public static List<Integer> encode(@NotNull String modelName, String text) {
        if (StrUtil.isBlank(text)) {
            return new ArrayList<>();
        }
        Encoding enc = getEncoding(modelName);
        if (Objects.isNull(enc)) {
            log.warn("[{}]模型不存在或者暂不支持计算tokens，直接返回tokens==0");
            return new ArrayList<>();
        }
        List<Integer> encoded = enc.encode(text);
        return encoded;
    }

    /**
     * 通过模型名称, 计算指定字符串的tokens
     *
     * @param modelName
     * @param text
     * @return
     */
    public static int tokens(@NotNull String modelName, String text) {
        return encode(modelName, text).size();
    }


    /**
     * 通过模型名称计算messages获取编码数组
     * 参考官方的处理逻辑：
     * <a href=https://github.com/openai/openai-cookbook/blob/main/examples/How_to_count_tokens_with_tiktoken.ipynb>https://github.com/openai/openai-cookbook/blob/main/examples/How_to_count_tokens_with_tiktoken.ipynb</a>
     *
     * @param modelName 模型名称
     * @param messages  消息体
     * @return
     */
    public static int tokens(@NotNull String modelName, @NotNull List<Message> messages) {
        Encoding encoding = getEncoding(modelName);
        int tokensPerMessage = 0;
        int tokensPerName = 0;
        //3.5统一处理
        if (modelName.equals("gpt-3.5-turbo-0301") || modelName.equals("gpt-3.5-turbo")) {
            tokensPerMessage = 4;
            tokensPerName = -1;
        }
        //4.0统一处理
        if (modelName.equals("gpt-4") || modelName.equals("gpt-4-0314")) {
            tokensPerMessage = 3;
            tokensPerName = 1;
        }
        int sum = 0;
        for (Message msg : messages) {
            sum += tokensPerMessage;
            sum += tokens(encoding, msg.getContent());
            sum += tokens(encoding, msg.getRole());
            sum += tokens(encoding, msg.getName());
            if (StrUtil.isNotBlank(msg.getName())) {
                sum += tokensPerName;
            }
        }
        sum += 3;
        return sum;
    }

    /**
     * 通过模型名称和encoded编码数组，反推字符串文本
     *
     * @param modelName
     * @param encoded
     * @return
     */
    public static String decode(@NotNull String modelName, @NotNull List<Integer> encoded) {
        Encoding enc = getEncoding(modelName);
        return enc.decode(encoded);
    }


    /**
     * 获取modelType
     *
     * @param name
     * @return
     */
    public static ModelType getModelTypeByName(String name) {
        if (ChatCompletion.Model.GPT_3_5_TURBO_0301.getName().equals(name)) {
            return ModelType.GPT_3_5_TURBO;
        }
        if (ChatCompletion.Model.GPT_4.getName().equals(name)
                || ChatCompletion.Model.GPT_4_32K.getName().equals(name)
                || ChatCompletion.Model.GPT_4_32K_0314.getName().equals(name)
                || ChatCompletion.Model.GPT_4_0314.getName().equals(name)) {
            return ModelType.GPT_4;
        }

        for (ModelType modelType : ModelType.values()) {
            if (modelType.getName().equals(name)) {
                return modelType;
            }
        }
        log.warn("[{}]模型不存在或者暂不支持计算tokens", name);
        return null;
    }
}
