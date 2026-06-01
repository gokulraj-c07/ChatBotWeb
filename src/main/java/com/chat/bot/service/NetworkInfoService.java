package com.chat.bot.service;

import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.NetworkInterface;

@Service
public class NetworkInfoService {
    public String getNetworkDetails() {

        try {

            InetAddress localHost = InetAddress.getLocalHost();

            String hostName = localHost.getHostName();
            String ipAddress = localHost.getHostAddress();

            NetworkInterface network = NetworkInterface.getByInetAddress(localHost);

            if (network == null) {
                return "Network details not available";
            }
            byte[] macBytes = network.getHardwareAddress();

            if (macBytes == null) {
                return "Network details not available";
            }

            return "Hostname: " + hostName + "\n"
                    + "IP: " + ipAddress;

        } catch (Exception e) {

            return "Network details not available";
        }
    }
}
