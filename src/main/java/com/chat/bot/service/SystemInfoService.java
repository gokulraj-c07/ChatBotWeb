package com.chat.bot.service;

import org.springframework.stereotype.Service;

@Service
public class SystemInfoService {

    public String getOSDetails() {

        String osName = System.getProperty("os.name");
        String osVersion = System.getProperty("os.version");
        String architecture = System.getProperty("os.arch");

        return osName + "\n" + "Version " + osVersion + "\n" + architecture;
    }
}
