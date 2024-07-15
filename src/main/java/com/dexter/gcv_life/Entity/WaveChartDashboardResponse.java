package com.dexter.gcv_life.Entity;

import java.math.BigDecimal;

public class WaveChartDashboardResponse {
	private BigDecimal upperBand;
	private BigDecimal lowerBand;
	private BigDecimal simpleMovingAverage;
	private BigDecimal trendCurrentMonth;
	private String monthYear;

	public BigDecimal getUpperBand() {
		return upperBand;
	}

	public void setUpperBand(BigDecimal upperBand) {
		this.upperBand = upperBand;
	}

	public BigDecimal getLowerBand() {
		return lowerBand;
	}

	public void setLowerBand(BigDecimal lowerBand) {
		this.lowerBand = lowerBand;
	}

	public BigDecimal getSimpleMovingAverage() {
		return simpleMovingAverage;
	}

	public void setSimpleMovingAverage(BigDecimal simpleMovingAverage) {
		this.simpleMovingAverage = simpleMovingAverage;
	}

	public BigDecimal getTrendCurrentMonth() {
		return trendCurrentMonth;
	}

	public void setTrendCurrentMonth(BigDecimal trendCurrentMonth) {
		this.trendCurrentMonth = trendCurrentMonth;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	public WaveChartDashboardResponse(BigDecimal upperBand, BigDecimal lowerBand, BigDecimal simpleMovingAverage,
			BigDecimal trendCurrentMonth, String monthYear) {
		super();
		this.upperBand = upperBand;
		this.lowerBand = lowerBand;
		this.simpleMovingAverage = simpleMovingAverage;
		this.trendCurrentMonth = trendCurrentMonth;
		this.monthYear = monthYear;
	}

	@Override
	public String toString() {
		return "WaveChartDashboardResponse [upperBand=" + upperBand + ", lowerBand=" + lowerBand
				+ ", simpleMovingAverage=" + simpleMovingAverage + ", trendCurrentMonth=" + trendCurrentMonth
				+ ", monthYear=" + monthYear + "]";
	}

}