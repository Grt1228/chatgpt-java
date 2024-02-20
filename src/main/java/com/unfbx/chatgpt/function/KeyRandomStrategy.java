package com.unfbx.chatgpt.function;

import cn.hutool.core.util.RandomUtil;
import okhttp3.Request;

import java.util.List;

/**
 * 描述：随机策略
 *
 * @author https:www.unfbx.com
 * @since 2023-04-03
 */
public class KeyRandomStrategy implements KeyStrategyFunction<List<String>, String> {

    @Override
    public String apply(List<String> apiKeys, Request request) {
        return RandomUtil.randomEle(apiKeys);
    }
}
