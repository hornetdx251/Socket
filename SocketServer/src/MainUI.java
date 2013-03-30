
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import java.awt.Container;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI extends JFrame{

	private static final long serialVersionUID = 7137031271957399455L;
	
	int buttonPressedCount = 0;
	JLabel label1;
	myListener listener;
	CommController commController = null;
	
	
	MainUI(String title){
		
		// Frame Setting 
		setTitle(title);
		setBounds(100,100,400,200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Label
		label1 = new JLabel();
		label1.setText("Status: Nothing...");
		
		// TextField
		final JTextField textfield1 = new JTextField();
		textfield1.setText("                       ");
		
		// Button1
		JButton btn1 = new JButton();
		btn1.setText("Start Server");
		listener = new myListener();
		// Set Label for output to action listener.
		listener.setLabel(label1);
		
		btn1.addActionListener(listener);
		
		// Button2
		JButton btn2 = new JButton();
		btn2.setText("calcel");
		btn2.addActionListener(listener);
		
		// Button3
		JButton btn3 = new JButton();
		btn3.setText("Send");
		btn3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if(commController!=null){
					commController.sendMsg(textfield1.getText().toString());
				}
				
			}
			
		});
	
		// RadioButton
		/*
		JRadioButton radio1 = new JRadioButton();
		radio1.setText("radio1");
		JRadioButton radio2 = new JRadioButton();
		radio2.setText("radio2");
		JRadioButton radio3 = new JRadioButton();
		radio3.setText("radio3");
		*/

		
		// Layout.
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		
		JPanel panelN = new JPanel();
		panelN.add(btn1);
		panelN.add(btn2);
		panelN.add(btn3);
		container.add(BorderLayout.NORTH,panelN);
		
		JPanel panelC = new JPanel();
		panelC.add(label1);
		container.add(BorderLayout.CENTER,panelC);
		
		JPanel panelS = new JPanel();
		/*
		panelS.add(radio1);
		panelS.add(radio2);
		panelS.add(radio3);
		*/
		panelS.add(textfield1);
		container.add(BorderLayout.SOUTH,panelS);
	}
	
	class myListener implements ActionListener{

		JLabel label = null;
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if( ((JButton)e.getSource()).getText() == "Start Server"){
				
				// If communication controller has not initialized,
				// initialize by port "1234".
				if(commController == null)
				{
					commController = new CommController(1234);
					commController.setLabel(label1);
				}
				// Start server.
				commController.start();		
				
			}else{
				// Halt
				commController.halt();
			}
		}
		
		void setLabel(JLabel label){
			this.label = label;
		}
		
	}
}


