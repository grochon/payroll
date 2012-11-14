package com.unclebob.payroll.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.unclebob.payroll.domain.PaymentSchedule;
import com.unclebob.util.DateHelper;

public class BiweeklySchedule implements PaymentSchedule {

	private static final Set<Date> paydates = calculateBiWeeklyPayDates();
	
	@Override
	public boolean isPayDay(Date payDate) {
		return paydates.contains(payDate);
	}

	@Override
	public Date getPayPeriodStartDate(Date payDate) {
		return DateHelper.subtractDays(payDate, 13);
	}
	
	// TODO: this only works from 1950 - 2050, need to calculate more dates for a payday outside that range
	private static Set<Date> calculateBiWeeklyPayDates() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(1950, 0, 6);
		
		Set<Date> paydates = new HashSet<Date>();
		for (int i = 0; i < 2610; i++) {
			paydates.add(cal.getTime());
			cal.add(Calendar.DATE, 14);
		}
		return paydates;
	}

}
