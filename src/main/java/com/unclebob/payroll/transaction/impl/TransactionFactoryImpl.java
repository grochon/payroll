package com.unclebob.payroll.transaction.impl;

import java.math.BigDecimal;
import java.util.Date;

import com.unclebob.payroll.transaction.TransactionFactory;
import com.unclebob.transaction.Transaction;

public class TransactionFactoryImpl implements TransactionFactory {

	@Override
	public Transaction makeAddCommissionedTransaction(int employeeId, String name, String address, BigDecimal salary, BigDecimal commissionRate) {
		return new AddCommissionedEmployee(employeeId, name, address, salary, commissionRate);
	}

	@Override
	public Transaction makeAddHourlyTransaction(int employeeId, String name, String address, BigDecimal hourlyRate) {
		return new AddHourlyEmployee(employeeId, name, address, hourlyRate);
	}

	@Override
	public Transaction makeAddSalariedTransaction(int employeeId, String name, String address, BigDecimal salary) {
		return new AddSalariedEmployee(employeeId, name, address, salary);
	}

	@Override
	public Transaction makeChangeCommissionedTransaction(int employeeId, BigDecimal salary, BigDecimal commissionRate) {
		return new ChangeCommissionedTransaction(employeeId, salary, commissionRate);
	}

	@Override
	public Transaction makeChangeHourlyTransaction(int employeeId, BigDecimal hourlyRate) {
		return new ChangeHourlyTransaction(employeeId, hourlyRate);
	}

	@Override
	public Transaction makeChangeMemberTransaction(int employeeId, int memberId, BigDecimal weeklyDues) {
		return new ChangeMemberTransaction(employeeId, memberId, weeklyDues);
	}

	@Override
	public Transaction makeChangeNameTransaction(int employeeId, String name) {
		return new ChangeNameTransaction(employeeId, name);
	}

	@Override
	public Transaction makeChangeSalariedTransaction(int employeeId, BigDecimal salary) {
		return new ChangeSalariedTransaction(employeeId, salary);
	}

	@Override
	public Transaction makeChangeUnaffiliatedTransaction(int employeeId) {
		return new ChangeUnaffiliatedTransaction(employeeId);
	}

	@Override
	public Transaction makeDeleteEmployeeTransaction(int employeeId) {
		return new DeleteEmployeeTransaction(employeeId);
	}

	@Override
	public Transaction makePaydayTransaction(Date payDate) {
		return new PaydayTransaction(payDate);
	}

	@Override
	public Transaction makeSalesReceiptTransaction(Date date, BigDecimal amount, int employeeId) {
		return new SalesReceiptTransaction(date, amount, employeeId);
	}

	@Override
	public Transaction makeServiceChargeTransaction(int memberId, Date date, BigDecimal charge) {
		return new ServiceChargeTransaction(memberId, date, charge);
	}

	@Override
	public Transaction makeTimeCardTransaction(Date date, BigDecimal hours, int employeeId) {
		return new TimeCardTransaction(date, hours, employeeId);
	}

}
