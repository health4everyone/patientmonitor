package org.health4everyone.patientmonitoring.models;

import java.util.ArrayList;

public class DashboardData {
	public Long totalDevices = Long.valueOf(0);
	public Long totalUsers = Long.valueOf(0);
	public Long totalValues = Long.valueOf(0);
	public ArrayList< ChartNumericValue > chartData = new ArrayList< ChartNumericValue >();
	public Long maxValuesCounts = Long.MIN_VALUE;
	public Long minValuesCounts = Long.MAX_VALUE;
	public Double dailyValuesPercent = Double.valueOf(0);
}
