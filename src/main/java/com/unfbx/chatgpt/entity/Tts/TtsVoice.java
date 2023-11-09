package com.unfbx.chatgpt.entity.Tts;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 生成不同声音的音频
 * <p>具体语音效果参考：https://platform.openai.com/docs/guides/text-to-speech</p>
 */
@Getter
@AllArgsConstructor
public enum TtsVoice {

    ALLOY("mp3"),
    ECHO("opus"),
    FABLE("aac"),
    ONYX("flac"),
    NOVA("flac"),
    SHIMMER("flac"),
    ;

    private final String name;
}
