package com.unclebob.payroll.impl;

import com.unclebob.payroll.domain.Paycheck;
import com.unclebob.payroll.domain.PaymentMethod;

public class HoldMethod implements PaymentMethod {

	@Override
	public void pay(Paycheck pc) {
		pc.setField("Disposition", "Hold");
	}

}
