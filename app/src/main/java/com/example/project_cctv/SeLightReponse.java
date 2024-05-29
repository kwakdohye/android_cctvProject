package com.example.project_cctv;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SeLightReponse {
        // 헤더 정보
        @SerializedName("header")
        private Header header;

        // 바디 정보
        @SerializedName("body")
        private Body body;

    public List<SeLightItem> getData() {
        return body.getItems();
    }


    // 헤더 정보 클래스
        public static class Header {
            @SerializedName("resultCode")
            private String resultCode;
            // 결과 코드

            @SerializedName("resultMsg")
            private String resultMsg;
            // 결과 메시지

            @SerializedName("totalCount")
            private int totalCount;
            // 총 개수

            @SerializedName("pageIndex")
            private int pageIndex;
            // 페이지 인덱스

            @SerializedName("pageUnit")
            private int pageUnit;
            // 페이지 단위

            @SerializedName("searchCondition")
            private String searchCondition;
            // 검색 조건

            @SerializedName("searchKeyword")
            private String searchKeyword;
            // 검색 키워드
        }

        // 바디 정보 클래스
        public static class Body {
            @SerializedName("items")
            private List<SeLightItem> items;

            public List<SeLightItem> getItems() {
                return items;
            }
        }

            // 아이템 정보 클래스
            public static class SeLightItem {
                public String LatLng;
                @SerializedName("lc")
                private String location;
                // 위치

                @SerializedName("instlCo")
                private int installationCount;
                // 설치 개수

                @SerializedName("addr")
                private String address;
                // 주소

                @SerializedName("la")
                private double latitude;
                // 위도

                @SerializedName("lo")
                private double longitude;
                // 경도

                @SerializedName("instlStle")
                private String installationStyle;
                // 설치 스타일

                @SerializedName("telno")
                private String telNumber;
                // 전화번호

                @SerializedName("mngInstNm")
                private String mngInstNm;
                // 관리 기관명

                @SerializedName("crtrYmd")
                private String creationDate;

                public String getmngInstNm() {
                    return mngInstNm;
                }

                public String gettelno() {
                    return telNumber;
                }

                public double getLatitude() {
                    return latitude;
                }

                public double getLongitude() {
                    return longitude;
                }

                public String getMngInstNm() {
                    return mngInstNm;
                }

                public void setMngInstNm(String mngInstNm) {
                    this.mngInstNm = mngInstNm;
                }

                public String getAddr() {
                    return address;
                }

                public void setAddr(String addr) {
                    this.address = addr;
                }

//                public double getlc() {
//                    return Double.parseDouble(location);
//                }

                public int getinstlCo() {
                    return installationCount;
                }

                public String getla() {
                    return String.valueOf(latitude);
                }

                public String getlo() {
                    return location;
                }
                // 생성일자
            }
        }
