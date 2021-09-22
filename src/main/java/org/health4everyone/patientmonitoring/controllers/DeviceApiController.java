package org.health4everyone.patientmonitoring.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.health4everyone.patientmonitoring.models.DataKeyType;
import org.health4everyone.patientmonitoring.models.Device;
import org.health4everyone.patientmonitoring.models.DeviceDataBinary;
import org.health4everyone.patientmonitoring.models.DeviceDataKey;
import org.health4everyone.patientmonitoring.models.DeviceDataNumeric;
import org.health4everyone.patientmonitoring.models.DeviceDataString;
import org.health4everyone.patientmonitoring.repositories.DeviceDataBinaryRepository;
import org.health4everyone.patientmonitoring.repositories.DeviceDataKeyRepository;
import org.health4everyone.patientmonitoring.repositories.DeviceDataNumericRepository;
import org.health4everyone.patientmonitoring.repositories.DeviceDataStringRepository;
import org.health4everyone.patientmonitoring.repositories.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DeviceApiController {
	@Autowired
	DeviceRepository deviceRepo;
	@Autowired
	DeviceDataKeyRepository deviceDataKeyRepo;
	@Autowired
	DeviceDataNumericRepository deviceDataNumericRepo;
	@Autowired
	DeviceDataStringRepository deviceDataStringRepo;
	@Autowired
	DeviceDataBinaryRepository deviceDataBinaryRepo;
	
	@RequestMapping(path="/device/register",method= {RequestMethod.GET, RequestMethod.POST})
	public String register(@RequestParam(value = "name", defaultValue = "new device", required=true) String name, 
			@RequestParam(value = "model",required=false)String model, 
			@RequestParam(value = "serial",required=false)String serial,
			@RequestParam(value = "uuid",required=false)String uuid) {
		Device device = new Device();
		if(uuid!=null )
			device.setId(UUID.fromString(uuid));
		else
			device.setId(UUID.randomUUID());
		if(model!=null)
			device.setModel(model);
		if(serial!=null)
			device.setSerial(serial);
		device.setName(name);
		device = deviceRepo.save(device);
		deviceRepo.save(device);
		return device.getId().toString();
	}
	
	@RequestMapping(path="/device/check",method= {RequestMethod.GET, RequestMethod.POST})
	public String register(@RequestParam(value = "device", required=true) String uuid) {
		Optional<Device> device = deviceRepo.findById(UUID.fromString(uuid));
		if(device.isPresent() && !device.isEmpty() && device.get().getId().toString().equalsIgnoreCase(uuid))
			return "true";
		else
			return "false";
	}

	@GetMapping(path="/device/postNumeric")
	public String postNumeric(
			@RequestParam(value = "device", required=true) String deviceUUID, 
			@RequestParam(value = "timestamp", required=true) String timestamp, 
			@RequestParam(value = "key",required=true)String key, 
			@RequestParam(value = "value",required=true)String value) {
		
		DeviceDataKey deviceDataKey = null;
		Device device = deviceRepo.findById(UUID.fromString(deviceUUID)).get();
		if(device!=null) {
			deviceDataKey = deviceDataKeyRepo.findByDeviceAndKey(device, key);
			if(deviceDataKey==null) {
				deviceDataKey = new DeviceDataKey();
				deviceDataKey.setDevice(device);
				deviceDataKey.setKey(key);
				DataKeyType dtk = DataKeyType.NUMERIC;
				deviceDataKey = deviceDataKeyRepo.save(deviceDataKey);
			}
		}
		DeviceDataNumeric deviceDataNumeric = new DeviceDataNumeric();
		deviceDataNumeric.setData(new BigDecimal(value));
		deviceDataNumeric.setKey(deviceDataKey);
		deviceDataNumeric.setTimestamp(Timestamp.valueOf(timestamp));
		deviceDataNumeric = deviceDataNumericRepo.save(deviceDataNumeric);
		return deviceDataNumeric.getId().toString();
	}
	

	@PostMapping(path="/device/postNumeric")
	@ResponseBody
	public String postNumeric(HttpServletRequest request,
            HttpServletResponse response, Model model) {
	
		String deviceUUID = request.getParameter("device");
		String timestamp = request.getParameter("timestamp");
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		
		return postNumeric(deviceUUID,timestamp,key,value);
	}

	@GetMapping(path="/device/postString")
	public String postString(
			@RequestParam(value = "device", required=true) String deviceUUID, 
			@RequestParam(value = "timestamp", required=true) String timestamp, 
			@RequestParam(value = "key",required=true)String key, 
			@RequestParam(value = "value",required=true)String value) {
		
		DeviceDataKey deviceDataKey = null;
		Device device = deviceRepo.findById(UUID.fromString(deviceUUID)).get();
		if(device!=null) {
			deviceDataKey = deviceDataKeyRepo.findByDeviceAndKey(device, key);
			if(deviceDataKey==null) {
				deviceDataKey = new DeviceDataKey();
				deviceDataKey.setDevice(device);
				deviceDataKey.setKey(key);
				DataKeyType dtk = DataKeyType.NUMERIC;
				deviceDataKey = deviceDataKeyRepo.save(deviceDataKey);
			}
		}
		DeviceDataString deviceDataString = new DeviceDataString();
		deviceDataString.setData(value);
		deviceDataString.setKey(deviceDataKey);
		deviceDataString.setTimestamp(Timestamp.valueOf(timestamp));
		deviceDataString = deviceDataStringRepo.save(deviceDataString);
		return deviceDataString.getId().toString();
	}
	
	@PostMapping(path="/device/postString")
	@ResponseBody
	public String postString(HttpServletRequest request,
            HttpServletResponse response, Model model) {
	
		String deviceUUID = request.getParameter("device");
		String timestamp = request.getParameter("timestamp");
		String key = request.getParameter("key");
		String value = request.getParameter("value");
		
		return postString(deviceUUID,timestamp,key,value);
	}


	@PostMapping("/device/postBinary")
	public String postBinary(
			@RequestParam(value = "device", required=true) String deviceUUID, 
			@RequestParam(value = "timestamp", required=true) String timestamp, 
			@RequestParam(value = "key",required=true)String key, 
			@RequestParam(value = "file",required=true)MultipartFile file) throws IOException 
	{
		
		DeviceDataKey deviceDataKey = null;
		Device device = deviceRepo.findById(UUID.fromString(deviceUUID)).get();
		if(device!=null) {
			deviceDataKey = deviceDataKeyRepo.findByDeviceAndKey(device, key);
			if(deviceDataKey==null) {
				deviceDataKey = new DeviceDataKey();
				deviceDataKey.setDevice(device);
				deviceDataKey.setKey(key);
				DataKeyType dtk = DataKeyType.NUMERIC;
				deviceDataKey = deviceDataKeyRepo.save(deviceDataKey);
			}
		}
		DeviceDataBinary deviceDataBinary = new DeviceDataBinary();
		deviceDataBinary.setData(file.getBytes());
		deviceDataBinary.setKey(deviceDataKey);
		deviceDataBinary.setTimestamp(Timestamp.valueOf(timestamp));
		deviceDataBinary = deviceDataBinaryRepo.save(deviceDataBinary);
		return deviceDataBinary.getId().toString();
	}
}

