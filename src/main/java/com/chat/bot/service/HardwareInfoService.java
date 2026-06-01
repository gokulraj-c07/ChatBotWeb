package com.chat.bot.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class HardwareInfoService {
    private String executeCommand(String command) {

        try {
            ProcessBuilder builder = new ProcessBuilder("powershell", "-Command", command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line.trim()).append("\n");
            }
            return output.toString().trim();
        } catch (Exception e) {
            return "Hardware details not available";
        }
    }

    public String getMotherboardDetails() {
        try {
            String manufacturer = executeCommand("(Get-CimInstance Win32_BaseBoard).Manufacturer");
            String product = executeCommand("(Get-CimInstance Win32_BaseBoard).Product");
            return "Manufacturer: " + manufacturer +
                    "\nProduct: " + product;
        } catch (Exception e) {
            return "Motherboard details not available";
        }
    }

    public String getWirelessDetails() {
        try {
            String name = executeCommand("(Get-NetAdapter | Where {$_.Status -eq 'Up'}).Name");
            String description = executeCommand("(Get-NetAdapter | Where {$_.Status -eq 'Up'}).InterfaceDescription");
            return "Name: " + name +
                    "\nInterfaceDescription: " + description;
        } catch (Exception e) {
            return "Wireless adapter details not available";
        }
    }

    public String getBatteryDetails() {
        try {
            String name = executeCommand("(Get-CimInstance Win32_Battery).Name");
            String charge = executeCommand("(Get-CimInstance Win32_Battery).EstimatedChargeRemaining");
            String statusCode = executeCommand("(Get-CimInstance Win32_Battery).BatteryStatus");
            String status = convertBatteryStatus(statusCode);

            return "Name: " + name +
                    "\nEstimatedChargeRemaining: " + charge + "%" +
                    "\nBatteryStatus: " + status;
        } catch (Exception e) {
            return "Battery details not available";
        }
    }

    private String convertBatteryStatus(String code) {
        switch (code) {
            case "1":
                return "Discharging";
            case "2":
                return "Charging";
            case "3":
                return "Fully Charged";
            case "4":
                return "Low";
            case "5":
                return "Critical";
            default:
                return "Unknown";
        }
    }
}
