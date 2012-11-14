package com.unclebob.transaction;

import com.unclebob.app.Application;

public class TransactionApplication extends Application {

	private final TransactionSource source;

	public TransactionApplication(TransactionSource source) {
		this.source = source;
	}
	
	@Override
	public void run() {
		Transaction trans;
		while ((trans = source.getTransaction()) != null) {
			trans.execute();
		}
	}
	
}
