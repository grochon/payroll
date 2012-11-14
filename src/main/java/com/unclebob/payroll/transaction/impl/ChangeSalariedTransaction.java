package com.unclebob.payroll.transaction.impl;

import java.math.BigDecimal;

import com.unclebob.payroll.domain.PaymentClassification;
import com.unclebob.payroll.domain.PaymentSchedule;
import com.unclebob.payroll.impl.MonthlySchedule;
import com.unclebob.payroll.impl.SalariedClassification;

public class ChangeSalariedTransaction extends ChangeClassificationTransaction {

	private final BigDecimal salary;

	public ChangeSalariedTransaction(int employeeId, BigDecimal salary) {
		super(employeeId);
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
