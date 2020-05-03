package clientApplication;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.lang.SecurityManager;
import java.rmi.RemoteException;

import common.IServer;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JTextField txtCancel;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(320, 180, 751, 501);
		contentPane = new JPanel();
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextField txtUsername = new JTextField();
		txtUsername.setBounds(262, 241, 244, 26);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		JPasswordField txtPassword = new JPasswordField();
		txtPassword.setBounds(262, 295, 244, 26);
		contentPane.add(txtPassword);
		
		txtCancel = new JTextField();
		txtCancel.setForeground(Color.WHITE);
		txtCancel.setBackground(new Color(220, 20, 60));
		txtCancel.setDisabledTextColor(new Color(255, 127, 80));
		txtCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
				contentPane.setVisible(false); 
			    dispose();  
			    Sensorinformation.main(null);
			    
			}
		});
		txtCancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		txtCancel.setHorizontalAlignment(SwingConstants.CENTER);
		txtCancel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtCancel.setText("Cancel");
		txtCancel.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		txtCancel.setEditable(false);
		txtCancel.setBounds(393, 371, 113, 26);
		contentPane.add(txtCancel);
		txtCancel.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel.setBounds(140, 238, 112, 26);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblNewLabel_1.setBounds(140, 295, 87, 26);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Login");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 30));
		lblNewLabel_2.setBounds(281, 0, 176, 59);
		contentPane.add(lblNewLabel_2);
		
		JLabel lbl_username = new JLabel("");
		lbl_username.setForeground(Color.RED);
		lbl_username.setBackground(Color.RED);
		lbl_username.setBounds(262, 264, 244, 26);
		contentPane.add(lbl_username);
		
		JLabel lbl_pass = new JLabel("");
		lbl_pass.setForeground(Color.RED);
		lbl_pass.setBackground(Color.RED);
		lbl_pass.setBounds(260, 321, 246, 26);
		contentPane.add(lbl_pass);
		
		
		
		txtUsername.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
								
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					btnNewButton.doClick();
			    }
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		txtPassword.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
								
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					btnNewButton.doClick();
			    }
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btnNewButton = new JButton("Login");
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String password = txtPassword.getText().trim();
				String username = txtUsername.getText().trim();
				
				if (password.isEmpty() && username.isEmpty()) {				//validations are done for empty fields
					
					lbl_username.setText("Username field canno't be empty");
					lbl_pass.setText("Password field canno't be empty");					
				}
				else if(username.isEmpty()) {
					
					lbl_username.setText("Username field canno't be empty");
					
				}
				else if(password.isEmpty()) {
					
					lbl_pass.setText("Password field canno't be kept empty");
					
				}
				else {
//					if (System.getSecurityManager() == null){
//			            System.setSecurityManager (new RMISecurityManager());
//			        }
			 

			      System.setProperty("java.security.policy", "file:allowall.policy");
			        try {
			            IServer service = (IServer) Naming.lookup("rmi://localhost/RMIServer");
			            if(service.login(username, password)) {
			            	contentPane.setVisible(false); 
						    dispose();
						    AdminPage admpage = new AdminPage();
						    admpage.setVisible(true);				//closes the current frame and opens AdminPage frame
			            }else {
			            	return;
			            }
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
		
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.setForeground(Color.WHITE);
		btnNewButton.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		btnNewButton.setBackground(new Color(0, 0, 255));
		btnNewButton.setBounds(263, 370, 97, 26);
		contentPane.add(btnNewButton);
		
		JLabel lbl_login = new JLabel("");
		
		Image img = new ImageIcon(this.getClass().getResource("/loginpage.png")).getImage();		//setting the login icon
		lbl_login.setIcon(new ImageIcon(img));
		
		lbl_login.setBounds(301, 60, 302, 156);
		contentPane.add(lbl_login);
		
		JLabel lbl_bgimg = new JLabel("New label");
		lbl_bgimg.setForeground(Color.WHITE);
		
		Image imgg = new ImageIcon(this.getClass().getResource("/image1.jpg")).getImage();		//setting of the background image
		lbl_bgimg.setIcon(new ImageIcon(imgg));
		lbl_bgimg.setBounds(0, 0, 747, 464);
		contentPane.add(lbl_bgimg);
		
		
		
		
	}
}
