package com.example.project_cctv;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_DELAY = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 일정 시간 지연 후 메인 액티비티로 이동
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }
}
//SplashActivity생성될 때
// onCreate 메서드에서 지연된 후에 MainActivity로 이동하는 인텐트를 설정하고 있습니다.
// 따라서 "Intent를 통해 일정 시간 후 MainActivity로 이동하도록 함"으로 충분히 설명할 수 있습니
