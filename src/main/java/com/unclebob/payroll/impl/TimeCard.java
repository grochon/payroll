package com.unclebob.payroll.impl;

import java.math.BigDecimal;
import java.util.Date;

public class TimeCard {

	private final Date date;
	private final BigDecimal hours;

	public TimeCard(Date date, BigDecimal hours) {
		this.date = date;
		this.hours = hours;
	}

	public Date getDate() {
		return date;
	}
	
	public BigDecimal getHours() {
		return hours;
	}

}
