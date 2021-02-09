package com.example.rawex;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    //읽기 전용 파일처리

    Button btnRead;
    TextView tvRaw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnRead=findViewById(R.id.btnRead);
        tvRaw=findViewById(R.id.tvRaw);

        btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputStream inputS=getResources().openRawResource(R.raw.today);
                try {
                    byte txt[]=new byte[inputS.available()];
                    inputS.read(txt);
                    tvRaw.setText(new String(txt));
                    inputS.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "해당파일을 읽을 수가 없습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}