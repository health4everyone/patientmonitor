package org.health4everyone.patientmonitoring.models;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="Device")
@Table(name="devices")
public class Device {
	@Id
    private UUID id;
	
	private String name;
	
	private String model;
	
	private String serial;
	
	private Timestamp createDate;
	
	private Timestamp deleteDate;
	
	private Boolean active=true;

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID uuid) {
		this.id = uuid;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public boolean isActive() {
		return active.booleanValue();
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Timestamp getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Timestamp deleteDate) {
		this.deleteDate = deleteDate;
	}
	
	
}
