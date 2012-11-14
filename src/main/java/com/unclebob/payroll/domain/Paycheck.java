package com.unclebob.payroll.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Paycheck {

	private final Map<String, String> fields = new HashMap<String, String>();
	private final Date payPeriodStartDate;
	private final Date payPeriodEndDate;
	private BigDecimal grossPay;
	private BigDecimal deductions;
	private BigDecimal netPay;

	public Paycheck(Date payPeriodStartDate, Date payPeriodEndDate) {
		this.payPeriodStartDate = payPeriodStartDate;
		this.payPeriodEndDate = payPeriodEndDate;
	}

	public Date getPayPeriodStartDate() {
		return payPeriodStartDate;
	}
	
	public Date getPayPeriodEndDate() {
		return payPeriodEndDate;
	}

	public BigDecimal getGrossPay() {
		return grossPay;
	}

	public void setGrossPay(BigDecimal grossPay) {
		this.grossPay = grossPay;
	}
	
	public BigDecimal getDeductions() {
		return deductions;
	}

	public void setDeductions(BigDecimal deductions) {
		this.deductions = deductions;
	}
	
	public BigDecimal getNetPay() {
		return netPay;
	}

	public void setNetPay(BigDecimal netPay) {
		this.netPay = netPay;
	}

	public String getField(String name) {
		return fields.get(name);
	}

	public void setField(String name, String value) {
		fields.put(name, value);
	}

}
