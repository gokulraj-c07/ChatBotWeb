package com.chat.bot.service;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Service;

@Service
public class SystemMonitorService {
    public String getRamDetails() {

        try {

            OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

            long totalMemory = osBean.getTotalMemorySize();
            long freeMemory = osBean.getFreeMemorySize();
            long usedMemory = totalMemory - freeMemory;

            return "Total RAM: " + formatGB(totalMemory) + "\n"
                    + "Available RAM: " + formatGB(freeMemory) + "\n"
                    + "Used RAM: " + formatGB(usedMemory);

        } catch (Exception e) {

            return "RAM details not available";
        }
    }

    private String formatGB(long bytes) {

        double gb = bytes / (1024.0 * 1024 * 1024);

        return String.format("%.2f GB", gb);
    }
}
