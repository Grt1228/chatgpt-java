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
public class DefaultPluginEventSourceListener extends PluginEventSourceListener {

    private String msg = "";

    final CountDownLatch countDownLatch;

    public DefaultPluginEventSourceListener(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        log.debug("插件开发返回信息收集sse监听器建立连接...");
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        log.debug("插件开发返回信息收集sse监听器返回数据：{}", data);
        msg += data;
        if ("[DONE]".equals(data)) {
            countDownLatch.countDown();
            log.debug("插件开发返回信息收集sse监听器返回数据结束了");
            return;
        }
        ChatCompletionResponse chatCompletionResponse = JSONUtil.toBean(data, ChatCompletionResponse.class);
        if (Objects.nonNull(chatCompletionResponse.getChoices().get(0).getDelta().getFunctionCall())) {
            msg += chatCompletionResponse.getChoices().get(0).getDelta().getFunctionCall().getArguments();
        }
    }

    @Override
    public void onClosed(EventSource eventSource) {
        log.debug("插件开发返回信息收集sse监听器关闭连接...");
    }

    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {
        if (Objects.isNull(response)) {
            log.error("插件开发返回信息收集sse监听器,连接异常:{}", t);
            eventSource.cancel();
            return;
        }
        ResponseBody body = response.body();
        if (Objects.nonNull(body)) {
            log.error("插件开发返回信息收集sse监听器,连接异常data：{}，异常：{}", body.string(), t);
        } else {
            log.error("插件开发返回信息收集sse监听器,连接异常data：{}，异常：{}", response, t);
        }
        eventSource.cancel();
    }

    @Override
    public String getArguments() {
        return this.msg;
    }
}
