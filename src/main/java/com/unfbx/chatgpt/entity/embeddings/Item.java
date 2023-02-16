package com.unfbx.chatgpt.entity.embeddings;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Item {
    private String object;
    private List<BigDecimal> embedding;
    private Integer index;
}
