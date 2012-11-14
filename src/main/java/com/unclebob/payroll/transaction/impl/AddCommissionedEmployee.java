package com.unclebob.payroll.transaction.impl;

import java.math.BigDecimal;

import com.unclebob.payroll.domain.PaymentClassification;
import com.unclebob.payroll.domain.PaymentSchedule;
import com.unclebob.payroll.impl.BiweeklySchedule;
import com.unclebob.payroll.impl.CommissionedClassification;

public class AddCommissionedEmployee extends AddEmployeeTransaction {

	private final BigDecimal salary;
	private final BigDecimal commissionRate;

	public AddCommissionedEmployee(int employeeId, String name, String address,
			BigDecimal salary, BigDecimal commissionRate) {
		super(employeeId, name, address);
		this.salary = salary;
		this.commissionRate = commissionRate;
	}

	@Override
	protected PaymentClassification getClassification() {
		return new CommissionedClassification(salary, commissionRate);
	}

	@Override
	protected PaymentSchedule getSchedule() {
		return new BiweeklySchedule();
	}

}
