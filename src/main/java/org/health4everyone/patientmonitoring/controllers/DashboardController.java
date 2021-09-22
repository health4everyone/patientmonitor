package org.health4everyone.patientmonitoring.controllers;

import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.health4everyone.patientmonitoring.datatables.AppUtil;
import org.health4everyone.patientmonitoring.models.ChartNumericValue;
import org.health4everyone.patientmonitoring.models.DashboardData;
import org.health4everyone.patientmonitoring.models.ERole;
import org.health4everyone.patientmonitoring.repositories.DeviceDataBinaryRepository;
import org.health4everyone.patientmonitoring.repositories.DeviceDataNumericRepository;
import org.health4everyone.patientmonitoring.repositories.DeviceDataStringRepository;
import org.health4everyone.patientmonitoring.repositories.DeviceRepository;
import org.health4everyone.patientmonitoring.repositories.RoleRepository;
import org.health4everyone.patientmonitoring.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
@ResponseBody
public class DashboardController {

	@Autowired
	UserRepository userRepo;
	@Autowired
	RoleRepository roleRepo;
	@Autowired
	DeviceRepository deviceRepo;
	@Autowired
	DeviceDataNumericRepository deviceDataNumericRepo;
	@Autowired
	DeviceDataStringRepository deviceDataStringRepo;
	@Autowired
	DeviceDataBinaryRepository deviceDataBinaryRepo;

	@RequestMapping(value = "/dashboard/data", method = { RequestMethod.GET, RequestMethod.POST })
	public DashboardData dashboardData(Model mode, Principal principal,@RequestParam(value = "days", defaultValue = "15", required=true)int dataDays) {
		DashboardData dd = new DashboardData();
		dd.totalUsers = Long.valueOf(userRepo.count());
		dd.totalDevices = Long.valueOf(deviceRepo.count());
		dd.totalValues = Long
				.valueOf(deviceDataNumericRepo.count() + deviceDataStringRepo.count() + deviceDataBinaryRepo.count());
		double lastVal = 0;
		double currVal = 0;
		double percVal = 0;
		for (int i = 0; i < dataDays; i++) {
			java.sql.Date day = new java.sql.Date((new java.util.Date()).getTime() - 24 * 3600 * 1000 * (14-i));
			Long counts = deviceDataNumericRepo.countGroupByDate(day);
			if (i==dataDays-1)
				currVal = counts.doubleValue();
			if (i==dataDays-2)
				lastVal = counts.doubleValue();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM");
			String dayStr = sdf.format(day);
			ChartNumericValue cnv = new ChartNumericValue();
			cnv.x = dayStr;
			cnv.y = Long.valueOf(counts);
			dd.chartData.add(cnv);
			if (counts > dd.maxValuesCounts)
				dd.maxValuesCounts = counts;
			if (counts < dd.minValuesCounts)
				dd.minValuesCounts = counts;
		}
		if(lastVal==0)
			percVal = 100;
		else {
			percVal = currVal/lastVal*100;
			if(percVal==0)
				percVal = -100;
			else if(percVal>100) 
				percVal -=100;
			else 
				percVal = 100-percVal;
		}
		dd.dailyValuesPercent = Double.valueOf(percVal);
		return dd;
	}

}
