package common;

import java.io.Serializable;

public class SensorInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public int id;
	public int smoke_level;
	public int co2_level;
	public int room_no;
	public int floor_no;
	public boolean is_active;
	public String created_at;
	public String updated_at;
	
	public SensorInfo() {
		
	}
	
	public SensorInfo(int id, int smoke_level, int co2_level, int room_no, int floor_no, boolean is_active,
			String created_at, String updated_at) {

		this.id = id;
		this.smoke_level = smoke_level;
		this.co2_level = co2_level;
		this.room_no = room_no;
		this.floor_no = floor_no;
		this.is_active = is_active;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSmoke_level() {
		return smoke_level;
	}

	public void setSmoke_level(int smoke_level) {
		this.smoke_level = smoke_level;
	}

	public int getCo2_level() {
		return co2_level;
	}

	public void setCo2_level(int co2_level) {
		this.co2_level = co2_level;
	}

	public int getRoom_no() {
		return room_no;
	}

	public void setRoom_no(int room_no) {
		this.room_no = room_no;
	}

	public int getFloor_no() {
		return floor_no;
	}

	public void setFloor_no(int floor_no) {
		this.floor_no = floor_no;
	}

	public boolean isIs_active() {
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
}
