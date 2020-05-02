package clientApplication;

public class Sensordetails {
	
	public int sensorid;
	public String sensorstatus;
	public int floorno;
	public int roomno;
	public int smokelevel;
	public int CO2level;
	
	public Sensordetails(int senid, String sensorst, int floor, int room, int smoke, int co2)
	{
		this.sensorid = senid;
		this.sensorstatus = sensorst;
		this.floorno = floor;
		this.roomno = room;
		this.smokelevel = smoke;
		this.CO2level = co2;
	}
	
	
	public int getSensorname() {
		return sensorid;
	}
	public void setSensorname(int sensorid) {
		this.sensorid = sensorid;
	}
	public String getSensorstatus() {
		return sensorstatus;
	}
	public void setSensorstatus(String sensorstatus) {
		this.sensorstatus = sensorstatus;
	}
	public int getFloorno() {
		return floorno;
	}
	public void setFloorno(int floorno) {
		this.floorno = floorno;
	}
	public int getRoomno() {
		return roomno;
	}
	public void setRoomno(int roomno) {
		this.roomno = roomno;
	}
	public int getSmokelevel() {
		return smokelevel;
	}
	public void setSmokelevel(int smokelevel) {
		this.smokelevel = smokelevel;
	}
	public int getCO2level() {
		return CO2level;
	}
	public void setCO2level(int cO2level) {
		CO2level = cO2level;
	}
	
	
	

}
