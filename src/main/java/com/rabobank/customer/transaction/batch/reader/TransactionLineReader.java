package com.rabobank.customer.transaction.batch.reader;

import java.io.File;
import java.util.List;

public interface TransactionLineReader<CustomerTransactionDetail> {
	public List<CustomerTransactionDetail> readLines(File file);
}
 