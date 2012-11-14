package com.unclebob.payroll.transaction.impl;

import static com.unclebob.payroll.db.PayrollDatabase.GlobalInstance.GpayrollDatabase;

import java.math.BigDecimal;
import java.util.Date;

import com.unclebob.payroll.domain.Employee;
import com.unclebob.payroll.domain.PaymentClassification;
import com.unclebob.payroll.impl.HourlyClassification;
import com.unclebob.payroll.impl.TimeCard;
import com.unclebob.transaction.Transaction;

public class TimeCardTransaction implements Transaction {

	private final Date date;
	private final BigDecimal hours;
	private final int employeeId;

	public TimeCardTransaction(Date date, BigDecimal hours, int employeeId) {
		this.date = date;
		this.hours = hours;
		this.employeeId = employeeId;
	}

	@Override
	public void execute() {
		Employee e = GpayrollDatabase.getEmployee(employeeId);
		if (e != null) {
			PaymentClassification pc = e.getClassification();
			if (pc instanceof HourlyClassification) {
				HourlyClassification hc = (HourlyClassification) pc;
				hc.addTimeCard(new TimeCard(date, hours));
			} else {
				throw new RuntimeException("Tried to add timecard to non-hourly employee");
			}
		} else {
			throw(new RuntimeException("No such employee."));
		}
	}

}
