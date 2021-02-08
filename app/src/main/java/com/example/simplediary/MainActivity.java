package com.example.simplediary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {

    DatePicker dPicker;
    EditText edtDiary;
    Button btnSave;
    int cYear, cMonth, cDay;
    String filename;

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
        dPicker.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                filename=year+"_"+(monthOfYear+1)+"_"+dayOfMonth;
                edtDiary.setText(readDiary(filename));
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream outputStream=openFileOutput(filename, MODE_PRIVATE);
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
            FileInputStream inputStream = openFileInput(filename);
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
}