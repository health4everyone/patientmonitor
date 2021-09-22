package org.health4everyone.patientmonitoring.models;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name="DeviceDataKey")
@Table(name="device_data_keys", 
	uniqueConstraints = { @UniqueConstraint(name = "UniqueDeviceAndKey", columnNames = { "device", "key" }) },
	indexes = { 
			@Index(name="device_data_key_device", columnList = "device"),
			@Index(name="device_data_key_key", columnList = "key")})

public class DeviceDataKey {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
	
	@ManyToOne
	@JoinColumn(name = "device", nullable=false, foreignKey = @ForeignKey(name="FK_KEY_DEVICE"))
	private Device device;
	
	private String key;
	
	@Enumerated(EnumType.ORDINAL)
	private DataKeyType dataKeyType;//numeric, string, blob
	private String unit;
	
	public UUID getId() {
		return this.id;
	}
	
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public DataKeyType getDataKeyType() {
		return dataKeyType;
	}
	public void setDataKeyType(DataKeyType dataKeyType) {
		this.dataKeyType = dataKeyType;
	}
	
	
	
}
