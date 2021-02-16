package com.example.sdcarddiary;

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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatePicker dPicker;
    EditText edtDiary;
    Button btnSave;
    int cYear, cMonth, cDay;
    String filename; //저장할 파일 이름
    String sdPath;
    File myFolder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dPicker=findViewById(R.id.dpicker);
        edtDiary=findViewById(R.id.edtDiary);
        btnSave=findViewById(R.id.btnSave);

        Calendar calendar=Calendar.getInstance();
        cYear=calendar.get(Calendar.YEAR);
        cMonth=calendar.get(Calendar.MONTH);
        cDay=calendar.get(Calendar.DAY_OF_MONTH);
        filename=cYear+"_"+(cMonth+1)+"_"+cDay;
        edtDiary.setText(readDiary(filename));

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

        dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                filename=year+"_"+(monthOfYear+1)+"_"+dayOfMonth;
                edtDiary.setText(readDiary(sdPath+filename));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream outputStream=new FileOutputStream(sdPath+filename);
                    String str=edtDiary.getText().toString();
                    outputStream.write(str.getBytes());
                    outputStream.close();
                    showToast(filename+"이 저장되었습니다.");
                    btnSave.setText("수정하기");
                } catch (IOException e) {
                    showToast("해당 파일을 저장할 수 없습니다.");
                }
            }
        });
    }
    void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
    //일기를 읽어오는 메소드
    String readDiary(String filename) {
        String diaryContent = null;
        try {
            FileInputStream inputStream = new FileInputStream(filename);
            byte txt[] = new byte[inputStream.available()];
            inputStream.read(txt);
            diaryContent=(new String(txt).trim());
            //trim>> 좌우 공백을 없앰.
            btnSave.setText("수정하기");
            inputStream.close();
        } catch (IOException e) {
            edtDiary.setHint("일기 없음");
            btnSave.setText("새로 저장");
        }
        return diaryContent;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_DENIED){
            showToast("SD카드 접근을 거부했습니다.");
        } else{
            sdcardProcess();
        }
    }

    void sdcardProcess(){
        sdPath= Environment.getExternalStorageDirectory().getAbsolutePath();
        sdPath+="/myDiary/";
        myFolder=new File(sdPath);
        if(!myFolder.isDirectory()){
            //폴더가 없을 경우 폴더 만들기.
            myFolder.mkdir();
        }
    }
}