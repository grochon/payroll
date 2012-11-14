package com.unclebob.payroll.transaction.impl;

import static com.unclebob.payroll.db.PayrollDatabase.GlobalInstance.GpayrollDatabase;

import com.unclebob.payroll.domain.Affiliation;
import com.unclebob.payroll.domain.Employee;
import com.unclebob.payroll.impl.NoAffiliation;
import com.unclebob.payroll.impl.UnionAffiliation;

public class ChangeUnaffiliatedTransaction extends ChangeAffiliationTransaction {

	public ChangeUnaffiliatedTransaction(int employeeId) {
		super(employeeId);
	}

	@Override
	protected Affiliation getAffiliation() {
		return new NoAffiliation();
	}
	
	@Override
	protected void recordMembership(Employee employee) {
		Affiliation af = employee.getAffiliation();
		if (af instanceof UnionAffiliation) {
			UnionAffiliation uf = (UnionAffiliation) af;
			GpayrollDatabase.deleteUnionMember(uf.getMemberId());			
		}
	}
	
}
