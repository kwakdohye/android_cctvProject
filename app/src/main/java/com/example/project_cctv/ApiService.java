package com.example.project_cctv;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService { //가변
    @GET("5690000/sjCCTV/sj_00000030?pageIndex=1&pageUnit=200&searchCondition=mngInst_Nm&serviceKey=2S8d1Ch%2FWRZXVmLghp0JH5L0gjTQsd5qfy7s3oRnPMepn2J4e%2FBMYmIi9WKkuJ6CnzYjeve9ROkQiybS008k3Q%3D%3D")
    Call<CctvResponse> getCctvList();

    @GET("5690000/sjSafetyEmergencyBellLocation/sj_00000640?pageIndex=1&pageUnit=20&searchCondition=instl_Place_Ty_Se&searchKeyword=2&serviceKey=2S8d1Ch%2FWRZXVmLghp0JH5L0gjTQsd5qfy7s3oRnPMepn2J4e%2FBMYmIi9WKkuJ6CnzYjeve9ROkQiybS008k3Q%3D%3D")
    Call<EmergeBellResponse> getBellList();   // 안전비상벨위치 조회

    @GET("5690000/sjSecLight/sj_00000440?pageIndex=1&pageUnit40=0&searchCondition=instl_Stle&searchKeyword=한전주&serviceKey=2S8d1Ch%2FWRZXVmLghp0JH5L0gjTQsd5qfy7s3oRnPMepn2J4e%2FBMYmIi9WKkuJ6CnzYjeve9ROkQiybS008k3Q%3D%3D")
    Call<SeLightReponse> getLightList();  // 보안등 조회
}
