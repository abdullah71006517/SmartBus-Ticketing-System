package com.example.demo1.Model;



public class BookingSession {
    private static BusInfo selectedBus;
    private static String seatNo;
    private static double price;
    private static String routeFrom;
    private static String routeTo;

    public static void setRoute(String from, String to) {
        routeFrom = from;
        routeTo = to;
    }

    public static String getRouteFrom() {
        return routeFrom;
    }

    public static String getRouteTo() {
        return routeTo;
    }

    public static void setBookingInfo(BusInfo bus, String seat, double fare) {
        selectedBus = bus;
        seatNo = seat;
        price = fare;
    }

    public static BusInfo getSelectedBus() {
        return selectedBus;
    }

    public static String getSeatNo() {
        return seatNo;
    }

    public static double getPrice() {
        return price;
    }

    public static void clear() {
        selectedBus = null;
        seatNo = null;
        price = 0.0;
    }



}
