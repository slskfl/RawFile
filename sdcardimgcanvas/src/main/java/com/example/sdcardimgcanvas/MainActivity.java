package com.example.sdcardimgcanvas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnPreV, btnNext, btnCG;
    File imgFile[]; // SD카드에 이미지가 잇는 폴더의 파일을 담을 배열
    String imgName; // 하나의 이미지 이름
    String sdcardPath;// SD카드 경로
    ArrayList<File> imgList; // 파일 중 이미지만 담을 동적 배열
    int position=0; //이미지 배열 인덱스
    TextView tvPosition;
    MyImageView sdImg;
    boolean colorOnOff=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPreV=findViewById(R.id.btnPrev);
        btnNext=findViewById(R.id.btnNext);
        imgList=new ArrayList<File>();
        tvPosition=findViewById(R.id.tvPosition);
        sdImg=findViewById(R.id.SDimg);
        btnCG=findViewById(R.id.btnCG);

        if(Build.VERSION.SDK_INT>=26){
            int pCheck= ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if(pCheck== PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
            } else {
                sdcardProcess();
            }
        } else {
            sdcardProcess();
        }
        //이전그림 버튼
        btnPreV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position<=0){
                    showToast("첫번째 이미지입니다.");
                    position=imgList.size()-1;
                } else {
                    position--;
                }
                imgName=imgList.get(position).toString();
                //그래픽 이용
                Bitmap bm= BitmapFactory.decodeFile(imgName);
                sdImg.imgPath=imgName;
                sdImg.invalidate();//onDraw 메서드 호출
                tvPosition.setText((position+1)+"/"+imgList.size());
            }
        });
        //다음그림 버튼
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position>=imgList.size()-1){
                    showToast("마지막 이미지입니다.");
                    position=0;
                } else {
                    position++;
                }
                imgName = imgList.get(position).toString();
                //그래픽 이용
                Bitmap bm = BitmapFactory.decodeFile(imgName);
                sdImg.imgPath=imgName;
                sdImg.invalidate();//onDraw 메서드 호출
                tvPosition.setText((position+1)+"/"+imgList.size());
            }
        });
        //흑백 버튼
        btnCG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(colorOnOff==false){
                    colorOnOff=true;
                    btnCG.setText("흑백");
                    btnCG.setBackgroundColor(Color.rgb(153,153,153));
                    sdImg.satur=1;
                } else{
                    colorOnOff=false;
                    btnCG.setText("칼라");
                    btnCG.setBackgroundColor(Color.rgb(126,53,193));
                    sdImg.satur=0;
                }
                sdImg.invalidate();
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
        //SD카드 파일을 읽어 와 이미지를 동적배열에 넣기
        sdcardPath= Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.e("test", sdcardPath);
        imgFile=new File(sdcardPath+"/Pictures").listFiles();
//        Log.e("test", "파일="+imgFile[0]);
        String fileName, extName; // 파일이름과 확장자를 담는 변수
        for(File file:imgFile){
            fileName=file.getName();
            extName=fileName.substring(fileName.length()-3); //확장자만 자르기
            if(extName.equals("jpg") || extName.equals("png") || extName.equals("gif")){
                imgList.add(file);
            }
        }
        imgName=imgList.get(position).toString();
        sdImg.imgPath=imgName;
        sdImg.invalidate();//onDraw 메서드 호출
        tvPosition.setText("1/"+imgList.size());
    }
    //토스트 메서드
    void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
