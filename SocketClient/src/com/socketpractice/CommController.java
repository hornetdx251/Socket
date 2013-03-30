package com.socketpractice;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class CommController extends AsyncTask<String,String,String>{
	
	public static final int MSG_SUCCESS_TO_CREATE_SOCKET = 0;
	public static final int MSG_CLOSE_COMMUNICATION = 1;
	public static final int MSG_DATA_RECIEVED = 2;
	
	Socket socket = null;
	String adress = null;
	int port;
	boolean _halt = false;
	
	PrintWriter printwriter;
	InputStream istream;
	BufferedReader buffreader;
	
	private Handler handler = null;
	void setHandler(Handler handler){
		this.handler = handler;
	}
	
	CommController(String adress,int port){
		this.adress = adress;
		this.port = port;
	}
	
	void SendMsg(String msg){
		printwriter.write(msg);
		printwriter.flush();
	}
	
	@Override
	protected String doInBackground(String... arg0) {

		String msg = arg0[0];
		
		try{
			// ソケットがnullの場合は生成する
			if(socket==null){
				socket = new Socket(adress,port);
			
				// ソケットの生成に成功した場合はBufferedWriter,Raderを生成する。
				printwriter = new PrintWriter(socket.getOutputStream(),true);
				// printwriter.println(msg);
				istream = socket.getInputStream();
				buffreader = new BufferedReader(new InputStreamReader(istream));
				
				/*
				 * 接続が成功したことをメインスレッドに通知する。
				 */
				Message msg2app = new Message();
				msg2app.arg1 = MSG_SUCCESS_TO_CREATE_SOCKET;
				handler.sendMessage(msg2app);
			}
		
			while(istream.available()>=0){
				if(_halt) break;
				if(istream.available()==0) continue;
				
				char[] data = new char[istream.available()];
				buffreader.read(data,0,istream.available());
				// printwriter.write(data);
				// printwriter.flush();
				
				/*
				 * データを受信したことをメインスレッドに通知する。
				 */
				Message msg2app = new Message();
				String strData = String.valueOf(data);
				msg2app.arg1 = MSG_DATA_RECIEVED;
				msg2app.obj = strData;
				handler.sendMessage(msg2app);
			}
	
			// 接続スレッドでは、ソケットの生成と、受信待機スレッドだけにしておく。
			// 文字列を送信する
			// printwriter.println(msg);
			
			// Stop the communication...
			buffreader.close();
			printwriter.close();
			istream.close();
			socket.close();
			
			/*
			 * 接続を切断したことをメインスレッドに通知する。
			 */
			Message msg2app = new Message();
			msg2app.arg1 = MSG_CLOSE_COMMUNICATION;
			handler.sendMessage(msg2app);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	void halt(){
		_halt = true;
		interrupt();
	}

	private void interrupt() {
		// TODO Auto-generated method stub
		
	}
	
}
