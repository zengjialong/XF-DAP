package com.chs.mt.xf_dap.tools.wheel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.chs.mt.xf_dap.datastruct.DataStruct;

public class WheelItem {
    // 起点Y坐标、宽度、高度
    // private float startY;
    private float startX;
    private int width;
    private int height;
    //四点坐标
    private RectF rect = new RectF();
    //字体大小、颜色
    private int fontColor;
    private int fontTextColor;
    private int fontSize;
    private String text;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Context mContext;

    //    public WheelItem(float startY, int width, int height, int fontColor, int fontSize, String text) {
//        this.startY = startY;
//        this.width = width;
//        this.height = height;
//        this.fontColor = fontColor;
//        this.fontSize = fontSize;
//        this.text = text;
//        adjust(0);
//    }
    public WheelItem(float startX, int width, int height, int fontColor, int fontSize, String text, int fontTextColor) {

        this.startX = startX;
        this.width = width;
        this.height = height;
        this.fontColor = fontColor;
        this.fontTextColor=fontTextColor;
        this.fontSize = fontSize;
        this.text = text;
        this.fontColor = fontColor;

        adjust(0);
    }
    /**
     * 根据Y坐标的变化值，调整四点坐标值
     */
//    public void adjust(float dy){
//        startY += dy;
//        rect.left = 0;
//        rect.top = startY;
//        rect.right = width;
//        rect.bottom = startY + height;
//    }
    public void adjust(float dx){
        startX += dx;
        rect.left = startX;
        rect.top = 0;
        rect.right = startX + width;
        rect.bottom = height;
    }
    //    public float getStartY() {
//        return startY;
//    }
    public float getStartX() {
        return startX;
    }
    /**
     * 直接设置Y坐标属性，调整四点坐标属性
     */
//    public void setStartY(float startY) {
//        this.startY = startY;
//        rect.left = 0;
//        rect.top = startY;
//        rect.right = width;
//        rect.bottom = startY + height;
//    }
    public void setStartX(float startX) {
        this.startX = startX;
        rect.left = this.startX;
        rect.top = 0;
        rect.right = this.startX + width;
        rect.bottom = height;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void onDraw( Canvas mCanvas){
        //设置钢笔属性
        //Log.d("BUG", "WheelItem fontSize:" + fontSize);
        mPaint.setTextSize(fontSize);
        mPaint.setColor(fontColor);

        mTextPaint.setColor(fontTextColor);
        mTextPaint.setTextSize(fontSize);
        //得到字体的宽度
        int textWidth = (int)mPaint.measureText(text);
        //drawText的绘制起点是左下角,y轴起点为baseLine
        Paint.FontMetrics metrics =  mPaint.getFontMetrics();
        int baseLine = (int)(rect.centerY() + (metrics.bottom - metrics.top) / 2 - metrics.bottom);
        //居中绘制


        if(DataStruct.RcvDeviceData.SYS.mode==1) {


            if (text.equals("CH2")) {
                mCanvas.drawText(text, rect.centerX() - textWidth / 2, baseLine, mTextPaint);
            } else {
                mCanvas.drawText(text, rect.centerX() - textWidth / 2, baseLine, mPaint);
            }
        }else{
            mCanvas.drawText(text, rect.centerX() - textWidth / 2, baseLine, mPaint);
        }


    }
}
