package com.example.sdcardimgcanvas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class MyImageView extends View {
    String imgPath=null;
    float satur=1;
    public MyImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(imgPath!=null){
            Paint paint=new Paint(); //붓 역할
            Bitmap bm= BitmapFactory.decodeFile(imgPath);
            int x=(this.getWidth()-bm.getWidth())/2;
            int y=(this.getHeight()-bm.getHeight())/2;
            ColorMatrix cm=new ColorMatrix();
            if(satur==0){
                cm.setSaturation(satur);//흑백으로 그려짐
            }
            paint.setColorFilter(new ColorMatrixColorFilter(cm));
            canvas.drawBitmap(bm, 0,0, paint);
            bm.recycle();//그리는 것을 해제시킴
        }
    }
}
