package com.chat.bot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private String message;

    /**
     * Optional: frontend sends the intentId when the user clicks a menu item.
     * This lets us skip keyword matching and go straight to the correct child list.
     * Null = normal text message.
     */
    private Long intentId;
}