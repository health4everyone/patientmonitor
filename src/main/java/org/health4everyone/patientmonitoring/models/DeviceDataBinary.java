package org.health4everyone.patientmonitoring.models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name="DeviceDataBinary")
@Table(name="device_data_binaries",
indexes = { 
		@Index(name="device_data_binaries_key",columnList="key"),
		@Index(name="device_data_binaries_timestamp",columnList="timestamp")})
public class DeviceDataBinary {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;	
	
	private Date timestamp;
		
	@ManyToOne
	@JoinColumn(name = "key", nullable=false, foreignKey = @ForeignKey(name="FK_BINARY_KEY"))
	private DeviceDataKey key;
	
	@Lob
	private byte[] data;
	
	public UUID getId() {
		return this.id;
	}

	
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public DeviceDataKey getKey() {
		return key;
	}

	public void setKey(DeviceDataKey key) {
		this.key = key;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
	

}
