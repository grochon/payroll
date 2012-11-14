package com.unclebob.payroll.domain;

import java.math.BigDecimal;
import java.util.Date;


public class Employee {

	private final int employeeId;
	private String name;
	private String address;
	private PaymentClassification classification;
	private PaymentSchedule schedule;
	private PaymentMethod method;
	private Affiliation affiliation;

	public Employee(int employeeId, String name, String address) {
		this.employeeId = employeeId;
		this.name = name;
		this.address = address;
	}

	public int getEmployeeId() {
		return employeeId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public PaymentClassification getClassification() {
		return classification;
	}

	public void setClassification(PaymentClassification classification) {
		this.classification = classification;
	}
	
	public PaymentSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(PaymentSchedule schedule) {
		this.schedule = schedule;
	}
	
	public PaymentMethod getMethod() {
		return method;
	}

	public void setMethod(PaymentMethod method) {
		this.method = method;
	}

	public void setAffiliation(Affiliation affiliation) {
		this.affiliation = affiliation;
	}

	public Affiliation getAffiliation() {
		return affiliation;
	}

	public boolean isPayDay(Date payDate) {
		return schedule.isPayDay(payDate);
	}

	public Date getPayPeriodStartDate(Date payDate) {
		return schedule.getPayPeriodStartDate(payDate);
	}
	
	public void payDay(Paycheck pc) {
		BigDecimal grossPay = classification.calculatePay(pc);
		BigDecimal deductions = affiliation.calculateDeductions(pc);
		BigDecimal netPay = grossPay.subtract(deductions);
		pc.setGrossPay(grossPay);
		pc.setDeductions(deductions);
		pc.setNetPay(netPay);
		method.pay(pc);
	}

}

