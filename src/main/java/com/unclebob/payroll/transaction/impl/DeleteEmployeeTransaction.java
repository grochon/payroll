package com.unclebob.payroll.transaction.impl;

import static com.unclebob.payroll.db.PayrollDatabase.GlobalInstance.GpayrollDatabase;

import com.unclebob.transaction.Transaction;

public class DeleteEmployeeTransaction implements Transaction {

	private final int employeeId;

	public DeleteEmployeeTransaction(int employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public void execute() {
		GpayrollDatabase.deleteEmployee(employeeId);
	}

}
