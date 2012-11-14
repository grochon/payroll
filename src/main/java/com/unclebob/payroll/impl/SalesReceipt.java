package com.unclebob.payroll.impl;

import java.math.BigDecimal;
import java.util.Date;

public class SalesReceipt {

	private final Date date;
	private final BigDecimal amount;

	public SalesReceipt(Date date, BigDecimal amount) {
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
