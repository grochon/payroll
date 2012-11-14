package com.unclebob.payroll.transaction.impl;

import com.unclebob.payroll.domain.Employee;
import com.unclebob.payroll.domain.PaymentClassification;
import com.unclebob.payroll.domain.PaymentSchedule;

public abstract class ChangeClassificationTransaction extends ChangeEmployeeTransaction {

	public ChangeClassificationTransaction(int employeeId) {
		super(employeeId);
	}
	
	@Override
	protected void change(Employee employee) {
		employee.setClassification(getClassification());
		employee.setSchedule(getSchedule());
	}

	protected abstract PaymentClassification getClassification();
	
	protected abstract PaymentSchedule getSchedule();

}
