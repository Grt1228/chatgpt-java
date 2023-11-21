package com.unfbx.chatgpt.entity.moderations;

import com.unfbx.chatgpt.exception.BaseException;
import com.unfbx.chatgpt.exception.CommonError;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 描述：文本审核，敏感词鉴别
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Data
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class Moderation implements Serializable {

    @NonNull
    private List<String> input;
    @Builder.Default
    private String model = Model.TEXT_MODERATION_LATEST.getName();

    public void setInput(List<String> input) {
        if (Objects.isNull(input) || input.size() == 0) {
            log.error("input不能为空");
            throw new BaseException(CommonError.PARAM_ERROR);
        }
        this.input = input;
    }

    public void setModel(Model model) {
        if (Objects.isNull(model)) {
            model = Model.TEXT_MODERATION_LATEST;
        }
        this.model = model.getName();
    }

    @Getter
    @AllArgsConstructor
    public enum Model {
        TEXT_MODERATION_STABLE("text-moderation-stable"),
        TEXT_MODERATION_LATEST("text-moderation-latest"),
        ;

        private final String name;
    }
}
