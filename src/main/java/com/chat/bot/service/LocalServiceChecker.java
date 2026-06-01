package com.chat.bot.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class LocalServiceChecker {
    
    public String checkServiceStatus(String serviceName) {

        try {

            ProcessBuilder processBuilder = new ProcessBuilder("sc", "query", serviceName);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            process.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {

                if (line.contains("RUNNING")) {
                    return "Running";
                }

                if (line.contains("STOPPED")) {
                    return "Stop";
                }
            }

        } catch (Exception e) {
            return "Service not found";
        }

        return "Unknown";
    }
}
