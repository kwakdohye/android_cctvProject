// 추가할 부분
// DB관련 => 자주찾는 장소를 저장할 수 있는 것을 참고하면 될듯(네이버 , 구글 참고)
package com.example.project_cctv;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private static LatLng Input_LatLng = null;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "MyMapFragment";
    SupportMapFragment mapFragment;
    GoogleMap map4;
    LinearLayout btnScLightMap, btnBellMap, btnCctvMap, btnLampMap;
    Switch btnGoogleMap;


    private List<EmergeBellResponse.BellItem> bellList = new ArrayList<>();
    private List<SeLightReponse.SeLightItem> seLightList = new ArrayList<>();
    private List<CctvResponse.CctvItem> cctvList = new ArrayList<>();
    private List<String> lampList = new ArrayList<>(); //csv파일임
    int placeCount = 0;
    private LocationManager locationManager;
    private ApiService apiService;
    private SearchView searchView;
    private String PlaceName;
    // 리스트 표시 여부를 기록하는 변수

    // 초기값 false로 설정  (숨김)
    // isVisible으로 변수이름을 설정함/
    boolean isCctvListVisible = false;
    boolean isBellListVisible = false;
    boolean isSeLightListVisible = false;
    boolean isLampListVisible = false;


    public void showCctvMap(List<CctvResponse.CctvItem> cctvList) {
        List<CityInfo> cityInfos = new ArrayList<>();

        if (cctvList != null && !cctvList.isEmpty()) {
            for (CctvResponse.CctvItem cctv : cctvList) {
                CityInfo cityInfo = new CityInfo();
                cityInfo.setLat(cctv.getLatitude());
                cityInfo.setLon(cctv.getLongitude());
                cityInfo.setBuildAddr(cctv.getAddr());
                cityInfos.add(cityInfo);
            }
        }
        processMap(cityInfos, "cctv");
        showFullMap(cityInfos);
        isCctvListVisible = true;
    }
    public void hideCctvMap(){
        map4.clear();
        isCctvListVisible = false;
    }
    
    public void showBellMap(List<EmergeBellResponse.BellItem> bellList) {
        List<CityInfo> cityInfos = new ArrayList<>();

        if (bellList != null && !bellList.isEmpty()) {
            for (EmergeBellResponse.BellItem bell : bellList) {
                CityInfo cityInfo = new CityInfo();
                cityInfo.setLat(String.valueOf(bell.getLatitude()));
                cityInfo.setLon(String.valueOf(bell.getLongitude()));
                cityInfo.setBuildAddr(bell.getAddr());
                cityInfos.add(cityInfo);
            }
        }
        processMap(cityInfos, "bell");
        showFullMap(cityInfos);
        isBellListVisible = true;
    }

    public void hideBellMap(){
        map4.clear();
        isBellListVisible = false;
    }

    public void showSeLightMap(List<SeLightReponse.SeLightItem> seLightList) {
        List<CityInfo> cityInfos = new ArrayList<>();

        if (seLightList != null && !seLightList.isEmpty()) {
            for (SeLightReponse.SeLightItem selight : seLightList) {
                CityInfo cityInfo = new CityInfo();
                cityInfo.setLat(String.valueOf(selight.getLatitude()));
                cityInfo.setLon(String.valueOf(selight.getLongitude()));
                cityInfo.setBuildAddr(selight.getAddr());
                cityInfos.add(cityInfo);
            }
        }
        processMap(cityInfos, "selight");
        showFullMap(cityInfos);
        isSeLightListVisible = true;
    }

    private void hideSeLightMap() {
        map4.clear();
        isSeLightListVisible = false;

    }

    public void showLampMap(List<String> lampList) {
        List<CityInfo> cityInfos = new ArrayList<>();

        if (lampList != null && !lampList.isEmpty()) {
            int count = 0; // 처리한 데이터 개수를 세는 변수
            for (String lamp : lampList) {
                String[] data = lamp.split(","); // 데이터를 쉼표(,)로 분리하여 배열로 저장
                if (data.length  >= 3) { // 최소한 주소, 위도, 경도가 있어야 함
                    CityInfo cityInfo = new CityInfo();
                    cityInfo.setBuildAddr(data[0].trim()); // 주소
                    cityInfo.setLat(data[1].trim()); // 위도
                    cityInfo.setLon(data[2].trim()); // 경도
                    cityInfos.add(cityInfo);
                    count++; // 처리한 데이터 개수 증가
                    if(count >=300){  // 300건 이상의 데이터를 처리하면 루프종료
                        break;
                    }
                }
            }
        }
        processMap(cityInfos, "lamp");
        showFullMap(cityInfos);
        isLampListVisible = true;
    }

    private void hideLampMap() {
        map4.clear();
        isLampListVisible = false;
    }

    public void processMap(List<CityInfo> cityInfos, String iconType) {
        map4.clear();

        if (cityInfos != null && !cityInfos.isEmpty()) {
            /*CityInfo cityInfo = cityInfos.get(0);*/
            // cctvList가 비어 있는지 확인  //두개 꼭 써주기
            // !cctvList.isEmpty() cctv 리스트가 비어있지 않을 때
            for (CityInfo cityInfo : cityInfos) {
                try {
                    // 두번째 반복문 : onMapReady 메서드 내부
                    // 지도가 사용가능한 상태가 되었을 때 지도에 마커를 추가하기 위해 필요
                    double latitude = Double.parseDouble(cityInfo.getLat());
                    double longitude = Double.parseDouble(cityInfo.getLon());

                    LatLng markerLocation = new LatLng(latitude, longitude);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(markerLocation);
                    if ("cctv".equals(iconType)) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.cctvmarkermini));
                    } else if ("bell".equals(iconType)) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bellmarker1));
                    } else if ("selight".equals(iconType)) {
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.securitymarker));
                    } else if ("lamp". equals(iconType)){
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.streetlamp));
                    }
                    markerOptions.title(cityInfo.getBuildNm());
                    markerOptions.snippet(cityInfo.getBuildAddr());


                    map4.addMarker(markerOptions);

                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    // 숫자 변환 오류 처리
                }
            }
        }
    }
    private void showFullMap(List<CityInfo> cityInfos) {
        if (!cityInfos.isEmpty()) {  // 리스트가 비어있지 않을때(확인하는 요소)
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (CityInfo cityInfo : cityInfos) {
                double latitude = Double.parseDouble(cityInfo.getLat());
                double longitude = Double.parseDouble(cityInfo.getLon());
                builder.include(new LatLng(latitude, longitude));
            }
            LatLngBounds bounds = builder.build();
            map4.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        }
    }
    // 모든 마커가 보이도록 지도의 경계를 계산하여 이동
    // 모든 마커가 추간된 후 호출됨, 첫 번째 반복분이 모두 완료된 후 발생
    // 지도 경계를 구성하는 것은 사용자가 모든 마커를 볼 수 있도록 지도 조정하는 것
    // 지도 경계 설정함으로써 사용자가 보다

    // LatLngBounds.Builder 클래스를 이용하여 지도 경계 구성하는 빌더 객체 생성
    // 이 빌더를 사용하여 마커의 위치를 포함하는 지도의 경계를 구성함

    //<1>
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mapView = inflater.inflate(R.layout.fragment_map, container, false);
        
        btnScLightMap = mapView.findViewById(R.id.btnScLightMap); // 보안등
        btnBellMap = mapView.findViewById(R.id.btnBellMap);
        btnCctvMap = mapView.findViewById(R.id.btnCctvMap);
        btnLampMap = mapView.findViewById(R.id.btnLampMap); // 가로등
        btnGoogleMap = mapView.findViewById(R.id.btnGoogleMap);

        readCSV();
        
        // 가로등
        btnLampMap.setOnClickListener(v -> {
            if(isLampListVisible){
                hideLampMap();
            }else {
                showLampMap(lampList);
            }
        });
        
        // cctv
        btnCctvMap.setOnClickListener(v -> {
            // showCctvMap 메서드 호출하여 CCTV 마커를 지도에 표시
            if(isCctvListVisible){
                hideCctvMap();
            }else {
                showCctvMap(cctvList);
            }
        });

        // 비상벨
        btnBellMap.setOnClickListener(v -> {
            // showCctvMap 메서드 호출하여 CCTV 마커를 지도에 표시
            if(isBellListVisible){
                hideBellMap();
            }else {
                showBellMap(bellList);
            }
        });

        // 보안등
        btnScLightMap.setOnClickListener(v -> {
            if(isSeLightListVisible){
                hideSeLightMap();
            }else{
                showSeLightMap(seLightList);
            }
        });

        //XML 레이아웃에서 정의된 SupportMapFragment를 찾아서 변수 mapFragment에 할당
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map4);
        mapFragment.getMapAsync(googleMap -> {
            //비동기적으로 지도를 로드하고 설정
            map4 = googleMap;
            map4.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.5040736, 127.2494855), 10));
            map4.clear();
            map4.getUiSettings().setZoomControlsEnabled(true);
            //지도의 UI 설정에서 확대/축소 컨트롤을 활성화

        });

        // 검색기능 불러오기
        // Places API 초기화함
        Places.initialize(getContext(),"AIzaSyDG_AcAMbrh8wdJe1Icy1FPOcNCUv9lr4Y");
        // Places API 클라이언트 생성
        PlacesClient placesClient = Places.createClient(requireContext());

        // 자동완성 Fragment 초기화
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        // 검색창 배경색 설정
        // autocompleteFragment코드는 라이브러리에서 제공되는 것이기에 xml에서 설정못함
        autocompleteFragment.getView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        // 힌트 텍스트 설정
        autocompleteFragment.setHint("검색하세요");

//        // 힌트 텍스트 색상 설정
        EditText searchEditText = autocompleteFragment.getView()
                .findViewById(com.google.android.libraries.places.R.id.places_autocomplete_search_input);
//        EditText searchEditText = autocompleteFragment.getView().findViewById(R.id.autocomplete_fragment);
        //autocompleteFragment의 레이아웃 구조가 코드에서 접근한 레이아웃과 일치하지 않기에 오류난다.
        searchEditText.setHintTextColor(ContextCompat.getColor(getContext(), R.color.black));

        // 필터링 할 장소의 유형을 설정함
        autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
        // 대한민국에서만 장소를 검색함
        autocompleteFragment.setCountries("KR");
        // 반환할 장소 필드 설정, 장소의 ID, 이름 및 위도 경도 정보
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG));

        // 장소 선택시 발생할 이벤트
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latLng = place.getLatLng();
                String placeName = place.getName();

                // 마커를 추가하기 전에 이전 마커를 모두 제거합니다.
                //  map4.clear();
                // 이 부분만 지우면 주변 마커 표시된게 그대러 나타남

                // 새로운 마커를 추가합니다.
                map4.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title(placeName));

                // 지도를 선택된 장소의 위치로 이동합니다.
                map4.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                Log.i(TAG, "Place:" + place.getName() + "," + place.getId());

                // 선택된 위치 주변의 CCTV를 표시합니다.
                // showCctvNearby(latLng);
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i(TAG,"An error occurred" + status);
            }
        });

        // 위치 권한 요청코드, 사용자에게 위치권한 요청
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }


        //위치서비스 사용하는 경우 필요, 위치 관리자 초기화함.
        locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);   // 실행안되서 바꾼부분 Context -> getActivity로 변경하니 앱이 꺼지지 않는다.
        // 앱은 위치 관리자와 같은 위치 관련 서비스를 액세스 할 수 있음.

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        //RetrofitClient 클래스는 Retrofit라이브러리를 초기화하고 설정함

        Call<CctvResponse> cctvCall = apiService.getCctvList();
        Call<EmergeBellResponse> emergeBellCall = apiService.getBellList();
        Call<SeLightReponse> seLightCall = apiService.getLightList();
        // ApiServce에서 가져옴

        // <4>
        // cctvCall.enqueue() 메서드를 사용하여 CCTV 정보를 서버로부터 비동기적으로 가져옵니다.
        // 이는 UI 스레드를 차단하지 않고 백그라운드에서 데이터를 로딩하여 앱의 응답성을 유지하는 데 도움
        cctvCall.enqueue(new Callback<CctvResponse>() {
            @Override
            public void onResponse(Call<CctvResponse> call, Response<CctvResponse> response) {
                Log.i("cctv", "확인");
                if (response.isSuccessful()) {
                    CctvResponse cctvResponse = response.body();
                    cctvList = cctvResponse.getData();
                    String content = ""; //이걸 사용함으로써 문자열 저장하기 위한 공간 생성하고 초기에 아무내용 없는 상태로 만듦.
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

                        map4.addMarker(markerOptions);
                    }
                    Log.i("확인", content);
                } else {
                    Toast.makeText(getActivity(), "오류입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            //<4>
            @Override
            public void onFailure(Call<CctvResponse> call, Throwable t) {
                Log.e("NetworkError", "네트워크 요청 실패: " + t.getMessage());
                Toast.makeText(getActivity(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        emergeBellCall.enqueue(new Callback<EmergeBellResponse>() {
            @Override
            public void onResponse(Call<EmergeBellResponse> call, Response<EmergeBellResponse> response) {
                Log.i("emergebell", "확인");
                if (response.isSuccessful()) {
                    EmergeBellResponse emergeBellResponse = response.body();
                    bellList = emergeBellResponse.getData();
                    String content = "";
                    for (EmergeBellResponse.BellItem bell : bellList) {
                        content += "설치 목적: " + bell.getInstallPurpose() + "\n";
                        content += "설치 장소 유형: " + bell.getInstallPlaceType() + "\n";
                        content += "설치 위치: " + bell.getInstallLocation() + "\n";
                        content += "도로명 주소: " + bell.getRoadNameAddress() + "\n";
                        content += "주소: " + bell.getAddress() + "\n";
                        content += "위도: " + bell.getLatitude() + "\n";
                        content += "경도: " + bell.getLongitude() + "\n";
                        content += "추가 기능: " + bell.getAdditionalSkill() + "\n";
                        content += "설치 년도: " + bell.getInstallationYear() + "\n";
                        content += "마지막 점검 일자: " + bell.getLastCheckDate() + "\n";
                        content += "마지막 점검 결과: " + bell.getLastCheckResult() + "\n";
                        content += "관리 기관명: " + bell.getMngInstNm() + "\n";
                        content += "전화번호: " + bell.getTelephoneNumber() + "\n";
                        content += "생성일자: " + bell.getCreateDate() + "\n";


                        double latitude = Double.parseDouble(String.valueOf(bell.getLatitude()));
                        double longitude = Double.parseDouble(String.valueOf(bell.getLongitude()));

                        LatLng markerLocation = new LatLng(latitude, longitude);
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(markerLocation);
                        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bellmarker));
                        markerOptions.title(bell.getInstallLocation());
                        markerOptions.snippet(bell.getTelephoneNumber());

                        map4.addMarker(markerOptions);
                    }
                    Log.i("확인", content);
                } else {
                    Toast.makeText(getActivity(), "오류입니다.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<EmergeBellResponse> call, Throwable t) {
                Log.e("NetworkError", "네트워크 요청 실패: " + t.getMessage());
                Toast.makeText(getActivity(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        seLightCall.enqueue(new Callback<SeLightReponse>() {
            @Override
            public void onResponse(Call<SeLightReponse> call, Response<SeLightReponse> response) {
                Log.i("selight", "확인");
                if (response.isSuccessful()) {
                    SeLightReponse seLightReponse = response.body();
                    seLightList = seLightReponse.getData();
                    String content = ""; //이걸 사용함으로써 문자열 저장하기 위한 공간 생성하고 초기에 아무내용 없는 상태로 만듦.
                    for (SeLightReponse.SeLightItem selight : seLightList) {
//                        content += "위치: " + selight.getlc() + "\n";
                        content += "설치개수: " + selight.getinstlCo() + "\n";
                        content += "주소: " + selight.getAddr() + "\n";
                        content += "위도: " + selight.getla() + "\n";
                        content += "경도: " + selight.getlo() + "\n";
                        content += "전화번호: " + selight.gettelno() + "\n";
                        content += "관리기관명: " + selight.getmngInstNm() + "\n";

                        // 여기에 있어야 마커 표시됨
                        // 첫번째 반복문 :call.enqueue 콜백 메서드 내부에 존재,
                        // Retrofit를 사용하요 웹 서비스로부터 데이터를 비동기적으로 가져오기에 필요
                        try {
                            double latitude = Double.parseDouble(selight.getla());
                            double longitude = Double.parseDouble(selight.getlo());


                            LatLng markerLocation = new LatLng(latitude, longitude);
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(markerLocation);
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.securitymarker));
                            markerOptions.title(selight.getAddr());
                            markerOptions.snippet(selight.LatLng);

                            map4.addMarker(markerOptions);
                            Log.i("확인", content);
                        } catch (NumberFormatException e) {
                            // 아무 작업도 수행하지 않고 해당 값 건너뛰기

                        }
                    }
                }
            }

            //<4>
            @Override
            public void onFailure(Call<SeLightReponse> call, Throwable t) {
                Log.e("NetworkError", "네트워크 요청 실패: " + t.getMessage());
                Toast.makeText(getActivity(), "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        return mapView;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    // 구글지도 고정된 형식<2번째>
    // 구글 지도가 사용 가능한 상태가 되었을 때 실행되는 콜백
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.i(TAG,"Place:" + PlaceName + Input_LatLng);
        map4 = googleMap; // 메서드 내에서 전역 변수 map4에 지도 객체를 할당하는 것
        map4.setMapType(GoogleMap.MAP_TYPE_NORMAL); // 일반 구글지도
        map4.getUiSettings().setZoomControlsEnabled(true);
    }

    //가로등 csv파일
    public List<String> readCSV() {
        InputStream inputStream = getResources().openRawResource(R.raw.sejong_streetlamp);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String streetlampInfo;
            streetlampInfo = reader.readLine();
            while ((streetlampInfo = reader.readLine()) != null) {
                lampList.add(streetlampInfo);
            }
            reader.close();
        } catch (Exception e) {
        }
        return lampList;
    }
}


//
//    public void showCctvNearby(LatLng latLng) {
//        PlacesClient placesClient = Places.createClient(getContext());
//
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(new LatLng(latLng.latitude - 0.01, latLng.longitude - 0.01))
//                .include(new LatLng(latLng.latitude + 0.01, latLng.longitude + 0.01))
//                .build();
//
//        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(Arrays.asList(Place.Field.ID, Place.Field.NAME))
//                .locationRestriction(bounds)
//                .build();
//
//
//        placesClient.findCurrentPlace(request).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                List<PlaceLikelihood> placeLikelihoods = task.getResult().getPlaceLikelihoods();
//                List<CctvResponse.CctvItem> cctvList = new ArrayList<>();
//
//                for (PlaceLikelihood placeLikelihood : placeLikelihoods) {
//                    String placeId = placeLikelihood.getPlace().getId();
//                    String placeName = placeLikelihood.getPlace().getName();
//                    LatLng cctvLatLng = placeLikelihood.getPlace().getLatLng();
//
//                    CctvResponse.CctvItem cctvItem = new CctvResponse.CctvItem(placeId, placeName, cctvLatLng);
//                    cctvList.add(cctvItem);
//                }
//
//                showCctvMap(cctvList);
//            } else {
//                Exception exception = task.getException();
//                // 요청이 실패한 경우에 대한 처리를 수행합니다.
//            }
//        });
//    }

