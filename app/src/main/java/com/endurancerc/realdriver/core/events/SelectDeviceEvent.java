package com.endurancerc.realdriver.core.events;

public class SelectDeviceEvent {
    private int device;

    public SelectDeviceEvent(int device) {
        this.device = device;
    }

    public int getDevice() {
        return device;
    }
}