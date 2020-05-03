package rmiserver;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;

import java.rmi.registry.Registry;
import common.IServer;
import common.SensorInfo;

public class Server extends UnicastRemoteObject implements IServer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// flag to remember which fire sensors have been already alerted
	private HashMap<Integer, Boolean> hasAlertedCO2 = new HashMap<Integer, Boolean>();
	private HashMap<Integer, Boolean> hasAlertedSmoke = new HashMap<Integer, Boolean>();
	
	protected Server() throws RemoteException {
		super();
	}

	public static void main(String[] args) {
		// set the policy file as the system security policy
        System.setProperty("java.security.policy", "file:allowall.policy");

        try {
            Server svr = new Server();
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("RMIServer", svr);
            System.out.println("Server started....");
        } catch (RemoteException re) {
            System.err.println(re.getMessage());
        }

	}

	// IServer implementations
	// All the WebRequest implementations are in WebRequest Class
	@Override
	public boolean login(String username, String password) {
//		System.out.println("Calling login");
		try {
			return WebRequest.checkLogin(username, password);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public List<SensorInfo> getSensorInfo() {		
		List<SensorInfo> data = WebRequest.getSensorInfo();
		for(SensorInfo s : data) {			
			if(!hasAlertedCO2.containsKey(s.id)) {
				hasAlertedCO2.put(s.id, false);
			}
			
			if(!hasAlertedSmoke.containsKey(s.id)) {
				hasAlertedSmoke.put(s.id, false);
			}
			
			if (s.is_active) {
				// check for CO2 Level
				if (s.co2_level > 5 && !hasAlertedCO2.get(s.id)) {
					String msg = "CO2 level is greater than 5 on room "+ s.room_no + " of floor "+s.floor_no;
					SendEmail(msg);
					SendSMS(msg);
					
					// update the flag
					hasAlertedCO2.put(s.id, true);
					
				}else if(s.co2_level <= 5) {
					// reset the flag
					hasAlertedCO2.put(s.id, false);
				} 
				
				// Check for Smoke Level
				if (s.smoke_level > 5 && !hasAlertedSmoke.get(s.id)) {
					String msg = "Smoke level is greater than 5 on room "+ s.room_no + " of floor "+s.floor_no;
					SendEmail(msg);
					SendSMS(msg);
					
					// update the flag
					hasAlertedSmoke.put(s.id, true);
				}else if(s.smoke_level <= 5) {
					// reset the flag
					hasAlertedSmoke.put(s.id, false);
				}
			}
		}		
		return data;
	}

	@Override
	public void addNewSensor(SensorInfo sensorInfo) {
		WebRequest.addSensor(sensorInfo);		
	}

	@Override
	public void updateSensor(int id, SensorInfo updatedSensorInfo) {
		WebRequest.updateSensorInfo(id, updatedSensorInfo);
	}

	@Override
	public void logout(String username, String password) throws RemoteException {
		WebRequest.logout();
	}

	// delete the sensor by id
	@Override
	public void deleteSensor(int id) throws RemoteException {
		WebRequest.deleteSensorById(id);
	}
	
	// method to send an email
	private void SendEmail(String msg) {
		System.out.println("[CRITICAL] Email: "+msg);		
	}
	
	// method to send SMS
	private void SendSMS(String msg) {
		System.out.println("[CRITICAL] SMS: "+msg);		
	}
}
