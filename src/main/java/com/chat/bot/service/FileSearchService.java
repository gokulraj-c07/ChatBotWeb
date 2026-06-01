package com.chat.bot.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class FileSearchService {

    public String findFilePath(String fileName) {
        try {
            String command = "Get-ChildItem -Path C:\\,D:\\ -Filter \"" + fileName +
                    "\" -Recurse -ErrorAction SilentlyContinue | Select -First 1 -ExpandProperty FullName";
            ProcessBuilder builder = new ProcessBuilder("powershell", "-Command", command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String result = reader.readLine();
            if (result != null && !result.isBlank()) {
                return result.trim();
            }
        } catch (Exception e) {
            return "File search failed.";
        }
        return "File not found on this system.";
    }
}