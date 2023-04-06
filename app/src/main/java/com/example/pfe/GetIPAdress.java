package com.example.pfe;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

public class GetIPAdress {
    public String getIPadress() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isUp() && networkInterface.getDisplayName().equals("wlan0")) {
                    for (InterfaceAddress address : networkInterface.getInterfaceAddresses()) {
                        if (address.getAddress() instanceof Inet4Address) {
                            String ipv4Address = address.getAddress().getHostAddress();
                            System.out.println("Wi-Fi IPv4 address: " + ipv4Address);
                            return ipv4Address;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}



