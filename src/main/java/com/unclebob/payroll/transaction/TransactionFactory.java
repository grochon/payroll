package com.unclebob.payroll.transaction;

import java.math.BigDecimal;
import java.util.Date;

import com.unclebob.transaction.Transaction;

public interface TransactionFactory {

	Transaction makeAddCommissionedTransaction(int employeeId, String name, String address, BigDecimal salary, BigDecimal commissionRate);

	Transaction makeAddHourlyTransaction(int employeeId, String name, String address, BigDecimal hourlyRate);

	Transaction makeAddSalariedTransaction(int employeeId, String name, String address, BigDecimal salary);

	Transaction makeChangeCommissionedTransaction(int employeeId, BigDecimal salary, BigDecimal commissionRate);

	Transaction makeChangeHourlyTransaction(int employeeId, BigDecimal hourlyRate);

	Transaction makeChangeMemberTransaction(int employeeId, int memberId, BigDecimal weeklyDues);

	Transaction makeChangeNameTransaction(int employeeId, String name);

	Transaction makeChangeSalariedTransaction(int employeeId, BigDecimal salary);

	Transaction makeChangeUnaffiliatedTransaction(int employeeId);

	Transaction makeDeleteEmployeeTransaction(int employeeId);

	Transaction makePaydayTransaction(Date payDate);

	Transaction makeSalesReceiptTransaction(Date date, BigDecimal amount, int employeeId);

	Transaction makeServiceChargeTransaction(int memberId, Date date, BigDecimal charge);

	Transaction makeTimeCardTransaction(Date date, BigDecimal hours, int employeeId);

}