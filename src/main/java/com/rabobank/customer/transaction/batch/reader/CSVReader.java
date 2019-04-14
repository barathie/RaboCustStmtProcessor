package com.rabobank.customer.transaction.batch.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.rabobank.customer.transaction.application.CustomerTransactionConstants;
import com.rabobank.customer.transaction.exception.CustomerTransactionException;
import com.rabobank.customer.transaction.model.CustomerTransactionDetail;

/**
 * Helper class for parsing csv file and converting to objects.
 *
 */

@Component("csvReader")
public class CSVReader implements TransactionLineReader<CustomerTransactionDetail> {	

	@SuppressWarnings("resource")
	@Override
	public List<CustomerTransactionDetail> readLines(File file) {
		List<CustomerTransactionDetail> customerTransactions = null;

		String line = "";

		// Using the try-with resource to avoid the cleaning up of resources in finally block
		// which will be taken care automatically
		try (BufferedReader br = new BufferedReader(new FileReader(file));) {
			// br = new BufferedReader(new FileReader(file));

			// Read to skip the header
			br.readLine();

			// Reading from the second line
			customerTransactions = new ArrayList<CustomerTransactionDetail>();

			while ((line = br.readLine()) != null) {
				String[] transDetails = line.split(CustomerTransactionConstants.COMMA_DELIMITER);
				if (transDetails.length > 0) {
					CustomerTransactionDetail  customerTransactionDetail = new CustomerTransactionDetail();
					customerTransactionDetail.setReference(transDetails[CustomerTransactionConstants.COL_REFERENCE]);
					customerTransactionDetail.setAccountNumber(transDetails[CustomerTransactionConstants.COL_ACCTNUMBER]);
					customerTransactionDetail.setDescription(transDetails[CustomerTransactionConstants.COL_DESCRIPTION]);
					customerTransactionDetail.setStartBalance(transDetails[CustomerTransactionConstants.COL_STARTBAL]);
					customerTransactionDetail.setMutation(transDetails[CustomerTransactionConstants.COL_MUTATION]);
					customerTransactionDetail.setEndBalance(transDetails[CustomerTransactionConstants.COL_ENDBAL]);
					customerTransactions.add(customerTransactionDetail);
				}
			}
		} catch (IOException excep) {
			throw new CustomerTransactionException(String.format("Error while parsing the file %s", file.getName()),
					excep);
		}

		return customerTransactions;
	}

}
