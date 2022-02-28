package com.chs.mt.xf_dap.tools;
import com.chs.mt.xf_dap.R;



import android.R.color;
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
import android.graphics.SweepGradient;  
import android.graphics.drawable.Drawable;
import android.os.Bundle;  
import android.os.Parcelable;  
import android.util.AttributeSet;  
import android.view.MotionEvent;  
import android.view.View;  
  
@SuppressLint("ClickableViewAccessibility") public class MCL_SeekBar extends View {
	
	//private boolean DEBUG = true false ;
	private boolean DEBUG = false;
	private OnMCLSeekBarChangeListener mOnMCLSeekBarChangeListener;  
    //����״̬    
    private static final String STATE_PARENT = "parent";  
    private static final String STATE_ANGLE = "angle";  
  
    //Բ��paint����   
    private Paint mColorOutsideWheelPaint;//�⻷  
    private Paint mColorWheelPaint;       //seekbar����
    private Paint mColorWheelProgresPaint;//������seekbar
    private Paint mColorInsideCirclePaint;//��Բ
    private Paint mColorUnitPaint;//��λ
    private Paint mColorProgressPaint;//chs_progress
    //Բ���Ŀ��  
    private int mColorWheelStrokeWidth = 10;  
    private RectF mColorWheelRectangle = new RectF();  
    
    //private float mTranslationOffset;  
  
    //thumb
    private Drawable mThumbDrawable = null;
    @SuppressWarnings("unused")
	private int mThumbHeight = 0;
    private int mThumbWidth = 0;
    private int[] mThumbNormal = null;
    private int[] mThumbPressed = null;
    @SuppressWarnings("unused")
	private float mThumbLeft = 0;
    @SuppressWarnings("unused")
	private float mThumbTop = 0;
    private float[] Out_pointerPosition;
    private float[] In_pointerPosition;
    private float[] thumb_pointerPosition;
    private int mSeekBarCenterX = 0;
    private int mSeekBarCenterY = 0;
    @SuppressWarnings("unused")
	private float mSeekBarSize = 0;
    private int mSeekBaroutsideRadius = 0;//�⻷�뾶
    @SuppressWarnings("unused")
	private int mSeekBarThumbRadius = 0;//�ڻ��뾶
    private int mSeekBarRadius = 0;//seekbar�뾶
    private int mlinelong = 0;//seekbar chs_progress �ϵ��ߵĳ���
    private int mthumbRadius = 0;//thumb�뾶
    //private int mSeekBarInsideCircleRadius = 0;//seekbar��Բ�뾶
    private static float mSeekBarDegreeThreshold = 2;
    
    private boolean bool_Max=false;//�����������߽�
    private boolean bool_Min=false;//����������С�߽�
    
    private boolean bool_DrawDir=false;//��������true:����
    private boolean bool_Drawing=false;//true:���ڻ���
    private float mCurDrawDegree = 0;//��ǰʵ�ʵ���ָ���ڵĽǶ�
    private float mOldDrawDegree = 0;//��ǰʵ�ʵ���ָ���ڵĽǶ�
    //private float mCanDrawDegreeRight = 0;//�����һ��еĽǶ�
    private float mCanDrawDegreeLeft = 0;//�������еĽǶ�
    private float mSeekBarDegree = 0;//���л���seekbar chs_progress�̶ȵĽǶ�
    private float mSeekBarStartDegree = 0;//��ʼ�Ƕ�
    private float mSeekBarProgressStartDegree = 0;//��ʼ�Ƕ�
    //private float mSeekBarStopDegree = 0;//�����Ƕ�
    @SuppressWarnings("unused")
	private float mSeekBarMax360 = 0;//��ʼ�Ƕ�
    
    private float mSeekBarMaxDegree = 0;
    private int mCurrentProgress = 0;
    private int mSeekBarMax = 100;  
    private float mDegree = (float) 4.5;  
    @SuppressWarnings("unused")
	private Bitmap bitmapOrg,resizedBitmap ;
    @SuppressWarnings("unused")
	private int thumbwidth = 0;
    @SuppressWarnings("unused")
	private int thumbheight = 0;
    @SuppressWarnings("unused")
	private Matrix matrix;
    //private boolean DEBUG=false;
    private float seekbar_outside_width = 0;
    //��ɫ
    private int seekbar_outside_color;
    private int seekbar_bg_color;
    @SuppressWarnings("unused")
	private int seekbar_inside_color;
    private int seekbar_progress_bg_color;
    
    
    /*
     * 1:��һ����ɫ
     * 2:��һ���趨�Ľ���ɫ
     */
    private int seekbar_progress_color_style=0;
    
    private int seekbar_progress_text_color;
    private int seekbar_progress_unit_color;
    private int seekbar_progress_start_color;
    @SuppressWarnings("unused")
	private int seekbar_progress_mid_color;
    private int seekbar_progress_end_color;
    @SuppressWarnings("unused")
	private int color_default;
    //��ʾ���嵥λ�ʹ�С
    @SuppressWarnings("unused")
	private String progress_unit_text="dB";
    private int progress_textsize=0;
    private int progress_unit_textsize=0; 
    @SuppressWarnings("unused")
	private String text_seekbar_progress="0";  
    
    private int conversion = 0;  
    
    private SweepGradient s;  
    // ���½ǿ�ʼ  
    private int start_arc = 135;  
    private int end_wheel;  
    
    

    
    
    private Context mContext = null;
  
    public MCL_SeekBar(Context context) {      	
        super(context);  
        mContext = context;
        init(null, 0);  
    }  
  
    public MCL_SeekBar(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        mContext = context;
        init(attrs, 0);  
    }  
  
    public MCL_SeekBar(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        mContext = context;
        init(attrs, defStyle);  
    }  
  
    private void init(AttributeSet attrs, int defStyle) {  
        final TypedArray a = getContext().obtainStyledAttributes(attrs,  
                R.styleable.MCL_SeekBar, defStyle, 0);  
  
        initAttributes(a);  
  
        a.recycle();  
        // mAngle = (float) (-Math.PI / 2);  
        //�⻷
        mColorOutsideWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mColorOutsideWheelPaint.setShader(s);  
        mColorOutsideWheelPaint.setColor(seekbar_outside_color);  
        mColorOutsideWheelPaint.setStyle(Paint.Style.STROKE);  
        mColorOutsideWheelPaint.setStrokeWidth(seekbar_outside_width);  
        //seekbar ����
        mColorWheelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mColorWheelPaint.setShader(s);  
        mColorWheelPaint.setColor(seekbar_bg_color);  
        //mColorWheelPaint.setStyle(Paint.Style.STROKE);  
        mColorWheelPaint.setStrokeWidth(mColorWheelStrokeWidth); 
        //seekbar Progress        
        mColorWheelProgresPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mColorWheelProgresPaint.setShader(s);  
        mColorWheelProgresPaint.setColor(seekbar_progress_bg_color);  
        mColorWheelProgresPaint.setStyle(Paint.Style.STROKE);  
        mColorWheelProgresPaint.setStrokeWidth(mColorWheelStrokeWidth); 
        //��Բ
        mColorInsideCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mColorInsideCirclePaint.setShader(s);  
        mColorInsideCirclePaint.setColor(seekbar_progress_text_color); 
        //Text ��λ
        mColorUnitPaint = new Paint();  
        mColorUnitPaint.setColor(seekbar_progress_unit_color);  
        mColorUnitPaint.setTextSize(progress_unit_textsize);  
        //Text chs_progress
        mColorProgressPaint = new Paint();  
        mColorProgressPaint.setColor(seekbar_progress_text_color);  
        mColorProgressPaint.setTextSize(progress_textsize); 
        
        bitmapOrg = BitmapFactory.decodeResource(getResources(), R.drawable.chs_thrmb_nornal);//thrmb_nornal mcl_thumb
    	thumbwidth = bitmapOrg.getWidth();
    	thumbheight = bitmapOrg.getHeight();
    	
        invalidate();  
    }  
  
    private void initAttributes(TypedArray a) {  
  
    	mSeekBarMax = a.getInteger(R.styleable.MCL_SeekBar_mcl_max, 100);  
    	
        //seekbar����ʼ�ǶȺͽ����Ƕ�
        start_arc = a.getInteger(R.styleable.MCL_SeekBar_mcl_start_angle, 0);
        //start_arc += 5;
        end_wheel = a.getInteger(R.styleable.MCL_SeekBar_mcl_end_angle, 360);  
        mSeekBarMaxDegree = end_wheel-start_arc;
        if(mSeekBarMaxDegree<0){
        	mSeekBarMaxDegree+=360;
        }
        
        if(mSeekBarMax == 46){
        	mDegree= (float) 5.8;
    	}else{    		
    		mDegree= mSeekBarMaxDegree/mSeekBarMax;
    	}
        mSeekBarDegree = (mCurrentProgress * mSeekBarMaxDegree / mSeekBarMax);

        //���δ�С����
        mColorWheelStrokeWidth = (int) a.getDimension(R.styleable.MCL_SeekBar_mcl_seekbar_bg_width, 6);
        //��ɫ����
        seekbar_outside_color = a.getColor(R.styleable.MCL_SeekBar_mcl_seekbar_outside_color, Color.BLUE);
        seekbar_bg_color = a.getColor(R.styleable.MCL_SeekBar_mcl_seekbar_bg_color,Color.GRAY);
        seekbar_inside_color = a.getColor(R.styleable.MCL_SeekBar_mcl_seekbar_inside_color,Color.WHITE);
        seekbar_progress_bg_color = a.getColor(R.styleable.MCL_SeekBar_mcl_seekbar_progress_bg_color,Color.GREEN);
        seekbar_progress_color_style = a.getInteger(R.styleable.MCL_SeekBar_mcl_seekbar_progress_color_style, 1);
        seekbar_progress_text_color = a.getColor(R.styleable.MCL_SeekBar_mcl_seekbar_progress_text_color,Color.YELLOW);
        seekbar_progress_unit_color = a.getColor(R.styleable.MCL_SeekBar_mcl_seekbar_progress_unit_color,Color.BLACK);
        
        seekbar_progress_start_color = a.getColor(R.styleable.MCL_SeekBar_mcl_seekbar_progress_start_color,Color.YELLOW);
        seekbar_progress_mid_color = a.getColor(R.styleable.MCL_SeekBar_mcl_seekbar_progress_mid_color,Color.GREEN);
        seekbar_progress_end_color = a.getColor(R.styleable.MCL_SeekBar_mcl_seekbar_progress_end_color,Color.BLACK);
        color_default = color.darker_gray;
        
        
        /*thumb*/
        mThumbDrawable = a.getDrawable(R.styleable.MCL_SeekBar_android_thumb);
        mThumbWidth = this.mThumbDrawable.getIntrinsicWidth();
        mThumbHeight = this.mThumbDrawable.getIntrinsicHeight();
        
        mThumbNormal = new int[]{-android.R.attr.state_focused, -android.R.attr.state_pressed, 
                -android.R.attr.state_selected, -android.R.attr.state_checked};
        mThumbPressed = new int[]{android.R.attr.state_focused, android.R.attr.state_pressed, 
                android.R.attr.state_selected, android.R.attr.state_checked};
        seekbar_outside_width=mThumbWidth/2;
        //��ʾ���嵥λ�ʹ�С
        progress_unit_text=a.getString(R.styleable.MCL_SeekBar_mcl_seekbar_progress_unit_text);  
        progress_textsize=a.getInteger(R.styleable.MCL_SeekBar_mcl_seekbar_progress_textsize, 100);  
        progress_unit_textsize=a.getInteger(R.styleable.MCL_SeekBar_mcl_seekbar_progress_unit_textsize, 50);  
        text_seekbar_progress=String.valueOf( mCurrentProgress);
        
        mlinelong=(int) a.getDimension(R.styleable.MCL_SeekBar_mcl_seekbar_progress_linelong, 30);  
        mthumbRadius=(int) a.getDimension(R.styleable.MCL_SeekBar_mcl_seekbar_progress_thumb_r, 10);  

    }  
  
    @Override  
    protected void onDraw(Canvas canvas) {      	
        //�⻷
        //canvas.drawCircle(mSeekBarCenterX, mSeekBarCenterY, mSeekBaroutsideRadius, mColorOutsideWheelPaint);
        //��Բ
        //canvas.drawCircle(mSeekBarCenterX, mSeekBarCenterY, mSeekBarInsideCircleRadius, mColorInsideCirclePaint);
        //seekbar����
        //canvas.drawCircle(mSeekBarCenterX, mSeekBarCenterY, mSeekBarRadius, mColorWheelPaint);   	
        //canvas.translate(0, 0);  
        //seekbar Progress
        //canvas.drawArc(mColorWheelRectangle, start_arc, mSeekBarDegree, false, mColorWheelProgresPaint);  
        //seekbar ��λ���ַ�
        //canvas.drawText(progress_unit_text, mSeekBarCenterX-progress_unit_textsize*3/2, 
        //		mSeekBarCenterY+progress_textsize*3/4,mColorUnitPaint); 
        //seekbar chs_progress���ַ�
    	/*
        int text_seekbar_progress_x=0;
        switch(text_seekbar_progress.length()){
	        case 1:
	        	text_seekbar_progress_x=progress_textsize*3/11;
	        	break;
	        case 2:
	        	text_seekbar_progress_x=progress_textsize*3/6;
	        	break;
	        case 3:
	        	text_seekbar_progress_x = progress_textsize;
	        	break;
        	default: 
        		text_seekbar_progress_x = progress_textsize;
        		break;
        }
        canvas.drawText(text_seekbar_progress, mSeekBarCenterX-text_seekbar_progress_x,
        		mSeekBarCenterY+progress_unit_textsize,mColorProgressPaint); 
        */
        //seekbar
        //canvas.save();
        canvas.translate(mSeekBarCenterX, mSeekBarCenterY);  
        drawSeekbar_bg(canvas);
        //drawSeekbar_progress(canvas);
        drawThumbBitmap(canvas);
    	//canvas.restore();
    	//thumb
    	//canvas.translate(0, 0); 
        //drawThumbBitmap(canvas);
    }
    
    private void drawSeekbar_bg(Canvas canvas){
    	float angle=315;//���ߵĽǶ�    
    	//start_color="#FFf00000"
    	//end_color=  "#FF0FFF80" 
    	int tempc=0;
    	int BaseColor=seekbar_progress_start_color;//0xFF0FF080;
    	int EndColor=seekbar_progress_end_color;//0xFF0FF080;
    	int r = 0,g = 0,b = 0;
    	
    	if(mSeekBarMax == 46){
    		angle=312;
    	}
    	
    	if(seekbar_progress_color_style>=1000){
	    	if(mCurrentProgress != 0){
	    		//r=0xf0/mCurrentProgress;
		    	//g=0xff/mCurrentProgress;
		    	//b=0x80/mCurrentProgress;
		    	//R�ķ���
	    		if((seekbar_progress_color_style/100)%10==0){//�ݼ�
	    			r=(((BaseColor&0x00ff0000)>>16)-((EndColor&0x00ff0000)>>16))/mCurrentProgress;
	    			r=-r;
	    		}else if((seekbar_progress_color_style/100)%10==1){//����
	    			r=(((EndColor&0x00ff0000)>>16)-((BaseColor&0x00ff0000)>>16))/mCurrentProgress;
	    		}else{
	    			seekbar_progress_color_style=1;
	    		}
	    		//G�ķ���
	    		if((seekbar_progress_color_style/10)%10==0){//�ݼ�
	    			g=(((BaseColor&0x0000ff00)>>8)-((EndColor&0x0000ff00)>>8))/mCurrentProgress;
	    			g=-g;
	    		}else if((seekbar_progress_color_style/10)%10==1){//����
	    			g=(((EndColor&0x0000ff00)>>8)-((BaseColor&0x0000ff00)>>8))/mCurrentProgress;
	    		}else{
	    			seekbar_progress_color_style=1;
	    		}
	    		//B�ķ���
	    		if(seekbar_progress_color_style%10==0){//�ݼ�
	    			b=((BaseColor&0x0000ff)-(EndColor&0x0000ff))/mCurrentProgress;
	    			b=-b;
	    		}else if(seekbar_progress_color_style%10==1){//����
	    			b=((EndColor&0x0000ff)-(BaseColor&0x0000ff))/mCurrentProgress;
	    		}else{
	    			seekbar_progress_color_style=1;
	    		}
		    	//r=(256-((BaseColor&0x00ff0000)>>16))/mCurrentProgress;
		    	//g=((BaseColor&0x0000ff00)>>8)/mCurrentProgress;
		    	//b=(BaseColor&0x000000ff)/mCurrentProgress;
	    	}
    	}
    	
    	//draw seekbar background
    	for(int i=0;i<=mSeekBarMax;i++){
    		Out_pointerPosition=calculatePointerPosition(angle, mSeekBaroutsideRadius);
        	In_pointerPosition=calculatePointerPosition(angle, mSeekBarRadius);
        	angle-=mDegree;

        	canvas.drawLine(In_pointerPosition[0], In_pointerPosition[1],
        		Out_pointerPosition[0], Out_pointerPosition[1], mColorWheelPaint );

        	if(i<=mCurrentProgress){
        		if((seekbar_progress_color_style>=1000)&&(seekbar_progress_color_style<2000)){
            		tempc = BaseColor+((i*r)<<16)+((i*g)<<8)+i*b;
                	mColorWheelProgresPaint.setColor(tempc);  
                	if((mCurrentProgress==1)&&(i==1)){
                		tempc = EndColor;
                		mColorWheelProgresPaint.setColor(tempc);  
                	}
        		}else if(seekbar_progress_color_style==1){
        			mColorWheelProgresPaint.setColor(seekbar_progress_bg_color); 
        		}

        		
        		
        		canvas.drawLine(In_pointerPosition[0], In_pointerPosition[1],
            			Out_pointerPosition[0], Out_pointerPosition[1], mColorWheelProgresPaint );
        	}
        	
    	}	
    }
    /*
    private void drawSeekbar_progress(Canvas canvas){
    	float angle = (float) 319.5;
    	int tempc=0;
    	int BaseColor=0xFF0FF080;
    	int r = 0,g = 0,b = 0;
    	if(mCurrentProgress != 0){
	    	r=(256-((BaseColor&0x00ff0000)>>16))/mCurrentProgress;
	    	g=((BaseColor&0x0000ff00)>>8)/mCurrentProgress;
	    	b=(BaseColor&0x000000ff)/mCurrentProgress;
    	}
    	
    	for(int i=0;i<=mCurrentProgress;i++){
    		angle-=mDegree;
    		Out_pointerPosition=calculatePointerPosition(angle, mSeekBaroutsideRadius);
        	In_pointerPosition=calculatePointerPosition(angle, mSeekBarRadius);
        	tempc = BaseColor+((i*r)<<16)-((i*g)<<8)-i*b;
        	mColorWheelProgresPaint.setColor(tempc);  
        	if((mCurrentProgress==1)&&(i==1)){
        		tempc = 0xFFFF0000;
        		mColorWheelProgresPaint.setColor(tempc);  
        	}
    		canvas.drawLine(In_pointerPosition[0], In_pointerPosition[1],
        			Out_pointerPosition[0], Out_pointerPosition[1], mColorWheelProgresPaint );
    	}
    }
    */
    //����Ƕȣ��뾶��Բ������
    private float[] calculatePointerPosition(float angle,float Radius ) {
    	float x, y;
    	x=(float) Math.sin(2*Math.PI / 360*angle)*Radius;
    	y=(float) Math.cos(2*Math.PI / 360*angle)*Radius;
 
        //System.out.println("angle x:"+x+",y:"+y);
        return new float[] { x, y };  
    }  
    //��thumb
    private void drawThumbBitmap(Canvas canvas) {
    	thumb_pointerPosition=calculatePointerPosition(45+(mSeekBarMax-mCurrentProgress)*mDegree, mSeekBaroutsideRadius+mthumbRadius);
    	canvas.drawCircle(thumb_pointerPosition[0], thumb_pointerPosition[1], mthumbRadius, mColorInsideCirclePaint);
    	
//    	float angle = 0;
//    	//��������ͼƬ�õ�matrix����
//    	matrix = new Matrix();
//    	//��תͼƬ����
//    	if(mCurrentProgress<=mSeekBarMax/2){
//    		angle = (float) (225+mCurrentProgress*mDegree);
//    	}else {
//    	    angle = (float) ((float) (mCurrentProgress - mSeekBarMax/2)*mDegree);
//    	}
//    	//matrix.postRotate(angle);
//    	resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, thumbwidth, thumbheight, matrix, true);
//    	canvas.drawBitmap(resizedBitmap, mThumbLeft, mThumbTop, null);

    }
    /*
    private void setThumbPosition(double radian) {
        //if(DEBUG) Log.v(TAG, "setThumbPosition radian = " + radian);
        double x = mSeekBarCenterX + mSeekBarThumbRadius * Math.cos(radian);
        double y = mSeekBarCenterY + mSeekBarThumbRadius * Math.sin(radian);
        mThumbLeft = (float) (x - mThumbWidth / 2);
        mThumbTop = (float) (y - mThumbHeight / 2);
    }
    */
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
    	super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);  
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);  
        int min = Math.min(width, height);  
        setMeasuredDimension(min, min);  

        mSeekBarCenterX = min / 2;
        mSeekBarCenterY = min / 2;   
        mSeekBarSize = min;
        //��Բ�İ뾶
        mSeekBaroutsideRadius = mSeekBarCenterX - mthumbRadius*2;
        mSeekBarRadius = mSeekBaroutsideRadius-mlinelong;
        //mSeekBarRadius = mSeekBarCenterX-mSeekBarCenterX/5;
        //mSeekBaroutsideRadius = mSeekBarCenterX - mthumbRadius;//(int) (mSeekBarSize / 2 - mThumbHeight*3/4);
        //mSeekBarRadius = mSeekBaroutsideRadius - mThumbHeight/2;
        //mSeekBarInsideCircleRadius=mSeekBarRadius-mColorWheelStrokeWidth/2;
        //thumbλ��
        //setThumbPosition(Math.toRadians(mSeekBarDegree+start_arc));
        //seekbar progressλ��
        mColorWheelRectangle.set(mSeekBarCenterX-mSeekBarRadius, mSeekBarCenterX-mSeekBarRadius, 
        		mSeekBarCenterX+mSeekBarRadius, mSeekBarCenterX+mSeekBarRadius);         
    }  
  
    public int getValue() {  
        return conversion;  
    }  
    
    private void SeekTo(){
    	float drawDegree = 0;

    	
 		if(bool_DrawDir==true){//���򣬱��
 			bool_Min=false;
 			
 			if(mOldDrawDegree>mCurDrawDegree){
 				if( DEBUG) System.out.println("MCL +mOldDrawDegree:"+mOldDrawDegree);
 				if( DEBUG) System.out.println("MCL +mCurDrawDegree:"+mCurDrawDegree);
 				if((360 >= mOldDrawDegree)&&(mOldDrawDegree>=225)&&(135 >= mCurDrawDegree)&&(mCurDrawDegree>=0)){
 					//���ٽ���й������򲻱�
 					bool_DrawDir=true;
 			
 				}else{
	 				bool_DrawDir=false;
	 				
		            mSeekBarStartDegree = mOldDrawDegree;
		            mSeekBarProgressStartDegree = mCurrentProgress*mDegree;
		            mCanDrawDegreeLeft = mCurrentProgress*mDegree;
		            mSeekBarMax360 = mSeekBarStartDegree+mSeekBarMaxDegree-360;
		            
		            if( DEBUG) System.out.println("MCL +bool_DrawDir:"+bool_DrawDir);
		            
	 				return;
 				}
 			}
 			
			//�������߽�
 			if(bool_Max==true){
 				mOldDrawDegree=mCurDrawDegree;
 				return;
 			}
 			if(mCurDrawDegree>=mSeekBarStartDegree){
 				drawDegree = mCurDrawDegree-mSeekBarStartDegree+mSeekBarDegreeThreshold;
 			}else if(mCurDrawDegree < mSeekBarStartDegree){
 				drawDegree = mCurDrawDegree+(360-mSeekBarStartDegree)+mSeekBarDegreeThreshold;
 			}
			
			//���ڼ���mCurrentProgress
			mSeekBarDegree = mSeekBarProgressStartDegree+drawDegree;
			
			if(DEBUG)System.out.println("MCL +mSeekBarDegree:"+mSeekBarDegree);
			if(DEBUG)System.out.println("MCL +mCurrentProgress:"+mCurrentProgress);
			//System.out.println("MCL +mSeekBarProgressStartDegree:"+mSeekBarProgressStartDegree);
			
			if((mSeekBarDegree>mSeekBarMaxDegree)||(0 > mSeekBarDegree)){
				mSeekBarDegree = mSeekBarMaxDegree;
				
				bool_Max=true; 				
			}
			if((mSeekBarDegree >= 0) && (mSeekBarDegree <= mSeekBarMaxDegree)){//�����Ĵ��Բ
	        	mCurrentProgress = (int) (mSeekBarMax * mSeekBarDegree / mSeekBarMaxDegree);
	        }
			//�����������з��򻬶�
			mOldDrawDegree=mCurDrawDegree;
			
			if( DEBUG) System.out.println("MCL +++++++++");
		}else if(bool_DrawDir==false){//���򣬱�С	
			bool_Max=false;
			
			if(mOldDrawDegree<mCurDrawDegree){
				if(DEBUG) System.out.println("MCL -mOldDrawDegree:"+mOldDrawDegree);
 				if(DEBUG) System.out.println("MCL -mCurDrawDegree:"+mCurDrawDegree);
				if((360 >= mCurDrawDegree)&&(mCurDrawDegree>=225)&&(135 >= mOldDrawDegree)&&(mOldDrawDegree>=0)){
 					//���ٽ���й������򲻱�
 					bool_DrawDir=false;
 	
 				}else{
	 				bool_DrawDir=true;
	 				
		            mSeekBarStartDegree = mOldDrawDegree;
		            mSeekBarProgressStartDegree = mCurrentProgress*mDegree;
		            mCanDrawDegreeLeft = mCurrentProgress*mDegree;
		            mSeekBarMax360 = mSeekBarStartDegree+mSeekBarMaxDegree-360;

		            if(DEBUG) System.out.println("MCL -bool_DrawDir:"+bool_DrawDir);
		            
	 				return;
 				}
 			}
			
			//������С�߽�
 			if(bool_Min==true){
 				mOldDrawDegree=mCurDrawDegree;
 				return;
 			}
 			if(mCurrentProgress==0){
 				bool_Min=true;
 				mOldDrawDegree=mCurDrawDegree;
 				return;
 			}
 			if(mSeekBarStartDegree>=mCurDrawDegree){
 				drawDegree = mSeekBarStartDegree - mCurDrawDegree - mSeekBarDegreeThreshold;
 			}else if(mSeekBarStartDegree<mCurDrawDegree){
 				drawDegree = mSeekBarStartDegree+(360- mCurDrawDegree) - mSeekBarDegreeThreshold;
 			}
			
 			if(DEBUG) System.out.println("MCL -drawDegree:"+drawDegree);
			if(drawDegree>=mCanDrawDegreeLeft){
				bool_Min=true;
				mSeekBarDegree = 0;
				//System.out.println("MCL X");
			}else{
				mSeekBarDegree = mSeekBarProgressStartDegree-drawDegree;
				if(mSeekBarDegree>360){
					mSeekBarDegree = mSeekBarDegree-360;
				}
			}
			//�����ڷ��������򻬶�
			mOldDrawDegree=mCurDrawDegree;
			
			if((mSeekBarDegree >= 0) && (mSeekBarDegree <= mSeekBarMaxDegree)){//�����Ĵ��Բ
	        	mCurrentProgress = (int) (mSeekBarMax * mSeekBarDegree / mSeekBarMaxDegree);
	        }
			
			if(DEBUG) System.out.println("MCL -------------------------------------------");
			//System.out.println("MCL -mCanDrawDegreeLeft:"+mCanDrawDegreeLeft);
			if(DEBUG) System.out.println("MCL -mSeekBarMaxDegree:"+mSeekBarMaxDegree);
			if(DEBUG) System.out.println("MCL -mSeekBarDegree:"+mSeekBarDegree);
			if(DEBUG) System.out.println("MCL -mCurrentProgress:"+mCurrentProgress);
			//System.out.println("MCL -mSeekBarProgressStartDegree:"+mSeekBarProgressStartDegree);
			
			if(DEBUG) System.out.println("MCL ---------");
		}
        //setThumbPosition(Math.toRadians(mSeekBarDegree+start_arc));
        //text_seekbar_progress=String.valueOf(mCurrentProgress);
        if (mOnMCLSeekBarChangeListener != null){  
            mOnMCLSeekBarChangeListener.onProgressChanged(this,mCurrentProgress, true); 
        }
        
        invalidate(); 
        
         
    }
    @Override  
    public boolean onTouchEvent(MotionEvent event) {  
        float x = event.getX();  
        float y = event.getY();  
        //System.out.println("onTouchEvent x:"+x+",y:"+y);
        switch (event.getAction()) {  
	        case MotionEvent.ACTION_DOWN:  
	        	mThumbDrawable.setState(mThumbPressed);	        	                  
	            mSeekBarStartDegree = GetDegree(x,y);
	            mSeekBarProgressStartDegree = mCurrentProgress*mDegree;
	            mCanDrawDegreeLeft = mCurrentProgress*mDegree;
	            mSeekBarMax360 = mSeekBarStartDegree+mSeekBarMaxDegree-360;
	            mOldDrawDegree=mSeekBarStartDegree;
	            bool_Drawing = false;
	            if(DEBUG)System.out.println("MCL d mSeekBarStartDegree:"+mSeekBarStartDegree);
	            if(DEBUG)System.out.println("MCL d mCurDrawDegree:"+mCurDrawDegree);
	            if(DEBUG) System.out.println("MCL ACTION_DOWN");
	            break;  
	        case MotionEvent.ACTION_MOVE:  
	        	mThumbDrawable.setState(mThumbPressed);	        	       
	        	mCurDrawDegree = GetDegree(x,y);
	        	if(DEBUG)System.out.println("MCL mCurDrawDegree:"+mCurDrawDegree);
	        	if(bool_Drawing==false){	
	        		bool_Drawing=true;
	        		if(mCurDrawDegree>=mSeekBarStartDegree+mSeekBarDegreeThreshold){
	        			if((360 >= mCurDrawDegree)&&(mCurDrawDegree>=225)&&
	        					(135 >= mSeekBarStartDegree+mSeekBarDegreeThreshold)
	        					&&(mSeekBarStartDegree+mSeekBarDegreeThreshold>=0)){
		        			bool_DrawDir=false;//���򣬱�С    
		        		}else{
		        			bool_DrawDir=true;//���򣬱��
		        		}
	        			
		        	}else if(mCurDrawDegree<mSeekBarStartDegree-mSeekBarDegreeThreshold){
		        		if((360 >= mSeekBarStartDegree-mSeekBarDegreeThreshold)&&
		        				(mSeekBarStartDegree-mSeekBarDegreeThreshold>=225)
		        				&&(135 >= mCurDrawDegree)&&(mCurDrawDegree>=0)){
	        				bool_DrawDir=true;//���򣬱��
	        			}else{
	        				bool_DrawDir=false;//���򣬱�С     	
	        			}
		        		
			        }
	        		
	        		if(DEBUG)System.out.println("MCL MOVE bool_DrawDir:"+bool_DrawDir);
	        	}else if(bool_Drawing==true){
	        		SeekTo();
	        	}
	            break;  
	        case MotionEvent.ACTION_UP:  
	        	bool_Drawing=false;
	        	mThumbDrawable.setState(mThumbNormal);
	            invalidate();  
	            break;  
	        default: 
	        	bool_Drawing=false;
	        	mThumbDrawable.setState(mThumbNormal);
	        	invalidate();  
	        	break;
        }  
        if (event.getAction() == MotionEvent.ACTION_MOVE && getParent() != null) {  
            getParent().requestDisallowInterceptTouchEvent(true);  
        }  
        return true;  
    }  
    private float GetDegree(float x,float y){
    	float Degree=0;
    	double radian = Math.atan2(y - mSeekBarCenterY, x - mSeekBarCenterX);
        if (radian < 0){
             radian = radian + 2*Math.PI;
        }  
        Degree = Math.round(Math.toDegrees(radian));
        return Degree;
    }
    @Override  
    protected Parcelable onSaveInstanceState() {  
        Parcelable superState = super.onSaveInstanceState();  
  
        Bundle state = new Bundle();  
        state.putParcelable(STATE_PARENT, superState);  
        state.putFloat(STATE_ANGLE, mCurrentProgress);  
  
        return state;  
    }  
  
    @Override  
    protected void onRestoreInstanceState(Parcelable state) {  
        Bundle savedState = (Bundle) state;  
  
        Parcelable superState = savedState.getParcelable(STATE_PARENT);  
        super.onRestoreInstanceState(superState);  
  
        mCurrentProgress = (int) savedState.getFloat(STATE_ANGLE);  
        mSeekBarDegree = (mCurrentProgress * mSeekBarMaxDegree / mSeekBarMax);
//        if(mCurrentProgress==mSeekBarMax){
//        	text_seekbar_progress=String.valueOf(mSeekBarMax-mCurrentProgress);
//        }else{
//        	text_seekbar_progress=String.valueOf(0-(mSeekBarMax-mCurrentProgress));
//        }
        text_seekbar_progress=String.valueOf(mCurrentProgress);
        //setThumbPosition(Math.toRadians(mSeekBarDegree+start_arc));
  
    }  
  
    public void setOnMCLSeekBarChangeListener(OnMCLSeekBarChangeListener l) {  
        mOnMCLSeekBarChangeListener = l;  
    }  
  
    public interface OnMCLSeekBarChangeListener {  
  
        public abstract void onProgressChanged(MCL_SeekBar mcSeekBar,  
                int progress, boolean fromUser);  
  
    }  
    
    public void setProgressThumb(int thumbId){
        mThumbDrawable = mContext.getResources().getDrawable(thumbId);
    }
    
    public void setProgressMax(int max){
        mSeekBarMax = max;
        if(mSeekBarMax == 46){
        	mDegree= (float) 5.8;
    	}else{    		
    		mDegree= mSeekBarMaxDegree/mSeekBarMax;
    	}
    }
    
    /*
     * ����set������������java�����е���
     */
    public void setProgress(int progress) {
        if (progress > mSeekBarMax){
            progress = mSeekBarMax;
        }
        if (progress < 0){
            progress = 0;
        }
        mCurrentProgress = progress;
        //if (mOnMCLSeekBarChangeListener != null){  
        //    mOnMCLSeekBarChangeListener.onProgressChanged(this,mCurrentProgress, true); 
        //}
       // mSeekBarDegree = (chs_progress * mSeekBarMaxDegree / mSeekBarMax);
//        if(mCurrentProgress==mSeekBarMax){
//        	text_seekbar_progress=String.valueOf(mSeekBarMax-mCurrentProgress);
//        }else{
//        	text_seekbar_progress=String.valueOf(0-(mSeekBarMax-mCurrentProgress));
//        }
        //text_seekbar_progress=String.valueOf(mCurrentProgress);
        //setThumbPosition(Math.toRadians(mSeekBarDegree+start_arc));
        //System.out.println("MCL setProgress:"+mCurrentProgress);
        invalidate();
    }
    
    /*
     * ����setCanTouch������������java�����е���
     */
    public void setTouch(boolean touch){
    	//CanTouch=touch;
    }
    
    /*
     * ����setCanTouch������������java�����е���
     */
    public int getProgress(){
    	return mCurrentProgress;
    }
}  