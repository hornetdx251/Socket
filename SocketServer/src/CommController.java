
import java.io.*;
import java.net.*;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class CommController extends Thread{
	public int port;
	private boolean halt_;
	JLabel label;
	BufferedWriter out = null;
	
	public void setLabel(JLabel label){
		this.label = label;
	}
	
	CommController(int port){
		this.port = port;
		halt_ = false;
	}

	void sendMsg(String msg){
		try {
			out.write(msg);
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	void startServer(){
		try{
			ServerSocket srvSocket = new ServerSocket(port);
			
			
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					label.setText("Status : accepting..." );
				}
			});	
			final Socket socket = srvSocket.accept();
			
			System.out.println(socket.getInetAddress() + "is Accepted");
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					label.setText("Status : " + socket.getInetAddress() + "is Accepted" );
				}
			});
			
			out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			java.io.InputStream is = socket.getInputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			
			while(is.available()>=0){
				
				// halt
				if(halt_) break;
				
				// continue when input stream is available but data is not exist.
				if(is.available()==0) continue;
				
				final char[] data = new char[is.available()];
				in.read(data,0,is.available());
				
				// Echo Back.
				// out.write(data);
				// out.flush();
				
				System.out.println(data);
				SwingUtilities.invokeLater(new Runnable(){
					@Override
					public void run() {
						
						label.setText("Status : data Recievied " + String.valueOf(data) );
						
					}
				});
				
				Thread.sleep(100);
			}
			
			SwingUtilities.invokeLater(new Runnable(){
				@Override
				public void run() {
					label.setText("Status : Close the communication..." );
					
				}
			});	
			
			out.close();
			in.close();
			socket.close();
			srvSocket.close();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		startServer();
	}
	
	public void halt(){
		halt_ = true;
		interrupt();
	}
	
}
