package com.unfbx.chatgpt.sse;

import cn.hutool.json.JSONUtil;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * 描述： 插件开发返回信息收集sse监听器
 *
 * @author https:www.unfbx.com
 * 2023-08-18
 */
@Slf4j
public abstract class PluginEventSourceListener extends EventSourceListener {

    public abstract String getArguments();
}
