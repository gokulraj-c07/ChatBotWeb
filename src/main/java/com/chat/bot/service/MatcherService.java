package com.chat.bot.service;

import org.springframework.stereotype.Service;

@Service
public class MatcherService {

    /**
     * Returns true if any comma-separated keyword in `pattern`
     * is found inside the normalized userInput.
     */
    public boolean match(String userInput, String pattern) {
        String normInput = normalize(userInput);
        for (String keyword : pattern.split(",")) {
            if (normInput.contains(normalize(keyword))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Normalize: lowercase → collapse repeated chars → strip punctuation
     * e.g. "Hiiii!!" → "hi"
     */
    public String normalize(String input) {
        if (input == null)
            return "";
        input = input.toLowerCase().trim();
        input = input.replaceAll("(.)\\1+", "$1"); // hiii → hi
        input = input.replaceAll("[^a-z0-9 ]", ""); // strip punctuation
        return input;
    }
}