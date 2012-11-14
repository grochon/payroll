package com.unclebob.payroll;
import static com.unclebob.payroll.db.PayrollDatabase.GlobalInstance.GpayrollDatabase;

import com.unclebob.payroll.db.impl.InMemoryPayrollDatabase;
import com.unclebob.payroll.transaction.TransactionFactory;
import com.unclebob.payroll.transaction.impl.TransactionFactoryImpl;
import com.unclebob.payroll.transaction.source.TextParserTransactionSource;
import com.unclebob.transaction.TransactionApplication;

public class PayrollApplication {

	public static void main(String[] args) {
		GpayrollDatabase = new InMemoryPayrollDatabase();
		TransactionFactory transFactory = new TransactionFactoryImpl();
		TextParserTransactionSource source = new TextParserTransactionSource(transFactory, null);
		TransactionApplication app = new TransactionApplication(source);
		app.run();
	}
	
}
