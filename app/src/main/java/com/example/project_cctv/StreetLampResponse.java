package com.example.project_cctv;

public class StreetLampResponse {
    private String address;
    private double latitude;
    private double longitude;

    // 생성자, getter 및 setter 메서드 등 필요한 메서드를 추가할 수 있음

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
