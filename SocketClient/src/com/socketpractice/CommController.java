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
			// �\�P�b�g��null�̏ꍇ�͐�������
			if(socket==null){
				socket = new Socket(adress,port);
			
				// �\�P�b�g�̐����ɐ��������ꍇ��BufferedWriter,Rader�𐶐�����B
				printwriter = new PrintWriter(socket.getOutputStream(),true);
				// printwriter.println(msg);
				istream = socket.getInputStream();
				buffreader = new BufferedReader(new InputStreamReader(istream));
				
				/*
				 * �ڑ��������������Ƃ����C���X���b�h�ɒʒm����B
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
				 * �f�[�^����M�������Ƃ����C���X���b�h�ɒʒm����B
				 */
				Message msg2app = new Message();
				String strData = String.valueOf(data);
				msg2app.arg1 = MSG_DATA_RECIEVED;
				msg2app.obj = strData;
				handler.sendMessage(msg2app);
			}
	
			// �ڑ��X���b�h�ł́A�\�P�b�g�̐����ƁA��M�ҋ@�X���b�h�����ɂ��Ă����B
			// ������𑗐M����
			// printwriter.println(msg);
			
			// Stop the communication...
			buffreader.close();
			printwriter.close();
			istream.close();
			socket.close();
			
			/*
			 * �ڑ���ؒf�������Ƃ����C���X���b�h�ɒʒm����B
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
