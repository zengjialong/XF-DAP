package com.chs.mt.xf_dap.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.datastruct.ControlPoint;
import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.DataStruct_Output;
import com.chs.mt.xf_dap.datastruct.Define;
import com.chs.mt.xf_dap.datastruct.MacCfg;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EQ_Genter extends View {
    private Paint xlinePaint;
    private Paint ylinePaint;
    private Paint circlePaint;
    private Paint circleoutPaint;
    private Paint txtPaint;
    private Paint outtxtPaint;
    private Paint midlinePaint;


    private Paint ImagePaint;//+ -号的定义
    private Paint IncPaint;//+ -号的定义

    private float[] X_shuzu = new float[31];
    private float[] Y_shuzu = new float[31];
    private float width, height, FrameWidth, FrameHeight;
    private float lineSmoothness = 0.6f;
    private PathMeasure mPathMeasure;
    private List<Integer> mControlPointList;
    private int lastIndex = 0;
    private int endIndex = 0;
    private int CurrentX = 0;


    private PointF conPoint1;
    private PointF conPoint2;

    public PointF getConPoint1() {
        return conPoint1;
    }

    public void setConPoint1(PointF conPoint1) {
        this.conPoint1 = conPoint1;
    }

    public PointF getConPoint2() {
        return conPoint2;
    }

    public void setConPoint2(PointF conPoint2) {
        this.conPoint2 = conPoint2;
    }

    private float mTextCenterY;
    private float margin_all;
    private float mTextOutY;
    private Path path;
    private int StokeWidth = 2;
    private boolean first = false;
    private int Width = 0;
    private int Height = 0;
    private Rect rect;
    private Thread sThread;
    private Paint.FontMetricsInt fmi;
    private Paint.FontMetricsInt fmo;
    private float frameTextSize = 10f; //内框线宽

    private float ImageSize = 80; //内框线宽
    private float IncSize = 50; //内框线宽

    private float radius = 25;//圆的半径
    private String[] gain_txt = new String[]{"12", "6", "0", "6", "12"};
    private Bitmap bitmap;
    private RectF mSrcRect;
    public DataStruct_Output EQ_Filter = new DataStruct_Output();
    private float x, y;

    private String[] freq_txt = new String[]{"20", "32", "50", "80", "125", "200", "315", "800", "1.25K", "2K", "3.15K", "5K", "8K", "12.5K", "20K"};
    /*事件监听 */
    private EQ_GenterChangeListener eq_genterChangeListener = null;

    public interface EQ_GenterChangeListener {
        public abstract void onProgressChanged(EQ_Genter eq_genter,
                                               int progress, int CurrentX, boolean fromUser);
    }


    public void setOnEQ_GenterChangeListener(EQ_Genter.EQ_GenterChangeListener num) {
        eq_genterChangeListener = num;
    }


    public EQ_Genter(Context context) {
        super(context);
        init(null, 0);
    }

    public EQ_Genter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public EQ_Genter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.eq_genter, defStyle, 0);

        initAttributes(a);


        xlinePaint = new Paint();
        xlinePaint.setColor(getResources().getColor(R.color.ylineColor));//抗锯齿
        xlinePaint.setAntiAlias(true);//防抖动
        xlinePaint.setDither(true);//描边
        xlinePaint.setStyle(Paint.Style.STROKE);//笔宽
        xlinePaint.setStrokeWidth(StokeWidth); //设置线宽


        ylinePaint = new Paint();
        ylinePaint.setColor(getResources().getColor(R.color.xlineColor));//抗锯齿
        ylinePaint.setAntiAlias(true);//防抖动
        ylinePaint.setDither(true);//描边
        ylinePaint.setStyle(Paint.Style.STROKE);//笔宽
        ylinePaint.setStrokeWidth(StokeWidth);


        path = new Path();

        //设置外圆
        circlePaint = new Paint();
        circlePaint.setColor(getResources().getColor(R.color.circleoutColor));
        circlePaint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白
        circlePaint.setStrokeWidth(StokeWidth);
        circlePaint.setStyle(Paint.Style.STROKE);


        //设置外圆
        circleoutPaint = new Paint();
        //circleoutPaint.setColor(getResources().getColor(R.color.circleinColor));
        circleoutPaint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白
        circleoutPaint.setStyle(Paint.Style.FILL);


        //设置中间文字的颜色
        txtPaint = new Paint();
        txtPaint.setColor(Color.WHITE);//抗锯齿
        txtPaint.setTextAlign(Paint.Align.CENTER);
        txtPaint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白
        txtPaint.setTextSize(20f);
        txtPaint.setStrokeWidth(StokeWidth); //设置线宽


        //设置框两边的文字颜色
        outtxtPaint = new Paint();
        outtxtPaint.setColor(Color.WHITE);//抗锯齿
        outtxtPaint.setTextAlign(Paint.Align.CENTER);
        outtxtPaint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白
        outtxtPaint.setTextSize(frameTextSize);
        outtxtPaint.setStrokeWidth(StokeWidth); //设置线宽



        /**+ - 号单独做处理*/
        ImagePaint = new Paint();
        ImagePaint.setColor(Color.WHITE);//抗锯齿
        ImagePaint.setTextAlign(Paint.Align.CENTER);
        ImagePaint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白
        ImagePaint.setTextSize(ImageSize);
        ImagePaint.setStrokeWidth(StokeWidth); //设置线宽

        IncPaint = new Paint();
        IncPaint.setColor(Color.WHITE);//抗锯齿
        IncPaint.setTextAlign(Paint.Align.CENTER);
        IncPaint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白
        IncPaint.setTextSize(IncSize);
        IncPaint.setStrokeWidth(StokeWidth); //设置线宽


        midlinePaint = new Paint();
        midlinePaint.setColor(getResources().getColor(R.color.mlineColor));//抗锯齿
        midlinePaint.setTextAlign(Paint.Align.CENTER);
        midlinePaint.setStyle(Paint.Style.STROKE);

        midlinePaint.setAntiAlias(true);// 设置画笔的锯齿效果。 true是去除，大家一看效果就明白
        //lPaint.setStrokeJoin(Paint.Join.ROUND);
        midlinePaint.setStrokeWidth(5); //设置线宽
        // 获得资源
        Resources rec = getResources();
        @SuppressLint("ResourceType") InputStream in = rec.openRawResource(R.drawable.eq_genter);
        // BitmapDrawable 解析数据流
        BitmapDrawable bitmapDrawable = new BitmapDrawable(in);
        // 得到图片
        bitmap = bitmapDrawable.getBitmap();


        mControlPointList = new ArrayList<>();

    }

    private void initAttributes(TypedArray a) {
        margin_all = a.getDimension(R.styleable.eq_genter_margin_all, 10); //框离底边距
        frameTextSize = a.getDimension(R.styleable.eq_genter_frame_text_size, 10); //框离底边距
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        DrawText(canvas);
        DrawBottomText(canvas);
        canvas.translate(margin_all + radius, margin_all - radius);//再将canvas 平移（translate）（100，100）
        //canvas.drawColor(getResources().getColor(R.color.divider), PorterDuff.Mode.SCREEN);

        DrawFrame(canvas);
        setEQ();
        DrawMyPoint(canvas);
        DrawPoint(canvas);
    }


    /**
     * 把一般坐标转为 Android中的视图坐标
     **/
//    private List<PointF> changePoint(List<PointF> oldPointFs){
//        List<PointF> pointFs = new ArrayList<>();
//        //间隔，减去某个值是为了空出多余空间，为了画线以外，还要写坐标轴的值，除以坐标轴最大值(这里设为定值)
//        //相当于缩小图像
//        int intervalX = (getMeasuredWidth()-20)/12;
//        int intervalY = (getMeasuredHeight()-20)/250;
//        int height = getMeasuredHeight();
//        PointF p;
//        float x;
//        float y;
//        for (PointF pointF: oldPointFs){
//            //最后的正负值是左移右移
//            x = pointF.x  + 0;
//            y =  pointF.y * intervalY - 100;
//            p = new PointF(x, y);
//            pointFs.add(p);
//        }
//        return pointFs;
//    }
    private void DrawMyPoint(Canvas canvas) {
//        path.reset();
//        for (int i=0;i<X_shuzu.length;i++) {
//            if(i>0&&(i+1)<X_shuzu.length){
//                path.cubicTo(X_shuzu[i-1], Y_shuzu[i-1], X_shuzu[i], Y_shuzu[i], X_shuzu[i+1], Y_shuzu[i+1]);
//            }
//        }
//        canvas.drawPath(path, circlePaint);
        List<PointF> oldPointF1 = new ArrayList<>();
        for (int i = 0; i < X_shuzu.length; i++) {
            oldPointF1.add(new PointF(X_shuzu[i], Y_shuzu[i]));
        }
        //  List<PointF> pointFs1 = changePoint(oldPointF1);
//        for (int i = 0; i <oldPointF1.size() ; i++) {
//            System.out.println("BUG 这里获取到的X的值为"+oldPointF1.get(i).x* lineSmoothness+"y的值为"+oldPointF1.get(i).y* lineSmoothness);
//        }


        List<ControlPoint> controlPoints1 = ControlPoint.getControlPointList(oldPointF1);
        Path mPath1 = new Path();

        for (int i = 0; i < controlPoints1.size(); i++) {
            if (i == 0) {
                mPath1.moveTo(oldPointF1.get(i).x, oldPointF1.get(i).y);
            }
            //画三价贝塞尔曲线
//            mPath1.quadTo(
//                    controlPoints1.get(i).getConPoint1().x,controlPoints1.get(i).getConPoint1().y,
//
//                    oldPointF1.get(i+1).x, oldPointF1.get(i+1).y
//            );
            //else {
            mPath1.cubicTo(
                    oldPointF1.get(i).x, oldPointF1.get(i).y,
                    controlPoints1.get(i).getConPoint2().x, controlPoints1.get(i).getConPoint2().y,
                    oldPointF1.get(i + 1).x, oldPointF1.get(i + 1).y
            );
            // }
        }
//        LinearGradient mLinearGradient = new LinearGradient(
//                0,
//                0,
//                0,
//                getMeasuredHeight(),
//                new int[]{
//                        0xffffffff,
//                        getResources().getColor(R.color.red),
//                        getResources().getColor(R.color.green),
//                        getResources().getColor(R.color.green),
//                        getResources().getColor(R.color.green)},
//                null,
//                Shader.TileMode.CLAMP
//        );
//        circleoutPaint.setShader(mLinearGradient);

        canvas.drawPath(mPath1, midlinePaint);
        //  canvas.save();

        //   mControlPointList.clear();

//        for (int i=0;i<X_shuzu.length;i++) {
//            if(i==0){
//                float controlX=X_shuzu[1]*lineSmoothness;
//                float controlY=Y_shuzu[1];
//                mControlPointList.add(new PointF(controlX, controlY));
//            }else if(i==X_shuzu.length-1){
//              //  val lastPoint = X_shuzu[i - 1];
//                float controlX = X_shuzu[i] - (X_shuzu[i] - X_shuzu[i-1] ) * lineSmoothness;
//                float controlY =Y_shuzu[i];
//                mControlPointList.add(new PointF(controlX, controlY));
//            }else{
////                int lastPoint = pointList[i - 1]
////                val nextPoint = pointList[i + 1]
//                float k = (X_shuzu[i-1] - Y_shuzu[i-1]) / (X_shuzu[i+1] - Y_shuzu[i+1]);
//                float b = Y_shuzu[i] - k * X_shuzu[i];
//                //添加前控制点
//                float lastControlX = X_shuzu[i] - (X_shuzu[i] - X_shuzu[i-1]) * lineSmoothness;
//                float lastControlY = k * lastControlX + b;
//                mControlPointList.add(new PointF(lastControlX, lastControlY));
//                //添加后控制点
//                float nextControlX = X_shuzu[i] + ( X_shuzu[i+1] -  X_shuzu[i]) * lineSmoothness;
//                float nextControlY = k * nextControlX + b;
//                mControlPointList.add(new PointF(nextControlX, nextControlY));
//            }
//        }
//        path.reset();
//        path.moveTo(X_shuzu[0], height);
//        path.lineTo(X_shuzu[0], Y_shuzu[0]);
//        for (int i=0;i<mControlPointList.size();i++) {
//            PointF leftControlPoint = mControlPointList.get(i);
//            PointF rightControlPoint= rightControlPoint = mControlPointList.get(i);;
//            if((i+1)<mControlPointList.size()){
//                rightControlPoint = mControlPointList.get(i+1);
//            }
//
//
//            PointF rightPoint = mControlPointList.get(i / 2 + 1);
//            path.cubicTo(leftControlPoint.x, leftControlPoint.y, rightControlPoint.x, rightControlPoint.y, rightPoint.x, rightPoint.y);
//        }
        // canvas.drawPath(path,circlePaint);


//            when (i) {
//                0 -> {//第一项
//                    //添加后控制点
//                    val nextPoint = pointList[i + 1]
//                    val controlX = point.x + (nextPoint.x - point.x) * SMOOTHNESS
//                    val controlY = point.y
//                    mControlPointList.add(PointF(controlX, controlY))
//                }
//                pointList.size - 1 -> {//最后一项
//                    //添加前控制点
//                    val lastPoint = pointList[i - 1]
//                    val controlX = point.x - (point.x - lastPoint.x) * SMOOTHNESS
//                    val controlY = point.y
//                    mControlPointList.add(PointF(controlX, controlY))
//                }
//            else -> {//中间项
//                    val lastPoint = pointList[i - 1]
//                    val nextPoint = pointList[i + 1]
//                    val k = (nextPoint.y - lastPoint.y) / (nextPoint.x - lastPoint.x)
//                    val b = point.y - k * point.x
//                    //添加前控制点
//                    val lastControlX = point.x - (point.x - lastPoint.x) * SMOOTHNESS
//                    val lastControlY = k * lastControlX + b
//                    mControlPointList.add(PointF(lastControlX, lastControlY))
//                    //添加后控制点
//                    val nextControlX = point.x + (nextPoint.x - point.x) * SMOOTHNESS
//                    val nextControlY = k * nextControlX + b
//                    mControlPointList.add(PointF(nextControlX, nextControlY))
//                }
//            }


//            //保存曲线路径
//            path = new Path();
//            //保存辅助线路径
//            float prePreviousPointX = Float.NaN;
//            float prePreviousPointY = Float.NaN;
//            float previousPointX = Float.NaN;
//            float previousPointY = Float.NaN;
//            float currentPointX = Float.NaN;
//            float currentPointY = Float.NaN;
//            float nextPointX;
//            float nextPointY;
//
//            //final int lineSize = mPointList.size();
//            for (int valueIndex = 0; valueIndex < X_shuzu.length; ++valueIndex) {
//                if (Float.isNaN(currentPointX)) {
//                    //Point point = mPointList.get(valueIndex);
//                    currentPointX =X_shuzu[valueIndex];
//                    currentPointY = Y_shuzu[valueIndex];
//                }
//                if (Float.isNaN(previousPointX)) {
//                    //是否是第一个点
//                    if (valueIndex > 0) {
////                        Point point = mPointList.get(valueIndex - 1);
////                        previousPointX = point.x;
////                        previousPointY = point.y;
//                        previousPointX = X_shuzu[valueIndex - 1];
//                       previousPointY = Y_shuzu[valueIndex - 1];
//
//                    } else {
//                        //是的话就用当前点表示上一个点
//                        previousPointX = currentPointX;
//                        previousPointY = currentPointY;
//                    }
//                }
//
//                if (Float.isNaN(prePreviousPointX)) {
//                    //是否是前两个点
//                    if (valueIndex > 1) {
//                       // Point point = mPointList.get(valueIndex - 2);
//                        prePreviousPointX =  X_shuzu[valueIndex - 2];
//                        prePreviousPointY = Y_shuzu[valueIndex - 2];
//                    } else {
//                        //是的话就用当前点表示上上个点
//                        prePreviousPointX = previousPointX;
//                        prePreviousPointY = previousPointY;
//                    }
//                }
//
//                // 判断是不是最后一个点了
//                if (valueIndex < X_shuzu.length - 1) {
//                   // Point point = mPointList.get(valueIndex + 1);
//                    nextPointX =X_shuzu[valueIndex  + 1];
//                    nextPointY =Y_shuzu[valueIndex  + 1];
//                } else {
//                    //是的话就用当前点表示下一个点
//                    nextPointX = currentPointX;
//                    nextPointY = currentPointY;
//                }
//
//                if (valueIndex == 0) {
//                    // 将Path移动到开始点
//                    path.moveTo(currentPointX, currentPointY);
//                 //   mAssistPath.moveTo(currentPointX, currentPointY);
//                } else {
//                    // 求出控制点坐标
//                    final float firstDiffX = (currentPointX - prePreviousPointX);
//                    final float firstDiffY = (currentPointY - prePreviousPointY);
//                    final float secondDiffX = (nextPointX - previousPointX);
//                    final float secondDiffY = (nextPointY - previousPointY);
//                    final float firstControlPointX = previousPointX + (lineSmoothness * firstDiffX);
//                    final float firstControlPointY = previousPointY + (lineSmoothness * firstDiffY);
//                    final float secondControlPointX = currentPointX - (lineSmoothness * secondDiffX);
//                    final float secondControlPointY = currentPointY - (lineSmoothness * secondDiffY);
//                    //画出曲线
//                    path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
//                            currentPointX, currentPointY);
//                    //将控制点保存到辅助路径上
////                    mAssistPath.lineTo(firstControlPointX, firstControlPointY);
////                    mAssistPath.lineTo(secondControlPointX, secondControlPointY);
////                    mAssistPath.lineTo(currentPointX, currentPointY);
//                }
//
//                // 更新值,
//                prePreviousPointX = previousPointX;
//                prePreviousPointY = previousPointY;
//                previousPointX = currentPointX;
//                previousPointY = currentPointY;
//                currentPointX = nextPointX;
//                currentPointY = nextPointY;
//            }
//            mPathMeasure = new PathMeasure(path, false);
//        canvas.drawPath(path, circlePaint);
    }

    private void setEQ() {
        for (int i = 0; i < DataStruct.CurMacMode.EQ.Max_EQ; i++) {
            Y_shuzu[i] = (-((DataStruct.RcvDeviceData.OUT_CH[MacCfg.OutputChannelSel].EQ[i].level
                    - DataStruct.CurMacMode.EQ.EQ_LEVEL_ZERO) / 10) * (FrameHeight / 24) + FrameHeight / 2);

            //System.out.println("BUG 设置EQ的值为"+Y_shuzu[i]);

            if (Y_shuzu[i] >= FrameHeight) {
                Y_shuzu[i] = FrameHeight;
            }
            if (Y_shuzu[i] <= 0) {
                Y_shuzu[i] = 0;
            }
        }
    }

    private void DrawBottomText(Canvas canvas) {
        float tl = (width - margin_all) / freq_txt.length;
        int txml = 8;
        for (int i = 0; i < freq_txt.length; i++) {
            canvas.drawText(freq_txt[i], margin_all + frameTextSize + tl * i, height - radius, outtxtPaint);
        }

    }


    private void DrawText(Canvas canvas) {
        float tl = (height + frameTextSize) / gain_txt.length;
        int txml = 8;
        canvas.drawText("+", radius + frameTextSize / 2, margin_all- (frameTextSize ), IncPaint);
        //画右边的文本
        // canvas.drawText("+", width - margin_all + radius, margin_all - frameTextSize, outtxtPaint);
        for (int i = 0; i < gain_txt.length; i++) {
            // mTextCenterY = margin_all+frameTextSize/2+tl*i- fmi.top / 2 - fmi.bottom / 2;
            canvas.drawText(gain_txt[i], radius + frameTextSize / 2, margin_all + tl * i, outtxtPaint);
            //画右边的文本
            canvas.drawText(gain_txt[i], width - margin_all + radius, margin_all + tl * i, outtxtPaint);

        }
        canvas.drawText("-", radius + frameTextSize / 2, height - (frameTextSize / 2), ImagePaint);
        // canvas.drawText("－", width - margin_all + radius, height - (frameTextSize / 2), outtxtPaint);
    }

    private void drawScrollLine(Canvas canvas) {
//        Point startp = new Point();
//        Point endp = new Point();
//        for (int i = 0; i < X_shuzu.length - 1; i++)
//        {
//            startp = X_shuzu[i];
//            endp = X_shuzu[i + 1];
//            int wt = (startp.x + endp.x) / 2;
//            Point p3 = new Point();
//            Point p4 = new Point();
//            p3.y = startp.y;
//            p3.x = wt;
//            p4.y = endp.y;
//            p4.x = wt;
//
//            Path path = new Path();
//            path.moveTo(startp.x, startp.y);
//            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
//            canvas.drawPath(path, mPaint);
//        }
    }


    private void DrawPoint(Canvas canvas) {
        fmi = txtPaint.getFontMetricsInt();   //使文字居中的办法

        int mBitWidth = bitmap.getWidth();
        int mBitHeight = bitmap.getHeight();


        for (int i = 0; i < X_shuzu.length; i++) {
            if (i < 30) {
                CornerPathEffect cornerPathEffect = new CornerPathEffect(1000);
                midlinePaint.setPathEffect(cornerPathEffect);
                //  canvas.drawLine(X_shuzu[i], Y_shuzu[i], X_shuzu[i + 1], Y_shuzu[i + 1], midlinePaint);
            }
            // System.out.println("BUG  ===="+ Y_shuzu[i]);
            mTextCenterY = Y_shuzu[i] - fmi.top / 2 - fmi.bottom / 2;
//            canvas.drawCircle(X_shuzu[i], Y_shuzu[i], radius - StokeWidth, circleoutPaint);// 大圆
//            canvas.drawCircle(X_shuzu[i], Y_shuzu[i], radius, circlePaint);// 大圆

            // rect=new Rect((int)(X_shuzu[i]-radius), (int)(Y_shuzu[i]-radius),(int)(X_shuzu[i]+radius),(int)(Y_shuzu[i]+radius));


            rect = new Rect(0, 0, mBitWidth, mBitHeight);

            mSrcRect = new RectF((X_shuzu[i] - radius), (Y_shuzu[i] - radius), X_shuzu[i] + radius, Y_shuzu[i] + radius);


            canvas.drawBitmap(bitmap, rect, mSrcRect, circleoutPaint);

            // canvas.drawText(String.valueOf(i + 1), X_shuzu[i], mTextCenterY, txtPaint);// 画文本
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        x = FrameWidth / 30;
        y = FrameHeight / 2;
        for (int i = 0; i < X_shuzu.length; i++) {

            //  if (first == false) {
            //    Y_shuzu[i] = y;
            X_shuzu[i] = x * i;
            //}
        }
        width = getMeasuredWidth();//获取宽度
        height = getMeasuredHeight();//获取高度


//        FrameWidth=width;
//        FrameHeight=height;
        FrameWidth = width - margin_all * 2 - radius * 2;
        //FrameHeight = height - margin_all - radius * 2 - frameTextSize;
        FrameHeight = height - margin_all * 2;

    }

    private void DrawFrame(Canvas canvas) {

        //绘制网格线
        for (int i = 0; i < X_shuzu.length; i++) {

            canvas.drawLine(x * i, 0, x * i, FrameHeight, ylinePaint);
        }

        for (int i = 0; i <= 2; i++) {
            canvas.drawLine(0, y * i, x * 30, y * i, xlinePaint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        //mControlPointList.clear();
        float y = event.getY();
        if (y >= FrameHeight) {
            y = FrameHeight;
        }
        if (y <= 0) {
            y = 0;
        }
        float value2 = 0;
        first = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://0
                value2 = setY_shuzu(x, y);

                lastIndex = CurrentX;
                System.out.println("BUG 起始点为" + lastIndex);
                break;
            case MotionEvent.ACTION_UP://1
                endIndex = CurrentX;

                value2 = setY_shuzu(x, y);

                mControlPointList.clear();


                break;
            case MotionEvent.ACTION_MOVE://2
                value2 = setY_shuzu(x, y);
                endIndex = CurrentX;

                float value = 0;
                int index5=0;
                System.out.println("BUG 开始的值为"+lastIndex+"\t=-=\t"+endIndex);
                if (endIndex > lastIndex) {
                    index5=endIndex - lastIndex;
                    value = (Y_shuzu[endIndex] - Y_shuzu[lastIndex]) / (endIndex - lastIndex);
                } else {
                    index5=lastIndex - endIndex;
                    value = (Y_shuzu[lastIndex] - Y_shuzu[endIndex]) / (lastIndex - endIndex);
                }
                System.out.println("BUG index5="+index5);
                if( index5>(mControlPointList.size()-1)&&index5>0) {
                    for (int j = 0; j < mControlPointList.size() - 1; j++) {
                        int index = 0;
                        boolean fitsr=false;
                        if (mControlPointList.get(j + 1) > mControlPointList.get(j)) {
                            index = (mControlPointList.get(j + 1) - mControlPointList.get(j));
                        } else if (mControlPointList.get(j + 1) <mControlPointList.get(j)) {
                            //mControlPointList.get(j+1)>mControlPointList.get(j)
                            index = (mControlPointList.get(j)-mControlPointList.get(j+1) );
                        }

                        if(mControlPointList.get(j)<mControlPointList.get(j+1)&&index>1){
                            index=mControlPointList.get(j);


                            System.out.println("BUG 再次进入这里最大的值为"+index);
                            while(index<mControlPointList.get(j+1)){
                                index++;

                                // mControlPointList.set(j,index);
                                if(index!=mControlPointList.get(j)&&index!=mControlPointList.get(j+1)){

                                    Y_shuzu[index] += value;

//                                 if ( lastIndex> endIndex) {
                                    value2 = -((Y_shuzu[index] / FrameHeight) * 24) + 24 / 2;

//                                 }else{
//                                     value2 = ((Y_shuzu[index] / FrameHeight) * 24) + 24 / 2;
//                                 }
                                    System.out.println("BUG 最大的值为"+index+"" +
                                            "集合中的值为"+"value="+value+"=="+  Y_shuzu[index]+"==="+value2
                                            +"起始点"+lastIndex+"借宿点"+
                                            endIndex+"value2="+value2+"、他、"+(Y_shuzu[index] / FrameHeight)
                                    );
                                }

                                fitsr=true;
                            }
                        }
                    }
                }


                break;
        }
        if (eq_genterChangeListener != null) {
            eq_genterChangeListener.onProgressChanged(this, (int) (value2 * 10 + 600), CurrentX, true);
        }
        postInvalidate();
        return true;
    }


    public void SetEQData(DataStruct_Output SyncEQ) {
        for (int j = 0; j < Define.MAX_CHEQ; j++) {
            EQ_Filter.EQ[j].freq = SyncEQ.EQ[j].freq;
            EQ_Filter.EQ[j].level = SyncEQ.EQ[j].level;
            EQ_Filter.EQ[j].bw = SyncEQ.EQ[j].bw;
            EQ_Filter.EQ[j].shf_db = SyncEQ.EQ[j].shf_db;
            EQ_Filter.EQ[j].type = SyncEQ.EQ[j].type;
        }

        EQ_Filter.h_freq = SyncEQ.h_freq;
        EQ_Filter.h_filter = SyncEQ.h_filter;
        EQ_Filter.h_level = SyncEQ.h_level;
        EQ_Filter.l_freq = SyncEQ.l_freq;
        EQ_Filter.l_filter = SyncEQ.l_filter;
        EQ_Filter.l_level = SyncEQ.l_level;
        setEQ();
        postInvalidate();
    }


    private float setY_shuzu(float x, float y) {
        float value2 = 0;


        for (int i = 0; i < X_shuzu.length; i++) {

            if (i < (X_shuzu.length - 1)) {
                if (x <= X_shuzu[i + 1] && x > X_shuzu[i]) {
                    Y_shuzu[i] = y;
                    value2 = -((y / FrameHeight) * 24) + 24 / 2;
                    CurrentX = i;

                    if(!mControlPointList.contains(i)){

                        mControlPointList.add(i);
                    }
                    break;

                }
            } else {
                Log.e("TAG", "LinearLayout onTouchEvent 222 移动" + x + "\t当前\t" + X_shuzu[i] + "他喵的" + X_shuzu[i - 1]);
                if (x > X_shuzu[i]) {
                    Y_shuzu[i] = y;
                    value2 = -((y / FrameHeight) * 24) + 24 / 2;
                    CurrentX = i;
                    if(!mControlPointList.contains(i)){
                        mControlPointList.add(i);
                    }
                    break;
                }
            }
            Collections.sort(mControlPointList);
        }
        System.out.println("BUG 423423423423值为"+value2);
        return value2;
    }

    /*
     * 高通滤波器更新
     *
     */
    private void UpdateEQFilter() {
//        UpDataHPF();
//        UpDataLPF();
//        for(int i = 0; i<DataStruct.CurMacMode.EQ.Max_EQ; i++){
//            UpDataPEQ(i);
//        }
        invalidate();
    }


}
