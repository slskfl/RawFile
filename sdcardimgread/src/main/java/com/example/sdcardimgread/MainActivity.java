package com.example.sdcardimgread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnPreV, btnNext;
    ImageView sdImg;
    File imgFile[]; // SD카드에 이미지가 잇는 폴더의 파일을 담을 배열
    String imgName; // 하나의 이미지 이름
    String sdcardPath;// SD카드 경로
    ArrayList<File> imgList; // 파일 중 이미지만 담을 동적 배열
    int position; //이미지 배열 인덱스

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPreV=findViewById(R.id.btnPrev);
        btnNext=findViewById(R.id.btnNext);
        sdImg=findViewById(R.id.SDimg);
        if(Build.VERSION.SDK_INT>=26){
            int pCheck= ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(pCheck== PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
            } else {
                sdcardProcess();
            }
        }
        btnPreV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_DENIED){
            showToast("SD카드 접근을 거부했습니다.");
        } else{
            sdcardProcess();
        }
    }

    // SD카드 처리 메서드
    void sdcardProcess(){

    }
    //토스트 메서드
    void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
