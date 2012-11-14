package com.unclebob.payroll.transaction.impl;

import java.math.BigDecimal;

import com.unclebob.payroll.domain.PaymentClassification;
import com.unclebob.payroll.domain.PaymentSchedule;
import com.unclebob.payroll.impl.MonthlySchedule;
import com.unclebob.payroll.impl.SalariedClassification;

public class AddSalariedEmployee extends AddEmployeeTransaction {

	private final BigDecimal salary;

	public AddSalariedEmployee(int employeeId, String name, String address, BigDecimal salary) {
		super(employeeId, name, address);
		this.salary = salary;
	}

	@Override
	protected PaymentClassification getClassification() {
		return new SalariedClassification(salary);
	}

	@Override
	protected PaymentSchedule getSchedule() {
		return new MonthlySchedule();
	}

}
