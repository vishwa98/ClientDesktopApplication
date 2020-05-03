package clientApplication;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import common.IServer;
import common.SensorInfo;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;

public class AdminPage extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JScrollPane scrollPane;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1;
	private JTextField edID;
	private JTextField edStat;
	private JTextField edFloorNo;
	private JLabel lblNewLabel_3;
	private JTextField edRoomNo;
	private JLabel lblNewLabel_4;
	private JLabel lblNewLabel_5;
	private JButton btnNewButton_2;
	private JLabel lbl_sensorID;
	private JLabel lbl_sensorstatus;
	private JLabel lbl_floorno;
	private JLabel lbl_roomno;
	private JLabel lblNewLabel_6;	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AdminPage frame = new AdminPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	private HashMap<Integer, Boolean> hasAlertedCO2 = new HashMap<Integer, Boolean>();
	private HashMap<Integer, Boolean> hasAlertedSmoke = new HashMap<Integer, Boolean>();
	
	
	
	private IServer service = null;
	public List<SensorInfo> Sensorlist()
	{
		ArrayList<SensorInfo> sensors = new ArrayList<SensorInfo>();
		try {
			return service.getSensorInfo();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sensors;
	}
	
	
	public void sensorJTable()			//adding data to the table
	{
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		List<SensorInfo> sensors = Sensorlist();
		Object sensorData[] = new Object[6];
		for (int counter = 0; counter < sensors.size(); counter++) {
			
			sensorData[0] = sensors.get(counter).id;
			sensorData[1] = sensors.get(counter).is_active;
			sensorData[2] = sensors.get(counter).floor_no;
			sensorData[3] = sensors.get(counter).room_no;
			sensorData[4] = sensors.get(counter).smoke_level;
			sensorData[5] = sensors.get(counter).co2_level;
			
			
			model.addRow(sensorData);
			
		}
		
		
		for(SensorInfo s : sensors) {					//alert when the CO2 or smoke level passes 5
			if(!hasAlertedCO2.containsKey(s.id)) {
				hasAlertedCO2.put(s.id, false);
			}
			
			if(!hasAlertedSmoke.containsKey(s.id)) {
				hasAlertedSmoke.put(s.id, false);
			}
			
			if (s.is_active) {
				// check for CO2 Level
				if (s.co2_level > 5 && !hasAlertedCO2.get(s.id)) {
					String alertmsg = "CO2 level has moved to a value greater than 5 in room " + s.room_no + " of floor " + s.floor_no;
					JOptionPane.showMessageDialog(null,alertmsg,"Alert for Sensor ID " + s.id, JOptionPane.INFORMATION_MESSAGE);
					
					// update the flag
					hasAlertedCO2.put(s.id, true);
					
				}else if(s.co2_level <= 5) {
					// reset the flag
					hasAlertedCO2.put(s.id, false);
				} 
				
				// Check for Smoke Level
				if (s.smoke_level > 5 && !hasAlertedSmoke.get(s.id)) {
					String alertmsg = "Smoke level has moved to a value greater than 5 in room " + s.room_no + " of floor " + s.floor_no;
					JOptionPane.showMessageDialog(null,alertmsg,"Alert for Sensor ID " + s.id, JOptionPane.INFORMATION_MESSAGE);
					
					// update the flag
					hasAlertedSmoke.put(s.id, true);
				}else if(s.smoke_level <= 5) {
					// reset the flag
					hasAlertedSmoke.put(s.id, false);
				}
			}
		}
		
		
		
		
		
	}
	
		
		public void refreshTable() {		//refreshes the table every 15 seconds
		
		Timer timer;
	    timer = new Timer(15000, new ActionListener() {
	      @Override
	      public void actionPerformed(ActionEvent e) {
	    	  DefaultTableModel model = (DefaultTableModel)table.getModel();
	          model.setRowCount(0);
	          sensorJTable();
	      }
	    });
	    
	    timer.start();
	  }
	
	

	/**
	 * Create the frame.
	 */
	public AdminPage() {
		
		System.setProperty("java.security.policy", "file:allowall.policy");
        try {
            service = (IServer) Naming.lookup("rmi://localhost/RMIServer");            
        } catch (NotBoundException ex) {
            System.err.println(ex.getMessage());
        } catch (MalformedURLException ex) {
            System.err.println(ex.getMessage());
        } catch (RemoteException ex) {
        	System.out.println("Error");
            System.err.println(ex.getMessage());
        }
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1005, 782);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
				
		JButton btnNewButton = new JButton("Add New Sensor");
		Image imgg = new ImageIcon(this.getClass().getResource("/plus.png")).getImage();		//setting the button image
		btnNewButton.setIcon(new ImageIcon(imgg));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				contentPane.setVisible(false); 
			    dispose();
			    AdminAddSensor admsensor = new AdminAddSensor();
				admsensor.setVisible(true);								//opens the AdminAddSensor frame when the button is clicked				
			}
		});
		btnNewButton.setBounds(681, 402, 219, 48);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Logout");
		Image imglogout = new ImageIcon(this.getClass().getResource("/logout.png")).getImage();		//setting the button image
		btnNewButton_1.setIcon(new ImageIcon(imglogout));
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				contentPane.setVisible(false); 
			    dispose();  
			    Sensorinformation.main(null);						//Closes the current frame and opens the Sensorinformation main window
			}
		});
		btnNewButton_1.setBounds(681, 476, 219, 48);
		contentPane.add(btnNewButton_1);
		
		scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		scrollPane.setBounds(20, 90, 945, 187);
		contentPane.add(scrollPane);
		
		
		scrollPane.setOpaque( false );
		scrollPane.getViewport().setOpaque( false ); //sets the non content area of the table same as the background image
		
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				int i = table.getSelectedRow();
				TableModel model = table.getModel();
				edID.setText(model.getValueAt(i, 0).toString());
				edStat.setText(model.getValueAt(i, 1).toString());
				edFloorNo.setText(model.getValueAt(i, 2).toString());
				edRoomNo.setText(model.getValueAt(i, 3).toString());
			}
		});
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Sensor ID", "Sensor Active", "Floor No", "Room No", "Smoke level", "CO2 level"
			}
		) {
			Class[] columnTypes = new Class[] {
				String.class, String.class, String.class, String.class, String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		
		
		JTableHeader Tableheader = table.getTableHeader();
        
        Tableheader.setBackground(Color.red); // changes the Background color
        Tableheader.setForeground(Color.WHITE);	//changes the font color to white
        
        Tableheader.setFont(new Font("Tahome", Font.BOLD, 20));
        
        TableCellRedColourRenderer renderer = new TableCellRedColourRenderer();
        table.setDefaultRenderer(Object.class, renderer);
				
		lblNewLabel = new JLabel("Edit Sensor Details");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewLabel.setBounds(98, 317, 219, 32);
		contentPane.add(lblNewLabel);
		
		lblNewLabel_1 = new JLabel("Sensor ID");
		lblNewLabel_1.setFont(new Font("Rockwell", Font.BOLD, 15));
		lblNewLabel_1.setBounds(28, 379, 141, 32);
		contentPane.add(lblNewLabel_1);
		
		edID = new JTextField();
		edID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				edID.setEditable(false);
				lbl_sensorID.setText("Please select a row in the table to edit floor no and room no");
				
			}
		});
		edID.setBounds(191, 382, 219, 32);
		contentPane.add(edID);
		edID.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Sensor status");
		lblNewLabel_2.setFont(new Font("Rockwell", Font.BOLD, 15));
		lblNewLabel_2.setBounds(28, 447, 101, 32);
		contentPane.add(lblNewLabel_2);
		
		edStat = new JTextField();
		edStat.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {	
				
				edStat.setEditable(false);
				lbl_sensorstatus.setText("Please select a row in the table to edit floor no and room no");
				
			}
		});
		edStat.setBounds(191, 450, 219, 32);
		contentPane.add(edStat);
		edStat.setColumns(10);
		
		edFloorNo = new JTextField();
		edFloorNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				char a = e.getKeyChar();
				if(Character.isLetter(a)) {						//ensures that only numbers are entered in the Floor Number field
					edFloorNo.setEditable(false);
					lbl_floorno.setText("Only numbers are allowed in this field");
				}
				else {
					edFloorNo.setEditable(true);
					lbl_floorno.setText("");
					lbl_sensorID.setText("");
					lbl_sensorstatus.setText("");
					
				}
			}
		});
		edFloorNo.setBounds(191, 521, 219, 32);
		contentPane.add(edFloorNo);
		edFloorNo.setColumns(10);
		
		lblNewLabel_3 = new JLabel("Floor No");
		lblNewLabel_3.setFont(new Font("Rockwell", Font.BOLD, 15));
		lblNewLabel_3.setBounds(28, 518, 101, 32);
		contentPane.add(lblNewLabel_3);
		
		edRoomNo = new JTextField();
		edRoomNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				char a = e.getKeyChar();
				if(Character.isLetter(a)) {						//ensures that only numbers are entered in the Room Number field
					edRoomNo.setEditable(false);
					lbl_roomno.setText("Only numbers are allowed in this field");
				}
				else {
					edRoomNo.setEditable(true);
					lbl_roomno.setText("");
					lbl_sensorID.setText("");
					lbl_sensorstatus.setText("");
					
				}
				
				
			}
		});
		edRoomNo.setBounds(191, 588, 219, 32);
		contentPane.add(edRoomNo);
		edRoomNo.setColumns(10);
		
		lblNewLabel_4 = new JLabel("Room No");
		lblNewLabel_4.setFont(new Font("Rockwell", Font.BOLD, 15));
		lblNewLabel_4.setBounds(28, 588, 123, 32);
		contentPane.add(lblNewLabel_4);
		
		lblNewLabel_5 = new JLabel("Welcome Admin");
		lblNewLabel_5.setBackground(Color.BLUE);
		lblNewLabel_5.setFont(new Font("Comic Sans MS", Font.BOLD | Font.ITALIC, 25));
		lblNewLabel_5.setBounds(403, 21, 207, 42);
		contentPane.add(lblNewLabel_5);
		
		btnNewButton_2 = new JButton("Save");													//Save
		Image imgsave = new ImageIcon(this.getClass().getResource("/save.png")).getImage();		//setting the button image
		btnNewButton_2.setIcon(new ImageIcon(imgsave));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
					if(edID.getText().trim().isEmpty() && edStat.getText().trim().isEmpty() && edFloorNo.getText().trim().isEmpty() && edRoomNo.getText().trim().isEmpty()) {	//checks whether all the fields are empty
					
					lbl_sensorID.setText("Sensor ID field canno't be kept empty");
					lbl_sensorstatus.setText("Sensor status field canno't be kept empty");
					lbl_floorno.setText("Floor Number field canno't be kept empty");
					lbl_roomno.setText("Room Number field canno't be kept empty");
					
				}
				else if(edID.getText().trim().isEmpty()){								//checks whether the Sensor ID field is empty
					lbl_sensorID.setText("Sensor ID field canno't be kept empty");
				}
				else if(edStat.getText().trim().isEmpty()) {								//checks whether the sensor status field is empty
					lbl_sensorstatus.setText("Sensor status field canno't be kept empty");
				}
				else if(edFloorNo.getText().trim().isEmpty()) {								//checks whether the floor number field is empty
					lbl_floorno.setText("Floor Number field canno't be kept empty");
				}
				else if(edRoomNo.getText().trim().isEmpty()) {
					lbl_roomno.setText("Room Number field canno't be kept empty");			//checks whether the room number field is empty
				}
					
				else {
				
					System.setProperty("java.security.policy", "file:allowall.policy");
			        try {
			            IServer service = (IServer) Naming.lookup("rmi://localhost/RMIServer");
			            
			            int id = Integer.parseInt(edID.getText().trim());
			            int room_no = Integer.parseInt(edRoomNo.getText().trim());
			            int floor_no = Integer.parseInt(edFloorNo.getText().trim());
			            SensorInfo updatedSensorInfo = new SensorInfo(id, 0, 0, room_no, floor_no, true, "", "");
			            service.updateSensor(id, updatedSensorInfo);			            
		            	JOptionPane.showMessageDialog(null,"Saved Successfully");			            
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
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_2.setBounds(46, 654, 141, 42);
		contentPane.add(btnNewButton_2);
		
		
		lbl_sensorID = new JLabel("");
		lbl_sensorID.setForeground(Color.RED);
		lbl_sensorID.setBounds(191, 404, 442, 48);
		contentPane.add(lbl_sensorID);
		
		lbl_sensorstatus = new JLabel("");
		lbl_sensorstatus.setForeground(Color.RED);
		lbl_sensorstatus.setBounds(191, 486, 459, 32);
		contentPane.add(lbl_sensorstatus);
		
		lbl_floorno = new JLabel("");
		lbl_floorno.setForeground(Color.RED);
		lbl_floorno.setBounds(191, 552, 296, 42);
		contentPane.add(lbl_floorno);
		
		lbl_roomno = new JLabel("");
		lbl_roomno.setForeground(Color.RED);
		lbl_roomno.setBounds(191, 616, 307, 46);
		contentPane.add(lbl_roomno);
		
		
		
		JButton btnNewButton_3 = new JButton("Delete");											//Delete
		Image imgdel = new ImageIcon(this.getClass().getResource("/del.png")).getImage();		//setting the button image
		btnNewButton_3.setIcon(new ImageIcon(imgdel));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(edID.getText().trim().isEmpty() && edStat.getText().trim().isEmpty() && edFloorNo.getText().trim().isEmpty() && edRoomNo.getText().trim().isEmpty()) {	//checks whether all the fields are empty
					
					lbl_sensorID.setText("Sensor ID field canno't be kept empty");
					lbl_sensorstatus.setText("Sensor status field canno't be kept empty");
					lbl_floorno.setText("Floor Number field canno't be kept empty");
					lbl_roomno.setText("Room Number field canno't be kept empty");
					
				}
				else if(edID.getText().trim().isEmpty()){								//checks whether the Sensor ID field is empty
					lbl_sensorID.setText("Sensor ID field canno't be kept empty");
				}
				else if(edStat.getText().trim().isEmpty()) {								//checks whether the sensor status field is empty
					lbl_sensorstatus.setText("Sensor status field canno't be kept empty");
				}
				else if(edFloorNo.getText().trim().isEmpty()) {								//checks whether the floor number field is empty
					lbl_floorno.setText("Floor Number field canno't be kept empty");
				}
				else if(edRoomNo.getText().trim().isEmpty()) {
					lbl_roomno.setText("Room Number field canno't be kept empty");			//checks whether the room number field is empty
				}
					
				else
				{				
					int id = Integer.parseInt(edID.getText().trim());
					if(id > 0) {
						try {
							service.deleteSensor(id);
							JOptionPane.showMessageDialog(null,"Deleted Successfully");
							// to update the table immediately
							DefaultTableModel model = (DefaultTableModel)table.getModel();
							model.setRowCount(0);
							sensorJTable();
								} catch (RemoteException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
									JOptionPane.showMessageDialog(null,"Error when deleting the sensor...");
									}
							}
				
				}
			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_3.setBounds(211, 654, 150, 42);
		contentPane.add(btnNewButton_3);
		
		JLabel lblNewLabel_7 = new JLabel("");
		Image img = new ImageIcon(this.getClass().getResource("/addimg.jpg")).getImage();		//setting the background image
		lblNewLabel_7.setIcon(new ImageIcon(img));
		lblNewLabel_7.setBounds(0, 0, 1003, 757);
		contentPane.add(lblNewLabel_7);
		
		sensorJTable();
		
		refreshTable();		
		
	}
}



