package com.chs.mt.xf_dap.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;

import com.chs.mt.xf_dap.R;


public class KnobViewLine_OutThumb extends android.support.v7.widget.AppCompatImageView {
    private static final float MAX_SWEEP_ANGLE = 270;
    private static final float START_ANGLE = 135;

    private int wheelHeight, wheelWidth,min;
    private Bitmap imageOriginal;
    private Matrix matrix;
    private boolean bool_matrix=false;
    private RotateChangeListener listener;
    private float totalDegree;
    private RectF arcRect, rect;
    private static final float PROGRESS_RATIO = 0.1f;
    private static final float PROG_RATIO = 0.80f;
    private int progressWidth = 10;
    private int progressBgWidth=10;

    private int shadowWidth = 40;
    private Paint progressBgPaint, progressPaint, progressPaint2, paint;
    private Drawable left, right, thumb;
    int drawableWidth = 0;
    int drawableHeight = 0;
    private float radius;
    private float pic_radius;
    private float centerX, centerY;
    private float drawableRadius=0;
    private float ratio = 0;

    private float InsideRadius = 0;
    private float OutsideRadius = 0;
    private Bitmap bitmap;
    private float inside_line_width = 0;
    private float inside_point_radius = 0;
    private float outside_point_radius = 0;
    private int inside_point_color = 0;
    private int inside_cir_color = 0;
    private int outside_point_color_press = 0;
    private int outside_point_color_normal = 0;


    private Drawable mBgDrawable=null;
    private int outside_line_color_press = 0;
    private int outside_line_color_normal = 0;
    private Paint PaintInsideCir;
    private Paint PaintInsidePoint;
    private Paint PaintOutsidePointPress;
    private Paint PaintOutsidePointNormal;

    private float[] Out_pointerPosition;
    private float[] In_pointerPosition;
    private int mSeekBaroutsideRadius = 0;//�⻷�뾶
    private int mSeekBarRadius = 0;//seekbar�뾶
    private int mlinelong = 0;//seekbar chs_progress �ϵ��ߵĳ���
    private int mthumbRadius = 0;//thumb�뾶
    /**绘制外面的半圈*/
    private int start_arc = 135;
    private int end_wheel;
    private float mSeekBarMaxDegree = 0;
    private float mSeekBarDegree = 0;
    private float mDegree = (float) 4.5;

    private Context mContext = null;
    private AttributeSet mAttrs = null;
    public KnobViewLine_OutThumb(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        init(context);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            this.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    private int thumbHalfWidth;
    private float thumbRadius;
    @SuppressLint("ClickableViewAccessibility")
    private void init(Context context) {
        this.setScaleType(ScaleType.MATRIX);
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            matrix.reset();
        }
        this.setOnTouchListener(new WheelTouchListener());

        initView();
        thumb = getResources().getDrawable(R.drawable.chs_color_thumb);
        thumbHalfWidth = thumb.getIntrinsicWidth() / 2;
        int thumbHalfHeight = thumb.getIntrinsicHeight() / 2;
        thumb.setBounds(-thumbHalfWidth, -thumbHalfHeight,thumbHalfWidth, thumbHalfHeight);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#e8e8e8"));

        progressBgPaint = new Paint();
        progressBgPaint.setStyle(Paint.Style.STROKE);
        progressBgPaint.setColor(getResources().getColor(R.color.color_grey));
        progressBgPaint.setAntiAlias(true);
        progressBgPaint.setStrokeCap(Paint.Cap.ROUND);


        int colorProgress = outside_line_color_normal;//getResources().getColor(R.color.color_main);
        int midColor = outside_line_color_press;//getResources().getColor(R.color.color_main_light);

        //int blurRadius = (int) inside_line_width;//PixelUtil.dp2px(5, getContext());
        //BlurMaskFilter maskFilter = new BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.SOLID);
        progressPaint = new Paint();
        progressPaint.setColor(midColor);
        //progressPaint.setMaskFilter(maskFilter);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeCap(Paint.Cap.SQUARE);

        //BlurMaskFilter maskFilter2 = new BlurMaskFilter(blurRadius/3, BlurMaskFilter.Blur.NORMAL);
        progressPaint2 = new Paint();
        progressPaint2.setColor(getResources().getColor(R.color.color_grey));
        //progressPaint2.setMaskFilter(maskFilter2);
        progressPaint2.setStyle(Paint.Style.STROKE);
        progressPaint2.setAntiAlias(true);
        progressPaint2.setStrokeCap(Paint.Cap.ROUND);

        PaintInsidePoint = new Paint();
        PaintInsidePoint.setColor(inside_point_color);
        PaintInsidePoint.setAntiAlias(true);

        PaintInsideCir = new Paint();
        PaintInsideCir.setColor(inside_cir_color);
        PaintInsideCir.setAntiAlias(true);

        PaintOutsidePointPress = new Paint();
        PaintOutsidePointPress.setColor(outside_point_color_press);
        PaintOutsidePointPress.setAntiAlias(true);

        PaintOutsidePointNormal = new Paint();
        PaintOutsidePointNormal.setColor(outside_point_color_normal);
        PaintOutsidePointNormal.setAntiAlias(true);



        bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.chs_master_vol_indicator);
        drawableHeight= bitmap.getHeight();
        drawableWidth= bitmap.getWidth();
        ratio = MAX_SWEEP_ANGLE/mMax;

        mBgDrawable = getResources().getDrawable(R.drawable.chs_master_vol_indicator_bg);
        invalidate();

        invalidate();
    }
    private void initView(){
        TypedArray localTypedArray = mContext.obtainStyledAttributes(mAttrs, R.styleable.KNOB);

        inside_point_radius = localTypedArray.getDimension(R.styleable.KNOB_inside_point_radius, 4);
        outside_point_radius = localTypedArray.getDimension(R.styleable.KNOB_outside_point_radius, 2);

        inside_cir_color = localTypedArray.getColor(R.styleable.KNOB_inside_cir_color, getResources().getColor(android.R.color.darker_gray));
        inside_point_color = localTypedArray.getColor(R.styleable.KNOB_inside_point_color, getResources().getColor(android.R.color.darker_gray));
        outside_point_color_press = localTypedArray.getColor(R.styleable.KNOB_outside_point_color_press, getResources().getColor(android.R.color.darker_gray));
        outside_point_color_normal = localTypedArray.getColor(R.styleable.KNOB_outside_point_color_normal,getResources().getColor(android.R.color.darker_gray));

        outside_line_color_press = localTypedArray.getColor(R.styleable.KNOB_outside_line_color_press,getResources().getColor(android.R.color.darker_gray));
        //outside_line1_color_press = localTypedArray.getColor(R.styleable.KNOB_outside_line1_color_press, getResources().getColor(android.R.color.darker_gray));
        outside_line_color_normal = localTypedArray.getColor(R.styleable.KNOB_outside_line_color_normal, getResources().getColor(android.R.color.darker_gray));
        inside_line_width = localTypedArray.getDimension(R.styleable.KNOB_outside_line_width, 2);


        mlinelong=(int) localTypedArray.getDimension(R.styleable.KNOB_line_long, 1);  //设置线的长度
        mthumbRadius=(int) localTypedArray.getDimension(R.styleable.KNOB_line_width, 3);//设置推子的宽度

        progressWidth = (int) inside_line_width;
        localTypedArray.recycle();
    }
    public void setRotateChangeListener(RotateChangeListener listener) {
        this.listener = listener;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        min = Math.min(width, height);
        setMeasuredDimension(min, min);
        centerX = min / 2;
        centerY = min / 2;
        drawableRadius = min / 2;
       progressBgWidth  = (int)(drawableRadius*0.02f);//��Seekbar�뾶
        progressWidth = (int)(drawableRadius*PROGRESS_RATIO);//��Seekbar�뾶
        radius = (float) (drawableRadius*0.75);//��Seekbar����λ�ð뾶
        thumbRadius = radius;//thumbRadius����λ�ð뾶
        pic_radius=min / 2*PROG_RATIO;//��ͼ���ڰ뾶

        InsideRadius = (float) (drawableRadius*0.5);
        OutsideRadius= (float) (drawableRadius*0.2);


        mSeekBaroutsideRadius = (int)centerX - mthumbRadius;
        mSeekBarRadius = mSeekBaroutsideRadius-mlinelong;

        if(!bool_matrix){
            bool_matrix=true;
            float scaleWidth = (pic_radius*2) / drawableWidth;
            float scaleHeight = (pic_radius*2) / drawableHeight;
            matrix.postScale(scaleWidth, scaleHeight);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (wheelHeight == 0 || wheelWidth == 0) {
            wheelHeight = min;
            wheelWidth = min;

            progressBgPaint.setStrokeWidth(progressBgWidth);
            progressPaint.setStrokeWidth(progressWidth);
            // progressPaint2.setStrokeWidth(progressWidth / 3);
            progressPaint2.setStrokeWidth(progressWidth);
            float translate = 0;//(min/2)*(1-PROG_RATIO);
            translate = (min/2)*(1-PROG_RATIO);

            //float translate = (min/2 - pic_radius)-progressWidth/2f;
            matrix.postTranslate(translate, translate);
            // this.setImageBitmap(imageOriginal);
            this.setImageMatrix(matrix);

            //matrix.postRotate(165, wheelWidth / 2, wheelHeight / 2);80 60 295
            matrix.postRotate(270, wheelWidth / 2, wheelHeight / 2);
            setImageMatrix(matrix);
            if (listener != null) {
                listener.onRoateChange(totalDegree, true);
            }
            arcRect = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
            //float radius2 = radius + shadowWidth;
            float radius2 = radius + shadowWidth;
            rect = new RectF(centerX - radius2, centerY - radius2, centerX + radius2, centerY + radius2);
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawArc(rect, 0, 360, false, paint);
        super.onDraw(canvas);
        //canvas.drawArc(arcRect, START_ANGLE, MAX_SWEEP_ANGLE, false, progressBgPaint);
        //drawFullCir(canvas);
        canvas.drawArc(arcRect, START_ANGLE, START_ANGLE+MAX_SWEEP_ANGLE, false, progressPaint2);
        drawProgress(canvas);
//        drawDrawBg(canvas);


        canvas.translate(centerX, centerY);
       // drawOutsidePointPbg(canvas);
//        drawInsidePoint(canvas);
      drawSeekbar_bg(canvas);

    }
    private void drawSeekbar_bg(Canvas canvas){
        float angle=315;//���ߵĽǶ�
        if( mMax== 46){
            angle=312;
        }
        if(mMax==66){
            start_arc =45;
            end_wheel=310;
        }else{
            start_arc =45;
            end_wheel=320;
        }
        mSeekBarMaxDegree = end_wheel-start_arc;

        mDegree= mSeekBarMaxDegree/mMax;
        //draw seekbar background
        for(int i=0;i<mMax;i++){
            Out_pointerPosition=calculatePointerPosition(angle, mSeekBaroutsideRadius);
            In_pointerPosition=calculatePointerPosition(angle, mSeekBarRadius);
            angle-=mDegree;

            canvas.drawLine(In_pointerPosition[0], In_pointerPosition[1],
                    Out_pointerPosition[0], Out_pointerPosition[1], progressBgPaint );
        }
    }

    private float sweepAngle = 0;
    private void drawProgress(Canvas canvas){
        canvas.drawArc(arcRect, START_ANGLE, sweepAngle, false, progressPaint);
        canvas.drawArc(arcRect, START_ANGLE, sweepAngle, false, progressPaint);
    }

    private void drawDrawBg(Canvas canvas){
        this.mBgDrawable.setBounds((int)(centerX-drawableRadius), (int)(centerY-drawableRadius),
                (int)(centerX+drawableRadius), (int)(centerY+drawableRadius));
        this.mBgDrawable.draw(canvas);

    }

    private void drawFullCir(Canvas canvas){
        canvas.drawCircle(centerX,centerY,thumbRadius,PaintInsideCir);
    }


    private void drawDrawables(Canvas canvas){
        drawThumb(canvas);
    }

    private void drawThumb(Canvas canvas){
        float angle = (START_ANGLE + sweepAngle);
        double radians = Math.toRadians(angle);
        double x = Math.cos(radians)*thumbRadius + centerX;
        double y = Math.sin(radians)*thumbRadius + centerY;
        canvas.save();
        canvas.translate((float)x, (float)y);
        thumb.draw(canvas);
        canvas.restore();
    }
    private void drawOutsidePointPbg(Canvas canvas) {
        for(int i=0;i<mMax;i++){
            float[] pointerPosition=calculatePointerPosition(325-i*ratio, OutsideRadius);
            canvas.drawCircle(pointerPosition[0], pointerPosition[1], outside_point_radius, PaintOutsidePointNormal);
            if(i <= mprogress ){
                canvas.drawCircle(pointerPosition[0], pointerPosition[1], outside_point_radius, PaintOutsidePointPress);
            }
        }

    }

    private void drawInsidePoint(Canvas canvas) {
        float[] pointerPosition=calculatePointerPosition(320-sweepAngle, InsideRadius);
        canvas.drawCircle(pointerPosition[0], pointerPosition[1], inside_point_radius, PaintInsidePoint);
    }
    private float[] calculatePointerPosition(float angle,float Radius ) {
        float x, y;
        x=(float) Math.sin(2* Math.PI / 360*angle)*Radius;
        y=(float) Math.cos(2* Math.PI / 360*angle)*Radius;

        //System.out.println("angle x:"+x+",y:"+y);
        return new float[] { x, y };
    }
    private boolean touchable = true;

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    private void rotateWheel(float degrees) {
        float temp = degrees;
        if (degrees > 300) {
            degrees = degrees - 360;
        } else if (degrees < -300) {
            degrees = 360 + degrees;
        }
        float sum = totalDegree + degrees;
        if(sum > MAX_SWEEP_ANGLE || sum < 0){
            return;
        }
        matrix.postRotate(temp, wheelWidth / 2, wheelHeight / 2);
        setImageMatrix(matrix);

        totalDegree = sum;
        totalDegree %= 360;
        if (totalDegree < 0) {
            totalDegree += 360;
        }
        sweepAngle = totalDegree;
        invalidate();
        if (listener != null) {
            listener.onRoateChange(totalDegree, true);
        }
        if(mProListener != null){
            mProListener.onProgressChanged(KnobViewLine_OutThumb.this,true, getProgress());
        }
    }
    private void setAngle(float angle) {
        setAngle(angle, false);
    }
    private void setAngle(float angle, boolean callback) {
        float delta = Math.abs(angle) % 360 - totalDegree;
        totalDegree = Math.abs(angle) % 360;
        if (wheelWidth == 0 || wheelHeight == 0) {
            return;
        } else {
            matrix.postRotate(delta, wheelWidth / 2, wheelHeight / 2);
            setImageMatrix(matrix);
            if (listener != null && callback) {
                listener.onRoateChange(totalDegree, false);
            }
            if(mProListener != null){
                mProListener.onProgressChanged(KnobViewLine_OutThumb.this,false, getProgress());
            }
        }
    }

    private double getAngle(double x, double y) {
        x = x - (wheelWidth / 2d);
        y = wheelHeight - y - (wheelHeight / 2d);

        switch (getQuadrant(x, y)) {
            case 1:
                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 2:
                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            case 3:
                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:
                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    private double getAngle2(double x, double y){
        x = x - (wheelWidth / 2d);
        y = y - (wheelHeight / 2d);
        return Math.toDegrees(Math.atan(y / x));
    }

    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }

    public interface RotateChangeListener {
        public void onRoateChange(float degress, boolean fromUser);

        void onRotateChangeStart(float degree);

        void onRotateChangeEnd(float degree);
    }

    private class WheelTouchListener implements OnTouchListener {
        private double startAngle;

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (!isTouchWheel((int) event.getX(), (int) event.getY())) {
                        return false;
                    }
                    startAngle = getAngle(event.getX(), event.getY());
                    if (listener != null) {
                        listener.onRotateChangeStart(totalDegree);
                    }
                    if(mProListener != null){
                        mProListener.onStartTrackingTouch(KnobViewLine_OutThumb.this, getProgress());
                    }
                    if (!touchable) {
                        return false;
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    double currentAngle = getAngle(event.getX(), event.getY());
                    rotateWheel((float) (startAngle - currentAngle));
                    startAngle = currentAngle;
                    break;
                case MotionEvent.ACTION_UP:
                    if (listener != null) {
                        listener.onRotateChangeEnd(totalDegree);
                    }
                    if(mProListener != null){
                        mProListener.onStopTrackingTouch(KnobViewLine_OutThumb.this, getProgress());
                    }
                    break;
            }
            requestDisallowInterceptTouchEvent();
            return true;
        }
    }

    private boolean isTouchWheel(int x, int y) {
        double d = getTouchRadius(x, y);
        //if (d < imageOriginal.getWidth() / 2) { min / 2
        if (d < min / 2) {
            return true;
        }
        return false;
    }

    private double getTouchRadius(int x, int y) {
        int cx = x - getWidth() / 2;
        int cy = y - getHeight() / 2;
        return Math.hypot(cx, cy);
    }

    private void requestDisallowInterceptTouchEvent() {
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
    }

    private int mMax = 100;
    private int mprogress = 0;
    /**
     * ����������ֵ
     */
    public void setMax(int max){
        mMax = max+1;
        ratio = MAX_SWEEP_ANGLE/mMax;
    }
    public int getMax() {
        return mMax;
    }
    public void setProgress(int progress){
//        chs_progress = Math.min(chs_progress, mMax);
//        chs_progress = Math.max(1, chs_progress);
        mprogress = progress;
        sweepAngle = ratio * mprogress;
        setProgressAngle(sweepAngle);
        invalidate();

        // System.out.println("FUCK setProgress:"+mprogress);
        //System.out.println("FUCK setsweepAngle:"+sweepAngle);
    }
    private void setProgressAngle(float angle) {
        float delta = Math.abs(angle) % 360 - totalDegree;
        totalDegree = Math.abs(angle) % 360;
        if (wheelWidth == 0 || wheelHeight == 0) {
            return;
        } else {
            matrix.postRotate(delta, wheelWidth / 2, wheelHeight / 2);
            setImageMatrix(matrix);
        }
    }
    public int getProgress() {
        mprogress = (int)(sweepAngle / ratio);
        return mprogress;
    }

    private OnProgressChangeListener mProListener;

    public void setProgressChangeListener(OnProgressChangeListener listener) {
        this.mProListener = listener;
    }
    public interface OnProgressChangeListener{

        void onProgressChanged(KnobViewLine_OutThumb view, boolean fromUser, int progress);

        void onStartTrackingTouch(KnobViewLine_OutThumb view, int progress);
        void onStopTrackingTouch(KnobViewLine_OutThumb view, int progress);
    }
}
