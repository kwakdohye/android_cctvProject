package com.example.project_cctv;
//
//import com.google.gson.annotations.SerializedName;
//
//import java.util.List;
//
//public class EmergeBellResponse {
//
//    @SerializedName("resultCode")
//        private String resultCode;
//        // 결과 코드
//
//    @SerializedName("resultMsg")
//        private String resultMsg;
//        // 결과 메시지
//
//    @SerializedName("totalCount")
//        private int totalCount;
//        // 총 항목 수
//
//    @SerializedName("pageIndex")
//        private int pageIndex;
//        // 페이지 인덱스
//
//    @SerializedName("pageUnit")
//        private int pageUnit;
//        // 페이지 단위
//
//    @SerializedName("searchCondition")
//        private String searchCondition;
//        // 검색 조건
//
//    @SerializedName("searchKeyword")
//        private String searchKeyword;
//        // 검색 키워드
//
//    @SerializedName("items")
//    private List<BellItem> items;
//
//    public List<BellItem> getData() {
//        return items;
//    }

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class EmergeBellResponse {
    @SerializedName("header")
    private Header header;

    @SerializedName("body")
    private Body body;

    public Header getHeader() {
        return header;
    }

    public Body getBody() {
        return body;
    }

    public List<BellItem> getData() {
        return body.getItems();
    }

    public static class Header {
        @SerializedName("resultCode")
        private String resultCode;

        @SerializedName("resultMsg")
        private String resultMsg;

        @SerializedName("totalCount")
        private int totalCount;

        @SerializedName("pageIndex")
        private int pageIndex;

        @SerializedName("pageUnit")
        private int pageUnit;

        @SerializedName("searchCondition")
        private String searchCondition;

        @SerializedName("searchKeyword")
        private String searchKeyword;

        public String getResultCode() {
            return resultCode;
        }

        public String getResultMsg() {
            return resultMsg;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public int getPageIndex() {
            return pageIndex;
        }

        public int getPageUnit() {
            return pageUnit;
        }

        public String getSearchCondition() {
            return searchCondition;
        }

        public String getSearchKeyword() {
            return searchKeyword;
        }
    }

    public static class Body {
        @SerializedName("items")
        private List<BellItem> items;

        public List<BellItem> getItems() {
            return items;
        }
    }

    public static class BellItem {
            @SerializedName("instlPurpsSe")
            private int installPurpose;
            // 설치 목적

            @SerializedName("instlPlaceTySe")
            private int installPlaceType;
            // 설치 장소 유형

            @SerializedName("instlLc")
            private String installLocation;
            // 설치 위치

            @SerializedName("roadNmAddr")
            private String roadNameAddress;
            // 도로명 주소

            @SerializedName("addr")
            private String address;
            // 주소

            @SerializedName("la")
            private double latitude;
            // 위도

            @SerializedName("lo")
            private double longitude;
            // 경도

            @SerializedName("adiSkll")
            private String additionalSkill;
            // 추가 기능

            @SerializedName("instlYear")
            private int installationYear;
            // 설치 년도

            @SerializedName("lastChckYmd")
            private String lastCheckDate;
            // 마지막 점검 일자

            @SerializedName("lastChckResultSe")
            private String lastCheckResult;
            // 마지막 점검 결과

            @SerializedName("mngInstNm")
            private String mngInstNm;
            // 관리 기관명

            @SerializedName("telno")
            private String telephoneNumber;
            // 전화번호

            @SerializedName("crtrYmd")
            private String createDate;

            public double getLatitude() {return latitude;}

            public double getLongitude() {return longitude;}

            public String getAddr() {return address;}

            public String getmngInstNm() {return mngInstNm;}

            public int getInstallPurpose() {return installPurpose;}

            public int getInstallPlaceType() {return installPlaceType;}

            public String getInstallLocation() {return installLocation;}

            public String getRoadNameAddress() {return roadNameAddress;}

            public String getAddress() {return address;}

            public String getAdditionalSkill() {return additionalSkill;}

            public int getInstallationYear() {return installationYear;}

            public String getLastCheckDate() {return lastCheckDate;}

            public String getLastCheckResult() {return lastCheckResult;}

            public String getMngInstNm() {return mngInstNm;}

            public String getTelephoneNumber() {return telephoneNumber;}

            public String getCreateDate() {return createDate;}
            // 생성일자
            }
        }
