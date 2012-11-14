package com.unclebob.payroll.transaction.impl;

import com.unclebob.payroll.domain.Affiliation;
import com.unclebob.payroll.domain.Employee;

public abstract class ChangeAffiliationTransaction extends ChangeEmployeeTransaction {

	public ChangeAffiliationTransaction(int employeeId) {
		super(employeeId);
	}

	@Override
	protected void change(Employee employee) {
		recordMembership(employee);
		employee.setAffiliation(getAffiliation());
	}
	
	protected abstract void recordMembership(Employee employee);

	protected abstract Affiliation getAffiliation();

}