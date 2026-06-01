package com.chat.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    private String reply;
    private String responseType;
    private List<MenuItem> menuItems;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MenuItem {
        private Long id;
        private String label;
    }
}