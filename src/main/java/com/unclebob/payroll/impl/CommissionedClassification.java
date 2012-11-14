package com.unclebob.payroll.impl;

import static java.math.BigDecimal.ROUND_HALF_UP;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.unclebob.payroll.domain.Paycheck;
import com.unclebob.payroll.domain.PaymentClassification;

public class CommissionedClassification extends PaymentClassification {

	private static final BigDecimal MONTHS_PER_YEAR = BigDecimal.valueOf(12);
	private static final BigDecimal PAYCHECKS_PER_YEAR = BigDecimal.valueOf(26);
	
	private final Map<Date, SalesReceipt> salesReceipts = new HashMap<Date, SalesReceipt>();
	private final BigDecimal salary;
	private final BigDecimal commissionRate;

	public CommissionedClassification(BigDecimal salary, BigDecimal commissionRate) {
		this.salary = salary.setScale(2);
		this.commissionRate = commissionRate;
	}
	
	public BigDecimal getSalary() {
		return salary;
	}

	public BigDecimal getCommissionRate() {
		return commissionRate;
	}

	public SalesReceipt getSalesReceipt(Date date) {
		return salesReceipts.get(date);
	}

	public void addSalesReceipt(SalesReceipt salesReceipt) {
		salesReceipts.put(salesReceipt.getDate(), salesReceipt);
	}

	@Override
	public BigDecimal calculatePay(Paycheck pc) {
		BigDecimal basePay = salary.multiply(MONTHS_PER_YEAR).divide(PAYCHECKS_PER_YEAR, ROUND_HALF_UP);
		BigDecimal sales = calculateSales(pc);
		BigDecimal commissionPay = commissionRate.movePointLeft(2).multiply(sales).setScale(2, ROUND_HALF_UP);
		return basePay.add(commissionPay);
	}

	private BigDecimal calculateSales(Paycheck pc) {
		BigDecimal sales = BigDecimal.ZERO;
		for (SalesReceipt receipt : salesReceipts.values()) {
			if (isInPayPeriod(receipt.getDate(), pc)) {
				sales = sales.add(receipt.getAmount());
			}
		}
		return sales;
	}

}
