package com.chs.mt.xf_dap.fragment.dialogFragment;
import com.chs.mt.xf_dap.R;

import com.chs.mt.xf_dap.datastruct.DataStruct;
import com.chs.mt.xf_dap.datastruct.MacCfg;
import com.chs.mt.xf_dap.operation.DataOptUtil;
import com.chs.mt.xf_dap.tools.LongCickButton;
import com.chs.mt.xf_dap.tools.LongCickButton.LongTouchListener;
import com.chs.mt.xf_dap.tools.MHS_Num_SeekBar;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class SetDelayDialogFragment extends DialogFragment {

	private LongCickButton SetDelayDialogB_SUB,SetDelayDialogB_INC;
	private Button SetDelayDialogButton;
	private MHS_Num_SeekBar SetDelayDialogSeekBar;
//    private KnobViewPD sb_val;
    private TextView TV_Val,TV_Ch;
	private int data = 0;
	public static final String ST_Data = "Data";
	private int DelayUnit = DataUNIT_MS;
	public static final String ST_DelayUnit = "ST_DelayUnit";
	/*延时单位切换，1：CM，2：MS，3：Inch*/	
	public static final int DataUNIT_CM   = 1;
	public static final int DataUNIT_MS   = 2;
	public static final int DataUNIT_INCH = 3;
	
 	private OnDelayDialogFragmentChangeListener mDelayListener;
    public void OnSetDelayDialogFragmentChangeListener(OnDelayDialogFragmentChangeListener listener) {
        this.mDelayListener = listener;
    }

    public interface OnDelayDialogFragmentChangeListener{
        void onDelayVolChangeListener(int delay, int type, boolean flag);
    }

	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
        Bundle savedInstanceState) {  
		data = getArguments().getInt(ST_Data);
		DelayUnit = getArguments().getInt(ST_DelayUnit);
		getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chs_seekbar_dialog_setdelay, container,false);
        initView(view);
		initData();
		initClick();
        return view;  
    }  
	private void initView(View V_SetDelaySeekBarDialog) {
        if(data > DataStruct.CurMacMode.Delay.MAX){
            data = DataStruct.CurMacMode.Delay.MAX;
        }
		SetDelayDialogB_INC = (LongCickButton)V_SetDelaySeekBarDialog.findViewById(R.id.id_b_setdelay_dialog_inc);
    	SetDelayDialogB_SUB = (LongCickButton)V_SetDelaySeekBarDialog.findViewById(R.id.id_b_setdelay_dialog_sub);
    	SetDelayDialogButton = (Button)V_SetDelaySeekBarDialog.findViewById(R.id.id_setdelay_dialog_button);
    	SetDelayDialogSeekBar = (MHS_Num_SeekBar)V_SetDelaySeekBarDialog.findViewById(R.id.id_setdelay_dialog_seekbar);

    	SetDelayDialogSeekBar.setTextUnit("Delay");
    	SetDelayDialogSeekBar.setDelayUnit(DelayUnit);
    	SetDelayDialogSeekBar.setTextColor(Color.WHITE);

		SetDelayDialogSeekBar.setMax(DataStruct.CurMacMode.Delay.MAX);
		SetDelayDialogSeekBar.setProgress(data);

        TV_Val = (TextView) V_SetDelaySeekBarDialog.findViewById(R.id.id_text);
        TV_Ch = (TextView) V_SetDelaySeekBarDialog.findViewById(R.id.id_text_channel);



	}

	private void initData() {
        FlashVal(data);
        TV_Ch.setText(String.valueOf("CH"+(MacCfg.OutputChannelSel+1)));
	}

	private void initClick() {
		SetDelayDialogButton.setOnClickListener(new OnClickListener() {   		
			@Override
			public void onClick(View view) {
				getDialog().cancel();
			}
		});

		SetDelayDialogSeekBar.setOnMHS_NumSeekBarChangeListener(new MHS_Num_SeekBar.OnMHS_NumSeekBarChangeListener() {
			@Override
			public void onProgressChanged(MHS_Num_SeekBar mhs_SeekBar, int progress,
					boolean fromUser) {
				if(mDelayListener != null){
					data = progress;
					mDelayListener.onDelayVolChangeListener(progress, 0, fromUser);
		        }
			}
		});
		/*对话框-选择*/


		SetDelayDialogB_SUB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EQ_INC_SUB(false);
			}
		});
		SetDelayDialogB_SUB.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				SetDelayDialogB_SUB.setStart();
				return false;
			}
		});
		SetDelayDialogB_SUB.setOnLongTouchListener(new LongTouchListener() {
			@Override
			public void onLongTouch() {
				EQ_INC_SUB(false);
			}
		}, MacCfg.LongClickEventTimeMax);
		/////
		SetDelayDialogB_INC.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				EQ_INC_SUB(true);
			}
		});
		SetDelayDialogB_INC.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View arg0) {
				SetDelayDialogB_INC.setStart();
				return false;
			}
		});
		SetDelayDialogB_INC.setOnLongTouchListener(new LongTouchListener() {
			@Override
			public void onLongTouch() {
				EQ_INC_SUB(true);
			}
		}, MacCfg.LongClickEventTimeMax);


	}
	
	private void EQ_INC_SUB(boolean inc){
		if(inc){	//递增
			if(++data > DataStruct.CurMacMode.Delay.MAX){
				data = DataStruct.CurMacMode.Delay.MAX;
			}
			if(mDelayListener != null){
				mDelayListener.onDelayVolChangeListener(data, 0, false);
	        }
		}else{
			if(--data < 0){
				data = 0;
			}
			if(mDelayListener != null){
				mDelayListener.onDelayVolChangeListener(data, 0, false);
	        }
		}
		SetDelayDialogSeekBar.setProgress(data);
        FlashVal(data);
	}

	private void   FlashVal(int data){

        switch(DelayUnit){
            case 1:
            	TV_Val.setText(String.valueOf(DataOptUtil.CountDelayCM(data)));
                break;
            case 2:TV_Val.setText(String.valueOf(DataOptUtil.CountDelayMs(data)));
                break;
            case 3:TV_Val.setText(String.valueOf(DataOptUtil.CountDelayInch(data)));
                break;
            default: break;
        }
    }


}
