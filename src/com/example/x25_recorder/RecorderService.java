package com.example.x25_recorder;

import java.io.File;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.sip.SipAudioCall.Listener;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class RecorderService extends Service {

	private MediaRecorder recorder;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//拿到电话管理器
		TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		//监听电话状态
		//events：决定 PhoneStateListener 监听什么内容
		tm.listen(new MyListener(), PhoneStateListener.LISTEN_CALL_STATE);
	}
	
	class MyListener extends PhoneStateListener{
		
		//一旦电话状态改变，此方法调用
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			
			switch (state) {
			
			case TelephonyManager.CALL_STATE_IDLE:
				System.out.println("空闲状态");
				if(recorder != null){
					//录音停止
					recorder.stop();
					//释放资源
					recorder.release();
					recorder = null;
				}
				break;
				
			case TelephonyManager.CALL_STATE_RINGING:
				System.out.println("响铃状态");
				
				//初始化录音机
				if(recorder == null){
					recorder = new MediaRecorder();
					//设置音频来源      设置为麦克风
					recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					//设置保存的音频文件的保存格式          设置为3GP格式
					recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					//设置保存的音频文件的路径和名字
					File file = new File(Environment.getExternalStorageDirectory(),"record.3gp");
					String path = file.getAbsolutePath();
					System.out.println(path);
					recorder.setOutputFile(path);
					//设置音频编码
					recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					try {
						//准备就绪
						recorder.prepare();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				break;
				
			case TelephonyManager.CALL_STATE_OFFHOOK:
				System.out.println("摘机状态");
				//开始录音
				if(recorder != null){
					//开始录音
					recorder.start();
				}
				break;

			}
		}
	}
}
