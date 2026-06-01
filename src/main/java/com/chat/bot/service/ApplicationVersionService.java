package com.chat.bot.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class ApplicationVersionService {

    public String getVersion(String command) {
        try {
            ProcessBuilder builder = new ProcessBuilder("powershell", "-Command", command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.readLine();
            if (output != null && !output.isBlank()) {
                return output.trim();
            }
        } catch (Exception e) {
        }
        return "Not Installed";
    }

    // Chrome version detection
    public String getChromeVersion() {
        return getVersion(
                "(Get-Item 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe').VersionInfo.ProductVersion");
    }

    // VS Code version detection
    public String getVSCodeVersion() {
        return getVersion(
                "(Get-Item \"$env:LOCALAPPDATA\\Programs\\Microsoft VS Code\\Code.exe\").VersionInfo.ProductVersion");
    }

    // Java version detection
    public String getJavaVersion() {
        return getVersion("java -version 2>&1 | Select-Object -First 1");
    }

    // MySQL version detection
    public String getPythonVersion() {
        return getVersion("python --version");
    }

    // Node.js version detection
    public String getNodeVersion() {
        return getVersion("node -v");
    }

    // Git version detection
    public String getGitVersion() {
        return getVersion("git --version");
    }
}
