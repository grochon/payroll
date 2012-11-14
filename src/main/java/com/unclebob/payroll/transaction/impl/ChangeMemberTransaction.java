package com.unclebob.payroll.transaction.impl;

import static com.unclebob.payroll.db.PayrollDatabase.GlobalInstance.GpayrollDatabase;

import java.math.BigDecimal;

import com.unclebob.payroll.domain.Affiliation;
import com.unclebob.payroll.domain.Employee;
import com.unclebob.payroll.impl.UnionAffiliation;

public class ChangeMemberTransaction extends ChangeAffiliationTransaction {

	private final int memberId;
	private final BigDecimal weeklyDues;

	public ChangeMemberTransaction(int employeeId, int memberId, BigDecimal weeklyDues) {
		super(employeeId);
		this.memberId = memberId;
		this.weeklyDues = weeklyDues;
	}

	@Override
	protected Affiliation getAffiliation() {
		return new UnionAffiliation(memberId, weeklyDues);
	}
	
	@Override
	protected void recordMembership(Employee employee) {
		GpayrollDatabase.addUnionMember(memberId, employee);
	}
	
}
