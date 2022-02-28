package com.chs.mt.xf_dap.main;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;

import com.chs.mt.xf_dap.R;
import com.chs.mt.xf_dap.util.Utils;

public class WelcomeActivity extends Activity {
	private String statementShowString="";
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		setContentView(R.layout.chs_welcome);
		

		mContext=this;

//复制assets文件到手机存储中
		try {
			doCopy();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//获取SharedPreferences对象
		Context ctx = this;
		SharedPreferences sp = ctx.getSharedPreferences("SP", MODE_PRIVATE);
		statementShowString = sp.getString("Statement", "SHOW");
		handler.postDelayed(runnable, 800);
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	Handler handler = new Handler();
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
//			if(statementShowString.equals("NULL")){
				Intent intent = new Intent();
				intent.setClass(WelcomeActivity.this, Select_Mcu_twoActivity.class);//MainTURTActivity
				WelcomeActivity.this.startActivity(intent);
//			}else{
//				Intent intent = new Intent();
//				intent.setClass(WelcomeActivity.this, StatementActivity.class);
//				WelcomeActivity.this.startActivity(intent);
//			}
			Timer timer = new Timer();
			timer.schedule(new TimerTask(){
				@Override
				public void run(){
					finish();
				}
			}, 388);
		}
	};
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Timer timer = new Timer();
			timer.schedule(new TimerTask(){
				@Override
				public void run(){
					finish();
				}
			}, 388);
			return false;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}


	private void doCopy() throws IOException {
		File externalDir = this.getExternalFilesDir(null);
		Utils.doCopy(this,"folder",externalDir.getPath());
	}


}
