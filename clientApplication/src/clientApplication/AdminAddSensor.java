package clientApplication;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import common.IServer;
import common.SensorInfo;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class AdminAddSensor extends JFrame {

	private JPanel contentPane;
	private JTextField floornm;
	private JTextField roomnm;
	private JTextField sensornm;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminAddSensor frame = new AdminAddSensor();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public AdminAddSensor() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 890, 689);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Add New Sensor");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 28));
		lblNewLabel.setBounds(295, 76, 312, 57);
		contentPane.add(lblNewLabel);
		
		floornm = new JTextField();
		floornm.setBounds(274, 224, 348, 62);
		contentPane.add(floornm);
		floornm.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Floor No");
		lblNewLabel_2.setFont(new Font("Bookman Old Style", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_2.setBounds(111, 224, 162, 62);
		contentPane.add(lblNewLabel_2);
		
		roomnm = new JTextField();
		roomnm.setBounds(273, 380, 349, 62);
		contentPane.add(roomnm);
		roomnm.setColumns(10);
		
		JLabel lblNewLabel_3 = new JLabel("Room No");
		lblNewLabel_3.setFont(new Font("Bookman Old Style", Font.BOLD | Font.ITALIC, 20));
		lblNewLabel_3.setBounds(111, 380, 162, 62);
		contentPane.add(lblNewLabel_3);
		
		JButton btnNewButton = new JButton("Add Sensor");
		Image imgg = new ImageIcon(this.getClass().getResource("/plus.png")).getImage();		//setting the button image
		btnNewButton.setIcon(new ImageIcon(imgg));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton.setBounds(200, 527, 193, 57);
		contentPane.add(btnNewButton);
			
		JLabel lbl_sendor = new JLabel("");
		lbl_sendor.setForeground(Color.RED);
		lbl_sendor.setBounds(273, 244, 348, 42);
		contentPane.add(lbl_sendor);
		
		JLabel lbl_floornum = new JLabel("");
		lbl_floornum.setForeground(Color.RED);
		lbl_floornum.setBounds(273, 344, 348, 42);
		contentPane.add(lbl_floornum);
		
		JLabel lbl_roomnum = new JLabel("");
		lbl_roomnum.setForeground(Color.RED);
		lbl_roomnum.setBounds(273, 444, 348, 48);
		contentPane.add(lbl_roomnum);
		
		
		JButton btnNewButton_1 = new JButton("Cancel");
		Image imgclose = new ImageIcon(this.getClass().getResource("/close.png")).getImage();		//setting of the button image
		btnNewButton_1.setIcon(new ImageIcon(imgclose));
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPane.setVisible(false); 
			    dispose();
			    AdminPage admpage = new AdminPage();
			    admpage.setVisible(true);
				
			}
		});
		btnNewButton_1.setBounds(450, 527, 193, 57);
		contentPane.add(btnNewButton_1);
		
		JLabel lblNewLabel_4 = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/image1.jpg")).getImage();		//setting of the background image
		lblNewLabel_4.setIcon(new ImageIcon(img));
		lblNewLabel_4.setBounds(0, 0, 876, 662);
		contentPane.add(lblNewLabel_4);
		
		
		floornm.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				char a = e.getKeyChar();
				if(Character.isLetter(a)) {					//ensures that only numbers are entered in the Floor Number field
					floornm.setEditable(false);
					lbl_floornum.setText("Only numbers are allowed in this field");
				}
				else {
					floornm.setEditable(true);
					lbl_floornum.setText("");
				}
			}
		});
		
		roomnm.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				char a = e.getKeyChar();
				if(Character.isLetter(a)) {					//ensures that only numbers are entered in the Room Number field
					roomnm.setEditable(false);
					lbl_roomnum.setText("Only numbers are allowed in this field");
				}
				else {
					roomnm.setEditable(true);
					lbl_roomnum.setText("");
				}
			}
		});
		
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(floornm.getText().trim().isEmpty() && roomnm.getText().trim().isEmpty()) {	//checks whether all the fields are empty

					
					lbl_floornum.setText("Floor Number field canno't be kept empty");
					lbl_roomnum.setText("Room Number field canno't be kept empty");
					
				}
				else if(floornm.getText().trim().isEmpty()) {								//checks whether the floor number field is empty
					lbl_floornum.setText("Floor Number field canno't be kept empty");
				}
				else if(roomnm.getText().trim().isEmpty()) {								//checks whether the room number field is empty
					lbl_roomnum.setText("Room Number field canno't be kept empty");
				}				
				else {
				
					System.setProperty("java.security.policy", "file:allowall.policy");
			        try {
			            IServer service = (IServer) Naming.lookup("rmi://localhost/RMIServer");
			            
			            int id = 0;
			            int room_no = Integer.parseInt(roomnm.getText().trim());
			            int floor_no = Integer.parseInt(floornm.getText().trim());
			            SensorInfo newSensor = new SensorInfo(id, 0, 0, room_no, floor_no, true, "", "");
			            service.addNewSensor(newSensor);
						JOptionPane.showMessageDialog(null,"Sensor added successfully");		            
			        } catch (NotBoundException ex) {
			            System.err.println(ex.getMessage());
			        } catch (MalformedURLException ex) {
			            System.err.println(ex.getMessage());
			        } catch (RemoteException ex) {
			            System.err.println(ex.getMessage());
			        }
									
				}
			}
		});
				
		
	}
}
