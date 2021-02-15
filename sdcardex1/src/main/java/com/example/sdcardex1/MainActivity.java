package com.example.sdcardex1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

//SD카드 활용(보안)
public class MainActivity extends AppCompatActivity {

    Button btnSDRead, btnMKdir, btnDel;
    TextView tvContent;
    String sdPath;
    File myFolder;
    boolean sdCheck=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSDRead=findViewById(R.id.btnSDread);
        tvContent=findViewById(R.id.tvContent);
        btnMKdir=findViewById(R.id.btnMKdir);
        btnDel=findViewById(R.id.btnDel);

        //2차 보안>> 대화상자로 차단과 허용 메세지.
        if (Build.VERSION.SDK_INT >= 26) {
            //26은 8.0이상
            int pCheck= ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(pCheck==PackageManager.PERMISSION_DENIED) { // 보안체크가 안 되어있는 상태를 확인.
                // PERMISSION_DENIED >> 보안체크가 안되어있음.
                // PERMISSION_GRANTED >> 보안체크가 되어있음
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
            } else{
                sdcardProcess();
            }
        } else {
            sdcardProcess();
        }

        //SD카드에서 파일 읽기
        btnSDRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sdCheck==true){
                    try {
                        FileInputStream inputS= new FileInputStream (sdPath+"/memo.txt");
                        byte[] txt=new byte[inputS.available()];
                        inputS.read(txt);
                        tvContent.setText(new String (txt));
                        inputS.close();
                    } catch (IOException e) {
                        showToast("해당 파일을 읽을 수가 없습니다.");
                    }
                } else{
                    showToast("거부되었습니다.");
                }
            }
        });
        //SD카드에서 폴더 생성 >> 안 생김 >> 처음 실행할 경우 sdPath를 알 수 없음.
        btnMKdir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFolder=new File(sdPath+"/nina");
                myFolder.mkdir();
                showToast("폴더가 생성되었습니다.");
            }
        });
        //SD카드에서 폴더 삭제
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFolder.delete();
                showToast("삭제되었습니다.");
            }
        });
    }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                showToast("접근 거부되었습니다");
                sdCheck=false;
            }else {
                sdcardProcess();
            }
        }

        //토스트 메서드
        void showToast(String msg){
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        }

        //SD카드 구현
        void sdcardProcess(){
            sdPath= Environment.getExternalStorageDirectory().getAbsolutePath();
            sdCheck=true;
        }

    }