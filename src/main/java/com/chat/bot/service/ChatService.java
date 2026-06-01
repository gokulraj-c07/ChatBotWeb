package com.chat.bot.service;

import com.chat.bot.dto.ChatRequest;
import com.chat.bot.dto.ChatResponse;
import com.chat.bot.model.Intent;
import com.chat.bot.model.ServiceStatus;
import com.chat.bot.repository.IntentRepository;
import com.chat.bot.repository.ServiceStatusRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final IntentRepository intentRepository;
    private final MatcherService matcherService;
    private final SystemMonitorService systemMonitorService;
    private final DiskInfoService diskInfoService;
    private final NetworkInfoService networkInfoService;
    private final HardwareInfoService hardwareInfoService;
    private final FileSearchService fileSearchService;
    private final ApplicationVersionService applicationVersionService;
    private final LocalServiceChecker localServiceChecker;
    private final ServiceStatusRepository serviceStatusRepository;
    private final CpuInfoService cpuInfoService;

    private static final String FALLBACK = "I'm sorry, I didn't understand that. 😕\nType *hi* to see the main menu.";

    public ChatResponse getResponse(ChatRequest request) {

        if (request.getIntentId() != null) {
            return intentRepository.findById(request.getIntentId())
                    .map(this::buildResponse)
                    .orElse(fallback());
        }

        String message = request.getMessage();

        if (message == null || message.isBlank())
            return fallback();

        String lowerMsg = message.toLowerCase();

        Map<String, String> serviceMap = Map.of(
                "tomcat", "Tomcat9",
                "mysql", "MySQL80");

        if (lowerMsg.contains("os") || lowerMsg.contains("windows version") ||
                lowerMsg.contains("os version") || lowerMsg.contains("system version") ||
                lowerMsg.contains("windows info") || lowerMsg.contains("os details") ||
                lowerMsg.contains("system info")) {

            String osName = System.getProperty("os.name");
            String osVersion = System.getProperty("os.version");
            String osArchitecture = System.getProperty("os.arch");
            ServiceStatus log = new ServiceStatus();
            log.setOsName(osName);
            log.setOsVersion(osVersion);
            log.setOsArchitecture(osArchitecture);
            log.setCheckedAt(LocalDateTime.now());
            serviceStatusRepository.save(log);

            return ChatResponse.builder()
                    .reply("Operating System: " +
                            osName + " (Version " + osVersion + ", " + osArchitecture + ")")
                    .responseType("text")
                    .build();
        }

        if (lowerMsg.contains("cpu") ||
                lowerMsg.contains("processor")) {

            String cpuDetails = cpuInfoService.getCpuDetails();
            saveStatus("CPU", cpuDetails);
            return text("CPU Details: " + cpuDetails);
        }

        if (lowerMsg.contains("ram") ||
                lowerMsg.contains("memory")) {

            String ramDetails = systemMonitorService.getRamDetails();
            saveStatus("RAM", ramDetails);
            return text("RAM Details: " + ramDetails);
        }

        if (lowerMsg.matches(".*\\b[a-z]\\s*drive\\b.*") ||
                lowerMsg.matches(".*drive\\s*[a-z]\\b.*")) {

            char driveLetter = 'C';
            for (char ch = 'a'; ch <= 'z'; ch++) {
                if (lowerMsg.contains(ch + " drive")) {
                    driveLetter = ch;
                    break;
                }
            }
            String diskDetails = diskInfoService.getDiskDetails(String.valueOf(driveLetter));
            saveStatus("Disk " + Character.toUpperCase(driveLetter), diskDetails);
            return text(diskDetails);
        }

        if (lowerMsg.contains("disk") || lowerMsg.contains("disk space") || lowerMsg.contains("storage")) {

            String diskDetails = diskInfoService.getAllDrivesDetails();
            saveStatus("All Disks", diskDetails);
            return text(diskDetails);
        }

        if (lowerMsg.contains("network") || lowerMsg.contains("ip address") || lowerMsg.contains("ip")) {

            String networkDetails = networkInfoService.getNetworkDetails();
            saveStatus("Network", networkDetails);
            return text(networkDetails);
        }

        if (lowerMsg.contains("motherboard")) {
            String details = hardwareInfoService.getMotherboardDetails();
            saveStatus("Motherboard", details);
            return text("Motherboard Details:\n" + details);
        }

        if (lowerMsg.contains("wireless") || lowerMsg.contains("wifi") || lowerMsg.contains("bluetooth")) {

            String details = hardwareInfoService.getWirelessDetails();
            saveStatus("Wireless Adapter", details);
            return text("Wireless Details:\n" + details);
        }

        if (lowerMsg.contains("battery")) {
            String details = hardwareInfoService.getBatteryDetails();
            saveStatus("Battery", details);
            return text("Battery Details:\n" + details);
        }

        if (lowerMsg.contains("find") && message.contains(".")) {
            String fileName = extractFileName(message);
            if (fileName == null)
                return text("Please provide a valid file name with extension.");
            String filePath = fileSearchService.findFilePath(fileName);
            saveStatus("File Search", filePath);
            return text(filePath);
        }

        String version = null;
        String appName = null;
        if (lowerMsg.contains("chrome")) {
            version = applicationVersionService.getChromeVersion();
            appName = "Google Chrome";
        } else if (lowerMsg.contains("vs code") || lowerMsg.contains("vscode")) {
            version = applicationVersionService.getVSCodeVersion();
            appName = "VS Code";
        } else if (lowerMsg.contains("java")) {
            version = applicationVersionService.getJavaVersion();
            appName = "Java";
        } else if (lowerMsg.contains("python")) {
            version = applicationVersionService.getPythonVersion();
            appName = "Python";
        } else if (lowerMsg.contains("node")) {
            version = applicationVersionService.getNodeVersion();
            appName = "Node.js";
        } else if (lowerMsg.contains("git")) {
            version = applicationVersionService.getGitVersion();
            appName = "Git";
        }
        if (appName != null) {
            saveStatus(appName, version);
            if ("Not Installed".equals(version))
                return text(appName + " Not Installed");
            return text(appName + " version: " + version);
        }

        for (String keyword : serviceMap.keySet()) {
            if (lowerMsg.contains(keyword)) {
                String serviceName = serviceMap.get(keyword);
                String status = localServiceChecker.checkServiceStatus(serviceName);
                saveStatus(serviceName, status);
                return text(formatServiceName(keyword) + " is " + status + ".");
            }
        }

        List<Intent> roots = intentRepository.findByParentIdIsNullAndIsActiveTrueOrderByDisplayOrderAsc();
        for (Intent intent : roots) {
            if (matcherService.match(message, intent.getPattern()))
                return buildResponse(intent);
        }
        if (message.matches("^[a-zA-Z0-9]+$")) {
            return text(message + " Not Installed");
        }
        return fallback();
    }

    private ChatResponse buildResponse(Intent intent) {

        if ("menu".equalsIgnoreCase(intent.getResponseType())) {
            List<Intent> children = intentRepository
                    .findByParentIdAndIsActiveTrueOrderByDisplayOrderAsc(intent.getId());
            List<ChatResponse.MenuItem> items = children.stream().map(c -> new ChatResponse.MenuItem(c.getId(),
                    firstKeyword(c.getPattern()))).collect(Collectors.toList());
            return ChatResponse.builder()
                    .reply(intent.getResponse())
                    .responseType("menu")
                    .menuItems(items)
                    .build();
        }
        return text(intent.getResponse());
    }

    private String firstKeyword(String pattern) {

        if (pattern == null || pattern.isBlank())
            return "Option";

        String first = pattern.split(",")[0].trim();
        if (first.isEmpty())
            return "Option";

        return Character.toUpperCase(first.charAt(0)) + first.substring(1);
    }

    private ChatResponse fallback() {
        return text(FALLBACK);
    }

    private ChatResponse text(String msg) {

        return ChatResponse.builder()
                .reply(msg)
                .responseType("text")
                .build();
    }

    private void saveStatus(String name, String status) {

        ServiceStatus log = new ServiceStatus();
        log.setServiceName(name);
        log.setStatus(status);
        log.setCheckedAt(LocalDateTime.now());

        serviceStatusRepository.save(log);
    }

    private String extractFileName(String message) {

        String[] words = message.split("\\s+");
        for (String word : words) {

            if (word.matches(".*\\.[a-zA-Z0-9]{1,6}"))
                return word.replaceAll("[\"']", "").trim();
        }

        return null;
    }

    private String formatServiceName(String keyword) {

        switch (keyword) {

            case "tomcat":
                return "Apache Tomcat 9.0";

            case "mysql":
                return "MySQL";

            default:
                return keyword;
        }
    }
}