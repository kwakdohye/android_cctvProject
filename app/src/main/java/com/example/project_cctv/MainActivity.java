package com.example.project_cctv;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {


//    // BottomNavigationView 선언
//    BottomNavigationView bottomNavigationView;
//    HomeFragment homeFragment;
//    LawFragment lawFragment;
//    CertificateFragment certificateFragment;
//    GooFragment map1Fragment;


    private FragmentManager fragmentManager = getSupportFragmentManager();
    private CertificateFragment certificateFragment = new CertificateFragment();
    private MapFragment mapFragment = new MapFragment();
    private LawFragment lawFragment = new LawFragment();
    private HomeFragment homeFragment = new HomeFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("cctv");
        // 하단 네비게이션
        // FragmentTransaction transaction = fragmentManager.beginTransaction();   //beginTransaction(); 프래그먼트를 추가, 교체, 제거할 때 사용
//        transaction.replace(R.id.main_container,homeFragment).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        fragmentManager.beginTransaction().replace(R.id.main_container, homeFragment).commitAllowingStateLoss();
        // 처음 시작할 때 나오는 화면
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            int id = item.getItemId();

            if (id == R.id.btnHome) {
                transaction.replace(R.id.main_container, homeFragment).commitAllowingStateLoss();
                //.commitAllowingStateLoss()를 사용하면 액티비티의 상태가 손실되었을 때에도 프래그먼트 트랜잭션을 커밋할 수 있으며
                // 이를 통해 앱이 충돌하지 않고, 프래그먼트 트랜잭션이 손실된 상태에서도 안전하게 수행
            } else if (id == R.id.btnLaw) {
                transaction.replace(R.id.main_container, lawFragment).commitAllowingStateLoss();
            } else if (id == R.id.btnCert) {
                transaction.replace(R.id.main_container, certificateFragment).commitAllowingStateLoss();
            } else if (id == R.id.btnGoo) {
                transaction.replace(R.id.main_container, mapFragment).commitAllowingStateLoss();
            }
            return true;   // false로 쓰면 홈버튼이 계속 눌려있음
            // -> return true;를 반환하면 이벤트가 소비되었음을 나타내어 다른 이벤트 처리기가 호출되지 않는다.
            // 선택된 항목이 이미 처리되었음을 표시함
        }
    }
}