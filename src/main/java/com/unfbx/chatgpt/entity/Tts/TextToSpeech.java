package com.unfbx.chatgpt.entity.Tts;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class TextToSpeech {

    @Builder.Default
    private String model = Model.TTS_1.getName();
    /**
     * 音频声音源
     *
     * @see TtsVoice
     */
    private String voice;
    /**
     * 输入内容
     */
    private String input;
    /**
     * 输出音频文件格式
     *
     * @see TtsFormat
     */
    @JsonProperty("response_format")
    private String responseFormat;


    @Getter
    @AllArgsConstructor
    public enum Model {
        TTS_1("tts-1"),
        TTS_1_HD("tts-1-hd"),
        ;
        private final String name;
    }
}
