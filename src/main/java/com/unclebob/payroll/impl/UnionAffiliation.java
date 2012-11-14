package com.unclebob.payroll.impl;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.FRIDAY;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.unclebob.payroll.domain.Affiliation;
import com.unclebob.payroll.domain.Paycheck;
import com.unclebob.util.DateHelper;

public class UnionAffiliation implements Affiliation {

	private final Map<Date, ServiceCharge> serviceCharges = new HashMap<Date, ServiceCharge>();
	private final int memberId;
	private final BigDecimal weeklyDues;
	
	public UnionAffiliation(int memberId, BigDecimal weeklyDues) {
		this.memberId = memberId;
		this.weeklyDues = weeklyDues;
	}

	public ServiceCharge getServiceCharge(Date date) {
		return serviceCharges.get(date);
	}

	public void addServiceCharge(ServiceCharge charge) {
		serviceCharges.put(charge.getDate(), charge);
	}

	public int getMemberId() {
		return memberId;
	}
	
	public BigDecimal getWeeklyDues() {
		return weeklyDues;
	}

	@Override
	public BigDecimal calculateDeductions(Paycheck pc) {
		return calculateUnionDues(pc).add(calculateServiceCharges(pc));
	}

	private BigDecimal calculateUnionDues(Paycheck pc) {
		int fridays = numberOfFridaysInPayPeriod(pc.getPayPeriodStartDate(), pc.getPayPeriodEndDate());
		return weeklyDues.multiply(BigDecimal.valueOf(fridays)).setScale(2, ROUND_HALF_UP);
	}

	private BigDecimal calculateServiceCharges(Paycheck pc) {
		BigDecimal charges = BigDecimal.ZERO;
		for (ServiceCharge charge : serviceCharges.values()) {
			if (DateHelper.isBetween(charge.getDate(), pc.getPayPeriodStartDate(), pc.getPayPeriodEndDate())) {
				charges = charges.add(charge.getAmount());
			}
		}
		return charges;
	}
	
	private int numberOfFridaysInPayPeriod(Date payPeriodStartDate, Date payPeriodEndDate) {
		int fridays = 0;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(payPeriodStartDate);
		while (!cal.getTime().after(payPeriodEndDate)) {
			if (cal.get(DAY_OF_WEEK) == FRIDAY) {
				fridays++;
			}
			cal.add(DATE, 1);
		}
		
		return fridays;
	}

}
