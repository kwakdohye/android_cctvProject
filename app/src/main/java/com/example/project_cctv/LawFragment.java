package com.example.project_cctv;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;


public class LawFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            return inflater.inflate(R.layout.fragment_law, container, false);

        }
}

// 앱이 보다 유연하게 Fragment를 관리하고 , 특정상황이나 사용자 동작에 따라 fragment를 추가하거나 제거할 수 있다.
// LayoutInflater는 XML 레이아웃 파일을 실제 뷰 객체로 인스턴스화하는 데 사용되는 클래스