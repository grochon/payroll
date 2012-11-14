package com.unclebob.payroll.db.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.unclebob.payroll.db.PayrollDatabase;
import com.unclebob.payroll.domain.Employee;

public class InMemoryPayrollDatabase implements PayrollDatabase {

	private final Map<Integer, Employee> employees = new HashMap<Integer, Employee>();
	private final Map<Integer, Employee> unionMembers = new HashMap<Integer, Employee>();
	
	
	@Override
	public Employee getEmployee(int employeeId) {
		return employees.get(employeeId);
	}

	@Override
	public void addEmployee(int employeeId, Employee employee) {
		employees.put(employeeId, employee);
	}

	@Override
	public void deleteEmployee(int employeeId) {
		employees.remove(employeeId);
	}
	
	@Override
	public List<Integer> getAllEmployeeIds() {
		return new ArrayList<Integer>(employees.keySet());
	}
	
	@Override
	public Employee getUnionMember(int memberId) {
		return unionMembers.get(memberId);
	}
	
	@Override
	public void addUnionMember(int memberId, Employee employee) {
		unionMembers.put(memberId, employee);
	}
	
	@Override
	public void deleteUnionMember(int memberId) {
		unionMembers.remove(memberId);
	}
	
	@Override
	public void clear() {
		employees.clear();
		unionMembers.clear();
	}

}
