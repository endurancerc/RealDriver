package com.endurancerc.realdriver.core.events;

public class USBDataSendEvent {
    //private final String data;
    private final byte[] arr;

    //public USBDataSendEvent(String data) {
    //    this.data = data;
    public USBDataSendEvent(byte[] data) {
        this.arr = data;
    }

    //public String getData() {
    //    return data;
    public byte[] getData() {
        return arr;
    }

}