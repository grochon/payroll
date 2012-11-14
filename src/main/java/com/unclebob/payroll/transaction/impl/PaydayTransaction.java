package com.unclebob.payroll.transaction.impl;

import static com.unclebob.payroll.db.PayrollDatabase.GlobalInstance.GpayrollDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unclebob.payroll.domain.Employee;
import com.unclebob.payroll.domain.Paycheck;
import com.unclebob.transaction.Transaction;

public class PaydayTransaction implements Transaction {

	private final Map<Integer, Paycheck> paychecks = new HashMap<Integer, Paycheck>();
	private final Date payDate;
	

	public PaydayTransaction(Date payDate) {
		this.payDate = payDate;
	}

	@Override
	public void execute() {
		List<Integer> empIds = GpayrollDatabase.getAllEmployeeIds();
		for (int empId : empIds) {
			Employee e = GpayrollDatabase.getEmployee(empId);
			if (e != null && e.isPayDay(payDate)) {
				Paycheck pc = new Paycheck(e.getPayPeriodStartDate(payDate), payDate);
				paychecks.put(empId, pc);
				e.payDay(pc);
			}
		}
	}

	public Paycheck getPaycheck(int employeeId) {
		return paychecks.get(employeeId);
	}

}
