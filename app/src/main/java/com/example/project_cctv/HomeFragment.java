// 내위치 표시할 때 내가 어디있는지를 긴급상황 시 바로 전송될 수 있도록 (문자, 그 외의 것들 -긴급번호 OR 관련 사람)
package com.example.project_cctv;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    // 위치 권한 요청 사용되는 코드, 사용자 위치 접근
    // 개발자가 임의로 선택한 값을 말한다.
    private ToggleButton toggleButton1, toggleButton2;
    private GoogleMap gMap;
    private LocationManager locationManager;

    RelativeLayout btnAmbul, btnPolice, btnFire;

    SupportMapFragment mapFragment;

    ImageView imgSos;

    //private boolean PAUSED;
    // 아직 필요한지는 모름 - 이것을 쓰는 이유는 재생상태를 추적하기 위해서 쓴다.

    boolean isPlaying = false;
    // 변수초기화
    // 초기에 아무것도 재생되고 있지 않음을 나타냄

    List<String> lines = new ArrayList<>();
    int placeCount = 0;

    //CCTVLIST
    private List<CctvResponse.CctvItem> cctvList = new ArrayList<>();

    // 카메라 위치 변경하는 메서드  -> 왜 하냐? 사용자 경험(UX)을 개선하고 지도의 표시를 사용자가 보고자 하는 방향으로 제어하기 위해
    private void changeLocation(String latitude, String longitude) {
        LatLng location = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
        //Double.parseDouble() 메서드를 사용하여 문자열로 받아들인 위도와 경도를 실수(double)로 변환
        gMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View homeView = inflater.inflate(R.layout.fragment_home, container, false);

        imgSos = homeView.findViewById(R.id.imgSos);

        toggleButton1 = homeView.findViewById(R.id.toggleButton1);
        toggleButton2 = homeView.findViewById(R.id.toggleButton2);

        btnAmbul = homeView.findViewById(R.id.btnAmbul);
        btnPolice = homeView.findViewById(R.id.btnPolice);
        btnFire = homeView.findViewById(R.id.btnFire);

        // 상단 sos누르면 112로 이동
        imgSos.setOnClickListener(v -> {
            Uri uri = Uri.parse("tel:112");   // 객체 변환
            Intent intent = new Intent(Intent.ACTION_DIAL, uri);
            startActivity(intent);
        });


        // 이미지 누르면 소리 나고
        MediaPlayer mPlayerAmbul, mPlayerFire, mPlayerPolice;
        mPlayerAmbul = MediaPlayer.create(getActivity(), R.raw.ambul);
        mPlayerFire = MediaPlayer.create(getActivity(), R.raw.fire);
        mPlayerPolice = MediaPlayer.create(getActivity(), R.raw.police);

        // 각 버튼에 대한 클릭 이벤트 처리
        btnAmbul.setOnClickListener(v -> {
            if (!isPlaying) {
                mPlayerAmbul.start();
                isPlaying = true; // 재생 중으로 설정
            } else {
                mPlayerAmbul.pause();
                isPlaying = false; // 재생 중이 아니라는 것을 설정
            }
        });

        //isplaying = true or isplaying = false 부분은 없어도 되나 보기 쉽게 하기위해 써놓음

        btnPolice.setOnClickListener(v -> {
            if (!isPlaying) {
                mPlayerPolice.start();
                isPlaying = true;
            } else {
                mPlayerPolice.pause();
                isPlaying = false;
            }
        });

        btnFire.setOnClickListener(v -> {
            if (!isPlaying) {
                mPlayerFire.start();
                isPlaying = true;


            } else {
                mPlayerFire.pause();
                isPlaying = false;
            }
        });


        //토글버튼 클릭시 위성 or 구글 지도
        toggleButton1.setOnClickListener(v -> {
            if (gMap != null) {
                gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
        });

        toggleButton2.setOnClickListener(v -> {
            if (gMap != null) {
                gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                Log.d("ToggleButton", "onMapReady() 호출 시도");
                // 메서드 호출

            }
        });

        // 구글지도 고정된 형식
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(googleMap -> {
            gMap = googleMap;    // 메서드 내에서 전역 변수 gMap에 지도 객체를 할당하는 것,
            // 어디서든 접근가능
            // 이 메서드 안에서만 사용할 경우는 쓸 필요 없음
            /* gMap = map; 충돌 일어날 수 있다. */
            // gMap.setMapStyle(null);
            // gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);  // 위성지도
            gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);   // 일반구글지도
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.5040736, 127.2494855), 13));
            gMap.getUiSettings().setZoomControlsEnabled(true); //확대. 축소 버튼
            if (cctvList != null && !cctvList.isEmpty()) {
                // cctvList가 비어 있는지 확인  //두개 꼭 써주기
                // !cctvList.isEmpty() cctv 리스트가 비어있지 않을 때
                for (CctvResponse.CctvItem cctv : cctvList) {
                    try {
                        // 두번째 반복문 : onMapReady 메서드 내부
                        // 지도가 사용가능한 상태가 되었을 때 지도에 마커를 추가하기 위해 필요
                        double latitude = Double.parseDouble(cctv.getLatitude());
                        double longitude = Double.parseDouble(cctv.getLongitude());

                        LatLng markerLocation = new LatLng(latitude, longitude);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(markerLocation);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.cctvmarkermini));
                        markerOptions.title(cctv.getMngInstNm());
                        markerOptions.snippet(cctv.getAddr());

                        gMap.addMarker(markerOptions);

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        // 숫자 변환 오류 처리
                    }
                }
                // 모든 마커가 보이도록 지도의 경계를 계산하여 이동
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (CctvResponse.CctvItem cctv : cctvList) {
                    try {
                        double latitude = Double.parseDouble(cctv.getLatitude());
                        double longitude = Double.parseDouble(cctv.getLongitude());
                        builder.include(new LatLng(latitude, longitude));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        // 숫자 변환 오류 처리
                    }
                }
                LatLngBounds bounds = builder.build();
                gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
            }

            // 위치 권한 요청코드, 사용자에게 위치권한 요청
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }

            gMap.setMyLocationEnabled(true);   // 내 위치 버튼을 탭하면 현재 위치 표시
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                LatLng currentLocation = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                gMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                Log.i("확인맵","확인맵");
            }

        });


        //위치서비스 사용하는 경우 필요
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // 앱은 위치 관리자와 같은 위치 관련 서비스를 액세스 할 수 있음.

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        //RetrofitClient 클래스는 Retrofit라이브러리를 초기화하고 설정함

        Call<CctvResponse> call = apiService.getCctvList();
        // ApiServce에서 가져옴

        // enqueue() 메서드는 http요청을 실행하고 해당 요청에 대한 응답을 처리하기 위한 콜백 등록
        // ul 스레드 차단하지 않고 비동기적으로 요청 실행
        call.enqueue(new Callback<CctvResponse>() {
            @Override
            public void onResponse(Call<CctvResponse> call, Response<CctvResponse> response) {
                Log.i("확인", "여기다");
                if (response.isSuccessful()) {
                    CctvResponse cctvResponse = response.body();
                    cctvList = cctvResponse.getData();
                    String content = ""; //이걸 사용함으로써 문자열을 저장하기 위한 공간 생성하고 초기에 아무내용 없는 상태로 만듦
                    for (CctvResponse.CctvItem cctv : cctvList) {
                        content += "관리기관명: " + cctv.getMngInstNm() + "\n";
                        content += "도로명주소: " + cctv.getRoadNmAddr() + "\n";
                        content += "지번주소: " + cctv.getAddr() + "\n";
                        content += "설치목적: " + cctv.getInstlPurpsSe() + "\n";
                        content += "카메라 대수: " + cctv.getCameraCo() + "\n";
                        content += "카메라 화소수: " + cctv.getCameraPixel() + "\n";
                        content += "촬영방면 정보: " + cctv.getPotogrf() + "\n";
                        content += "보관일수: " + cctv.getCstdyDeCo() + "\n";
                        content += "전화번호: " + cctv.getTelno() + "\n";
                        content += "위도: " + cctv.getLatitude() + "\n";
                        content += "경도: " + cctv.getLongitude() + "\n";
                        content += "데이터 기준일자: " + cctv.getCrtrYmd() + "\n\n";

                        // 여기에 있어야 마커 표시됨
                        // 첫번째 반복문 :call.enqueue 콜백 메서드 내부에 존재,
                        // Retrofit를 사용하요 웹 서비스로부터 데이터를 비동기적으로 가져오기에 필요
                        double latitude = Double.parseDouble(cctv.getLatitude());
                        double longitude = Double.parseDouble(cctv.getLongitude());

                        LatLng markerLocation = new LatLng(latitude, longitude);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(markerLocation);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.cctvmarker));
                        markerOptions.title(cctv.getRoadNmAddr());
                        markerOptions.snippet(cctv.getPotogrf());

                        gMap.addMarker(markerOptions);
                    }
                    Log.i("확인", content);
                } else {
                    Toast.makeText(getActivity(), "오류입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CctvResponse> call, Throwable t) {
                Log.e("NetworkError", "네트워크 요청 실패: " + t.getMessage());
                Toast.makeText(getActivity(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        // Inflate the layout for this fragment
        return homeView;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // 현재 위치가 변경 되었을 때 호출됩니다. 필요한 경우 구현함
    }

    //onRequestPermissionsResult 메서드에서는 위치 권한 요청에 대한 사용자의 응답을 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
                if(requestCode == LOCATION_PERMISSION_REQUEST_CODE){
            if(grantResults.length>0 && grantResults[0] ==
            PackageManager.PERMISSION_GRANTED){
                //위치 권한 허용시
                onMapReady(gMap);
            }else {
                // 위치 권한 거부시
                Toast.makeText(requireContext(),"허락이 안됨",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }
}