package com.unfbx.chatgpt.entity.chat.tool;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ToolChoiceObjFunction {

    private String name;
}
