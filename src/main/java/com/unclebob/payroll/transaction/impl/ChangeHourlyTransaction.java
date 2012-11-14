package com.unclebob.payroll.transaction.impl;

import java.math.BigDecimal;

import com.unclebob.payroll.domain.PaymentClassification;
import com.unclebob.payroll.domain.PaymentSchedule;
import com.unclebob.payroll.impl.HourlyClassification;
import com.unclebob.payroll.impl.WeeklySchedule;

public class ChangeHourlyTransaction extends ChangeClassificationTransaction {

	private final BigDecimal hourlyRate;

	public ChangeHourlyTransaction(int employeeId, BigDecimal hourlyRate) {
		super(employeeId);
		this.hourlyRate = hourlyRate;
	}

	@Override
	protected PaymentClassification getClassification() {
		return new HourlyClassification(hourlyRate);
	}
	
	@Override
	protected PaymentSchedule getSchedule() {
		return new WeeklySchedule();
	}
	
}
