package com.payroll;

import static com.unclebob.payroll.db.PayrollDatabase.GlobalInstance.GpayrollDatabase;
import static java.util.Calendar.DATE;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.unclebob.payroll.db.impl.InMemoryPayrollDatabase;
import com.unclebob.payroll.domain.Affiliation;
import com.unclebob.payroll.domain.Employee;
import com.unclebob.payroll.domain.Paycheck;
import com.unclebob.payroll.domain.PaymentClassification;
import com.unclebob.payroll.domain.PaymentMethod;
import com.unclebob.payroll.domain.PaymentSchedule;
import com.unclebob.payroll.impl.BiweeklySchedule;
import com.unclebob.payroll.impl.CommissionedClassification;
import com.unclebob.payroll.impl.HoldMethod;
import com.unclebob.payroll.impl.HourlyClassification;
import com.unclebob.payroll.impl.MonthlySchedule;
import com.unclebob.payroll.impl.NoAffiliation;
import com.unclebob.payroll.impl.SalariedClassification;
import com.unclebob.payroll.impl.SalesReceipt;
import com.unclebob.payroll.impl.ServiceCharge;
import com.unclebob.payroll.impl.TimeCard;
import com.unclebob.payroll.impl.UnionAffiliation;
import com.unclebob.payroll.impl.WeeklySchedule;
import com.unclebob.payroll.transaction.impl.AddCommissionedEmployee;
import com.unclebob.payroll.transaction.impl.AddHourlyEmployee;
import com.unclebob.payroll.transaction.impl.AddSalariedEmployee;
import com.unclebob.payroll.transaction.impl.ChangeCommissionedTransaction;
import com.unclebob.payroll.transaction.impl.ChangeHourlyTransaction;
import com.unclebob.payroll.transaction.impl.ChangeMemberTransaction;
import com.unclebob.payroll.transaction.impl.ChangeNameTransaction;
import com.unclebob.payroll.transaction.impl.ChangeSalariedTransaction;
import com.unclebob.payroll.transaction.impl.ChangeUnaffiliatedTransaction;
import com.unclebob.payroll.transaction.impl.DeleteEmployeeTransaction;
import com.unclebob.payroll.transaction.impl.PaydayTransaction;
import com.unclebob.payroll.transaction.impl.SalesReceiptTransaction;
import com.unclebob.payroll.transaction.impl.ServiceChargeTransaction;
import com.unclebob.payroll.transaction.impl.TimeCardTransaction;

public class PayrollTest {

	@Before
	public void setup() {
		GpayrollDatabase = new InMemoryPayrollDatabase();
	}
	
	@Test
	public void addSalariedEmployee() throws Exception {
		final int empId = 1;
		final BigDecimal salary = BigDecimal.valueOf(1000.00);
		AddSalariedEmployee t = new AddSalariedEmployee(empId, "Bob", "Home", salary);
		t.execute();
		
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e.getName(), is("Bob"));
		
		PaymentClassification pc = e.getClassification();
		assertThat(pc, instanceOf(SalariedClassification.class));
		
		SalariedClassification sc = (SalariedClassification) pc;
		assertThat(sc.getSalary(), is(salary));
		
		PaymentSchedule ps = e.getSchedule();
		assertThat(ps, instanceOf(MonthlySchedule.class));

		PaymentMethod pm = e.getMethod();
		assertThat(pm, instanceOf(HoldMethod.class));
		
		Affiliation af = e.getAffiliation();
		assertThat(af, instanceOf(NoAffiliation.class));
	}

	@Test
	public void addCommissionedEmployee() throws Exception {
		final int empId = 1;
		final BigDecimal salary = new BigDecimal("2500.00");
		final BigDecimal commissionRate = BigDecimal.valueOf(3.2);
		AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Bob", "Home", salary, commissionRate);
		t.execute();
		
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e.getName(), is("Bob"));
		
		PaymentClassification pc = e.getClassification();
		assertThat(pc, instanceOf(CommissionedClassification.class));
		
		CommissionedClassification cc = (CommissionedClassification) pc;
		assertThat(cc.getSalary(), is(salary));
		assertThat(cc.getCommissionRate(), is(commissionRate));
		
		PaymentSchedule ps = e.getSchedule();
		assertThat(ps, instanceOf(BiweeklySchedule.class));
		
		PaymentMethod pm = e.getMethod();
		assertThat(pm, instanceOf(HoldMethod.class));
		
		Affiliation af = e.getAffiliation();
		assertThat(af, instanceOf(NoAffiliation.class));
	}
	
	@Test
	public void addHourlyEmployee() throws Exception {
		final int empId = 1;
		BigDecimal hourlyRate = BigDecimal.valueOf(22.50);
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bob", "Home", hourlyRate);
		t.execute();
		
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e.getName(), is("Bob"));
		
		PaymentClassification pc = e.getClassification();
		assertThat(pc, instanceOf(HourlyClassification.class));
		
		HourlyClassification hc = (HourlyClassification) pc;
		assertThat(hc.getHourlyRate(), is(hourlyRate));
		
		PaymentSchedule ps = e.getSchedule();
		assertThat(ps, instanceOf(WeeklySchedule.class));
		
		PaymentMethod pm = e.getMethod();
		assertThat(pm, instanceOf(HoldMethod.class));
		
		Affiliation af = e.getAffiliation();
		assertThat(af, instanceOf(NoAffiliation.class));
	}
	
	@Test
	public void deleteEmployee() throws Exception {
		final int empId = 3;
		AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Lance", "Home", BigDecimal.valueOf(2500), BigDecimal.valueOf(3.2));
		t.execute();
		{
			Employee e = GpayrollDatabase.getEmployee(empId);
			assertThat(e, is(notNullValue()));
		}
		DeleteEmployeeTransaction dt = new DeleteEmployeeTransaction(empId);
		dt.execute();
		{
			Employee e = GpayrollDatabase.getEmployee(empId);
			assertThat(e, is(nullValue()));
		}
	}
	
	@Test
	public void timeCardTransaction() throws Exception {
		final int empId = 2;
		final BigDecimal hours = BigDecimal.valueOf(8.0);
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", BigDecimal.valueOf(15.25));
		t.execute();
		TimeCardTransaction tct = new TimeCardTransaction(date(10, 31, 2001), hours, empId);
		tct.execute();
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e, is(notNullValue()));
		HourlyClassification hc = (HourlyClassification) e.getClassification();
		TimeCard tc = hc.getTimeCard(date(10, 31, 2001));
		assertThat(tc, is(notNullValue()));
		assertThat(tc.getHours(), is(hours));
	}
	
	@Test
	public void salesReceiptTransaction() throws Exception {
		final int empId = 2;
		final BigDecimal amount = BigDecimal.valueOf(525.00);
		AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Bill", "Home", BigDecimal.valueOf(2500), BigDecimal.valueOf(3.2));
		t.execute();
		SalesReceiptTransaction srt = new SalesReceiptTransaction(date(10, 31, 2001), amount, empId);
		srt.execute();
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e, is(notNullValue()));
		CommissionedClassification cc = (CommissionedClassification) e.getClassification();
		SalesReceipt sr = cc.getSalesReceipt(date(10, 31, 2001));
		assertThat(sr, is(notNullValue()));
		assertThat(sr.getAmount(), is(amount));
	}
	
	@Test
	public void addServiceCharge() throws Exception {
		final int empId = 2;
		final int memberId = 86;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", BigDecimal.valueOf(15.25));
		t.execute();
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e, is(notNullValue()));
		UnionAffiliation af = new UnionAffiliation(memberId, BigDecimal.valueOf(12.5));
		e.setAffiliation(af);
		GpayrollDatabase.addUnionMember(memberId, e);
		ServiceChargeTransaction sct = new ServiceChargeTransaction(memberId, date(11, 01, 2001), BigDecimal.valueOf(12.95));
		sct.execute();
		ServiceCharge sc = af.getServiceCharge(date(11, 01, 2001));
		assertThat(sc, is(notNullValue()));
		assertThat(sc.getAmount(), is(BigDecimal.valueOf(12.95)));
	}
	
	@Test
	public void changeNameTransaction() throws Exception {
		final int empId = 2;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", BigDecimal.valueOf(15.25));
		t.execute();
		ChangeNameTransaction cnt = new ChangeNameTransaction(empId, "Bob");
		cnt.execute();
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e, is(notNullValue()));
		assertThat(e.getName(), is("Bob"));
	}
	
	@Test
	public void changeHourlyTransaction() throws Exception {
		final int empId = 3;
		AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Lance", "Home", BigDecimal.valueOf(2500), BigDecimal.valueOf(3.2));
		t.execute();
		ChangeHourlyTransaction cht = new ChangeHourlyTransaction(empId, BigDecimal.valueOf(27.52));
		cht.execute();
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e, is(notNullValue()));
		PaymentClassification pc = e.getClassification();
		assertThat(pc, instanceOf(HourlyClassification.class));
		HourlyClassification hc = (HourlyClassification) pc;
		assertThat(hc.getHourlyRate(), is(BigDecimal.valueOf(27.52)));
		PaymentSchedule ps = e.getSchedule();
		assertThat(ps, instanceOf(WeeklySchedule.class));
	}
	
	@Test
	public void changeSalariedTransaction() throws Exception {
		final int empId = 3;
		AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Lance", "Home", BigDecimal.valueOf(2500), BigDecimal.valueOf(3.2));
		t.execute();
		ChangeSalariedTransaction cst = new ChangeSalariedTransaction(empId, BigDecimal.valueOf(1000.00));
		cst.execute();
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e, is(notNullValue()));
		PaymentClassification pc = e.getClassification();
		assertThat(pc, instanceOf(SalariedClassification.class));
		SalariedClassification sc = (SalariedClassification) pc;
		assertThat(sc.getSalary(), is(BigDecimal.valueOf(1000.00)));
		PaymentSchedule ps = e.getSchedule();
		assertThat(ps, instanceOf(MonthlySchedule.class));
	}
	
	@Test
	public void changeCommissionedTransaction() throws Exception {
		final int empId = 3;
		AddSalariedEmployee t = new AddSalariedEmployee(empId, "Lance", "Home", BigDecimal.valueOf(1000.00));
		t.execute();
		ChangeCommissionedTransaction cct = new ChangeCommissionedTransaction(empId, BigDecimal.valueOf(2500), BigDecimal.valueOf(3.2));
		cct.execute();
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e, is(notNullValue()));
		PaymentClassification pc = e.getClassification();
		assertThat(pc, instanceOf(CommissionedClassification.class));
		CommissionedClassification cc = (CommissionedClassification) pc;
		assertThat(cc.getSalary(), is(new BigDecimal("2500.00")));
		assertThat(cc.getCommissionRate(), is(BigDecimal.valueOf(3.2)));
		PaymentSchedule ps = e.getSchedule();
		assertThat(ps, instanceOf(BiweeklySchedule.class));
	}
	
	@Test
	public void changeMemberTransaction() throws Exception {
		final int empId = 2;
		final int memberId = 7734;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", BigDecimal.valueOf(15.25));
		t.execute();
		ChangeMemberTransaction cmt = new ChangeMemberTransaction(empId, memberId, BigDecimal.valueOf(99.42));
		cmt.execute();
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e, is(notNullValue()));
		Affiliation af = e.getAffiliation();
		assertThat(af, instanceOf(UnionAffiliation.class));
		UnionAffiliation uf = (UnionAffiliation) af;
		assertThat(uf.getMemberId(), is(memberId));
		assertThat(uf.getWeeklyDues(), is(BigDecimal.valueOf(99.42)));
		Employee member = GpayrollDatabase.getUnionMember(memberId);
		assertThat(member, is(notNullValue()));
		assertThat(e.equals(member), is(true));
	}
	
	@Test
	public void changeUnaffiliatedTransaction() throws Exception {
		final int empId = 2;
		final int memberId = 7734;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", BigDecimal.valueOf(15.25));
		t.execute();
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e, is(notNullValue()));
		UnionAffiliation uf = new UnionAffiliation(memberId, BigDecimal.valueOf(12.5));
		e.setAffiliation(uf);
		GpayrollDatabase.addUnionMember(memberId, e);
		ChangeUnaffiliatedTransaction cut = new ChangeUnaffiliatedTransaction(empId);
		cut.execute();
		Affiliation af = e.getAffiliation();
		assertThat(af, instanceOf(NoAffiliation.class));
		Employee member = GpayrollDatabase.getUnionMember(memberId);
		assertThat(member, is(nullValue()));
	}
	
	@Test
	public void changeUnaffiliatedTransaction_alreadyNoAffiliation() throws Exception {
		final int empId = 2;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", BigDecimal.valueOf(15.25));
		t.execute();
		Employee e = GpayrollDatabase.getEmployee(empId);
		assertThat(e, is(notNullValue()));
		ChangeUnaffiliatedTransaction cut = new ChangeUnaffiliatedTransaction(empId);
		cut.execute();
		Affiliation af = e.getAffiliation();
		assertThat(af, instanceOf(NoAffiliation.class));
	}
	
	@Test
	public void paySingleSalariedEmployee() throws Exception {
		final int empId = 1;
		AddSalariedEmployee t = new AddSalariedEmployee(empId, "Bob", "Home", BigDecimal.valueOf(1000.00));
		t.execute();
		final Date payDate = date(11, 30, 2001);
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		Paycheck pc = pt.getPaycheck(empId);
		assertThat(pc, is(notNullValue()));
		assertThat(pc.getPayPeriodEndDate(), is(payDate));
		assertThat(pc.getGrossPay(), is(BigDecimal.valueOf(1000.00)));
		assertThat(pc.getField("Disposition"), is("Hold"));
		assertThat(pc.getDeductions(), is(BigDecimal.valueOf(0.0)));
		assertThat(pc.getNetPay(), is(BigDecimal.valueOf(1000.00)));
	}
	
	@Test
	public void paySingleSalariedEmployeeOnWrongDate() throws Exception {
		final int empId = 1;
		AddSalariedEmployee t = new AddSalariedEmployee(empId, "Bob", "Home", BigDecimal.valueOf(1000.00));
		t.execute();
		Date payDate = date(11, 29, 2001);
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		Paycheck pc = pt.getPaycheck(empId);
		assertThat(pc, is(nullValue()));
	}
	
	@Test
	public void paySingleHourlyEmployeeNoTimeCards() throws Exception {
		final int empId = 2;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", BigDecimal.valueOf(15.25));
		t.execute();
		Date payDate = date(11, 9, 2001);	// Friday
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		validatePaycheck(pt, empId, payDate, new BigDecimal("0.00"));
	}
	
	@Test
	public void paySingleHourlyEmployeeOneTimeCard() throws Exception {
		final int empId = 2;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", BigDecimal.valueOf(15.25));
		t.execute();
		Date payDate = date(11, 9, 2001);	// Friday
		
		TimeCardTransaction tc = new TimeCardTransaction(payDate, BigDecimal.valueOf(2.0), empId);
		tc.execute();
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		validatePaycheck(pt, empId, payDate, new BigDecimal("30.50"));
	}

	@Test
	public void paySingleHourlyEmployeeOvertimeOneTimeCard() throws Exception {
		final int empId = 2;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", BigDecimal.valueOf(15.25));
		t.execute();
		Date payDate = date(11, 9, 2001);	// Friday
		
		TimeCardTransaction tc = new TimeCardTransaction(payDate, BigDecimal.valueOf(9.0), empId);
		tc.execute();
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		
		final BigDecimal expectedPay = new BigDecimal("144.88");	// (8 + 1.5) * 15.25
		validatePaycheck(pt, empId, payDate, expectedPay);
	}

	@Test
	public void paySingleHourlyEmployeeOnWrongDate() throws Exception {
		final int empId = 2;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", BigDecimal.valueOf(15.25));
		t.execute();
		Date payDate = date(11, 8, 2001);	// Thursday
		
		TimeCardTransaction tc = new TimeCardTransaction(payDate, BigDecimal.valueOf(9.0), empId);
		tc.execute();
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		
		Paycheck pc = pt.getPaycheck(empId);
		assertThat(pc, is(nullValue()));
	}

	@Test
	public void paySingleHourlyEmployeeTwoTimeCards() throws Exception {
		final int empId = 2;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", BigDecimal.valueOf(15.25));
		t.execute();
		Date payDate = date(11, 9, 2001);	// Friday
		
		TimeCardTransaction tc = new TimeCardTransaction(payDate, BigDecimal.valueOf(2.0), empId);
		tc.execute();
		TimeCardTransaction tc2 = new TimeCardTransaction(date(11, 3, 2001), BigDecimal.valueOf(5.0), empId);
		tc2.execute();
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		
		final BigDecimal expectedPay = new BigDecimal("106.75");	// (2 + 5) * 15.25
		validatePaycheck(pt, empId, payDate, expectedPay);
	}
	
	@Test
	public void paySingleHourlyEmployeeWithTimeCardsSpanningTwoPayPeriods() throws Exception {
		final int empId = 2;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", BigDecimal.valueOf(15.25));
		t.execute();
		Date payDate = date(11, 9, 2001);	// Friday
		Date dateInPreviousPayPeriod = date(11, 2, 2001);
		
		TimeCardTransaction tc = new TimeCardTransaction(payDate, BigDecimal.valueOf(2.0), empId);
		tc.execute();
		TimeCardTransaction tc2 = new TimeCardTransaction(dateInPreviousPayPeriod, BigDecimal.valueOf(5.0), empId);
		tc2.execute();
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		
		final BigDecimal expectedPay = new BigDecimal("30.50");	// 2 * 15.25
		validatePaycheck(pt, empId, payDate, expectedPay);
	}
	
	@Test
	public void paySingleCommissionedEmployeeNoSales() throws Exception {
		final int empId = 2;
		AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Lance", "Home", BigDecimal.valueOf(2500.00), BigDecimal.valueOf(3.2));
		t.execute();
		Date payDate = date(11, 16, 2012);	// Bi-weekly Friday
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		
		BigDecimal expectedPay = new BigDecimal("1153.85");	// 2500/mo * (12mo / 26checks)
		validatePaycheck(pt, empId, payDate, expectedPay);
	}
	
	@Test
	public void paySingleCommissionedEmployeeNoSalesEvery14DaysFor10Years() throws Exception {
		final int empId = 2;
		AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Lance", "Home", BigDecimal.valueOf(2500.00), BigDecimal.valueOf(3.2));
		t.execute();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date(11, 16, 2012));

		for (int i = 0; i < 260; i++) {
			Date payDate = cal.getTime();
			PaydayTransaction pt = new PaydayTransaction(payDate);
			pt.execute();

			Paycheck pc = pt.getPaycheck(empId);
			assertThat("expect paycheck on: " + payDate, pc, is(notNullValue()));

			cal.add(DATE, 14);
		}
	}

	@Test
	public void paySingleCommissionedEmployeeOnWrongDate() throws Exception {
		final int empId = 2;
		AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Lance", "Home", BigDecimal.valueOf(2500.00), BigDecimal.valueOf(3.2));
		t.execute();
		Date payDate = date(11, 23, 2012);	// Non-biweekly Friday
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		
		Paycheck pc = pt.getPaycheck(empId);
		assertThat(pc, is(nullValue()));
	}
	
	
	@Test
	public void paySingleCommissionedEmployeeWithOneSale() throws Exception {
		final int empId = 2;
		AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Lance", "Home", BigDecimal.valueOf(2500.00), BigDecimal.valueOf(3.2));
		t.execute();
		SalesReceiptTransaction srt = new SalesReceiptTransaction(date(11, 16, 2012), new BigDecimal("500.00"), empId);
		srt.execute();
		
		Date payDate = date(11, 16, 2012);	// Bi-weekly Friday
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		
		BigDecimal expectedPay = new BigDecimal("1169.85");	// 2500/mo * (12mo / 26checks) + 500.00 * 3.2%
		validatePaycheck(pt, empId, payDate, expectedPay);
	}
	
	@Test
	public void paySingleCommissionedEmployeeWithTwoSales() throws Exception {
		final int empId = 2;
		AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Lance", "Home", BigDecimal.valueOf(2500.00), BigDecimal.valueOf(3.2));
		t.execute();
		SalesReceiptTransaction srt = new SalesReceiptTransaction(date(11, 16, 2012), new BigDecimal("500.00"), empId);
		srt.execute();
		SalesReceiptTransaction srt2 = new SalesReceiptTransaction(date(11, 3, 2012), new BigDecimal("250.50"), empId);
		srt2.execute();
		
		Date payDate = date(11, 16, 2012);	// Bi-weekly Friday
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		
		BigDecimal expectedPay = new BigDecimal("1177.87");	// 2500/mo * (12mo / 26checks) + 750.50 * 3.2%
		validatePaycheck(pt, empId, payDate, expectedPay);
	}
	
	@Test
	public void paySingleCommissionedEmployeeWithSaleInPreviousPayPeriod() throws Exception {
		final int empId = 2;
		AddCommissionedEmployee t = new AddCommissionedEmployee(empId, "Lance", "Home", BigDecimal.valueOf(2500.00), BigDecimal.valueOf(3.2));
		t.execute();
		SalesReceiptTransaction srt = new SalesReceiptTransaction(date(11, 16, 2012), new BigDecimal("500.00"), empId);
		srt.execute();
		SalesReceiptTransaction srt2 = new SalesReceiptTransaction(date(11, 2, 2012), new BigDecimal("250.50"), empId);
		srt2.execute();
		
		Date payDate = date(11, 16, 2012);	// Bi-weekly Friday
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		
		BigDecimal expectedPay = new BigDecimal("1169.85");	// 2500/mo * (12mo / 26checks) + 500.00 * 3.2%
		validatePaycheck(pt, empId, payDate, expectedPay);
	}
	

	@Test
	public void salariedUnionMemberDues() throws Exception {
		final int empId = 1;
		AddSalariedEmployee t = new AddSalariedEmployee(empId, "Bob", "Home", new BigDecimal("1000.00"));
		t.execute();
		final int memberId = 7734;
		ChangeMemberTransaction cmt = new ChangeMemberTransaction(empId, memberId, new BigDecimal("9.42"));
		cmt.execute();
		Date payDate = date(11, 30, 2001);
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		validatePaycheckWithDeductions(pt, empId, payDate, new BigDecimal("1000.00"), new BigDecimal("952.90"), new BigDecimal("47.10"));	// 1000 - (9.42 * 5)
	}
	
	@Test
	public void hourlyUnionMemberServiceCharge() throws Exception {
		final int empId = 1;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", new BigDecimal("15.24"));
		t.execute();
		final int memberId = 7734;
		ChangeMemberTransaction cmt = new ChangeMemberTransaction(empId, memberId, new BigDecimal("9.42"));
		cmt.execute();
		Date payDate = date(11, 9, 2001);
		ServiceChargeTransaction sct = new ServiceChargeTransaction(memberId, payDate, new BigDecimal("19.42"));
		sct.execute();
		TimeCardTransaction tct = new TimeCardTransaction(payDate, new BigDecimal("8.0"), empId);
		tct.execute();
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		
		validatePaycheckWithDeductions(pt, empId, payDate, new BigDecimal("121.92"), new BigDecimal("93.08"), new BigDecimal("28.84"));
	}
	
	@Test
	public void serviceChargesSpanningMultiplePayPeriods() throws Exception {
		final int empId = 1;
		AddHourlyEmployee t = new AddHourlyEmployee(empId, "Bill", "Home", new BigDecimal("15.24"));
		t.execute();
		final int memberId = 7734;
		ChangeMemberTransaction cmt = new ChangeMemberTransaction(empId, memberId, new BigDecimal("9.42"));
		cmt.execute();
		Date earlyDate = date(11, 2, 2001);	// previous Friday
		Date payDate = date(11, 9, 2001);
		Date lateDate = date(11, 16, 2001);	// next Friday
		ServiceChargeTransaction sct = new ServiceChargeTransaction(memberId, payDate, new BigDecimal("19.42"));
		sct.execute();
		ServiceChargeTransaction sctEarly = new ServiceChargeTransaction(memberId, earlyDate, new BigDecimal("100.00"));
		sctEarly.execute();
		ServiceChargeTransaction sctLate = new ServiceChargeTransaction(memberId, lateDate, new BigDecimal("200.00"));
		sctLate.execute();
		TimeCardTransaction tct = new TimeCardTransaction(payDate, new BigDecimal("8.0"), empId);
		tct.execute();
		PaydayTransaction pt = new PaydayTransaction(payDate);
		pt.execute();
		
		validatePaycheckWithDeductions(pt, empId, payDate, new BigDecimal("121.92"), new BigDecimal("93.08"), new BigDecimal("28.84"));
	}
	
	
	
	
	// TODO: overtime pay for weekend time over 40 hours
	
	private void validatePaycheck(PaydayTransaction pt, int empId, Date payDate, BigDecimal pay) {
		Paycheck pc = pt.getPaycheck(empId);
		assertThat(pc, is(notNullValue()));
		assertThat(pc.getPayPeriodEndDate(), is(payDate));
		assertThat(pc.getGrossPay(), is(pay));
		assertThat(pc.getField("Disposition"), is("Hold"));
		assertThat(pc.getDeductions(), is(BigDecimal.valueOf(0.0)));
		assertThat(pc.getNetPay(), is(pay));
	}

	private void validatePaycheckWithDeductions(PaydayTransaction pt, int empId, Date payDate, BigDecimal grossPay, BigDecimal netPay, BigDecimal deductions) {
		Paycheck pc = pt.getPaycheck(empId);
		assertThat(pc, is(notNullValue()));
		assertThat(pc.getPayPeriodEndDate(), is(payDate));
		assertThat(pc.getGrossPay(), is(grossPay));
		assertThat(pc.getField("Disposition"), is("Hold"));
		assertThat(pc.getDeductions(), is(deductions));
		assertThat(pc.getNetPay(), is(netPay));
	}
	
	private Date date(int month, int day, int year) {
		Calendar c = Calendar.getInstance();
		c.clear();
		c.set(year, month - 1, day);
		return c.getTime();
	}
	
}
