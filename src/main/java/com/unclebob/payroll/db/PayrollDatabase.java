package com.unclebob.payroll.db;

import java.util.List;

import com.unclebob.payroll.domain.Employee;

public interface PayrollDatabase {

	class GlobalInstance {
		public static PayrollDatabase GpayrollDatabase;
	}
	
	void clear();

	void addEmployee(int employeeId, Employee employee);
	void deleteEmployee(int employeeId);
	Employee getEmployee(int employeeId);
	List<Integer> getAllEmployeeIds();

	void addUnionMember(int memberId, Employee employee);
	void deleteUnionMember(int memberId);
	Employee getUnionMember(int memberId);

}
