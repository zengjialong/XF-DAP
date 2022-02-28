package com.chs.mt.xf_dap.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;

import com.chs.mt.xf_dap.R;


@SuppressLint("AppCompatCustomView")
public class MainSeekbar extends ImageView {
    private static final float MAX_SWEEP_ANGLE = 270;
    private static final float START_ANGLE = 180;
    private RectF mSrcRect ;
    private Rect mDestRect;
    private  RectF rectF;

    private int radius;

    private int beginX;
    private int beginY;

    private int START_RADIU=0;
    private float END_RADIU=0;

    private int height;
    private  int width;
    private int wheelHeight, wheelWidth, min;
    private Bitmap imageOriginal;
    private Matrix matrix;
    private boolean bool_matrix = false;
    private RotateChangeListener listener;
    private float totalDegree;
    private RectF arcRect, rect;
    private static final float PROGRESS_RATIO = 0.08f;
    private static final float PROG_RATIO = 0.90f;
    private int progressWidth = 10;
    private int progressthumbWidth = 10;
    private Bitmap bitmap;
    private Drawable mBgDrawable=null;
    private int shadowWidth = 150;
    private Paint progressBgPaint, progressPaint, progressPaint2, paint;
    private Paint progressThumbPaint,progressBgThumbPaint;
    private Drawable left, right, thumb;
    int drawableWidth = 0;
    int drawableHeight = 0;
    //private float radius;
    //private float pic_radius;
    private float centerX, centerY;
    private float drawableRadius = 0;
    private float ratio = 0;
    private PorterDuffXfermode srcIn;
    private float InsideRadius = 0;
    private float OutsideRadius = 0;

    private float inside_line_width = 0;
    private float inside_thumb_width = 0;

    private float inside_point_radius = 0;
    private float outside_point_radius = 0;
    private int inside_point_color = 0;
    private int inside_cir_color = 0;
    private int outside_point_color_press = 0;
    private int outside_point_color_normal = 0;

    private int outside_line_color_press = 0;
    private int outside_line_color_normal = 0;
    private Paint PaintInsideCir;
    private Paint PaintInsidePoint;
    private Paint PaintOutsidePointPress;
    private Paint PaintOutsidePointNormal;
    private Bitmap bg;
    private Bitmap ct;

    private float max = 360;
    private double rate = 360;
    private int backgroundResId;
    private int contentResId;


    private Context mContext = null;
    private AttributeSet mAttrs = null;

    public MainSeekbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mAttrs = attrs;
        init(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    public void setBackgroundResId(int backgroundResId) {
        this.backgroundResId = backgroundResId;
        bg = BitmapFactory.decodeResource(getResources(), backgroundResId);
    }

    public void setContentResId(int contentResId) {
        this.contentResId = contentResId;
        ct = BitmapFactory.decodeResource(getResources(), contentResId);
    }


    private int thumbHalfWidth;
   // private float thumbRadius;

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
        thumb = getResources().getDrawable(R.drawable.chs_main_thumb);
        thumbHalfWidth = thumb.getIntrinsicWidth() / 2;
        int thumbHalfHeight = thumb.getIntrinsicHeight() / 2;
        thumb.setBounds(-thumbHalfWidth, -thumbHalfHeight, thumbHalfWidth, thumbHalfHeight);
         bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.seekbar_bg);
        mBgDrawable = getResources().getDrawable(R.drawable.seekbar_bg);

        bg = BitmapFactory.decodeResource(getResources(), R.drawable.main_seekbar_bg);
        ct = BitmapFactory.decodeResource(getResources(), R.drawable.seekbar_bg);

        int colorProgress = outside_line_color_normal;//getResources().getColor(R.color.color_main);
        int midColor = outside_line_color_press;//getResources().getColor(R.color.color_main_light);
        rate = 360 / max;

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(getResources().getColor(R.color.red));

        progressBgPaint = new Paint();
        progressBgPaint.setStyle(Paint.Style.STROKE);
        progressBgPaint.setColor(colorProgress);
        progressBgPaint.setAntiAlias(true);
       // progressBgPaint.setStrokeCap(Paint.Cap.ROUND);


        //绘制最外部圆
        progressPaint = new Paint();
        progressPaint.setColor(colorProgress);
        //progressPaint.setMaskFilter(maskFilter);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setAntiAlias(true);
        //progressPaint.setStrokeCap(Paint.Cap.ROUND);

        //绘制带线条的圆
        progressPaint2 = new Paint();
        progressPaint2.setColor(outside_line_color_press);
        progressPaint2.setStyle(Paint.Style.STROKE);
        progressPaint2.setAntiAlias(true);
        //progressPaint2.setStrokeCap(Paint.Cap.ROUND);



        progressThumbPaint= new Paint();
        progressThumbPaint.setColor(outside_line_color_press);
        progressThumbPaint.setStyle(Paint.Style.STROKE);
        progressThumbPaint.setAntiAlias(true);
        progressThumbPaint.setStrokeCap(Paint.Cap.ROUND);

        progressBgThumbPaint= new Paint();
        progressBgThumbPaint.setColor(colorProgress);
        progressBgThumbPaint.setStyle(Paint.Style.STROKE);
        progressBgThumbPaint.setAntiAlias(true);
        progressBgThumbPaint.setStrokeCap(Paint.Cap.ROUND);




        ratio = MAX_SWEEP_ANGLE / mMax;

        invalidate();
    }



    private void initView() {
        TypedArray localTypedArray = mContext.obtainStyledAttributes(mAttrs, R.styleable.KNOB);

        inside_point_radius = localTypedArray.getDimension(R.styleable.KNOB_inside_point_radius, 4);
        outside_point_radius = localTypedArray.getDimension(R.styleable.KNOB_outside_point_radius, 2);

        inside_cir_color = localTypedArray.getColor(R.styleable.KNOB_inside_cir_color, getResources().getColor(android.R.color.darker_gray));
        inside_point_color = localTypedArray.getColor(R.styleable.KNOB_inside_point_color, getResources().getColor(android.R.color.darker_gray));
        outside_point_color_press = localTypedArray.getColor(R.styleable.KNOB_outside_point_color_press, getResources().getColor(android.R.color.darker_gray));
        outside_point_color_normal = localTypedArray.getColor(R.styleable.KNOB_outside_point_color_normal, getResources().getColor(android.R.color.darker_gray));

        outside_line_color_press = localTypedArray.getColor(R.styleable.KNOB_outside_line_color_press, getResources().getColor(android.R.color.darker_gray));
        //outside_line1_color_press = localTypedArray.getColor(R.styleable.KNOB_outside_line1_color_press, getResources().getColor(android.R.color.darker_gray));
        outside_line_color_normal = localTypedArray.getColor(R.styleable.KNOB_outside_line_color_normal, getResources().getColor(android.R.color.darker_gray));
        inside_line_width = localTypedArray.getDimension(R.styleable.KNOB_outside_line_width, 2);

        inside_thumb_width = localTypedArray.getDimension(R.styleable.KNOB_outside_thumb_width, 2);

        progressWidth = (int) inside_line_width;
        progressthumbWidth = (int) inside_thumb_width;


        localTypedArray.recycle();
    }

    public void setRotateChangeListener(RotateChangeListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        min = Math.max(width, height);
        centerX = min / 2;
        centerY = min / 2;
        drawableRadius = min / 2;
        //radius = (float) (drawableRadius);//��Seekbar����λ�ð뾶
        //thumbRadius = radius;//thumbRadius����λ�ð뾶
        //pic_radius = min / 2 * PROG_RATIO;//��ͼ���ڰ뾶

        //InsideRadius与OutsideRadius不能相同不然会无显示
        InsideRadius = (float) (drawableRadius*0.67);  //控制线条的长度
        OutsideRadius = (float) (drawableRadius * 0.82); //控制离外部的距离

//        if (!bool_matrix) {
//            bool_matrix = true;
//            float scaleWidth = (pic_radius * 2) / drawableWidth;
//            float scaleHeight = (pic_radius * 2) / drawableHeight;
//            matrix.postScale(scaleWidth, scaleHeight);     //这个api的第一个参数是X轴的缩放大小，第二个参数是Y轴的缩放大小
//        }

    }

    private Bitmap resizeBitmap(Bitmap src, int w, int h){

        int width = src.getWidth();
        int height = src.getHeight();
        int scaleWidht = w / width;
        int scaleHeight = h / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidht, scaleHeight);

        Bitmap result = Bitmap.createScaledBitmap(src, w, h, true);
        src = null;

        return result;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int tmp = w >= h ? h : w;

        radius = tmp / 2;
        beginX = w / 2;
        beginY = 0;
        centerX = tmp / 2;
        centerY = tmp / 2;

        Bitmap bg_ = resizeBitmap(bg, tmp, tmp);
        Bitmap ct_ = resizeBitmap(ct, tmp, tmp);

        rectF = new RectF(0, (getHeight() - bg_.getHeight()) / 2, bg_.getWidth(), (getHeight() + bg_.getHeight()) / 2);

        bg.recycle();
        ct.recycle();

        bg = bg_;
        ct = ct_;





        if (wheelHeight == 0 || wheelWidth == 0) {
            wheelHeight = min;
            wheelWidth = min;

            progressBgPaint.setStrokeWidth(progressWidth);
            progressPaint.setStrokeWidth(progressWidth);
            // progressPaint2.setStrokeWidth(progressWidth / 3);
            progressPaint2.setStrokeWidth(progressWidth);
            progressThumbPaint.setStrokeWidth(progressWidth);
            progressBgThumbPaint.setStrokeWidth(progressthumbWidth);

            float translate = 0;//(min/2)*(1-PROG_RATIO);
            translate = (min / 2) * (1 - PROG_RATIO);

            //float translate = (min/2 - pic_radius)-progressWidth/2f;
            matrix.postTranslate(translate, translate);
            // this.setImageBitmap(imageOriginal);
            this.setImageMatrix(matrix);

            //matrix.postRotate(165, wheelWidth / 2, wheelHeight / 2);80 60 295
           // matrix.postRotate(60, wheelWidth / 2, wheelHeight / 2);
            setImageMatrix(matrix);
            if (listener != null) {
                listener.onRoateChange(totalDegree, true);
            }
            //arcRect = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
           // arcRect = new RectF(centerX , centerY, centerX, centerY);


            //float radius2 = radius + shadowWidth;
           // float radius2 = radius - shadowWidth;
            //rect = new RectF(centerX - radius2, centerY - radius2, centerX + radius2, centerY + radius2);

           // rect = new RectF(centerX , centerY , centerX , centerY );
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //  canvas.drawArc(rect, 0, 360, false, paint);
        super.onDraw(canvas);
       // canvas.drawArc(arcRect, START_ANGLE, MAX_SWEEP_ANGLE, false, progressBgPaint);
        // drawFullCir(canvas);
      //  canvas.drawArc(arcRect, START_ANGLE, 360, false, progressPaint);   //绘制最外面的圆
     //   canvas.drawArc(rect, START_ANGLE, START_ANGLE + MAX_SWEEP_ANGLE, false, progressBgThumbPaint);  //绘制最里面的圆
     //  drawProgress(canvas);
        //drawThumb(canvas);    //绘制带线条的圆
        // drawDrawables(canvas);
        //drawDrawBg(canvas);
       // drawableMine(canvas);



        //myUseInputStreamandBitmapDrawable(canvas);
        //canvas.drawRect();


        //mDestRect=new RectF(0, 0, min, min);
      //  canvas.drawArc(mDestRect, (90-36)*16, 36*16,true,paint);
       // paint.set



           //canvas.drawBitmap(bitmap, 100, 100, paint);
       // canvas.drawColor(getResources().getColor(R.color.red));

        canvas.drawBitmap(bg, 0, (getHeight() - bg.getHeight()) / 2, paint);
     int rc = canvas.saveLayer(0, (getHeight() - bg.getHeight()) / 2, bg.getWidth(), (getHeight() + bg.getHeight()) / 2, null, Canvas.ALL_SAVE_FLAG);
       //但Layer入栈时，后续的DrawXXX操作都发生在这个 Layer上，而Layer退栈时，就会把本层绘制的图像“绘制”到上层或是Canvas上，
        paint.setFilterBitmap(false);
        END_RADIU = (float) (END_RADIU * rate);
         canvas.drawArc(rectF, 90, END_RADIU, true, paint);
        srcIn = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        paint.setXfermode(srcIn);
        paint.setColor(getResources().getColor(R.color.red));
        canvas.drawBitmap(ct, 0, (getHeight() - ct.getHeight()) / 2, paint);
        paint.setXfermode(null);


    canvas.restoreToCount(rc); //是用来保存和回复Canvas的状态

       // canvas.translate(centerX, centerY);
//        System.out.println("BUG 当前的值为"+bitmap.getWidth()+"高度为"+bitmap.getHeight());
//
//        // 包装成为Drawable
//        PictureDrawable drawable = new PictureDrawable(bitmap);
//        // 设置绘制区域 -- 注意此处所绘制的内容不匹配时，
//        // 只会显示一部分，不会对picture进行裁剪
//        drawable.setBounds(0,0,200,bitmap.getHeight());
//        // 绘制
//        drawable.draw(canvas);




      //  canvas.drawBitmap(bitmap,280, 300, progressPaint2);
       // canvas.drawBitmap(bitmap, 10,10,progressPaint2);
//        canvas.clipRect(bitmap.getWidth(), bitmap.getHeight(), bitmap.getWidth()+bitmap.getWidth()/2 , bitmap.getHeight()+bitmap.getHeight()/2);
//
       //drawLinePart(canvas);

    }

    private void drawableMine(Canvas canvas) {
     rectF = new RectF(centerX - OutsideRadius, centerY - OutsideRadius, centerX + OutsideRadius, centerY + OutsideRadius);
        int dv = 12;

        int part = 120 / dv;
        float[] in, out;

        int ang = 0;
//        for (int i = 0; i < part; i++) {
//            ang = i * dv;
//            in = calculatePointerPosition(ang, InsideRadius);
//            out = calculatePointerPosition(ang, OutsideRadius);

//            if(i>((part)/4)) {
//                canvas.drawColor(getResources().getColor(R.color.nullc));
//
//            }
        paint.setXfermode(null);
           canvas.drawArc(rectF, 90, END_RADIU, true, paint);
       // canvas.clipOutRect(rectF, START_RADIU-270, 270, true, paint);
        //   }

    }

    private void drawDrawBg(Canvas canvas){
        this.mBgDrawable.setBounds((int)(centerX-drawableRadius), (int)(centerY-drawableRadius),
                (int)(centerX+drawableRadius), (int)(centerY+drawableRadius));
        this.mBgDrawable.draw(canvas);


    }


    private float sweepAngle = 0;

    private void drawProgress(Canvas canvas) {
        //canvas.drawArc(arcRect, START_ANGLE, 30, false, progressPaint2);

        // canvas.drawArc(arcRect, START_ANGLE, 30, false, progressPaint2);

        canvas.drawArc(arcRect, START_ANGLE, sweepAngle, false, progressPaint);
        canvas.drawArc(rect, START_ANGLE, sweepAngle, false, progressThumbPaint);
    }




//    private void drawThumb(Canvas canvas) {
//        float angle = (START_ANGLE + sweepAngle);
//        double radians = Math.toRadians(angle);
//        double x = Math.cos(radians) * thumbRadius + centerX;
//        double y = Math.sin(radians) * thumbRadius + centerY;
//        // System.out.println("BUG 所需要绘制的值为"+angle+"==radians"+radians+"x=="+x+"y=="+y);
//        canvas.save();
//        canvas.translate((float) x, (float) y);
//     //   thumb.draw(canvas);
//        canvas.restore();
//    }


    private void drawLinePart(Canvas canvas) {
        //int dv = 5;
        int dv = 12;

        int part = 360 / dv;
        float[] in, out;

        int ang = 0;
        for (int i = 0; i < part; i++) {
            ang = i * dv;
            in = calculatePointerPosition(ang, InsideRadius);
            out = calculatePointerPosition(ang, OutsideRadius);

            if(i>((part)/4)) {
                canvas.drawColor(getResources().getColor(R.color.nullc));
               // canvas.drawLine(in[0], in[1], out[0], out[1], progressBgPaint);
            }

        }
//        if (mprogress > 12) {
//            part = (int) (sweepAngle ) / dv;  //16=dv*2
//        }
//        else {
//            part = (int) (sweepAngle ) / dv;
//        }
//
//

        for (int i = 0; i < part; i++) {
            ang = 180 - (i * dv - 180);
            in = calculatePointerPosition(ang, InsideRadius);
            out = calculatePointerPosition(ang, OutsideRadius);
                canvas.drawLine(in[0], in[1], out[0], out[1], progressBgPaint);
           // canvas.drawPicture(R.drawable.main_seekbar_bg);
        }




    }

    private void drawInsidePoint(Canvas canvas) {
        float[] pointerPosition = calculatePointerPosition(320 - sweepAngle, InsideRadius);
        canvas.drawCircle(pointerPosition[0], pointerPosition[1], inside_point_radius, PaintInsidePoint);
    }

    private float[] calculatePointerPosition(float angle, float Radius) {
        float x, y;
        x = (float) Math.sin(2 * Math.PI / 360 * angle) * Radius;
        y = (float) Math.cos(2 * Math.PI / 360 * angle) * Radius;

        //System.out.println("angle x:"+x+",y:"+y);
        return new float[]{x, y};
    }

    private boolean touchable = true;

    public void setTouchable(boolean touchable) {
        this.touchable = touchable;
    }

    private void rotateWheel(float degrees) {
        float temp = degrees;
        if (degrees > 270) {
            degrees = degrees - 360;
        } else if (degrees < -270) {
            degrees = 360 + degrees;
        }
        float sum = totalDegree + degrees;

        if (sum > MAX_SWEEP_ANGLE || sum < 0) {
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
       // START_RADIU=(int)sweepAngle;

        END_RADIU=(int)sweepAngle;

        invalidate();
        if (listener != null) {
            listener.onRoateChange(totalDegree, true);
        }
        if (mProListener != null) {
            mProListener.onProgressChanged(MainSeekbar.this, true, getProgress());
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
            if (mProListener != null) {
                mProListener.onProgressChanged(MainSeekbar.this, false, getProgress());
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

    private double getAngle2(double x, double y) {
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
                    //START_RADIU=(int)startAngle;
                    if (listener != null) {
                        listener.onRotateChangeStart(totalDegree);
                    }
                    if (mProListener != null) {
                        mProListener.onStartTrackingTouch(MainSeekbar.this, getProgress());
                    }
                    if (!touchable) {
                        return false;
                    }

                    break;
                case MotionEvent.ACTION_MOVE:
                    double currentAngle = getAngle(event.getX(), event.getY());
                    rotateWheel((float) (startAngle - currentAngle));
                   // START_RADIU=(int)(startAngle - currentAngle);
                    startAngle = currentAngle;

                    break;
                case MotionEvent.ACTION_UP:
                    if (listener != null) {
                        listener.onRotateChangeEnd(totalDegree);
                    }
                    if (mProListener != null) {
                        mProListener.onStopTrackingTouch(MainSeekbar.this, getProgress());
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
    public void setMax(int max) {
        mMax = max + 1;
        ratio = MAX_SWEEP_ANGLE / mMax;
    }

    public int getMax() {
        return mMax;
    }

    public void setProgress(int progress) {
//        chs_progress = Math.min(chs_progress, mMax);
//        chs_progress = Math.max(1, chs_progress);
        mprogress = progress;
        sweepAngle = ratio * mprogress;
        END_RADIU=(int)sweepAngle;
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
        mprogress = (int) (sweepAngle / ratio);
        // System.out.println("BUG FUCK getProgress:" + mprogress);

        return mprogress;
    }

    private OnProgressChangeListener mProListener;

    public void setProgressChangeListener(OnProgressChangeListener listener) {
        this.mProListener = listener;
    }

    public interface OnProgressChangeListener {

        void onProgressChanged(MainSeekbar view, boolean fromUser, int progress);

        void onStartTrackingTouch(MainSeekbar view, int progress);

        void onStopTrackingTouch(MainSeekbar view, int progress);
    }
}
