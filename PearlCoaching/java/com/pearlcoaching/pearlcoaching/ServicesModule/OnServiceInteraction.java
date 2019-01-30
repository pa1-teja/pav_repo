package com.pearlcoaching.pearlcoaching.ServicesModule;

public interface OnServiceInteraction {
    void goToHome(int serviceId);
    void onServiceInfo(int serviceId);
    void onServiceBooking(int serviceId);
    void onThankYou(String serviceId);
}
