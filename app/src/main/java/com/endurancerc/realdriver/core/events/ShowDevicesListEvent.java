package com.endurancerc.realdriver.core.events;

public class ShowDevicesListEvent {

    private final CharSequence devicesName[];

    public ShowDevicesListEvent(CharSequence devicesName[]) {
        this.devicesName = devicesName;
    }

    public CharSequence[] getCharSequenceArray() {
        return devicesName;
    }

}