package com.unfbx.chatgpt.entity.embeddings;

import com.unfbx.chatgpt.entity.common.Usage;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 * @date 2023-02-15
 */
@Data
public class EmbeddingResponse {

    private String object;
    private List<Item> data;
    private String model;
    private Usage usage;
}
