package com.example.demo1.Model;

public class BusInfo {
    private String busId;
    private String from;
    private String to;
    private String price;
    private String time;

    public BusInfo(String busId, String from, String to, String price, String time) {
        this.busId = busId;
        this.from = from;
        this.to = to;
        this.price = price;
        this.time = time;
    }

    public String getBusId() {
        return busId;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getPrice() {
        return price;
    }

    public String getTime() {
        return time;
    }
}
