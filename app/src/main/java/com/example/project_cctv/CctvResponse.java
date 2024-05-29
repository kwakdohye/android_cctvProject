package com.example.project_cctv;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class CctvResponse {
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

    public List<CctvItem> getData() {
        return body.getItems();
//        return getData();// 이렇게 쓰면 무한 재귀됨
    }

    public static class Header {
        @SerializedName("resultCode")
        private String resultCode;
        //결과코드

        @SerializedName("resultMsg")
        private String resultMsg;
        //결과메시지
    }

    public static class Body {
        @SerializedName("items")
        private List<CctvItem> items;

        public List<CctvItem> getItems() {
            return items;
        }
    }

    // static을 써주는 이유는 (정적) -> 이 클래스들이 외부에 클래스에 의존하지 않고 외부에서도 독립적으로 사용될 수 있기에
    // retrofit의 경우 json 응답을 파싱하기 위한 데이터 모델 클래스들은 외부 클래스와 독립적으로 사용되는 것이 일반적임

    public static class CctvItem {
        @SerializedName("mngInstNm")
        private String mngInstNm;
        //CCTV 관리기관명

        @SerializedName("roadNmAddr")
        private String roadNmAddr;
        //CCTV 위치 도로명주소정보

        @SerializedName("addr")
        private String addr;
        //CCTV 위치 지번주소정보

        @SerializedName("instlPurpsSe")
        private String instlPurpsSe;
        //CCTV 설치 목적 구분자 정보
        @SerializedName("cameraCo")
        private int cameraCo;
        //CCTV 카메라 대수

        @SerializedName("cameraPixel")
        private String cameraPixel;
        //CCTV 카메라 화소수

        @SerializedName("potogrf")
        private String potogrf;
        //CCTV 촬영방면 정보

        @SerializedName("cstdyDeCo")
        private int cstdyDeCo;
        //CCTV 보관일수

        @SerializedName("telno")
        private String telno;
        //CCTV 관리기관 전화번호

        @SerializedName("la")
        private String latitude;
        //CCTV 위치 위도 좌표정보

        @SerializedName("lo")
        private String longitude;
        //CCTV 위치 경도 좌표정보

        @SerializedName("crtrYmd")
        private String crtrYmd;

        public String getMngInstNm() {
            return mngInstNm;
        }

        public String getRoadNmAddr() {
            return roadNmAddr;
        }

        public String getAddr() { return addr; }

        public String getInstlPurpsSe() { return instlPurpsSe; }

        public int getCameraCo() { return cameraCo; }

        public String getCameraPixel() { return cameraPixel; }

        public String getPotogrf() { return potogrf;}

        public int getCstdyDeCo() { return cstdyDeCo;}

        public String getTelno() { return telno; }

        public String getLatitude() { return latitude; }

        public String getLongitude() { return longitude; }

        public String getCrtrYmd() { return crtrYmd; }
        //CCTV 데이터 기준일자

        // Getter 및 Setter 메소드들을 추가할 수 있습니다.
    }
}
