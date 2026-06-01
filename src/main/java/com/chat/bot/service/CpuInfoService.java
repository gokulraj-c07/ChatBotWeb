package com.chat.bot.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class CpuInfoService {

    public String getCpuDetails() {

        try {
            ProcessBuilder builder = new ProcessBuilder(
                    "wmic", "cpu", "get", "Name,NumberOfCores,NumberOfLogicalProcessors", "/format:list");
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            String name = "";
            String cores = "";
            String logicalProcessors = "";

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name=")) {
                    name = line.replace("Name=", "").trim();
                }
                if (line.startsWith("NumberOfCores=")) {
                    cores = line.replace("NumberOfCores=", "").trim();
                }
                if (line.startsWith("NumberOfLogicalProcessors=")) {
                    logicalProcessors = line.replace("NumberOfLogicalProcessors=", "").trim();
                }
            }
            if (!name.isEmpty()) {
                return name + "\n" + cores + " cores" + "\n" + logicalProcessors + " logical processors";
            }
        } catch (Exception e) {
            return "CPU details not available";
        }
        return "CPU details not available";
    }
}
