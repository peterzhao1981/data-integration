package com.mode.ippool.ipmodel;

public class IPMessage {
    private String IPAddress;
    private int IPPort;
    private String IPType;
    private String IPSpeed;

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String iPAddress) {
        IPAddress = iPAddress;
    }

    public int getIPPort() {
        return IPPort;
    }

    public void setIPPort(int iPPort) {
        IPPort = iPPort;
    }

    public String getIPType() {
        return IPType;
    }

    public void setIPType(String iPType) {
        IPType = iPType;
    }

    public String getIPSpeed() {
        return IPSpeed;
    }

    public void setIPSpeed(String iPSpeed) {
        IPSpeed = iPSpeed;
    }
}
