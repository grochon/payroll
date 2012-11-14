package com.unclebob.payroll.impl;

import java.math.BigDecimal;
import java.util.Date;

public class ServiceCharge {

	private final Date date;
	private final BigDecimal amount;

	public ServiceCharge(Date date, BigDecimal amount) {
		this.date = date;
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

}
