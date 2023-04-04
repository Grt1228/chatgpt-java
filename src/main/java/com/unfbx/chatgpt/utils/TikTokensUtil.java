package com.unfbx.chatgpt.utils;

import cn.hutool.core.util.StrUtil;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.ModelType;
import com.unfbx.chatgpt.entity.chat.Message;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @since 2023-04-04
 */
@Slf4j
public class TikTokensUtil {

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
    public static long tokens(@NotNull Encoding enc, String text) {
        return encode(enc, text).size();
    }


    /**
     * 通过Encoding和encoded数组反推text信息
     *
     * @param enc
     * @param encoded
     * @return
     */
    public static String decode(@NotNull Encoding enc, List<Integer> encoded) {
        return enc.decode(encoded);
    }

    /**
     * 获取一个Encoding对象，通过Encoding类型
     *
     * @param encodingType
     * @return
     */
    public static Encoding getEncoding(EncodingType encodingType) {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        Encoding enc = registry.getEncoding(encodingType);
        return enc;
    }

    /**
     * 获取encode的编码数组
     *
     * @param text
     * @return
     */
    public static List<Integer> encode(EncodingType encodingType, String text) {
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
    public static long tokens(EncodingType encodingType, String text) {
        Encoding enc = getEncoding(encodingType);
        List<Integer> encoded = enc.encode(text);
        return encoded.size();
    }

    /**
     * 通过EncodingType和encoded编码数组，反推字符串文本
     *
     * @param encodingType
     * @param encoded
     * @return
     */
    public static String decode(EncodingType encodingType, List<Integer> encoded) {
        Encoding enc = getEncoding(encodingType);
        return enc.decode(encoded);
    }


    /**
     * 获取一个Encoding对象，通过模型名称
     *
     * @param modelName
     * @return
     */
    public static Encoding getEncoding(String modelName) {
        EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
        ModelType modelType = getModelTypeByName(modelName);
        if (Objects.isNull(modelType)) {
            return null;
        }
        Encoding enc = registry.getEncodingForModel(modelType);
        return enc;
    }

    /**
     * 获取encode的编码数组，通过模型名称
     *
     * @param text
     * @return
     */
    public static List<Integer> encode(String modelName, String text) {
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
     * 计算指定字符串的tokens，通过模型名称
     *
     * @param modelName
     * @param text
     * @return
     */
    public static long tokens(String modelName, String text) {
        Encoding enc = getEncoding(modelName);
        List<Integer> encoded = enc.encode(text);
        return encoded.size();
    }

    /**
     * 通过模型名称和encoded编码数组，反推字符串文本
     *
     * @param modelName
     * @param encoded
     * @return
     */
    public static String decode(String modelName, List<Integer> encoded) {
        Encoding enc = getEncoding(modelName);
        return enc.decode(encoded);
    }


    /**
     * 获取modelType
     *
     * @param name
     * @return
     */
    private static ModelType getModelTypeByName(String name) {
        for (ModelType modelType : ModelType.values()) {
            if (modelType.getName().equals(name)) {
                return modelType;
            }
        }
        log.warn("[{}]模型不存在或者暂不支持计算tokens", name);
        return null;
    }
}
