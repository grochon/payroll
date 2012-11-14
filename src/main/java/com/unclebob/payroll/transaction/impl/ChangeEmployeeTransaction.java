package com.unclebob.payroll.transaction.impl;

import static com.unclebob.payroll.db.PayrollDatabase.GlobalInstance.GpayrollDatabase;

import com.unclebob.payroll.domain.Employee;
import com.unclebob.transaction.Transaction;

public abstract class ChangeEmployeeTransaction implements Transaction {

	private final int employeeId;

	public ChangeEmployeeTransaction(int employeeId) {
		this.employeeId = employeeId;
	}
	
	@Override
	public void execute() {
		Employee e = GpayrollDatabase.getEmployee(employeeId);
		if (e != null) {
			change(e);
		}
	}
	
	protected abstract void change(Employee employee);
	
}
