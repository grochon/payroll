package com.unclebob.payroll.transaction.impl;

import static com.unclebob.payroll.db.PayrollDatabase.GlobalInstance.GpayrollDatabase;

import com.unclebob.payroll.domain.Affiliation;
import com.unclebob.payroll.domain.Employee;
import com.unclebob.payroll.domain.PaymentClassification;
import com.unclebob.payroll.domain.PaymentMethod;
import com.unclebob.payroll.domain.PaymentSchedule;
import com.unclebob.payroll.impl.HoldMethod;
import com.unclebob.payroll.impl.NoAffiliation;
import com.unclebob.transaction.Transaction;

public abstract class AddEmployeeTransaction implements Transaction {

	private final int employeeId;
	private final String name;
	private final String address;

	public AddEmployeeTransaction(int employeeId, String name, String address) {
		this.employeeId = employeeId;
		this.name = name;
		this.address = address;
	}

	@Override
	public final void execute() {
		PaymentClassification pc = getClassification();
		PaymentSchedule ps = getSchedule();
		PaymentMethod pm = new HoldMethod();
		Affiliation af = new NoAffiliation();
		Employee e = new Employee(employeeId, name, address);
		e.setClassification(pc);
		e.setSchedule(ps);
		e.setMethod(pm);
		e.setAffiliation(af);
		GpayrollDatabase.addEmployee(employeeId, e);
	}


	protected abstract PaymentClassification getClassification();
	protected abstract PaymentSchedule getSchedule();
	
}
