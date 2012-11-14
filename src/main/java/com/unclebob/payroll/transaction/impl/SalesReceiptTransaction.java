package com.unclebob.payroll.transaction.impl;

import static com.unclebob.payroll.db.PayrollDatabase.GlobalInstance.GpayrollDatabase;

import java.math.BigDecimal;
import java.util.Date;

import com.unclebob.payroll.domain.Employee;
import com.unclebob.payroll.domain.PaymentClassification;
import com.unclebob.payroll.impl.CommissionedClassification;
import com.unclebob.payroll.impl.SalesReceipt;
import com.unclebob.transaction.Transaction;

public class SalesReceiptTransaction implements Transaction {

	private final Date date;
	private final BigDecimal amount;
	private final int employeeId;

	public SalesReceiptTransaction(Date date, BigDecimal amount, int employeeId) {
		this.date = date;
		this.amount = amount;
		this.employeeId = employeeId;
	}

	@Override
	public void execute() {
		Employee e = GpayrollDatabase.getEmployee(employeeId);
		if (e != null) {
			PaymentClassification pc = e.getClassification();
			if (pc instanceof CommissionedClassification) {
				CommissionedClassification cc = (CommissionedClassification) pc;
				cc.addSalesReceipt(new SalesReceipt(date, amount));
			} else {
				throw new RuntimeException("Tried to add sales receipt to non-commissioned employee");
			}
		} else {
			throw(new RuntimeException("No such employee."));
		}
	}

}
