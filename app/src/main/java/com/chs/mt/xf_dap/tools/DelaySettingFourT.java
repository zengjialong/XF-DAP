package com.chs.mt.xf_dap.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.MacCfg;

@SuppressLint("ClickableViewAccessibility")
public class DelaySettingFourT extends View {

    //private boolean DEBUG = true false ;
    private boolean DEBUG = false;
    private OnDelaySettingChangeListener mOnDelaySettingChangeListener;
    //���ڴ�С
    private float windHeight = 0;
    private float windWidth = 0;
    private float CwindHeight = 0;
    private float CwindWidth = 0;

    private float MidWindWidth = 0;
    private float MidWindHeight = 0;
    //thumb
    private Drawable mThumbDrawable = null;
    private Drawable LPDrawable1 = null;
    private Drawable LPDrawable2 = null;
    private Drawable LPDrawable3 = null;
    private Drawable LPDrawable4 = null;

    private float LPDrawable1W = 0;
    private float LPDrawable1H = 0;
    private float LPDrawable2W = 0;
    private float LPDrawable2H = 0;
    private float LPDrawable3W = 0;
    private float LPDrawable3H = 0;
    private float LPDrawable4W = 0;
    private float LPDrawable4H = 0;
    private float mThumbDownTX = 0;
    private float mThumbDownTY = 0;
    private int mThumbHeight = 0;
    private int mThumbWidth = 0;
    private int[] mThumbNormal = null;
    private int[] mThumbPressed = null;
    private float mThumbTX = 220;
    private float mThumbTY = 220;
    private boolean ThumbTouch = false;
    private float mThumbLeftOld = 0;
    private float mThumbTopOld = 0;

    private float mThumbDrawX = 200;
    private float mThumbDrawY = 200;

    //private float TouchDegree = 0;

    private int arc_color;
    private int startarc = 1;
    private float rd1 = 0;
    private float rd2 = 0;
    private float rd3 = 0;
    private float rd4 = 0;

    private double xtap = 0;
    private double ytap = 0;
    private double rtap = 0;
    private int[] progress = {0, 0, 0, 0};
    private double[] rr = {0, 0, 0, 0};
    //RectF
    private RectF RectF_Point1 = new RectF();
    private RectF RectF_Point2 = new RectF();
    private RectF RectF_Point3 = new RectF();
    private RectF RectF_Point4 = new RectF();
    //Paint
    private Paint Paint_Arc;
    //private Paint Paint_Point2;

    private float arc_width = 0;
    private int arc_angle = 0;
    private float arc_interval = 0;
    private SweepGradient s;

    private double xMaxtap = 0;
    private double yMaxtap = 0;
    private boolean booSetDraw = false;


    private Context mContext = null;

    public DelaySettingFourT(Context context) {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public DelaySettingFourT(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public DelaySettingFourT(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.SField, defStyle, 0);

        initAttributes(a);
        a.recycle();

        Paint_Arc = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint_Arc.setShader(s);
        Paint_Arc.setColor(arc_color);
        Paint_Arc.setStyle(Paint.Style.STROKE);
        Paint_Arc.setStrokeWidth(arc_width);
        Paint_Arc.setAntiAlias(true);//���û���Ϊ�޾��
        Paint_Arc.setStrokeCap(Paint.Cap.ROUND);

        LPDrawable1 = mContext.getResources().getDrawable(R.drawable.chs_lp_left_top);
        LPDrawable2 = mContext.getResources().getDrawable(R.drawable.chs_lp_right_top);
        LPDrawable3 = mContext.getResources().getDrawable(R.drawable.chs_lp_left_bottom);
        LPDrawable4 = mContext.getResources().getDrawable(R.drawable.chs_lp_right_bottom);

        mThumbDrawX = mThumbDrawX - mThumbWidth / 3;
        mThumbDrawY = mThumbDrawY - mThumbHeight / 3;

        invalidate();
    }

    private void initAttributes(TypedArray a) {

        arc_color = a.getColor(R.styleable.SField_soundfield_arc_color, Color.YELLOW);

        arc_width = (int) a.getDimension(R.styleable.SField_soundfield_arc_line_width, 1);

        arc_angle = a.getInt(R.styleable.SField_soundfield_arc_angle, 30);

        arc_interval = (int) a.getDimension(R.styleable.SField_soundfield_arc_line_interval, 10);

        /*thumb*/
        mThumbDrawable = a.getDrawable(R.styleable.SField_android_thumb);
        mThumbWidth = this.mThumbDrawable.getIntrinsicWidth() / 2;
        mThumbHeight = this.mThumbDrawable.getIntrinsicHeight() / 2;

        mThumbNormal = new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed,
                -android.R.attr.state_selected, -android.R.attr.state_checked};
        mThumbPressed = new int[]{android.R.attr.state_focused, android.R.attr.state_pressed,
                android.R.attr.state_selected, android.R.attr.state_checked};

    }

    @SuppressLint("NewApi")
    @Override
    protected void onDraw(Canvas canvas) {
        //Flash ARC
        drawARCPoint1(canvas);
        drawARCPoint2(canvas);
        drawARCPoint3(canvas);
        drawARCPoint4(canvas);
        //Thumb
        drawThumb(canvas);
        //drawLP(canvas);
        //countDelayTime();
        if (booSetDraw) {
            booSetDraw = false;
        } else {
            countDelayTime();
        }
    }

    private void drawARCPoint1(Canvas canvas) {

        float xc = mThumbTX - CwindWidth;
        float yc = mThumbTY - CwindHeight;

        rd1 = (float) Math.sqrt(xc * xc + yc * yc) - mThumbWidth / 2;
        double x = xc / xtap;
        double y = yc / ytap;
        rr[0] = Math.sqrt(x * x + y * y);
    }

    private void drawARCPoint2(Canvas canvas) {
        float xc = windWidth - mThumbTX - CwindWidth;
        float yc = mThumbTY - CwindHeight;

        rd2 = (float) Math.sqrt(xc * xc
                + yc * yc) - mThumbWidth / 2;
//    	int n = (int) (rd2/arc_interval);
//    	int startAngle=(int) GetDegree(windWidth,0,mThumbTX,mThumbTY)-arc_angle/2;
        //System.out.println("ARC Point2 startAngle:"+startAngle);
//    	for(int i=startarc;i <= n;i++){
//    		if(i>12){
//    			break;
//    		}
//    		rd2 = arc_interval*i;
//    		RectF_Point2.set(windWidth-rd2, 0-rd2, windWidth+rd2, 0+rd2);
//    		canvas.drawArc(RectF_Point2, startAngle, arc_angle, false, Paint_Arc);
//    	}

        double x = xc / xtap;
        double y = yc / ytap;
        rr[1] = Math.sqrt(x * x + y * y);
    }

    private void drawARCPoint3(Canvas canvas) {

        float xc = mThumbTX - CwindWidth;
        float yc = windHeight - mThumbTY - CwindHeight;

        rd3 = (float) Math.sqrt((xc) * (xc)
                + yc * yc) - mThumbWidth / 2;
//    	int n = (int) (rd3/arc_interval);
//    	int startAngle=(int) GetDegree(0,windHeight,mThumbTX,mThumbTY)-arc_angle/2;
        //System.out.println("ARC Point2 startAngle:"+startAngle);
//    	for(int i=startarc;i <= n;i++){
//    		if(i>12){
//    			break;
//    		}
//    		rd3 = arc_interval*i;
//    		RectF_Point3.set(0-rd3, windHeight-rd3, 0+rd3, windHeight+rd3);
//    		canvas.drawArc(RectF_Point3, startAngle, arc_angle, false, Paint_Arc);
//    	}

        double x = xc / xtap;
        double y = yc / ytap;
        rr[2] = Math.sqrt(x * x + y * y);
    }

    private void drawARCPoint4(Canvas canvas) {
        float xc = windWidth - mThumbTX - CwindWidth;
        float yc = windHeight - mThumbTY - CwindHeight;

        rd4 = (float) Math.sqrt(xc * xc
                + yc * yc) - mThumbWidth / 2;
//    	int n = (int) (rd4/arc_interval);
//    	int startAngle=(int) GetDegree(windWidth,windHeight,mThumbTX,mThumbTY)-arc_angle/2;
        //System.out.println("ARC Point2 startAngle:"+startAngle);
//    	for(int i=startarc;i <= n;i++){
//    		if(i>12){
//    			break;
//    		}
//    		rd4 = arc_interval*i;
//    		RectF_Point4.set(windWidth-rd4, windHeight-rd4, windWidth+rd4, windHeight+rd4);
//    		canvas.drawArc(RectF_Point4, startAngle, arc_angle, false, Paint_Arc);
//    	}

        double x = xc / xtap;
        double y = yc / ytap;
        rr[3] = Math.sqrt(x * x + y * y);
    }

    private void countDelayTime() {
        int max1 = 0, max2 = 0, max = 0;
        if (rr[0] >= rr[1]) {
            max1 = 0;
        } else {
            max1 = 1;
        }

        if (rr[2] >= rr[3]) {
            max2 = 2;
        } else {
            max2 = 3;
        }

        if (rr[max1] >= rr[max2]) {
            max = max1;
        } else {
            max = max2;
        }
        for (int i = 0; i < 4; i++) {
            progress[i] = (int) Math.abs(((rr[i] - rr[max]) / 0.34) * 48);
        }
        //printf
//    	System.out.println("BUG ##############################################################");
//    	String rrString="BUG___RR:";
//    	for(int i=0;i<4;i++){
//    		rrString+=(","+rr[i]);
//    	}
//    	System.out.println("BUG___rrString:"+rrString);
//    	System.out.println("BUG___min:"+min);
//    	String progressString="BUG___progress:";
//    	for(int i=0;i<4;i++){
//    		progressString+=(","+chs_progress[i]);
//    	}
//    	System.out.println("BUG___progressString:"+progressString);
    }


    private void drawThumb(Canvas canvas) {
        //画横线
        canvas.drawLine(0, mThumbDrawY + mThumbWidth / 2, windWidth, mThumbDrawY + mThumbWidth / 2, Paint_Arc);
        //画竖线
        canvas.drawLine(mThumbDrawX + mThumbWidth / 2, 0, mThumbDrawX + mThumbWidth / 2, windHeight, Paint_Arc);

        this.mThumbDrawable.setBounds(
                (int) mThumbDrawX,
                (int) mThumbDrawY,
                (int) (mThumbDrawX + mThumbWidth),
                (int) (mThumbDrawY + mThumbHeight));
        this.mThumbDrawable.draw(canvas);


    }

    public void SetDrawThumb(int delay1, int delay2) {

//		int local=Math.abs(delay1-delay2);
//		float lx=(float)(local*xMaxtap);
//		booSetDraw=true;
////		System.out.println("BUG delay local:"+local);
////		System.out.println("BUG delay lx:"+lx);
////		System.out.println("BUG delay windWidth:"+windWidth);
////		System.out.println("BUG delay mThumbWidth:"+mThumbWidth);
////		if(lx > (windWidth-mThumbWidth)){
////			lx=(windWidth-mThumbWidth);
////		}
//		lx=(float) ((float) (lx/48*0.34)*xtap)+mThumbWidth*3/2;
//		if(lx > (windWidth-mThumbWidth)){
//			lx=(windWidth-mThumbWidth);
//		}
//
////		System.out.println("BUG delay mThumbDrawX:"+mThumbDrawX);
////		System.out.println("BUG delay MidWindHeight:"+MidWindHeight);
////		System.out.println("BUG delay mThumbHeight:"+mThumbHeight);
//
//		mThumbDrawX=lx;
//		mThumbPoint1X=lx;
//
//		mThumbDrawable.setState(mThumbNormal);
//
//		invalidate();
    }

    private void drawLP(Canvas canvas) {
        this.LPDrawable1.setBounds(0, 0, LPDrawable1.getIntrinsicWidth(), LPDrawable1.getIntrinsicHeight());
        this.LPDrawable1.draw(canvas);

        this.LPDrawable2.setBounds(
                (int) (windWidth - LPDrawable2.getIntrinsicWidth()),
                0,
                (int) windWidth,
                LPDrawable2.getIntrinsicHeight());
        this.LPDrawable2.draw(canvas);

        this.LPDrawable3.setBounds(0, (int) (windHeight - LPDrawable3.getIntrinsicHeight()),
                LPDrawable3.getIntrinsicWidth(),
                (int) windHeight);
        this.LPDrawable3.draw(canvas);

        this.LPDrawable4.setBounds(
                (int) (windWidth - LPDrawable2.getIntrinsicWidth()),
                (int) (windHeight - LPDrawable3.getIntrinsicHeight()),
                (int) windWidth,
                (int) windHeight);
        this.LPDrawable4.draw(canvas);
    }

    //����Ƕȣ��뾶��Բ������
    private float[] calculatePointerPosition(float angle, float Radius) {
        float x, y;
        x = (float) Math.sin(2 * Math.PI / 360 * angle) * Radius;
        y = (float) Math.cos(2 * Math.PI / 360 * angle) * Radius;

        //System.out.println("angle x:"+x+",y:"+y);
        return new float[]{x, y};
    }

    private float GetDegree(float centX, float centY, float x, float y) {
        float Degree = 0;
        double radian = Math.atan2(y - centY, x - centX);
        if (radian < 0) {
            radian = radian + 2 * Math.PI;
        }
        Degree = Math.round(Math.toDegrees(radian));
        return Degree;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        System.out.println("BUG 获取到的高度为" + windHeight + "宽度为" + windWidth);

        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int min = Math.min(width, height);
        //setMeasuredDimension(min, min);
        CwindHeight = height / 3;
        CwindWidth = width / 3;
        if (windHeight == 0 && windWidth == 0) {
            windHeight = height;
            windWidth = width;


            MidWindWidth = windWidth / 2;
            MidWindHeight = windHeight / 2;

            xtap = CwindWidth / MacCfg.CCar[0];
            ytap = CwindHeight / MacCfg.CCar[1];

            mThumbDrawX = MidWindWidth - mThumbWidth / 2;
            mThumbDrawY = MidWindHeight - mThumbHeight / 2;
        }
        setProgress();
    }

    public void SeekTo() {
        invalidate();
//        if (mOnDelaySettingChangeListener != null) {
//            mOnDelaySettingChangeListener.onProgressChanged(this, progress, true);
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (x < CwindWidth) {
            x = CwindWidth;
        } else if (x > windWidth - CwindWidth) {
            x = windWidth - CwindWidth;
        }
        if (y < CwindHeight) {
            y = CwindHeight;
        } else if (y > windHeight - CwindHeight) {
            y = windHeight - CwindHeight;
        }
        mThumbTX = x;
        mThumbTY = y;

        //System.out.println("onTouchEvent x:"+x+",y:"+y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (((mThumbTX + mThumbWidth * 2) > mThumbDrawX) && ((mThumbTX - mThumbWidth) < mThumbDrawX) &&
                        ((mThumbTY + mThumbHeight * 2) > mThumbDrawY) && ((mThumbTY - mThumbHeight) < mThumbDrawY)) {
                    ThumbTouch = true;
                    mThumbDrawable.setState(mThumbPressed);

                    mThumbDownTX = mThumbTX;
                    mThumbDownTY = mThumbTY;

                } else {
                    ThumbTouch = false;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (ThumbTouch) {
                    mThumbTX = x;
                    mThumbTY = y;
                    mThumbDrawX = mThumbTX - mThumbWidth / 2;
                    mThumbDrawY = mThumbTY - mThumbHeight / 2;
                    if (mThumbDrawX < 0) {
                        mThumbDrawX = 0;
                    } else if (mThumbDrawX > (windWidth - CwindWidth)) {
                        mThumbDrawX = (windWidth - CwindWidth);
                    }
                    if (mThumbDrawY < 0) {
                        mThumbDrawY = 0;
                    } else if (mThumbDrawY > (windHeight - CwindHeight-mThumbHeight)) {//
                        mThumbDrawY = (windHeight - CwindHeight-mThumbHeight);
                    }


                    mThumbDrawable.setState(mThumbNormal);
                    if (mOnDelaySettingChangeListener != null) {
                        mOnDelaySettingChangeListener.onProgressChanged(this, progress, false);
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (ThumbTouch) {
                    mThumbDrawable.setState(mThumbNormal);
                    if (mOnDelaySettingChangeListener != null) {
                        mOnDelaySettingChangeListener.onProgressChanged(this, progress, false);
                    }
                    invalidate();
                }
                break;
            default:
                if (mOnDelaySettingChangeListener != null) {
                    mOnDelaySettingChangeListener.onProgressChanged(this, progress, false);
                }
                mThumbDrawable.setState(mThumbNormal);
               // invalidate();
                break;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE && getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
        return ThumbTouch;
    }


    public void setDelaySettingChangeListener(OnDelaySettingChangeListener l) {
        mOnDelaySettingChangeListener = l;
    }

    public interface OnDelaySettingChangeListener {

        public abstract void onProgressChanged(DelaySettingFourT delaySettingFour,
                                               int[] progress, boolean fromUser);

    }

    public void setProgressThumb(int thumbId) {
        mThumbDrawable = mContext.getResources().getDrawable(thumbId);
    }

    public void setCenter() {
        mThumbDrawX = MidWindWidth - mThumbWidth / 2;
        mThumbDrawY = MidWindHeight - mThumbHeight / 2;
        invalidate();

    }

    public void setProgress() {
//        chs_progress = Math.min(chs_progress, mMax);
//        chs_progress = Math.max(1, chs_progress);
        //
        int delayValue1 = DataStruct.RcvDeviceData.OUT_CH[0].delay;
        int delayValue2 = DataStruct.RcvDeviceData.OUT_CH[1].delay;
        int delayValue3 = DataStruct.RcvDeviceData.OUT_CH[2].delay;
        int delayValue4 = DataStruct.RcvDeviceData.OUT_CH[3].delay;
        //
        //

        float x1 = CwindWidth
                + (CwindWidth - (CwindWidth / DataStruct.CurMacMode.Delay.MAX)
                * delayValue1);
        float y1 = CwindHeight
                + (CwindHeight - (CwindHeight / DataStruct.CurMacMode.Delay.MAX)
                * delayValue1);

        float x2 = CwindWidth
                * 2
                - (CwindWidth - (CwindWidth / DataStruct.CurMacMode.Delay.MAX)
                * delayValue2);
        float y2 = CwindHeight
                + (CwindHeight - (CwindHeight / DataStruct.CurMacMode.Delay.MAX)
                * delayValue2);

        float x3 = CwindWidth
                + (CwindWidth - (CwindWidth / DataStruct.CurMacMode.Delay.MAX)
                * delayValue3);
        float y3 = CwindHeight
                * 2
                - (CwindHeight - (CwindHeight / DataStruct.CurMacMode.Delay.MAX)
                * delayValue3);

        float x4 = CwindWidth
                * 2
                - (CwindWidth - (CwindWidth / DataStruct.CurMacMode.Delay.MAX)
                * delayValue4);
        float y4 = CwindHeight
                * 2
                - (CwindHeight - (CwindHeight / DataStruct.CurMacMode.Delay.MAX)
                * delayValue4);
        float centerx1 = (float) ((x1 + x4) / 2);
        float centery1 = (float) ((y1 + y4) / 2);

        float centerx2 = (float) ((x2 + x3) / 2);
        float centery2 = (float) ((y2 + y3) / 2);

        //

        mThumbTX = (centerx1 + centerx2) / 2;
        mThumbTY = (centery1 + centery2) / 2;

        if (delayValue1 < delayValue2) {
            mThumbTX += 30;
        } else if (delayValue1 > delayValue2) {
            mThumbTX -= 30;
        }
        if (delayValue3 < delayValue4) {
            mThumbTX += 30;
        } else if (delayValue3 > delayValue4) {
            mThumbTX -= 30;
        }

        if (delayValue1 < delayValue3) {
            mThumbTY +=60;
        } else if (delayValue1 > delayValue3) {
            mThumbTY -= 60;
        }


        if (delayValue2 < delayValue4) {
            mThumbTY += 60;
        } else if (delayValue2 > delayValue4) {
            mThumbTY -= 60;
        }
        if((DataStruct.RcvDeviceData.OUT_CH[0].delay==0)
                &&(DataStruct.RcvDeviceData.OUT_CH[1].delay==0)
                &&(DataStruct.RcvDeviceData.OUT_CH[2].delay==0)
                &&(DataStruct.RcvDeviceData.OUT_CH[3].delay==0)
        ){
            mThumbDrawX = MidWindWidth - mThumbWidth / 2;
            mThumbDrawY = MidWindHeight - mThumbHeight / 2;
        }else{
            mThumbDrawX = mThumbTX - mThumbWidth / 2;
            mThumbDrawY = mThumbTY - mThumbHeight / 2;
        }

        invalidate();
    }


}  