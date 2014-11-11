package com.socketpractice;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.R.id;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
// import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener{

    // test edit
	CommController comm = null;
	Handler handler = null;
	EditText editSendMsg = null;
	EditText editRcvMsg = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button btn1 = (Button)findViewById(R.id.button1);
        btn1.setOnClickListener(this);
        btn1.setText("Connect");
        
        
        Button btnSend = (Button)findViewById(R.id.button2);
        btnSend.setOnClickListener(this);
        btnSend.setText("Send");
        
        TextView text1 = (TextView)findViewById(R.id.textView1);
        text1.setText("IP Adress");
        EditText edit1 = (EditText)findViewById(R.id.editText1);
        edit1.setText("192.168.0.2");
        
        TextView text2 = (TextView)findViewById(R.id.textView2);
        text2.setText("Port");
        EditText edit2 = (EditText)findViewById(R.id.editText2);
        edit2.setText("1234");
        
        TextView text3 = (TextView)findViewById(R.id.textView3);
        text3.setText("Message");
        editSendMsg = (EditText)findViewById(R.id.editText3);
        
        
        TextView text4 = (TextView)findViewById(R.id.textView4);
        text4.setText("RecievedMsg");
        editRcvMsg = (EditText)findViewById(R.id.editText4);
        
        
        handler = new Handler(){
        	public void handleMessage(Message msg){
        		switch(msg.arg1){
        		case CommController.MSG_SUCCESS_TO_CREATE_SOCKET:
        			editRcvMsg.setText("Successful connect with the server.");
        			break;
        		case CommController.MSG_CLOSE_COMMUNICATION:
        			editRcvMsg.setText("Close communication...");
        			break;
        		case CommController.MSG_DATA_RECIEVED:
        			String rcv = (String)msg.obj;
        			editRcvMsg.setText("Data recieved : " + rcv);
        			break;
        		default:
        			editRcvMsg.setText("Unrecognized message recieved..");
        			break;		
        		}
        		
        	}
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.button1:
			Log.d("myDebug","button [Connect] clicked.");
			onClickButton1(v);
			break;
		case R.id.button2:
			Log.d("myDebug","button [Send] clicked.");
			onClickButtonSend(v);
			
			break;
		default:
			break;
		}
	}
	
	private void onClickButtonSend(View v) {
		// TODO Auto-generated method stub
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(comm != null){
					comm.SendMsg(editSendMsg.getText().toString());
				}
					
			}
			
		}).start();
		
	}


	public void onClickButton1(View v){
		
		// IP�A�h���X��ǂݎ��
		EditText edit1 = (EditText)findViewById(R.id.editText1);
		String adress = edit1.getText().toString();
		
		// �|�[�g�ԍ���ǂݎ��
		EditText edit2 = (EditText)findViewById(R.id.editText2);
		String strPort = edit2.getText().toString();
		int port = Integer.parseInt(strPort);
		
		// ���M���郁�b�Z�[�W��ǂݎ��
		EditText edit3 = (EditText)findViewById(R.id.editText3);
		String msg = edit3.getText().toString();
		
		// Main�X���b�h�ŒʐM��s�����Ƃ���ƁA
		// NetworkOnMainThreadException����������B
		// AsyncTask�N���X�̔h���N���X�������AdoInBackgound()���\�b�h��
		// �ŏ�����s�����Ƃł��̗�O�����B
		
		if(comm==null)
		{
			comm = new CommController(adress,Integer.parseInt(strPort));
			comm.setHandler(handler);
		}
		
		// �\�P�b�g��擾����B
		// TODO : ��œn���Ă���B���b�Z�[�W�𓊂���̂��߂��̂ō폜����B
		comm.execute(msg);

		
	}
    
}