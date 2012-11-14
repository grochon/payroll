package com.unclebob.payroll.impl;

import static java.util.Calendar.FRIDAY;

import java.util.Date;

import com.unclebob.payroll.domain.PaymentSchedule;
import com.unclebob.util.DateHelper;

public class WeeklySchedule implements PaymentSchedule {

	@Override
	public boolean isPayDay(Date payDate) {
		return DateHelper.dayOfWeek(payDate) == FRIDAY;
	}
	
	@Override
	public Date getPayPeriodStartDate(Date payDate) {
		return DateHelper.subtractDays(payDate, 6);
	}

}
