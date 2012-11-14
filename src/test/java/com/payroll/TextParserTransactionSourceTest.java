package com.payroll;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;

import org.junit.Test;

import com.unclebob.payroll.transaction.TransactionFactory;
import com.unclebob.payroll.transaction.source.TextParserTransactionSource;
import com.unclebob.transaction.Transaction;

public class TextParserTransactionSourceTest {

	private final Transaction addHourlyTransaction = new TestTransaction();
	private final Transaction addCommissionedTransaction = new TestTransaction();
	
	private TextParserTransactionSource source;
	
	@Test
	public void addCommissionedEmployee() throws Exception {
		given("AddEmp 2 \"Bill\" \"Work\" C 2500.00 3.2");
		assertThat(source.getTransaction(), is(addCommissionedTransaction));
	}
	
	@Test
	public void addHourlyEmployee() throws Exception {
		given("AddEmp 3 \"Lance\" \"Home\" H 15.25\n");
		assertThat(source.getTransaction(), is(addHourlyTransaction));
	}
	
	@Test
	public void canReadMultipleTransactions() throws Exception {
		given("AddEmp 2 \"Bill\" \"Work\" C 2500.00 3.2\nAddEmp 3 \"Lance\" \"Home\" H 15.25\n");
		assertThat(source.getTransaction(), is(addCommissionedTransaction));
		assertThat(source.getTransaction(), is(addHourlyTransaction));
		assertThat(source.getTransaction(), is(nullValue()));
	}
	
	private void given(String transactions) throws Exception {
		InputStream input = new ByteArrayInputStream(transactions.getBytes("UTF-8"));
		source = new TextParserTransactionSource(new TestTransactionFactory(), input);
	}



	private class TestTransaction implements Transaction {

		@Override
		public void execute() {
		}
		
	}
	
	private class TestTransactionFactory implements TransactionFactory {

		@Override
		public Transaction makeAddCommissionedTransaction(int employeeId, String name, String address, BigDecimal salary, BigDecimal commissionRate) {
			assertThat(employeeId, is(2));
			assertThat(name, is("Bill"));
			assertThat(address, is("Work"));
			assertThat(salary, is(new BigDecimal("2500.00")));
			assertThat(commissionRate, is(new BigDecimal("3.2")));
			return addCommissionedTransaction;
		}

		@Override
		public Transaction makeAddHourlyTransaction(int employeeId, String name, String address, BigDecimal hourlyRate) {
			assertThat(employeeId, is(3));
			assertThat(name, is("Lance"));
			assertThat(address, is("Home"));
			assertThat(hourlyRate, is(new BigDecimal("15.25")));
			return addHourlyTransaction;
		}

		@Override
		public Transaction makeAddSalariedTransaction(int employeeId, String name, String address, BigDecimal salary) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Transaction makeChangeCommissionedTransaction(int employeeId, BigDecimal salary, BigDecimal commissionRate) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Transaction makeChangeHourlyTransaction(int employeeId, BigDecimal hourlyRate) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Transaction makeChangeMemberTransaction(int employeeId, int memberId, BigDecimal weeklyDues) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Transaction makeChangeNameTransaction(int employeeId, String name) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Transaction makeChangeSalariedTransaction(int employeeId, BigDecimal salary) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Transaction makeChangeUnaffiliatedTransaction(int employeeId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Transaction makeDeleteEmployeeTransaction(int employeeId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Transaction makePaydayTransaction(Date payDate) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Transaction makeSalesReceiptTransaction(Date date, BigDecimal amount, int employeeId) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Transaction makeServiceChargeTransaction(int memberId, Date date, BigDecimal charge) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Transaction makeTimeCardTransaction(Date date, BigDecimal hours, int employeeId) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
