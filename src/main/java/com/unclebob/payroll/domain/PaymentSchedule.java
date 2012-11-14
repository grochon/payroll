package com.unclebob.payroll.domain;

import java.util.Date;

public interface PaymentSchedule {

	boolean isPayDay(Date payDate);
	Date getPayPeriodStartDate(Date payDate);

}
