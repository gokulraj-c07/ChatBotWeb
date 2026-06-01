package com.chat.bot.service;

import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class DiskInfoService {

    public String getDiskDetails(String driveLetter) {

        try {
            File drive = new File(driveLetter.toUpperCase() + ":\\");
            if (!drive.exists()) {
                return "Drive " + driveLetter.toUpperCase() + ": not found";
            }
            long totalSpace = drive.getTotalSpace();
            long freeSpace = drive.getFreeSpace();
            long usedSpace = totalSpace - freeSpace;
            return driveLetter.toUpperCase() + ": Total "
                    + formatGB(totalSpace) + "\n"
                    + " | Free "
                    + formatGB(freeSpace) + "\n"
                    + " | Used "
                    + formatGB(usedSpace);
        } catch (Exception e) {
            return "Disk details not available";
        }
    }

    public String getAllDrivesDetails() {
        File[] drives = File.listRoots();
        StringBuilder result = new StringBuilder();
        for (File drive : drives) {
            long totalSpace = drive.getTotalSpace();
            long freeSpace = drive.getFreeSpace();
            long usedSpace = totalSpace - freeSpace;
            result.append(drive.getPath())
                    .append(" Total ")
                    .append(formatGB(totalSpace))
                    .append(" | Free ")
                    .append(formatGB(freeSpace))
                    .append(" | Used ")
                    .append(formatGB(usedSpace))
                    .append("\n");
        }
        return result.toString();
    }

    private String formatGB(long bytes) {
        double gb = bytes / (1024.0 * 1024 * 1024);
        return String.format("%.2f GB", gb);
    }
}
