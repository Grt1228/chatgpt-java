package com.unfbx.chatgpt.entity.embeddings;

import com.unfbx.chatgpt.entity.common.Usage;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 描述：
 *
 * @author https:www.unfbx.com
 *  2023-02-15
 */
@Data
public class EmbeddingResponse implements Serializable {

    private String object;
    private List<Item> data;
    private String model;
    private Usage usage;
}
