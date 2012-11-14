package com.unclebob.payroll.impl;

import java.math.BigDecimal;

import com.unclebob.payroll.domain.Paycheck;
import com.unclebob.payroll.domain.PaymentClassification;

public class SalariedClassification extends PaymentClassification {

	private final BigDecimal salary;

	public SalariedClassification(BigDecimal salary) {
		this.salary = salary;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	@Override
	public BigDecimal calculatePay(Paycheck pc) {
		return salary;
	}

}
