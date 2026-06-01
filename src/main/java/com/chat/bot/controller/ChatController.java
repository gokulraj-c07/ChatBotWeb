package com.chat.bot.controller;

import com.chat.bot.dto.ChatRequest;
import com.chat.bot.dto.ChatResponse;
import com.chat.bot.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @PostMapping("/chat")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return chatService.getResponse(request);
    }
}