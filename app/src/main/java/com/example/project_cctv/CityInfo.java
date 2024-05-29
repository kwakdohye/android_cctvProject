package com.example.project_cctv;

public class CityInfo {

    private String lon;
    private String lat;
    private String buildNm;
    private String buildAddr;

    public void setBuildAddr(String buildAddr) {
        this.buildAddr = buildAddr;
    }


    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getBuildNm() {
        return buildNm;
    }

    public void setBuildNm(String buildNm) {
        this.buildNm = buildNm;
    }

    public String getBuildAddr() {
        return buildAddr;
    }
}
