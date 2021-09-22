package org.health4everyone.patientmonitoring.models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Index;
import javax.persistence.ForeignKey;

@Entity(name="DeviceDataNumeric")
@Table(name="device_data_numerics",
indexes = { 
		@Index(name="device_data_numerics_key",columnList="key"),
		@Index(name="device_data_numerics_timestamp",columnList="timestamp")})

public class DeviceDataNumeric {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;	
	
	private Date timestamp;
		
	@ManyToOne
	@JoinColumn(name = "key", nullable = false, foreignKey = @ForeignKey(name="FK_NUMERIC_KEY"))
	private DeviceDataKey key;
	
	@Column(precision=22, scale=10) 
	private BigDecimal data;

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

	public BigDecimal getData() {
		return data;
	}

	public void setData(BigDecimal data) {
		this.data = data;
	}
	
}
