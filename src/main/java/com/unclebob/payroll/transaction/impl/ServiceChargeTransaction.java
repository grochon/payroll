package com.unclebob.payroll.transaction.impl;

import static com.unclebob.payroll.db.PayrollDatabase.GlobalInstance.GpayrollDatabase;

import java.math.BigDecimal;
import java.util.Date;

import com.unclebob.payroll.domain.Affiliation;
import com.unclebob.payroll.domain.Employee;
import com.unclebob.payroll.impl.ServiceCharge;
import com.unclebob.payroll.impl.UnionAffiliation;
import com.unclebob.transaction.Transaction;

public class ServiceChargeTransaction implements Transaction {

	private final int memberId;
	private final Date date;
	private final BigDecimal charge;

	public ServiceChargeTransaction(int memberId, Date date, BigDecimal charge) {
		this.memberId = memberId;
		this.date = date;
		this.charge = charge;
	}

	@Override
	public void execute() {
		Employee e = GpayrollDatabase.getUnionMember(memberId);
		Affiliation af = e.getAffiliation();
		if (af instanceof UnionAffiliation) {
			UnionAffiliation uaf = (UnionAffiliation) af;
			uaf.addServiceCharge(new ServiceCharge(date, charge));
		}
	}

}
