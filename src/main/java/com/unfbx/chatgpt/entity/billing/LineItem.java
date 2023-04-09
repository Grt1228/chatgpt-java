package com.unfbx.chatgpt.entity.billing;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 描述：金额消耗列表
 *
 * @author https:www.unfbx.com
 * @since 2023-04-08
 */
@Data
public class LineItem {
    /**
     * 模型名称
     */
    private String name;
    /**
     * 消耗金额
     */
    private BigDecimal cost;
}
