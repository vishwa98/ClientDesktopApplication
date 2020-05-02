package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface IServer extends Remote {
	public boolean login(String username, String password)throws RemoteException;
	public void logout(String username, String password)throws RemoteException;
	public List<SensorInfo> getSensorInfo() throws RemoteException;
	public void addNewSensor(SensorInfo sensorInfo)throws RemoteException;
	public void updateSensor(int id, SensorInfo updatedSensorInfo)throws RemoteException;
	public void deleteSensor(int id)throws RemoteException;
}
