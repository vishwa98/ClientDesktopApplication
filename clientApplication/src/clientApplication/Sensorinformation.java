package clientApplication;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import common.IServer;
import common.SensorInfo;
import rmiserver.WebRequest;

import javax.swing.JScrollPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Sensorinformation {
	private JFrame frame;
	private JTable table;
	private IServer service = null;
	
	private static HashMap<Integer, Boolean> hasAlertedCO2 = new HashMap<Integer, Boolean>();
	private static HashMap<Integer, Boolean> hasAlertedSmoke = new HashMap<Integer, Boolean>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sensorinformation window = new Sensorinformation();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Sensorinformation() {
		
		System.setProperty("java.security.policy", "file:allowall.policy");
        try {
            service = (IServer) Naming.lookup("rmi://localhost/RMIServer");            
        } catch (NotBoundException ex) {
            
            System.err.println(ex.getMessage());
        } catch (MalformedURLException ex) {
            System.err.println(ex.getMessage());
        } catch (RemoteException ex) {
            System.out.println("error");
            System.err.println(ex.getMessage());
      
        }
		
		
		
		initialize();
		sensorJTable();
		refreshTable();
	}
	
	
	 public List<SensorInfo> Sensorlist()
	    {
	        ArrayList<SensorInfo> sensors = new ArrayList<SensorInfo>();
	        try {
	            return service.getSensorInfo();
	        } catch (RemoteException e) {
	            e.printStackTrace();
	        }
	        return sensors;
	    }
	
	public void sensorJTable()			//adding data to the table
	{
		if(frame == null) return;
		
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
	

	
	
	public void refreshTable() {							//Refreshes the table every 15 seconds
		
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
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1124, 597);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(90, 80, 948, 352);
		frame.getContentPane().add(scrollPane);
		
		scrollPane.setOpaque( false );
		scrollPane.getViewport().setOpaque( false ); //sets the non-content area of the table same as the background image
		
		table = new JTable();
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
	        
	        Tableheader.setBackground(Color.red); // changes the Background color of the table header
	        
	        Tableheader.setForeground(Color.WHITE); //changes the font color to white
	        
	        Tableheader.setFont(new Font("Tahome", Font.BOLD, 20));  
	        
	        TableCellRedColourRenderer renderer = new TableCellRedColourRenderer();
	        table.setDefaultRenderer(Object.class, renderer);  
	        
	        
	        
	        
		JButton btnNewButton = new JButton("Admin Login");
		Image imgg = new ImageIcon(this.getClass().getResource("/logiin.png")).getImage();		//setting the button image
		btnNewButton.setIcon(new ImageIcon(imgg));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				frame.dispose();	//closes the current frame
				Login log = new Login();
				log.setVisible(true);	//Login frame is displayed		
				frame = null;
			}
		});
		btnNewButton.setBounds(858, 473, 173, 42);
		frame.getContentPane().add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel("Sensor Details");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblNewLabel.setBounds(452, 10, 296, 42);
		frame.getContentPane().add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("");
		
		Image img = new ImageIcon(this.getClass().getResource("/pat1.jpg")).getImage();		//setting the background image
		lblNewLabel_1.setIcon(new ImageIcon(img));
		
		lblNewLabel_1.setBounds(0, 0, 1110, 560);
		frame.getContentPane().add(lblNewLabel_1);
		
				
	}
	
}
