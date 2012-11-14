package com.unclebob.payroll.impl;

import java.math.BigDecimal;

import com.unclebob.payroll.domain.Affiliation;
import com.unclebob.payroll.domain.Paycheck;

public class NoAffiliation implements Affiliation {

	@Override
	public BigDecimal calculateDeductions(Paycheck pc) {
		return BigDecimal.valueOf(0.0);
	}

}
