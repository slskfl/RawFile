package com.example.rawtest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    Button btnTest1, btnTest2, btnResult;
    RadioGroup rdoG;
    RadioButton rdoQ[] = new RadioButton[4];
    int rdoID[]={R.id.rdoQ1, R.id.rdoQ2, R.id.rdoQ3, R.id.rdoQ4};
    ImageView ivQShow;
    int imgID[]={R.drawable.tenq1, R.drawable.tenq2, R.drawable.tenq3, R.drawable.tenq4,
            R.drawable.loveq1, R.drawable.loveq2, R.drawable.loveq3, R.drawable.loveq4};
    TextView tvResult, tvQuestion;
    String answer;
    String test[]; //raw 폴더에 있는 텍스트를 배열화
    int position=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnTest1=findViewById(R.id.btnTest1);
        btnTest2=findViewById(R.id.btnTest2);
        btnResult=findViewById(R.id.btnResult);
        ivQShow=findViewById(R.id.ivQShow);
        tvResult=findViewById(R.id.tvResult);
        tvQuestion=findViewById(R.id.tvQuestion);
        rdoG=findViewById(R.id.rdoG);
        for(int i=0; i<rdoID.length; i++){
            rdoQ[i]=findViewById(rdoID[i]);
        }
        rawRead((R.raw.tenlater));
        btnTest1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTest1.setBackgroundColor(Color.rgb(204, 114,61));
                btnTest2.setBackgroundColor(Color.rgb(153, 138,0));
                rawRead(R.raw.tenlater);
                position=0;
            }
        });
        btnTest2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTest1.setBackgroundColor(Color.rgb(153, 138,0));
                btnTest2.setBackgroundColor(Color.rgb(204, 114,61));
                rawRead(R.raw.lovelife);
                position=4;
            }
        });

        for(int i=0; i<rdoQ.length; i++){
            final int index;
            index=i;
            rdoQ[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivQShow.setImageResource(imgID[index+position]);
                    answer=test[index+5];
                }
            });
        }

        btnResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvResult.setText(answer);
            }
        });
    }
    //raw폴더에 있는 파일 읽어오는 메소드
    void rawRead(int rawtext){
        //라디오버튼과 이미지 초기화
        rdoG.clearCheck();
        ivQShow.setImageResource(0);
        InputStream inputS=getResources().openRawResource(rawtext);
        try {
            byte txt[]=new byte[inputS.available()];
            inputS.read(txt);
            String str=new String(txt);
            // 파일 문자를 읽어 올 때 (#) 구분하여 배열에 넣기
            test=str.split("#");
            tvQuestion.setText(test[0]);
            //0번째에는 질문, 1번째부터는 질문에 대한 답
            for(int i=1; i<rdoQ.length+1; i++){
                rdoQ[i-1].setText(test[i]);
            }
            inputS.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}