package com.example.project_cctv;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://apis.data.go.kr/";
    // 서버의 기본주소   //불변    //http쓰면 보안때문에 안됨.

    public static Retrofit getRetrofitInstance(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    //Retrofit 빌더에 Gson 변환기(Converter)를 추가하는 메서드
                    //Json을 자바객체로 변환하기 위해 사용되는 부분
                    .build();
        }
        return retrofit;
    }
}