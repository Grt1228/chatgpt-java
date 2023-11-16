package com.unfbx.chatgpt.entity.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class PageRequest {

    /**
     * 一页数据大小
     */
    private Integer limit;
    /**
     * 排序类型
     */
    private String order;
    /**
     * 分页参数，之前的id，默认值：null
     */
    private String before;
    /**
     * 分页参数，之后的id，默认值：null
     */
    private String after;


    /**
     * 支持的2种类型
     */
    @Getter
    @AllArgsConstructor
    public enum Order {
        DESC("desc"),
        ASC("asc"),
        ;
        private final String name;
    }
}
